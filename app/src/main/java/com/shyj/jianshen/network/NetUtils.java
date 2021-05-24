package com.shyj.jianshen.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {


    private NetUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 判断网络是否连接
     *
     * @param context context
     * @return 判断网络是否连接
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != connectivity) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager != null) {
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null) {
                    return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 获取网络连接的类型.
     *
     * @return -1：当前没有连接网络 <br/> 1: wifi连接 <br/>2: 手机连接 <br/>3: 其他连接
     */
    public static int getNetWorkType(Context context) {
        ConnectivityManager manger = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manger.getActiveNetworkInfo();
        if (info != null && info.isConnectedOrConnecting()) {
            int name = info.getType();
            switch (name) {
                case ConnectivityManager.TYPE_WIFI:
                    return 1;
                case ConnectivityManager.TYPE_MOBILE:
                    return 2;
                default:
                    return 3;
            }
        } else {
            return -1;
        }
    }
}
