package com.shyj.jianshen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.fragment.PlanPageFragment;

import java.util.ArrayList;
import java.util.List;

public class PlanCoursePageAdapter extends FragmentStatePagerAdapter {

    private List<DaysCourseBean> strings ;
    private Context mContext;
    private int nowDay;

    public PlanCoursePageAdapter(@NonNull FragmentManager fm, Context context, List<DaysCourseBean> stringList, int nowDay) {
        super(fm);
        this.mContext = context;
        this.strings = stringList;
        this.nowDay = nowDay;
    }

    public void setStrings(List<DaysCourseBean> strings) {
        this.strings = strings;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        int day = position+1;
        return PlanPageFragment.getInstance(day+"",nowDay);
    }

    @Override
    public int getCount() {
        if (strings!=null&&strings.size()>0){
            return strings.size();
        }
        return 0;
    }
}
