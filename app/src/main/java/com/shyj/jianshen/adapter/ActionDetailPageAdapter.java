package com.shyj.jianshen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.fragment.ActionDetailFragment;

import java.util.List;

public class ActionDetailPageAdapter extends FragmentStatePagerAdapter {

    private List<CourseActionBean> strings;
    private Context mContext;
    private int nowDay;

    public ActionDetailPageAdapter(@NonNull FragmentManager fm, Context context, List<CourseActionBean> stringList){
        super(fm);
        this.mContext=context;
        this.strings=stringList;
    }

    @NonNull
    @Override
    public Fragment getItem(int position){
        return ActionDetailFragment.instance(position);
    }

    @Override
    public int getCount(){
        if(strings!=null&&strings.size()>0){
            return strings.size();
        }
        return 0;
    }
}

