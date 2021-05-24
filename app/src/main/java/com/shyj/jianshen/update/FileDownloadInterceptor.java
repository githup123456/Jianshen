package com.shyj.jianshen.update;


import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Master
 * @create 2018/8/7 11:11
 */
public class FileDownloadInterceptor implements Interceptor {

    private DownloadListener DownloadListener;

    public FileDownloadInterceptor(DownloadListener DownloadListener) {
        this.DownloadListener = DownloadListener;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        return response.newBuilder()
                .body(new FileResponseBody(response.body(), DownloadListener))
                .build();
    }
}
