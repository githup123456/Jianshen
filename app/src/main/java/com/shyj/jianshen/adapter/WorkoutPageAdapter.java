package com.shyj.jianshen.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.shyj.jianshen.fragment.GrandWorkFragment;
import com.shyj.jianshen.fragment.MyWorkoutFragment;
import com.shyj.jianshen.fragment.PlanPageFragment;
import com.shyj.jianshen.fragment.ReCommendWorkFragment;

import java.util.List;

public class WorkoutPageAdapter extends FragmentStatePagerAdapter {

    private Context mContext;

    public WorkoutPageAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new ReCommendWorkFragment();
        }else if (position==1){
            return new MyWorkoutFragment();
        }else {
            int grand = position-1;
            return GrandWorkFragment.getInstance(grand);
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
