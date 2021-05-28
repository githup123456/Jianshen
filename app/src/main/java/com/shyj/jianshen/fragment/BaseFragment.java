package com.shyj.jianshen.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.shyj.jianshen.R;
import com.shyj.jianshen.view.LoadingView;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public abstract class BaseFragment extends Fragment {
    public abstract int layoutId();
    public static  String TAG = "Base_LOG";
    public abstract void init();
    private Toast mToast;
    //普通Dialog的Loading
    protected LoadingView loadingProgress;
    private View rootView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(layoutId(), container, false);
        resetDensity(getActivity(), 750);
        ButterKnife.bind(this, rootView);
//        Faceplate.getDefault().performInjectLayoutLayers(this);
        init();
        return rootView;
    }

    public static void resetDensity(Context context, float designWidth) {
        if (context == null)
            return;
        Point size = new Point();
        ((WindowManager) context.getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getSize(size);

        Resources resources = context.getResources();

        resources.getDisplayMetrics().xdpi = size.x / designWidth * 72f;

        DisplayMetrics metrics = getMetricsOnMiui(context.getResources());
        if (metrics != null)
            metrics.xdpi = size.x / designWidth * 72f;
    }

    //解决MIUI更改框架导致的MIUI7+Android5.1.1上出现的失效问题(以及极少数基于这部分miui去掉art然后置入xposed的手机)
    private static DisplayMetrics getMetricsOnMiui(Resources resources) {
        if ("MiuiResources".equals(resources.getClass().getSimpleName()) || "XResources".equals(resources.getClass().getSimpleName())) {
            try {
                Field field = Resources.class.getDeclaredField("mTmpMetrics");
                field.setAccessible(true);
                return (DisplayMetrics) field.get(resources);
            } catch (Exception e) {
                return null;
            }
        }
        return null;
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
            loadingProgress = LoadingView.createDialog(getActivity());
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
    /**
     * 显示Toast消息
     *
     * @param messageResourceID
     */
    public void showToast(int messageResourceID) {
        try {
            showToast(getActivity().getResources().getString(messageResourceID), Gravity.CENTER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String message, int gravity) {
        try {
            hiddenKeyboard();
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT);
            }
            mToast.setGravity(gravity, 0, 0);
            mToast.setText(message);
            mToast.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void hiddenKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
