package com.shyj.jianshen.activity;


import android.app.Activity;
import android.content.Intent;
import android.net.wifi.hotspot2.pps.HomeSp;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.fragment.SwitchFragment;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.update.DownloadFileDelegate;
import com.shyj.jianshen.update.DownloadFileTaskSync;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;
import com.shyj.jianshen.view.BottomBarLayout;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.io.File;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private Activity activity;
    private SwitchFragment switchFragment;

    @BindView(R.id.main_bottom_layout)
    BottomBarLayout bottomBar;

    public void intent(View view) {
        Intent intent = new Intent(MainActivity.this, UserInformationActivity.class);
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
            } else if (content != null && content.equals("ProfileFragment")) {
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PROFILE);
                bottomBar.setCurrentItem(3);
            } else {
                switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PLAN);
                bottomBar.setCurrentItem(0);
            }
        } else {
            switchFragment.chooseFragment(SwitchFragment.FRAGMENT_TYPE.APP_PLAN);
        }

        bottomBar.setOnItemSelectedListener((bottomBarItem, previousPosition, currentPosition) -> {

            // previousPosition 如果等于  currentPosition  就相当于 在目前选中的图标上进行点击，  可进行刷新操作什么的
            if (switchFragment != null) {
                switchFragment.chooseFragment(currentPosition);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        downCourseBg();
    }

    private void downCourseBg() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<CourseBean> courseBeanList = LitePal.findAll(CourseBean.class);
                    for (int q = 0; q < courseBeanList.size(); q++) {
                        CourseBean courseBean = courseBeanList.get(q);
                        String bgUrl = SaveUtils.BASE_FILE_URL + "/course/" + StringUtil.getStringName(courseBean.getIndexs() + "") + ".jpg";
                        if (!SaveUtils.fileIsExists(bgUrl)) {
                            try {
                                DownloadFileTaskSync.downloadSync(courseBean.getBgUrl(), new File(bgUrl), new DownloadFileDelegate() {
                                    @Override
                                    public void onStart(String url) {

                                    }

                                    @Override
                                    public void onDownloading(long currentSizeInByte, long totalSizeInByte, int percentage, String speed, String remainTime) {
                                        Log.e(TAG, "onDownloading: " + percentage);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.e(TAG, "onComplete:courseBg ");
                                    }

                                    @Override
                                    public void onFail() {
                                        Log.e(TAG, "onComplete:courseBg ");
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "run: 课程bg 下载出错");
                            }
                        }
                    }
                }
            }).start();

        } catch (Exception e) {
            Log.e(TAG, "downCourseBg:下载图片异常 " + e.getMessage());
        }

    }
}