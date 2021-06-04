package com.shyj.jianshen.network;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.shyj.jianshen.MyApplication;
import com.shyj.jianshen.utils.HelpUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import retrofit2.http.FormUrlEncoded;

import static com.shyj.jianshen.key.IntentId.TAG;

public class PostGetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //post参数
        Request.Builder requestBuilder = original.newBuilder();
        //请求体定制：统一添加参数
        FormBody oidFormBody = (FormBody) original.body();
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (int i = 0; i < oidFormBody.size(); i++) {
            bodyBuilder.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
            Log.e("TAG", "intercept: " + oidFormBody.name(i)+ "\n"+"value: "+oidFormBody.encodedValue(i));
        }

        MyApplication myApplication = MyApplication.getContext();
        if (myApplication != null) {
            HashMap<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("versionNo", HelpUtils.getAppVersionCode(MyApplication.getContext()) + "");
            paramsMap.put("packageName", HelpUtils.getAppProcessName(MyApplication.getContext()));

            for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                bodyBuilder.addEncoded(entry.getKey(), entry.getValue());
            }
        }

        FormBody formBody = bodyBuilder.build();

        /*RequestBody requestBody = RequestBody.create(MediaType.parse("application/x-www-from-urlencoded"), json);
        requestBuilder.method(original.method(), requestBody);*/

        Request request = requestBuilder.post(formBody).build();
        return chain.proceed(request);
    }
}
