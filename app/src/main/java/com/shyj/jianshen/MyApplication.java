package com.shyj.jianshen;

import android.app.Application;


import org.litepal.LitePal;


public class MyApplication extends Application {
    public static MyApplication MY_SELF;
    @Override
    public void onCreate() {
        super.onCreate();
        // use prepend() avoid intercept by default decoder
/*        Glide.get(this).getRegistry()
                .prepend(InputStream.class, Drawable.class, )
                .prepend(ByteBuffer.class, Drawable.class, byteDecoder);*/
        LitePal.initialize(getApplicationContext());
        MY_SELF =this;
    }

    public static MyApplication getContext(){
        return MY_SELF;
    }


}
