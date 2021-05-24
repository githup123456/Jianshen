package com.shyj.jianshen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shyj.jianshen.fragment.DailyCalendarFragment;
import com.shyj.jianshen.fragment.DailyDataFragment;
import com.shyj.jianshen.fragment.DailyGirthFragment;
import com.shyj.jianshen.fragment.GrandWorkFragment;
import com.shyj.jianshen.fragment.MyWorkoutFragment;
import com.shyj.jianshen.fragment.ReCommendWorkFragment;

public class DailyDataAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public DailyDataAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new DailyCalendarFragment();
        }else if (position==1){
            return new DailyDataFragment();
        }else {
            return new DailyGirthFragment();
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
