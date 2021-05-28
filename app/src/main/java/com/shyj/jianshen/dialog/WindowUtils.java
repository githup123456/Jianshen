package com.shyj.jianshen.dialog;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class WindowUtils {

    public static PopupWindow popupWindow;
    public static View viewContent;

    public static AlertDialog.Builder alertBuilder;
    public static AlertDialog alertDialog;


    /**
     * activity
     * view  布局view
     * position popupWindow显示位置 0顶部 1中部 2底部
     */
    public static PopupWindow cancelShow(Activity activity, int view, int position) {
        dismissNODimBack(activity);
        popupWindow = new PopupWindow();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = new LinearLayout(activity);
        viewContent = inflater.inflate(view, linearLayout);
        popupWindow.setContentView(viewContent);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        switch (position) {
            case 0:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
                break;

            case 2:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            default:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
        }
        return popupWindow;
    }

    /**
     * activity
     * view  布局view
     * position popupWindow显示位置 0顶部 1中部 2底部
     */
    public static View Show(Activity activity, int view, int position) {
        dismissBrightness(activity);
        popupWindow = new PopupWindow();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = new LinearLayout(activity);
        viewContent = inflater.inflate(view, linearLayout);
        popupWindow.setContentView(viewContent);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        dimBackground(activity, 1.0f, 0.3f);
        switch (position) {
            case 0:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
                break;

            case 2:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            default:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
        }
        return viewContent;
    }


    /**
     * activity
     * view  布局view
     * position popupWindow显示位置 0顶部 1中部 2底部
     */
    public static void dialogShow(Activity activity, View view, int position) {
        alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder.setView(view);
        dimBackground(activity,1.0f,0.3f);
        alertBuilder.setCancelable(true);
        alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dimBackground(activity,0.3f,1.0f);
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static void dismissDialog(){
        if (alertDialog!=null&&alertDialog.isShowing()){
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    /**
     * activity
     * view  布局view
     * position popupWindow显示位置 0顶部 1中部 2底部
     */
    public static View noDimBackShow(Activity activity, int view, int position) {
        dismissNODimBack(activity);
        popupWindow = new PopupWindow();
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout linearLayout = new LinearLayout(activity);
        viewContent = inflater.inflate(view, linearLayout);
        popupWindow.setContentView(viewContent);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        switch (position) {
            case 0:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.TOP, 0, 0);
                break;
            case 2:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                break;
            default:
                popupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                break;
        }
        return viewContent;
    }

    /**
     * 关闭无虚化PopupWindow
     */
    public static void dismissNODimBack(Activity activity) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    /**
     * 关闭虚化PopupWindow
     */
    public static void dismissBrightness(Activity activity) {
        dimBackground(activity, 0.3f, 1.0f);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }
    /**
     * 设置屏幕亮度
     */
    public static void dimBackground(Activity activity, final float from, final float to) {
        final Window window = activity.getWindow();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }
}
