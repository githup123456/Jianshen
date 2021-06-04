package com.shyj.jianshen.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.PlanBean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;

public class HelpUtils {

    /**
     * 获取app version name
     *
     * @param context
     * @return
     */
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "1";
    }

    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取应用包名
     *
     * @param context
     * @return
     */
    public static String getAppProcessName(Context context) {
        if (context == null)
            return "";
        return context.getPackageName();
    }


    public static boolean copyString(String content, String toFile) {
        boolean result = false;
        try {
            if (content == null)
                return false;
            File toF = new File(toFile);
            if (!toF.getParentFile().exists()) {
                toF.getParentFile().mkdirs();
            }
            if (!toF.exists()) {
                toF.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(toF);
            byte[] data = new byte[100 * 1024];// 100k
            int len = -1;
            InputStream is = new ByteArrayInputStream(content.getBytes());
            while ((len = is.read(data)) != -1) {
                fos.write(data, 0, len);
                fos.flush();
            }
            fos.close();
            is.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param domain 域名
     * @param url    url路径
     * @return
     */
    public static String replaceDomain(String domain, String url) {
        String url_bak = "";
        if (url.indexOf("//") != -1) {
            String[] splitTemp = url.split("//");
            url_bak = splitTemp[0] + "//";
            url_bak = url_bak + domain;

            if (splitTemp.length >= 1 && splitTemp[1].indexOf("/") != -1) {
                String[] urlTemp2 = splitTemp[1].split("/");
                if (urlTemp2.length > 1) {
                    for (int i = 1; i < urlTemp2.length; i++) {
                        url_bak = url_bak + "/" + urlTemp2[i];
                    }
                }
            }
        }
        return url_bak;
    }


    public static boolean copyFile(InputStream is, String toFile) {
        boolean result = false;
        try {
            File toF = new File(toFile);
            if (!toF.getParentFile().exists()) {
                toF.getParentFile().mkdirs();
            }
            if (!toF.exists()) {
                toF.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(toF);
            byte[] data = new byte[100 * 1024];// 100k
            int len = -1;
            while ((len = is.read(data)) != -1) {
                fos.write(data, 0, len);
                fos.flush();
            }
            fos.close();
            is.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取当前进程名
     */
    public static String getCurrentProcessName(Context context) {
        if (context != null) {
            int pid = android.os.Process.myPid();
            String processName = "";
            ActivityManager manager = (ActivityManager) context.getSystemService
                    (Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo process : manager.getRunningAppProcesses()) {
                if (process.pid == pid) {
                    processName = process.processName;
                }
            }
            return processName;
        }
        return "";
    }

    /**
     * 是否主进程
     */
    public static boolean isMainProgress(Context context) {
        if (context != null) {
            return context.getApplicationContext().getPackageName().equals
                    (getCurrentProcessName(context));
        }
        return false;
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变  【代码里最终设置的单位基本都是px】
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变  【代码里最终设置的单位基本都是px，所以最常用使用这个】
     *
     * @param dipValue （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int getPhoneWitch(Context context){
        int witch = context.getResources().getDisplayMetrics().widthPixels;
        return witch;
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static float getDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }


    /**
     * 设置 图片 圆角
     */
    public static RequestOptions setImgRadius(Context context, float radius) {
        return RequestOptions.bitmapTransform(new RoundedCorners(HelpUtils.dip2px(context.getApplicationContext(), radius)))
                .placeholder(R.drawable.grey_radius_5);
    }

    /**
     * 黑色error背景
     */
    public static RequestOptions getBlackError() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.black)
                .error(R.color.black);
        return requestOptions;
    }

    /**
     * 黑色error背景
     */
    public static RequestOptions getGreyError() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.color.grey_ae)
                .error(R.color.grey_ae);
        return requestOptions;
    }

    /**
     * 毫秒转 分秒
     */
    public static String getMSTime(long millSeconds) {
        int seconds = (int) (millSeconds / 1000);
        int m = seconds / 60;
        int s = seconds % 60;
        if (m > 9 && s > 9) {
            return m + ":" + s;
        } else if (m < 10 && s > 9) {
            return "0" + m + ":" + s;
        } else if (m > 9 && s < 10) {
            return m + ":" + "0" + s;
        } else {
            return "0" + m + ":" + "0" + s;
        }
    }

    /**
     * 创建文件夹
     */

    public static void createFile(String fileUrl) {
        File destDir = new File(fileUrl);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
    }


    public static RequestOptions requestOptions(int witch,int height){
        RequestOptions options =
                new RequestOptions()
                        .override(witch, height).diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
        return options;
    }


}
