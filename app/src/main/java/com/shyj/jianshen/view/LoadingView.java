package com.shyj.jianshen.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;

import com.shyj.jianshen.R;

public class LoadingView extends Dialog {
    private Context context = null;
    private static LoadingView loadingView = null;

    private String message = "";

    private static TextView messageT;

    public LoadingView(Context context) {
        super(context);
        this.context = context;
    }

    public LoadingView(Context context, int theme) {
        super(context, theme);
    }

    //创建dialog
    public static LoadingView createDialog(Context context) {
        loadingView = new LoadingView(context, R.style.Dialog_Progress);//应用自定义style
        loadingView.setContentView(R.layout.progress_dialog);//加载自定义布局
        loadingView.getWindow().getAttributes().gravity = Gravity.CENTER;//居中
        loadingView.setCanceledOnTouchOutside(false);
        messageT = loadingView.findViewById(R.id.text);
        //  customProgressDialog.setCancelable(false);//设置能否取消，默认是True，也就是点击其他地方就会取消这个dialog的显示
        return loadingView;
    }

    public static void setMessage(String message) {
        messageT.setText(message);
    }
}
