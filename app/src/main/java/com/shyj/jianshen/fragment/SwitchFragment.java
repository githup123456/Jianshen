package com.shyj.jianshen.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.IntDef;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.StatuBarUtils;

import org.litepal.LitePal;

public class SwitchFragment {

    private int LAST_POSITION = -1;

    @IntDef({FRAGMENT_TYPE.APP_PLAN,  FRAGMENT_TYPE.APP_DAILY, FRAGMENT_TYPE.APP_WORKOUT,
            FRAGMENT_TYPE.APP_PROFILE})
    public @interface FRAGMENT_TYPE {
        int APP_PLAN = 0;
        int APP_WORKOUT = 1;
        int APP_DAILY = 2;
        int APP_PROFILE = 3;

    }

    private PlanNullFragment mPlanNullFragment;
    private PlanFragment mPlanFragment;
    private WorkOutFragment mWorkOutFragment;
    private DailyFragment mDailyFragment;
    private ProfileFragment mProfileFragment;
    private FragmentManager manager;
    private Activity activity;

    public SwitchFragment(FragmentManager manager, Activity activity) {
        this.manager = manager;
        this.activity = activity;
    }

    private void hiderFragment(FragmentTransaction transaction) {
        if (mPlanFragment != null) {
            transaction.hide(mPlanFragment);
        }
        if (mPlanNullFragment != null){
            transaction.hide(mPlanNullFragment);
        }
        if (mWorkOutFragment != null) {
            transaction.hide(mWorkOutFragment);
        }
       
        if (mDailyFragment != null) {
            transaction.hide(mDailyFragment);
        }
        if (mProfileFragment != null) {
            transaction.hide(mProfileFragment);
        }
    }

    public void chooseFragment(@FRAGMENT_TYPE int type) {
        if (LAST_POSITION == type) {
            return;
        }
        LAST_POSITION = type;
        FragmentTransaction transaction = manager.beginTransaction();
        switch (type) {
            case FRAGMENT_TYPE.APP_PLAN:
                boolean isVip = activity.getSharedPreferences("Vip", Context.MODE_PRIVATE).getBoolean(IntentId.IS_VIP,false);
                boolean isHavePlan =false;
                try {
                    SQLiteDatabase sqLiteDatabase = LitePal.getDatabase();
                    PlanBean planBean = LitePal.findFirst(PlanBean.class);
                    if (planBean!=null&& planBean.getId()!=null){
                        isHavePlan =true;
                    }
                }catch (Exception r){
                    isHavePlan=false;
                }

                isVip = true;
                isHavePlan = true;
                if (isVip&&isHavePlan){
                    if (mPlanFragment == null) {
                        mPlanFragment = new PlanFragment();
                    }
                    if (!mPlanFragment.isAdded()) {
                        transaction.add(R.id.content, mPlanFragment);
                    }
                    hiderFragment(transaction);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.show(mPlanFragment);
                    transaction.commitAllowingStateLoss();
                    StatuBarUtils.setTranslucentStatus(activity);
                }else {
                    if (mPlanNullFragment == null) {
                        mPlanNullFragment = new PlanNullFragment();
                    }
                    if (!mPlanNullFragment.isAdded()) {
                        transaction.add(R.id.content, mPlanNullFragment);
                    }
                    hiderFragment(transaction);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    transaction.show(mPlanNullFragment);
                    transaction.commitAllowingStateLoss();
                    StatuBarUtils.setTranslucentStatus(activity);
                    setStatusBarColor(R.color.white,true);
                }
                break;
            case FRAGMENT_TYPE.APP_WORKOUT:
                if (mWorkOutFragment == null) {
                    mWorkOutFragment = new WorkOutFragment();
                }
                if (!mWorkOutFragment.isAdded()) {
                    transaction.add(R.id.content, mWorkOutFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(mWorkOutFragment);
                transaction.commitAllowingStateLoss();
                setStatusBarColor(R.color.white,true);
                break;
            case FRAGMENT_TYPE.APP_DAILY:
                if (mDailyFragment == null) {
                    mDailyFragment = new DailyFragment();
                }
                if (!mDailyFragment.isAdded()) {
                    transaction.add(R.id.content, mDailyFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(mDailyFragment);
                transaction.commitAllowingStateLoss();
                StatuBarUtils.setTranslucentStatus(activity);
                setStatusBarColor(R.color.white,true);
                break;
           
            case FRAGMENT_TYPE.APP_PROFILE:
                if (mProfileFragment == null) {
                    mProfileFragment = new ProfileFragment();
                }
                if (!mProfileFragment.isAdded()) {
                    transaction.add(R.id.content, mProfileFragment);
                }
                hiderFragment(transaction);
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.show(mProfileFragment);
                transaction.commitAllowingStateLoss();
                StatuBarUtils.setTranslucentStatus(activity);
                setStatusBarColor(R.color.white,true);
                break;
        }
    }

    public void setStatusBarColor(int color, boolean dark) {
        StatuBarUtils.setStatusBarDarkTheme(activity, dark);
        StatuBarUtils.setStatusBarColor(activity, activity.getResources().getColor(color));
    }

}
