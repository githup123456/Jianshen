package com.shyj.jianshen.network;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.shyj.jianshen.BuildConfig;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.AESUtil;
import com.shyj.jianshen.utils.Md5Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {

    private static OkHttpClient mOkHttpClient;


    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    private static SSLSocketFactory getsslsocket() {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


    private static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient != null) {
            return mOkHttpClient;
        }

        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
//                Log.e(CommonUtils.TAG, "请求接口" + message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return mOkHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .addInterceptor(new PostGetInterceptor())
                .addNetworkInterceptor(logInterceptor)
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .sslSocketFactory(getsslsocket())
                .followRedirects(true)
                .retryOnConnectionFailure(true)
                .build();
    }

    // 创建网络接口请求实例
    public static <T> T createApi(Class<T> service) {

        Gson gson = new GsonBuilder().setLenient().create();
        GsonConverterFactory mGsonConverterFactory = GsonConverterFactory.create(gson);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(getOkHttpClient())
//                .addConverterFactory(mGsonConverterFactory)
                .addConverterFactory(StringConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(service);
    }

    public static void request(Context mContext, Observable<String> observable, final IResponseListener listener) {

        if (!NetUtils.isConnected(mContext)) {
            if (listener != null) {
                listener.onNotNetWork();
            }
            Toast.makeText(mContext, "请检查网络!", Toast.LENGTH_SHORT).show();
            return;
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {

                               @Override
                               public void onError(Throwable e) {
                                   if (listener != null) {
                                       listener.onFail(e);
                                   }
                               }

                               @Override
                               public void onComplete() {

                               }


                               @Override
                               public void onSubscribe(Disposable disposable) {

                               }

                               @Override
                               public void onNext(String data) {
                                   try {
                                       Log.e(IntentId.NET_RETURN_DATA, "onNext: "+data );
                                       JSONObject jsonObject = new JSONObject(data);
                                       if (jsonObject.optInt("statusCode") == 200 ) {
                                           if (listener != null) {
                                               String bean = jsonObject.optString("data");
                                               listener.onSuccess(AESUtil.decrypt(bean,AESUtil.INNER_KEY));
                                           }
                                       } else if (jsonObject.optInt("statusCode") == 205){
                                           if (listener!=null){
                                               listener.hasMore();
                                           }
                                       }else {
                                           if (listener != null) {
                                               listener.onFail(new Exception(data));
                                           }
                                           String msg = jsonObject.optString("message");
                                           if (!"重复请求".equals(msg) && !TextUtils.isEmpty(msg)) {
                                               Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   } catch (JSONException e) {
                                       listener.onFail(e);
                                   }

                               }
                           }
                );
    }

}
