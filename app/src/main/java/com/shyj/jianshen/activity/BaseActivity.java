package com.shyj.jianshen.activity;


import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shyj.jianshen.MyCrashHandler;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int layoutId();
    public static String TAG = "Activity_Log";
    public abstract void init();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        ButterKnife.bind(this);
//        MyCrashHandler myCrashHandler = MyCrashHandler.instance();
        init();
    }

    public void showToast(String message,int gravity){
        Toast toast = Toast.makeText(this,message,Toast.LENGTH_SHORT);
        toast.setGravity(gravity,0,0);
        toast.show();
    }

    public void showToast(String message){
        showToast(message, Gravity.CENTER);
    }
}
