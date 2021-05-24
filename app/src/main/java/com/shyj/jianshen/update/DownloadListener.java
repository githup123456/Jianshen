package com.shyj.jianshen.update;

public interface DownloadListener {
    void onProgress(int progress);
    void onFinishDownload();
    void onFail(Throwable throwable);
}
