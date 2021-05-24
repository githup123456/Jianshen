package com.shyj.jianshen.update;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author Master
 * @create 2018/8/7 11:26
 */
public interface DownloadService {

    /**
     * 文件下载
     *
     * @param url
     * @return
     */
    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String url);


}
