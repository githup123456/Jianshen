package com.shyj.jianshen.update;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shyj.jianshen.network.AndroidSchedulers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Master
 * @create 2018/8/7 10:29
 */
public class DownloadFileHelper {

    private Activity activity;
    private static final int DEFAULT_TIMEOUT = 10;
    private static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/bear.apk";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};


    public DownloadFileHelper() {
        int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // 没有写的权限，去申请写的权限，会弹出对话框
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public void downloadFile(String fileUrl, DownloadListener downloadListener) {
        FileDownloadInterceptor fileDownloadInterceptor = new FileDownloadInterceptor(downloadListener);
        OkHttpClient httpClient = new OkHttpClient()
                .newBuilder()
                .addInterceptor(fileDownloadInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dl.google.com/")
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        retrofit.create(DownloadService.class)
                .downloadFile(fileUrl)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(responseBody -> responseBody.byteStream())
                .observeOn(Schedulers.computation())
                .doOnNext(inputStream -> writeFile(inputStream, filePath,downloadListener))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subject<InputStream>() {
                    @Override
                    public boolean hasObservers() {
                        return false;
                    }

                    @Override
                    public boolean hasThrowable() {
                        return false;
                    }

                    @Override
                    public boolean hasComplete() {
                        return false;
                    }

                    @Override
                    public Throwable getThrowable() {
                        return null;
                    }

                    @Override
                    protected void subscribeActual(Observer<? super InputStream> observer) {

                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(InputStream inputStream) {
                        downloadListener.onFinishDownload();

                    }

                    @Override
                    public void onError(Throwable e) {
                        downloadListener.onFail(e);

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    /**
     * 将输入流写入文件
     *
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath,DownloadListener downloadListener) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024 * 8];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b, 0, len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
            downloadListener.onFail(e);
        } catch (IOException e) {
            downloadListener.onFail(e);
        }
    }


    private DownloadListener DownloadListener = new DownloadListener() {


        @Override
        public void onProgress(int progress) {

        }

        @Override
        public void onFinishDownload() {
            try {
              /*  updateDialog.dismiss();
                File apkFile = new File(filePath);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(activity, "com.bearmall.app", apkFile);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
                }
                activity.startActivity(intent);*/
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onFail(Throwable throwable) {

        }
    };


}
