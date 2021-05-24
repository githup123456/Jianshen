package com.shyj.jianshen.calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.shyj.jianshen.R;

/**
 * 演示一个变态需求的月视图
 * Created by huanghaibin on 2018/2/9.
 */

public class CustomMonthView extends MonthView {

    private int mRadius;

    public CustomMonthView(Context context) {
        super(context);
    }

    @Override
    protected void onPreviewHook() {
        mRadius = Math.min(mItemWidth, mItemHeight) / 5 * 2;
        mSchemePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLoopStart(int x, int y) {

    }

    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
        int cx = x + mItemHeight/6;
        int cy = y + mItemHeight/5;
        Bitmap bmp= BitmapFactory.decodeResource(getResources(), R.mipmap.icon_green_polygon);
        canvas.drawBitmap(bmp,cx,cy,new Paint(Paint.ANTI_ALIAS_FLAG));
        return false;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
        /*int cx = x + mItemWidth / 2;
        int cy = y + mItemHeight / 2;
        canvas.drawCircle(cx, cy, mRadius, mSchemePaint);*/
    }


    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        float baselineY = mTextBaseLine + y;
        int cx = x + mItemWidth / 2;

        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    mSelectTextPaint);
        } else if (hasScheme) {
            canvas.drawText(String.valueOf(calendar.getDay()),
                    cx,
                    baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mSchemeTextPaint : mOtherMonthTextPaint);

        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, baselineY,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
        }
    }
}
