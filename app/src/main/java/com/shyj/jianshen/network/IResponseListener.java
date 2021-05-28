package com.shyj.jianshen.network;

import org.json.JSONException;

public interface IResponseListener {
    void onSuccess(String data) throws JSONException;

    void onNotNetWork();

    void hasMore();

    void onFail(Throwable e);
}
