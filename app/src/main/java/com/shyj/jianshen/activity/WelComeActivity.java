package com.shyj.jianshen.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.UsersBean;

import org.litepal.LitePal;

public class WelComeActivity extends AppCompatActivity {

    private UsersBean usersBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        usersBean = LitePal.findFirst(UsersBean.class);
    }


    public void startAct(View view) {
        if (usersBean!=null&&usersBean.getFocusParts()!=null){
            Intent intent = new Intent(WelComeActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Intent intent = new Intent(WelComeActivity.this,UserInformationActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
