package com.shyj.jianshen.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shyj.jianshen.R;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

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
     * @param pxValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变  【代码里最终设置的单位基本都是px，所以最常用使用这个】
     *
     * @param dipValue
     *            （DisplayMetrics类中属性density）
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     *            （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public static float getDensity(Context context){
        return context.getResources().getDisplayMetrics().density;
    }


    /** 设置 图片 圆角 */
    public static RequestOptions setImgRadius(Context context,float radius){
        return RequestOptions.bitmapTransform(new RoundedCorners(HelpUtils.dip2px(context.getApplicationContext(), radius)))
                .placeholder(R.drawable.grey_radius_5);
    }

    /** 毫秒转 分秒*/
    public static String getMSTime(long millSeconds){
        int seconds = (int) (millSeconds/1000);
        int m = seconds/60;
        int s = seconds%60;
        if (m>9&&s>9){
            return m+":"+s;
        }else if (m>9&&s>9){
            return "0"+m+":"+s;
        }else if (m>9&&s<9){
            return m+":"+"0"+s;
        }else{
            return "0"+m+":"+"0"+s;
        }
    }
}
