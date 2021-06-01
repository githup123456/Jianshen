package com.shyj.jianshen.fragment;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.utils.DateUtil;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

public class DailyCalendarFragment extends BaseFragment{

    @BindView(R.id.fragment_calendar_img_previous)
    ImageView imgPrevious;
    @BindView(R.id.fragment_calendar_img_next)
    ImageView imgNext;
    @BindView(R.id.fragment_calendar_tv_date)
    TextView tvDate;
    @BindView(R.id.fragment_calendar_view_calendar)
    CalendarView calendarView;
    @BindView(R.id.fragment_calendar_tv_kcal)
    TextView tvCal;
    @BindView(R.id.fragment_calendar_tv_time)
    TextView tvTime;
    @BindView(R.id.fragment_calendar_tv_completed)
    TextView tvCompleted;

    private boolean isCompleted = false;

    @Override
    public int layoutId() {
        return R.layout.fragment_calender;
    }

    @Override
    public void init() {
        initData(calendarView.getSelectedCalendar());
        calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                initData(calendar);
            }
        });
    }

    private DailyWorkBean dailyWorkBean;
    public void initData(Calendar calendar){
        tvDate.setText(DateUtil.MouthToEnglish(calendar.getMonth())+"."+calendar.getDay()+"."+calendar.getYear());
        try {
            String date = DateUtil.getStringDate(calendar.getYear(),calendar.getMonth(),calendar.getDay());
            Log.e(TAG, "onCalendarSelect: "+date );
            dailyWorkBean = LitePal.where("date=?",date).find(DailyWorkBean.class).get(0);
            if (dailyWorkBean == null){
                isCompleted = false;
            }else {
                isCompleted = true;
            }
        }catch (Exception e){
            Log.e(TAG, "Calendar_catch: "+e.getMessage() );
            isCompleted = false;
        }
        if (isCompleted){
            tvCal.setText(dailyWorkBean.getKcal()+"");
            float min = (float)dailyWorkBean.getDuration()/60;
            tvTime.setText((float) (Math.round(min * 100)) / 100+" min");
            tvCompleted.setText(dailyWorkBean.getCompleted()+"");
        }else {
            tvCal.setText("0");
            tvTime.setText("0 m");
            tvCompleted.setText("0");
        }
    }

    @OnClick({R.id.fragment_calendar_img_previous,R.id.fragment_calendar_img_next})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.fragment_calendar_img_previous:
                calendarView.scrollToPre(false);
                break;
            case R.id.fragment_calendar_img_next:
                calendarView.scrollToNext(false);
                break;
        }
    }
}
