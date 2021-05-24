package com.shyj.jianshen.network;

import android.text.TextUtils;

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

public class PostGetInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        //post参数
        Request.Builder requestBuilder = original.newBuilder();
        //请求体定制：统一添加参数
        if (original.body() instanceof FormBody) {
            FormBody oidFormBody = (FormBody) original.body();
            JsonObject jsonObject = new JsonObject();
            for (int i = 0; i < oidFormBody.size(); i++) {
                jsonObject.addProperty(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
            }

            MyApplication myApplication = MyApplication.getContext();
            if (myApplication!=null){
                HashMap<String, String> paramsMap = new HashMap<String, String>();
                paramsMap.put("versionNo", HelpUtils.getAppVersionCode(MyApplication.getContext()) +"");
                paramsMap.put("packageName",HelpUtils.getAppProcessName(MyApplication.getContext()));

                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    jsonObject.addProperty(entry.getKey(), entry.getValue());
                }
            }
            String json = jsonObject.toString();//表单请求 转 json 请求 方便传递参数
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
            requestBuilder.method(original.method(), requestBody);
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}
