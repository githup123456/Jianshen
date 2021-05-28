package com.shyj.jianshen.activity;


import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.shyj.jianshen.MyCrashHandler;
import com.shyj.jianshen.R;
import com.shyj.jianshen.view.LoadingView;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    public abstract int layoutId();
    public static String TAG = "Activity_Log";
    public abstract void init();
    //普通Dialog的Loading
    protected LoadingView loadingProgress;
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

    /**
     * 显示加载中对话框
     *
     * @return
     */
    public void showLoading() {
        showLoading(getResources().getString(R.string.loading));
    }

    /**
     * 显示加载中对话框
     *
     * @param message default : "加载中..."
     * @return
     */
    public void showLoading(String message) {
        try {
            showLoading(null, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示加载中对话框
     *
     * @param title
     * @param message default : "加载中..."
     * @return
     */
    public void showLoading(String title, String message) {
        if (message == null) {
            message = getResources().getString(R.string.loading);
        }

        if (loadingProgress == null) {
            loadingProgress = LoadingView.createDialog(this);
        }
        if (title == null) {
            loadingProgress.setTitle("");
        } else {
            loadingProgress.setTitle(title);
        }
        loadingProgress.setMessage(message);
        loadingProgress.show();
        loadingProgress.setCancelable(true);// 设置进度条是否可以按退回键取消

        // 设置点击进度对话框外的区域对话框不消失
        loadingProgress.setCanceledOnTouchOutside(false);

    }

    /**
     * 隐藏普通加载框
     */
    public void hiddenLoadingView() {
        if (loadingProgress != null) {
            loadingProgress.dismiss();
            loadingProgress = null;
        }
    }
}
