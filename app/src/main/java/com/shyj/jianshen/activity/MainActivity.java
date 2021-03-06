package com.shyj.jianshen.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Handler;
import android.view.View;

import com.shyj.jianshen.R;
import com.shyj.jianshen.fragment.SwitchFragment;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.view.BottomBarLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private Activity activity;
    private SwitchFragment switchFragment;

    @BindView(R.id.main_bottom_layout)
    BottomBarLayout bottomBar;

    public void intent(View view){
        Intent intent = new Intent(MainActivity.this,UserInformationActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
//        EventBus.getDefault().register(this);
        activity = MainActivity.this;
        switchFragment = new SwitchFragment(getSupportFragmentManager(), this);
        switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PLAN);
        Intent intent = getIntent();
        if (intent != null) {
            String content = intent.getStringExtra(IntentId.SWITCH_FRAGMENT);
            if (content != null && content.equals("DailyFragment")) {
                bottomBar.setCurrentItem(2);
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_DAILY);
            } else if (content != null && content.equals("WorkOutFragment")) {
                bottomBar.setCurrentItem(1);
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_WORKOUT);
            } else if (content!=null&&content.equals("ProfileFragment")){
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PROFILE);
                bottomBar.setCurrentItem(3);
            }else {
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PLAN);
                bottomBar.setCurrentItem(0);
            }
        } else {
            switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PLAN);
        }

        bottomBar.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {

            // previousPosition ????????????  currentPosition  ???????????? ??????????????????????????????????????????  ??????????????????????????????
            if (switchFragment != null) {
                switchFragment.chooseFragment(currentPosition);
            }
        });

    }
}