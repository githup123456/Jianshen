package com.shyj.jianshen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.fragment.ActionPlayFragment;
import com.shyj.jianshen.fragment.PlanPageFragment;

import java.util.List;

public class ActionPlayPageAdapter extends FragmentStatePagerAdapter {

    private List<CourseActionBean> strings ;
    private Context mContext;
    private int nowDay;

    public ActionPlayPageAdapter(@NonNull FragmentManager fm, Context context, List<CourseActionBean> stringList) {
        super(fm);
        this.mContext = context;
        this.strings = stringList;
    }

    public ActionPlayPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        int day = position+1;
        return ActionPlayFragment.getInstance(strings.get(position));
    }

    @Override
    public int getCount() {
        if (strings!=null&&strings.size()>0){
            return strings.size();
        }
        return 0;
    }

}
