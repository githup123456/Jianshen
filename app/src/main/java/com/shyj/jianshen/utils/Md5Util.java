package com.shyj.jianshen.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Md5Util {
    private static final String TAG = "AES_Md5";

    /**
     * 采用AES加密算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 字符编码(用哪个都可以，要注意new String()默认使用UTF-8编码 getBytes()默认使用ISO8859-1编码)
     */
    private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

    /**
     * AES 加密
     *
     * @param secretKey 加密密码，长度：16 或 32 个字符
     * @param data      待加密内容
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String secretKey, String data) {
        try {
           return AESUtil.encrypt(data,secretKey);
        } catch (Exception e) {
            handleException("encrypt", e);
        }
        return null;
    }

    public static String encryptInt(int index){
        String data = index +"";
        return encrypt(AESUtil.INNER_KEY,data);
    }

    /**
     * AES 解密
     *
     * @param secretKey  解密的密钥，长度：16 或 32 个字符
     * @param base64Data 加密的密文 Base64 字符串
     */
    public static String decrypt(String secretKey, String base64Data) {
        try {
           AESUtil.decrypt(base64Data,secretKey);
        } catch (Exception e) {
            handleException("decrypt", e);
        }
        return null;
    }

    /**
     * 将 字节数组 转换成 Base64 编码
     * 用Base64.DEFAULT模式会导致加密的text下面多一行（在应用中显示是这样）
     */
    public static String base64Encode(byte[] data) {
        return Base64.encodeToString(data, Base64.NO_WRAP);
    }

    /**
     * 将 Base64 字符串 解码成 字节数组
     */
    public static byte[] base64Decode(String data) {
        return Base64.decode(data, Base64.NO_WRAP);
    }

    /**
     * 处理异常
     */
    private static void handleException(String methodName, Exception e) {
        e.printStackTrace();
        Log.e(TAG, methodName + "---->" + e);
    }

    // todo Md5加密字符串
    @NonNull
    public static String md5String(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // todo MD5 加密文件
    @NonNull
    public static String md5File(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        String result = "";
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null!=in){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
