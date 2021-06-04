package com.shyj.jianshen;

import android.app.Application;
import android.util.Log;
import com.bumptech.glide.load.ResourceDecoder;
import com.shyj.jianshen.voice.SystemTTS;

import org.litepal.LitePal;

import java.util.Locale;


public class MyApplication extends Application {
    public static MyApplication MY_SELF;
    public static String TAG = "My_Application";
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(getApplicationContext());
        MY_SELF =this;
        SystemTTS.getInstance(MY_SELF);
        Log.e(TAG, "onCreate: "+getLanguage() );
    }

    public static MyApplication getContext(){
        return MY_SELF;
    }


    public static String getLanguage(){
        Locale locale = getContext().getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;
    }

}
