package com.shyj.jianshen.update;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.shyj.jianshen.MyApplication;
import com.shyj.jianshen.network.NetUtils;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.HelpUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;

public class DownloadFileTaskSync {
    private static final int MIN_NOTIFY_UNIT = (int) (1024 * 1024 * 0.5);
    private static final int MAX_NOTIFY_UNIT = 1024 * 1024 * 1;
    protected final Context _context;
    protected final File _path;
    protected String _url;
    private final String _md5;
    protected DownloadFileDelegate _delegate;
    private boolean _isRetry = false;
    private int mHistoryContentLength = -1;

    public DownloadFileTaskSync(Context context, String url, File path, DownloadFileDelegate delegate, String md5) {
        if (delegate == null) {
            delegate = DownloadFileDelegate.Null;
        }
        this._context = context.getApplicationContext();
        this._url = url;
        this._path = path;
        this._delegate = delegate;
        this._md5 = md5;
    }

    public DownloadFileTaskSync(Context context, String url, File path, DownloadFileDelegate delegate) {
        this(context, url, path, delegate, null);
    }

    public static void downloadSync(String url, File path, DownloadFileDelegate listener) {
        DownloadFileTaskSync task = new  DownloadFileTaskSync(MyApplication.MY_SELF, url, path, listener);
        Log.e("TAG", "downloadSync: "+url +"\n"+path);
        task.run();
    }

    public synchronized void setDelegate(DownloadFileDelegate delegate) {
        if (delegate == null) {
            _delegate = DownloadFileDelegate.Null;
        } else {
            _delegate = delegate;
        }
    }

    public void run() {
        isRetry = true;
        if (_context == null || _path == null || TextUtils.isEmpty(_url)) {
            _delegate.onFail();
            return;
        }

        boolean hasActiveNetwork = NetUtils.isConnected(_context);
        if (hasActiveNetwork) {
            download2File(_delegate);
        } else {
            _delegate.onFail();
        }
    }

    private boolean isRetry = true;

    protected void download2File(DownloadFileDelegate listener) {
        File file = _path;
        new File(file.getParent()).mkdirs();
        if (file.exists()) {
            final int netLength = getContentLength(_url);
            if (netLength > 0 && file.length() == netLength) {
                listener.onDownloading(file.length(), file.length(), 100, null, null);
                listener.onComplete();
                return;
            } else {
                file.delete();
            }
        }

        BufferedOutputStream fos = null;
        RandomAccessFile randomFile = null;
        BufferedInputStream is = null;
        HttpURLConnection httpConn = null;
        byte[] data = new byte[100 * 1024];
        int readLength;
        File tmpFile = new File(file.getAbsoluteFile() + ".tmp");
        try {
            // 采用普通的下载方式
            if (!tmpFile.exists()) {
                // 采用普通的下载方式
                fos = new BufferedOutputStream(new FileOutputStream(tmpFile));
                httpConn = HttpHelper.openUrl(_context, _url);
                int respondCode = HttpHelper.connect(httpConn);
                if (respondCode == HttpURLConnection.HTTP_OK) {
                    _delegate.onStart(httpConn.getURL().toString());
                    final int totalSize = httpConn.getContentLength();
                    final int notifyUnit = computeNotifyUnit(totalSize);
                    int loadedSize = 0;
                    long currentSize = 0;
                    long startTime = System.currentTimeMillis();

                    _delegate.onDownloading(0, totalSize, 0, null, null);
                    is = new BufferedInputStream(httpConn.getInputStream());
                    while ((readLength = is.read(data)) != -1) {
                        fos.write(data, 0, readLength);
                        fos.flush();
                        currentSize += readLength;
                        loadedSize += readLength;

                        if (loadedSize > notifyUnit) {
                            long duration = System.currentTimeMillis() - startTime;
                            startTime = System.currentTimeMillis();
                            String speed = computeSpeed(loadedSize, duration);
                            String remainTime = computeRemainTime(totalSize, currentSize, loadedSize, duration);

                            int percentage = (int) ((totalSize == 0) ? 0 : (currentSize * 100 / totalSize));
                            listener.onDownloading(currentSize, totalSize, percentage, speed, remainTime);
                            loadedSize = 0;
                        }
                    }
                    fos.flush();
                    if (currentSize == totalSize && totalSize != 0) {
                        file.delete();
                        tmpFile.renameTo(file);
                        listener.onComplete();
                    } else {
                        tmpFile.delete();
                        file.delete();
                        errorRetry(listener);
                    }
                } else {
                    tmpFile.delete();
                    file.delete();
                    errorRetry(listener);
                }
            } else {
                long currentSize = tmpFile.length();
                int loadedSize = 0;

                httpConn = HttpHelper.openUrl(_context, _url);
                randomFile = new RandomAccessFile(tmpFile, "rw");
                // httpConn.setRequestProperty("Range", "bytes=" + currentSize +
                // "-" + (totalSize - 1));
                int respondCode = HttpHelper.connect(httpConn);
                if (respondCode == HttpURLConnection.HTTP_PARTIAL) {
                    _delegate.onStart(httpConn.getURL().toString());
                    int totalSize = (int) (httpConn.getContentLength() + currentSize);
                    final int notifyUnit = computeNotifyUnit(totalSize);
                    int percentage = (int) ((totalSize == 0) ? 0 : currentSize * 100 / totalSize);
                    _delegate.onDownloading(currentSize, totalSize, percentage, null, null);
                    long startTime = System.currentTimeMillis();
                    is = new BufferedInputStream(httpConn.getInputStream());
                    while ((readLength = is.read(data)) != -1) {
                        randomFile.seek(currentSize);
                        randomFile.write(data, 0, readLength);
                        currentSize += readLength;
                        loadedSize += readLength;
                        if (loadedSize > notifyUnit) {
                            long duration = System.currentTimeMillis() - startTime;
                            startTime = System.currentTimeMillis();

                            String speed = computeSpeed(loadedSize, duration);
                            String remainTime = computeRemainTime(totalSize, currentSize, loadedSize, duration);

                            percentage = (int) ((totalSize == 0) ? 0 : currentSize * 100 / totalSize);

                            listener.onDownloading(currentSize, totalSize, percentage, speed, remainTime);
                            loadedSize = 0;
                        }
                    }

                    if (currentSize == totalSize  && totalSize != 0) {
                        tmpFile.renameTo(file);
                        listener.onComplete();
                    } else {
                        tmpFile.delete();
                        file.delete();
                        errorRetry(listener);
                    }
                } else {
                    tmpFile.delete();
                    file.delete();
                    errorRetry(listener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            tmpFile.delete();
            file.delete();
            if (NetUtils.isConnected(_context)) {
                    listener.onFail();
            } else {
                listener.onCancel();
            }
        } finally {
            closeOutputStream(fos);
            closeInputStream(is);
            closeRandomAccessFile(randomFile);

            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
    }

/*    private boolean isFileHasSameMd5() {
        boolean result = false;
        File file = _path;
        if (file.exists()) {
            if (Md5Helper.isMd5Valid(_md5)) {
                final String fileMd5 = Md5Helper.generateMD5(file);
                if (fileMd5.equalsIgnoreCase(_md5)) {
                    result = true;
                }
            }
        }
        return result;
    }*/

    private void doStatisticNotDownload() {
        HttpURLConnection conn = null;
        try {
            conn = HttpHelper.openUrl(_context, _url);
            conn.connect();
            conn.getResponseCode();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void closeInputStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeOutputStream(OutputStream output) {
        if (output != null) {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeRandomAccessFile(RandomAccessFile file) {
        if (file != null) {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private int getContentLength(String url) {
        if (mHistoryContentLength != -1) {
            return mHistoryContentLength;
        }

        try {
            final HttpURLConnection conn = HttpHelper.openUrlHear(_context, url);
            conn.connect();
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                mHistoryContentLength = conn.getContentLength();
            }
            if (conn != null) {
                conn.disconnect();
            }
        } catch (Exception e) {
            mHistoryContentLength = -1;
        }

        return mHistoryContentLength;
    }

    private int computeNotifyUnit(int totalSize) {
        int notifyUnit = (totalSize < 100) ? totalSize : (totalSize / 25);
        if (notifyUnit < MIN_NOTIFY_UNIT) {
            notifyUnit = MIN_NOTIFY_UNIT;
        } else if (notifyUnit > MAX_NOTIFY_UNIT) {
            notifyUnit = MAX_NOTIFY_UNIT;
        }
        return notifyUnit;
    }

    private String computeSpeed(int loadedSize, long duration) {
        if (duration > 0) {
            long bytesPerSecond = loadedSize * 1000 / duration;
            return DateUtil.doTransform2ReadableTime(bytesPerSecond);
        } else {
            return "未知";
        }
    }

    private String computeRemainTime(long totalSize, long currentSize, long loadedSize, long duration) {
        if (duration > 0 && loadedSize > duration) {
            long bytesPerMills = loadedSize / duration;

            return DateUtil.doTransform2ReadableTime((totalSize - currentSize) / bytesPerMills);
        } else {
            return "未知";
        }
    }

    private void errorRetry(DownloadFileDelegate listener) {
        if (isRetry) {
//            _url = HelpUtils.replaceDomain("d1kwlluydj7epi.cloudfront.net", _url);
            isRetry = false;
            download2File(listener);
            return;
        } else {
            listener.onFail();
        }
    }
}
