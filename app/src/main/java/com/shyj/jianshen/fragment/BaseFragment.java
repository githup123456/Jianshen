package com.shyj.jianshen.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.lang.reflect.Field;

import butterknife.ButterKnife;

import static android.content.Context.WINDOW_SERVICE;

public abstract class BaseFragment extends Fragment {
    public abstract int layoutId();
    public static  String TAG = "Base_LOG";
    public abstract void init();
    private Toast mToast;

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

}
