package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.humrousz.sequence.AnimationImageView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.ActionPlayPageAdapter;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.eventbus.ActionPlayEvent;
import com.shyj.jianshen.eventbus.MusicPlayEvent;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StringUtil;
import com.shyj.jianshen.view.NoScrollViewPager;
import com.shyj.jianshen.voice.SystemTTS;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ActionPlayActivity extends AppCompatActivity {

    private String TAG = "action_play";

    @BindView(R.id.action_play_viewpager)
    NoScrollViewPager viewPager;
    @BindView(R.id.action_play_img_next)
    ImageView imgNext;
    @BindView(R.id.action_play_img_previous)
    ImageView imgPrevious;
    @BindView(R.id.action_play_tv_page_num)
    TextView tvPageNum;
    @BindView(R.id.action_play_ch_time)
    Chronometer timer;
    @BindView(R.id.action_play_seek_bar)
    SeekBar mSeekBar;

    private CourseBean courseBean;
    private List<CourseActionBean> courseActionBeanList;
    private Activity activity;
    private boolean isLocalSave = true;

    private AnimationImageView imgAction;

    private long s = 0;

    private ActionPlayPageAdapter actionPlayPageAdapter;

    private SystemTTS systemTTS;

    private String musicName;

    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(layoutId());
        ButterKnife.bind(this);
        mediaPlayer = new MediaPlayer();
        init();
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart: ");
        initMusic();
        super.onStart();
    }

    private void initMusic() {
        musicName = getSharedPreferences(PreferencesName.MUSIC_CHECKED, MODE_PRIVATE).getString(PreferencesName.MUSIC_CHECKED_NAME, "");
        if (musicName != null && musicName.length() > 0) {
            try {
                List<MusicItemBean> musicItemBeanList = LitePal.where("name = ?", musicName).find(MusicItemBean.class);
                if (musicItemBeanList != null && musicItemBeanList.size() > 0) {
                    String url = musicItemBeanList.get(0).getMusicFile();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(url);
                    mediaPlayer.prepare();
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(volume, volume);
                }

            } catch (Exception e) {
                Log.e(TAG, "initMusic: " + e.getMessage());
            }
        }
    }


    private int layoutId() {
        return R.layout.activity_action_play;
    }

    private void init() {
        systemTTS = SystemTTS.getInstance(ActionPlayActivity.this);
        firstClick();
        timer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                long mill = SystemClock.elapsedRealtime() - chronometer.getBase();
                s = mill / 1000;
            }
        });
        courseActionBeanList = new ArrayList<>();
        EventBus.getDefault().register(this);
        courseBean = (CourseBean) getIntent().getSerializableExtra(IntentId.COURSE_BEAN);
        isLocalSave = getIntent().getBooleanExtra(IntentId.COURSE_IS_SAVE, true);

        activity = ActionPlayActivity.this;
        if (courseBean != null && courseBean.getActionTypes() != null) {
            courseActionBeanList = courseBean.getActionTypes();
            tvPageNum.setText(1 + "/" + courseActionBeanList.size() + " " + courseActionBeanList.get(0).getName());
            actionPlayPageAdapter = new ActionPlayPageAdapter(getSupportFragmentManager(), activity, courseBean.getActionTypes());
            viewPager.setAdapter(actionPlayPageAdapter);
            viewPager.setCurrentItem(0);
            viewPager.setOffscreenPageLimit(0);
            viewPager.setIsEnableScroll(false);
            imgPrevious.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    int pos = viewPager.getCurrentItem();
                    if (pos > 0) {
                        viewPager.setCurrentItem(pos - 1);
                    }
                }
            });
            imgNext.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    int pos = viewPager.getCurrentItem();
                    if (pos != courseActionBeanList.size() - 1) {
                        viewPager.setCurrentItem(pos + 1);
                    } else {

                    }
                }
            });
            viewPager.setOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                    Log.e(TAG, "onPageScrolled: " + position);
                }

                @Override
                public void onPageSelected(int position) {
                    Log.e(TAG, "onPageSelected: " + position);
                    int pageNum = position + 1;
                    tvPageNum.setText(pageNum + "/" + courseActionBeanList.size() + " " + courseActionBeanList.get(position).getName());
                    int progress = (int) (pageNum * 100 / courseActionBeanList.size());
                    mSeekBar.setProgress(progress);
                }

                @Override
                public void onPageScrollStateChanged(int state) {
//                    Log.e(TAG, "onPageScrollStateChanged: " + state);

                }
            });


        } else {
            showToast(getString(R.string.course_null_data_try_again));
            finish();
        }
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @OnClick({R.id.action_play_rel_instruction})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_play_rel_instruction:
                showInstruction();
                break;
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void musicState(MusicPlayEvent musicPlayEvent) {
        if (musicPlayEvent.isPlay()) {
            if (isTimerRunning) {
            } else {
                btnClick();
            }
            if (mediaPlayer.isPlaying()) {
            } else {
                mediaPlayer.start();
                timer.start();
            }
        } else {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
            if (isTimerRunning) {
                stopClick();
            }
        }
    }

    private float volume = 0.5f;
    private boolean isOpenGuide = true;
    private boolean isOpenMusic = true;

    public void showInstruction() {
        WindowUtils.dismissNODimBack(activity);
        View popupWindow = WindowUtils.noDimBackShow(activity, R.layout.window_set_volume, 1);
        popupWindow.findViewById(R.id.set_volume_img_close).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissNODimBack(activity);
            }
        });
        popupWindow.findViewById(R.id.set_volume_view_bg).setOnClickListener(v -> {
        });
        SeekBar guideSeek = popupWindow.findViewById(R.id.set_volume_seekbar_guide);
        guideSeek.setProgress(10);
        guideSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float pitch = (float) 10.0f * progress / 100;
                systemTTS.setPitchRate(pitch);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekBar musicSeek = popupWindow.findViewById(R.id.set_volume_seekbar_music);
        musicSeek.setProgress(50);
        musicSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = 1.0f * progress / 100;
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(volume, volume);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        CheckBox checkGuide = popupWindow.findViewById(R.id.set_volume_check_guide);
        checkGuide.setChecked(isOpenGuide);
        checkGuide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpenGuide = isChecked;
                if (isChecked) {
                    systemTTS.stopSpeak();
                } else {
                    systemTTS = SystemTTS.getInstance(activity);
                }
            }
        });
        ImageView imgPlay = popupWindow.findViewById(R.id.set_volume_img_play);
        if (mediaPlayer.isPlaying()) {
            imgPlay.setImageResource(R.mipmap.icon_white_playing);
        } else {
            imgPlay.setImageResource(R.mipmap.icon_white_suspend);
        }
        imgPlay.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    imgPlay.setImageResource(R.mipmap.icon_white_suspend);
                } else {
                    mediaPlayer.start();
                    imgPlay.setImageResource(R.mipmap.icon_white_playing);
                }
            }
        });
        CheckBox checkMusic = popupWindow.findViewById(R.id.set_volume_check_music);
        checkMusic.setChecked(isOpenMusic);
        checkMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isOpenMusic = isChecked;
                if (isChecked) {
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        imgPlay.setImageResource(R.mipmap.icon_white_playing);
                    }
                } else {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        imgPlay.setImageResource(R.mipmap.icon_white_suspend);
                    }
                }
            }
        });
        ((TextView) popupWindow.findViewById(R.id.set_volume_tv_music_name)).setText(musicName);
        popupWindow.findViewById(R.id.set_volume_img_music_list).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissNODimBack(activity);
                Intent intent = new Intent(activity, MusicListActivity.class);
                startActivity(intent);
            }
        });
    }

    public void intentCompleted() {
        courseBean.setCompleted(true);
        courseBean.saveOrUpdate("courseId = ? and days = ?", courseBean.getCourseID(), courseBean.getDays() + "");
        try {
            DailyWorkBean dailyWorkBean = LitePal.where("date = ? ", DateUtil.getNowStringDate()).findFirst(DailyWorkBean.class);
            if (dailyWorkBean != null) {
                dailyWorkBean.setDuration(dailyWorkBean.getDuration() + s);
                dailyWorkBean.setCompleted(dailyWorkBean.getCompleted() + 1);
                dailyWorkBean.setKcal(dailyWorkBean.getKcal() + courseBean.getCalorie());
                dailyWorkBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
            } else {
                dailyWorkBean = new DailyWorkBean();
                dailyWorkBean.setDate(DateUtil.getNowStringDate());
                dailyWorkBean.setDuration(s);
                dailyWorkBean.setCompleted(1);
                dailyWorkBean.setKcal(courseBean.getCalorie());
                dailyWorkBean.save();
            }
        } catch (Exception e) {
            Log.e(TAG, "intentCompleted:catch ");
        }
        Intent intent = new Intent(ActionPlayActivity.this, CourseCompletedActivity.class);
        finish();
        intent.putExtra(IntentId.COURSE_INDEX, courseBean.getIndexs());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnClick();
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        if (timer != null) {
            stopClick();
        }
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
        if (WindowUtils.isShowing()){
            WindowUtils.dismissNODimBack(activity);
        }
        if (WindowUtils.allPopWindow!=null&&WindowUtils.allPopWindow.isShowing()){
            WindowUtils.disMissAllPop();
        }
        isSuspend = true;
        if (runnable!=null){
            mHandler.removeCallbacks(runnable);
            runnable =null;
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        if (WindowUtils.isShowing()) {
            WindowUtils.dismissNODimBack(activity);
        } else if (WindowUtils.allPopWindow!=null&&WindowUtils.allPopWindow.isShowing()){
            WindowUtils.disMissAllPop();
            isSuspend = true;
            if (runnable!=null){
                mHandler.removeCallbacks(runnable);
                runnable =null;
            }
        } else{
            if (viewPager.getCurrentItem() == courseActionBeanList.size() - 1) {
                intentCompleted();
            } else {
                super.onBackPressed();
            }
        }
    }

    private boolean isTimerRunning = false;

    public void firstClick() {
        timer.setBase(SystemClock.elapsedRealtime());//计时器清零
        timer.setFormat("%s");
    }

    public void btnClick() {
        timer.start();
        isTimerRunning = true;
    }

    public void stopClick() {
        timer.stop();
        isTimerRunning = false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventNext(ActionPlayEvent actionPlayEvent) {
        int pos = viewPager.getCurrentItem();
        if (pos < courseActionBeanList.size() - 1) {
            if (actionPlayEvent.isQuick()) {
                showQuickDialog(pos);
            } else {
                systemTTS.playText(getString(R.string.start_next));
                viewPager.setCurrentItem(pos + 1);
            }
        } else {
            intentCompleted();
        }
    }

    private  PopupWindow quickWindow;
    private ColorfulRingProgressView progressView;
    private TextView tvTime;
    private void showQuickDialog(int pos) {
        CourseActionBean actionBean = courseActionBeanList.get(pos+1);
        WindowUtils.disMissAllPop();
        WindowUtils.dismissNODimBack(activity);
        quickWindow = WindowUtils.allShow(activity,R.layout.window_action_quick,1);
        View view = quickWindow.getContentView();
        TextView tvNextName = (TextView)view.findViewById(R.id.action_quick_tv_next_name);
        tvNextName.setText(getString(R.string.next)+" "+actionBean.getName());
        view.findViewById(R.id.action_quick_btn_skip).setOnClickListener(v -> {
            Log.e(TAG, "showQuickDialog: closeWindow" );
            WindowUtils.disMissAllPop();
            isSuspend = true;
            if (runnable!=null){
                mHandler.removeCallbacks(runnable);
                runnable =null;
            }
        });
        quickWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                viewPager.setCurrentItem(pos+1);
            }
        });
        String bgUrl = null;
        if (actionBean.getActionID()!=null) {
            bgUrl = StringUtil.getActionMenUrl(actionBean.getActionID());
        } else {
            bgUrl = StringUtil.getActionMenUrl(actionBean.getId());
        }
        AnimationImageView imgActionBg = view.findViewById(R.id.action_quick_img_action);
        ViewGroup.LayoutParams layoutParams = (ViewGroup.LayoutParams) imgActionBg.getLayoutParams();
        Transformation<Bitmap> circleCrop = new CenterInside();
        WebpDrawableTransformation webpDrawableTransformation = new WebpDrawableTransformation(circleCrop);
        Glide.with(activity).load(bgUrl)
                .apply(HelpUtils.requestOptions(layoutParams.width, layoutParams.height).optionalTransform(circleCrop).optionalTransform(WebpDrawable.class,webpDrawableTransformation).skipMemoryCache(true))
                .into(imgActionBg);
        progressView = view.findViewById(R.id.action_quick_progress);
        tvTime = view.findViewById(R.id.action_quick_tv_remain_time);
        view.findViewById(R.id.action_quick_tv_add_time).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int times = Integer.valueOf(tvTime.getText().toString())+20;
                isSuspend = false;
                if (runnable!=null){
                    mHandler.removeCallbacks(runnable);
                    runnable =null;
                }
                timeTask(times*1000);
            }
        });
        tvTime.setText(actionBean.getGap()/1000+"");
        isSuspend = false;
        timeTask(actionBean.getGap());
    }

    private long nowTime = 0;
    private long currentTime;
    private Runnable runnable;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 11:
                    if (tvTime!=null){
                        tvTime.setText(msg.arg1+"");
                    }
                    break;
            }
        }
    };
    private boolean isSuspend = false;

    private void timeTask(long allTime){
        nowTime = allTime;
        if (tvTime!=null){
            tvTime.setText(nowTime/1000+"");
        }
        if (runnable == null) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    Long timeInterval = System.currentTimeMillis() - currentTime;
                    if (timeInterval > 30) {
                        timeInterval = 30L;
                    }
                    nowTime = (nowTime - timeInterval);
                    int percent = ((int)(nowTime))* 100 / (int)allTime;
                    if (progressView!=null){
                        progressView.setPercent(percent);
                    }
                    if (!isSuspend){
                        if (nowTime <= 0) {
                            WindowUtils.disMissAllPop();
                            isSuspend = true;
                            if (runnable!=null){
                                mHandler.removeCallbacks(runnable);
                                runnable =null;
                            }
                        } else {
                            Message message = new Message();
                            message.what = 11;
                            message.arg1 = (int) nowTime/1000;
                            mHandler.sendMessage(message);
                            currentTime = System.currentTimeMillis();
                            mHandler.postDelayed(this, 10);
                        }
                    }
                }
            };
        }
        currentTime = System.currentTimeMillis();
        mHandler.postDelayed(runnable, 10);
    }

    @Override
    protected void onDestroy() {
        if (systemTTS != null) {
            systemTTS.stopSpeak();
        }
        mediaPlayer.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
