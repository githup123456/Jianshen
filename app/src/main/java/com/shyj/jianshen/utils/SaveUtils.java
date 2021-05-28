package com.shyj.jianshen.utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;

import com.lcodecore.tkrefreshlayout.utils.LogUtil;
import com.shyj.jianshen.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SaveUtils {

    //文件路径
    public static String BASE_FILE_URL = getResDir();
    public static String RES_PATH = "/keepFit/data";

    public static String getResDir() {
        File rootFile = MyApplication.getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        if (rootFile == null || TextUtils.isEmpty(rootFile.getAbsolutePath())) {
            LogUtil.i("sd card is null");
            rootFile = MyApplication.getContext().getFilesDir();
        }
        return rootFile.getAbsoluteFile() + RES_PATH;
    }


    /**  保存图片到本地 */
    public static boolean saveBitmap(Bitmap bitmap, String filePath) {
        LogUtil.i("set wallpaper: save filepath = " + filePath);
        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /** 保存字符串到本地 */
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

    /** 保存 字节流到本地*/
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


    //判断文件是否存在
    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
