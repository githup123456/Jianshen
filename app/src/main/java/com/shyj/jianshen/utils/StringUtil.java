package com.shyj.jianshen.utils;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.shyj.jianshen.network.NetUrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class StringUtil {


    /**课程封面图地址：
    课程封面图路由 + ‘/’+ indexs + “_” + md5(aes(indexs)).jpg*/
    public static String getCourseBgUrl(int index){
        String url = NetUrl.COURSE_IMG  + index + "_"+Md5Util.md5String(Md5Util.encryptInt(index))+".jpg";
        return url;
    }
    /**动作文件地址：
    男：动作文件路由 + ‘/m/’+ id + “_” + md5(aes(id)).jpg*/
    public static String getActionMenUrl(String id){
        String url = NetUrl.ACTION_FILE +"m/"+ id + "_"+Md5Util.md5String(Md5Util.encrypt(AESUtil.INNER_KEY,id))+".jpg";
        return url;
    }

    //  todo  女：动作文件路由 + ‘/f/’+ id + “_” + md5(aes(id)).jpg
    public static String getActionWomenUrl(String id){
        String url = NetUrl.ACTION_FILE +"f/"+ id + "_"+Md5Util.md5String(Md5Util.encrypt(AESUtil.INNER_KEY,id))+".jpg";
        return url;
    }
   /** 音乐封面图地址：
    音乐封面图路由 + ‘/’+ id + “_” + md5(aes(id)).jpg*/
   public static String getMusicBgUrl(String id){
       String url = NetUrl.MUSIC_IMG  +id+"_"+Md5Util.md5String(Md5Util.encrypt(AESUtil.INNER_KEY,id))+".jpg";
       return url;
   }

    /**音乐文件地址：
    音乐文件路由 + ‘/’+ id + “_” + md5(aes(id)).mp3*/
    public static String getMusicFileUrl(String id){
        String url = NetUrl.MUSIC_FILE  +id+"_"+Md5Util.md5String(Md5Util.encrypt(AESUtil.INNER_KEY,id))+".mp3";
        return url;
    }
    /**计划封面图地址：
    计划封面图路由 + ‘/’+ mark + “_” + md5(aes(mark)).jpg*/
    public static String getPlanBgUrl(String mark){
        String url = NetUrl.PLAN_IMG  +mark+"_"+Md5Util.md5String(Md5Util.encrypt(AESUtil.INNER_KEY,mark))+".jpg";
        return url;
    }
}
