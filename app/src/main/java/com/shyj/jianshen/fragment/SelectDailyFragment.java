package com.shyj.jianshen.fragment;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.IntDef;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.StatuBarUtils;

import org.litepal.LitePal;

public class SelectDailyFragment {

    private int LAST_POSITION = -1;
    private DailyCalendarFragment mDailyCalendarFragment;
    private DailyDataFragment mDailyDataFragment;
    private DailyGirthFragment mDailyGirthFragment;
    private FragmentManager manager;
    private Activity activity;

    public SelectDailyFragment(FragmentManager manager, Activity activity) {
        this.manager = manager;
        this.activity = activity;
    }

    private void hiderFragment(FragmentTransaction transaction) {
        if (mDailyCalendarFragment != null){
            transaction.hide(mDailyCalendarFragment);
        }
        if (mDailyDataFragment != null) {
            transaction.hide(mDailyDataFragment);
        }

        if (mDailyGirthFragment != null) {
            transaction.hide(mDailyGirthFragment);
        }

    }

    public void chooseFragment( int type) {
        if (LAST_POSITION == type) {
            return;
        }
        LAST_POSITION = type;
        FragmentTransaction transaction = manager.beginTransaction();
        switch (type) {
            case 0:
                    if (mDailyCalendarFragment == null) {
                        mDailyCalendarFragment = new DailyCalendarFragment();
                    }
                    if (!mDailyCalendarFragment.isAdded()) {
                        transaction.add(R.id.fragment_daily_frame, mDailyCalendarFragment);
                    }
                    hiderFragment(transaction);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.show(mDailyCalendarFragment);
                    transaction.commitAllowingStateLoss();
                break;
            case 1:
                if (mDailyDataFragment == null) {
                    mDailyDataFragment = new DailyDataFragment();
                }
                if (!mDailyDataFragment.isAdded()) {
                    transaction.add(R.id.fragment_daily_frame, mDailyDataFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(mDailyDataFragment);
                transaction.commitAllowingStateLoss();
                break;
            case 2:
                if (mDailyGirthFragment == null) {
                    mDailyGirthFragment = new DailyGirthFragment();
                }
                if (!mDailyGirthFragment.isAdded()) {
                    transaction.add(R.id.fragment_daily_frame, mDailyGirthFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(mDailyGirthFragment);
                transaction.commitAllowingStateLoss();
                break;
        }
    }
    
}
