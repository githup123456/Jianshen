package com.shyj.jianshen.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * @author Lynn-God
 * @Description
 * @createTime 2019/12/17 9:24
 * @updateUser Lynn-God
 * @updateTime 2019/12/17 9:24
 * @desc
 */
public class AESUtil {


    private static final String KEY_ALGORITHM = "AES";
    private static final String CHAR_SET = "UTF-8";

    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
    public final static String INNER_KEY = "_____________^_^";

    /**
     * AES加密操作
     *
     * @param content  待加密内容
     * @param password 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String password) {
        try {
            byte[] contentBytes = content.getBytes(CHAR_SET);
            byte[] encryptedBytes = aesEncryptBytes(contentBytes, password);
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * AES解密操作
     *
     * @param encryptContent 加密的密文
     * @param password       解密的密钥
     * @return
     */
    public static String decrypt(String encryptContent, String password) {
        try {
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptContent);
            byte[] decryptedBytes = aesDecryptBytes(encryptedBytes, password);
            return new String(decryptedBytes, CHAR_SET);
        } catch (Exception e) {
            return null;
        }
    }


    private static byte[] aesEncryptBytes(byte[] contentBytes, String password) {
        try {
            return cipherOperation(contentBytes, password.getBytes(CHAR_SET), Cipher.ENCRYPT_MODE);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] aesDecryptBytes(byte[] contentBytes, String password) {
        try {
            return cipherOperation(contentBytes, password.getBytes(CHAR_SET), Cipher.DECRYPT_MODE);
        } catch (Exception e) {
            return null;
        }
    }

    private static byte[] cipherOperation(byte[] contentBytes, byte[] keyBytes, int mode) {
        try {
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(mode, secretKey);
            return cipher.doFinal(contentBytes);
        } catch (Exception e) {
            return null;
        }
    }



}