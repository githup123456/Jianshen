package com.shyj.jianshen.update;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author Master
 * @create 2018/8/7 11:06
 */
public class FileResponseBody extends ResponseBody {


    private ResponseBody responseBody;
    private DownloadListener DownloadListener;
    private BufferedSource bufferedSource;

    public FileResponseBody(okhttp3.ResponseBody responseBody, DownloadListener DownloadListener) {
        this.responseBody = responseBody;
        this.DownloadListener = DownloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }


    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: " + (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != DownloadListener) {
                    if (bytesRead != -1) {
                        DownloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }

                }
                return bytesRead;
            }
        };
    }


}
