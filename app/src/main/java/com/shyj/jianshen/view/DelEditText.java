package com.shyj.jianshen.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.shyj.jianshen.R;


@SuppressLint("AppCompatCustomView")
public class DelEditText extends EditText {
    private Drawable imgClear;
    private Context mContext;
    private boolean isShow = false;


    public DelEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        imgClear = mContext.getResources().getDrawable(R.mipmap.icon_ed_delete);
        addTextChangedListener(new TextWatcher() {
            //内容变化前
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //内容正在改变
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //在内容改变完之后
            @Override
            public void afterTextChanged(Editable editable) {
                setDrawable();
            }
        });

    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused) {
            isShow = true;
            setDrawable();
        } else {
            isShow = false;
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    public void cliearX() {
        isShow = false;
        setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
    }

    //绘制删除图片
    private void setDrawable() {
        if (length() < 1)
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        else
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgClear, null);
    }


    //当触摸范围在右侧时，触发删除方法，隐藏叉叉
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgClear != null && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();
            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 100;
            if (rect.contains(eventX, eventY))
                if(isShow){
                    setText("");
                }
        }
        return super.onTouchEvent(event);
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
