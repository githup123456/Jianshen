package com.shyj.jianshen;

import android.app.Application;
import android.graphics.drawable.Drawable;
import android.util.Log;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.shyj.jianshen.voice.SystemTTS;
import com.shyj.jianshen.webp.WebpBytebufferDecoder;
import com.shyj.jianshen.webp.WebpResourceDecoder;

import org.litepal.LitePal;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;


public class MyApplication extends Application {
    public static MyApplication MY_SELF;
    public static String TAG = "My_Application";
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceDecoder decoder = new WebpResourceDecoder(this);
        ResourceDecoder byteDecoder = new WebpBytebufferDecoder(this);
        // use prepend() avoid intercept by default decoder
/*        Glide.get(this).getRegistry()
                .prepend(InputStream.class, Drawable.class, decoder)
                .prepend(ByteBuffer.class, Drawable.class, byteDecoder);*/
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
