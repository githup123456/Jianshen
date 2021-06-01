package com.shyj.jianshen.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowAnimationFrameStats;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.github.mikephil.charting.utils.Transformer;
import com.humrousz.sequence.AnimationImageView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.ActionDetailAdapter;
import com.shyj.jianshen.adapter.ActionDetailPageAdapter;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.update.DownloadFile;
import com.shyj.jianshen.update.DownloadFileDelegate;
import com.shyj.jianshen.update.DownloadFileTaskSync;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;
import com.shyj.jianshen.view.NoScrollViewPager;
import com.shyj.jianshen.webp.WebpDrawable;


import org.litepal.LitePal;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class CourseDetailActivity extends BaseActivity {

    @BindView(R.id.course_detail_tv_title)
    TextView tvTitle;
    @BindView(R.id.course_detail_tv_kcal)
    TextView tvKcal;
    @BindView(R.id.course_detail_tv_time)
    TextView tvTime;
    @BindView(R.id.course_detail_tv_grand)
    TextView tvGrand;
    @BindView(R.id.course_detail_tv_equipment)
    TextView tvEquipment;
    @BindView(R.id.course_detail_tv_exercises)
    TextView tvExercises;
    @BindView(R.id.course_detail_tv_music_name)
    TextView tvMusicName;
    @BindView(R.id.course_detail_rel_women)
    RelativeLayout relWomen;
    @BindView(R.id.course_detail_rel_men)
    RelativeLayout relMen;
    @BindView(R.id.top_bar_green_img_right)
    ImageView imgCollect;

    @BindView(R.id.course_detail_img_top_bg)
    ImageView imgTopBg;

    @BindView(R.id.course_detail_btn_start)
    Button btnStart;
    @BindView(R.id.course_detail_lly_download)
    LinearLayout llyDownload;
    @BindView(R.id.course_detail_seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.course_detail_rcl)
    RecyclerView rclExercise;

    private ActionDetailAdapter actionDetailAdapter;

    private String courseId = "";
    private List<CourseActionBean> courseActionBeanList;
    private CourseBean courseBean;
    private int days;
    private boolean isLocalSave = false;
    private boolean isCollect = false;

    @Override
    public int layoutId() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        courseId = intent.getStringExtra(IntentId.COURSE_ID);
        days = intent.getIntExtra(IntentId.DAYS_ID, 0);
        try {
            isLocalSave = true;
            List<CourseBean> courseBeanList = LitePal.where("courseID = ? and days = ? ", courseId, days + "").find(CourseBean.class, true);
            Log.e(TAG, "init: " + courseBeanList.size());
            courseBean = courseBeanList.get(0);
            initCourse(courseBean);
            initAction(courseBean);
        } catch (Exception e) {
            Log.e(TAG, "onNewIntent:" + e.getMessage());
            Intent mainIntent = new Intent(CourseDetailActivity.this, MainActivity.class);
            startActivity(mainIntent);
        }
    }

    @Override
    public void init() {
        StatuBarUtils.setTranslucentStatus(CourseDetailActivity.this);
        courseId = getIntent().getStringExtra(IntentId.COURSE_ID);
        days = getIntent().getIntExtra(IntentId.DAYS_ID, 0);
        CourseBean course = (CourseBean) getIntent().getSerializableExtra(IntentId.COURSE_BEAN);
        courseActionBeanList = new ArrayList<>();

        try {
            if (course != null && course.getId() != null) {
                isLocalSave = false;
                courseBean = course;
            } else {
                isLocalSave = true;
                List<CourseBean> courseBeanList = LitePal.where("courseID = ? and days = ? ", courseId, days + "").find(CourseBean.class, true);
                Log.e(TAG, "init: " + courseBeanList.size());
                courseBean = courseBeanList.get(0);
            }
            actionDetailAdapter = new ActionDetailAdapter(CourseDetailActivity.this, courseActionBeanList, isLocalSave);
            actionDetailAdapter.setActionDetailListener(new ActionDetailAdapter.ActionDetailListener() {
                @Override
                public void onActionDetailClick(int pos) {
                    initActionDetailDialog(pos);
                }
            });
            rclExercise.setLayoutManager(new LinearLayoutManager(CourseDetailActivity.this));
            rclExercise.setAdapter(actionDetailAdapter);
            initCourse(courseBean);

        } catch (Exception e) {
            Log.e(TAG, "init:catch= " + e.getMessage());
            showToast(getString(R.string.course_null_data_try_again));
            finish();
        }
        initAction(courseBean);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        downActionFile();
    }

    private void initCourse(CourseBean courseBean) {
        if (courseBean != null) {
            tvTitle.setText(courseBean.getName());
            tvKcal.setText(courseBean.getCalorie() + " " + getString(R.string.kcal));
            float min = ((float) courseBean.getDuration()) / 1000 / 60;
            tvTime.setText((float) (Math.round(min * 100)) / 100 + " min");
            tvGrand.setText("L" + courseBean.getGrade());
            if (SaveUtils.fileIsExists(SaveUtils.getCourseBgFile(courseBean.getIndexs()))) {
                Glide.with(CourseDetailActivity.this).load(SaveUtils.getCourseBgFile(courseBean.getIndexs())).apply(HelpUtils.getBlackError()).into(imgTopBg);
            } else {
                Glide.with(CourseDetailActivity.this).load(StringUtil.getCourseBgUrl(courseBean.getIndexs())).apply(HelpUtils.getBlackError()).into(imgTopBg);
            }

            if (courseBean.getActionTypes() != null && courseBean.getActionTypes().size() > 0) {
                courseActionBeanList = courseBean.getActionTypes();
                tvExercises.setText("(" + courseActionBeanList.size() + ")");
            }
            if (courseBean.isCollect()) {
                imgCollect.setImageResource(R.mipmap.icon_collect_yellow);
            } else {
                imgCollect.setImageResource(R.mipmap.icon_collect_white);
            }
            if (courseBean.getEquipments() != null && courseBean.getEquipments().length > 0) {
                String[] equipments = courseBean.getEquipments();
                for (int i = 0; i < courseBean.getEquipments().length; i++) {
                    tvEquipment.setText(equipments[i] + "    ");
                }
            }
        }
    }

    private void downActionFile() {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String actionFile = null;
                    for (int q = 0; q < courseActionBeanList.size(); q++) {
                        CourseActionBean actionBeanBean = courseActionBeanList.get(q);
                        String bgUrl = null;
                        if (isLocalSave){
                            actionFile = StringUtil.getActionMenUrl(actionBeanBean.getActionID());
                            bgUrl = SaveUtils.getActionMenFile(actionBeanBean.getActionID());
                        }else {
                            actionFile = StringUtil.getActionMenUrl(actionBeanBean.getId());
                            bgUrl = SaveUtils.getActionMenFile(actionBeanBean.getId());
                        }
                        if (!SaveUtils.fileIsExists(bgUrl)) {
                            try {
                                DownloadFileTaskSync.downloadSync(actionFile, new File(bgUrl), new DownloadFileDelegate() {
                                    @Override
                                    public void onStart(String url) {

                                    }

                                    @Override
                                    public void onDownloading(long currentSizeInByte, long totalSizeInByte, int percentage, String speed, String remainTime) {
                                        Log.e(TAG, "onDownloading:actionBg: " + percentage);
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.e(TAG, "onComplete:actionBg ");
                                    }

                                    @Override
                                    public void onFail() {
                                        Log.e(TAG, "onComplete:actionBg ");
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

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences(PreferencesName.MUSIC_CHECKED, MODE_PRIVATE);
        if (sharedPreferences.getBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED, false)) {
            tvMusicName.setText(sharedPreferences.getString(PreferencesName.MUSIC_CHECKED_NAME, ""));
        } else {
            tvMusicName.setText("");
        }
    }

    private void initAction(CourseBean courseBean) {
        if (courseBean != null) {
            courseActionBeanList = courseBean.getActionTypes();
            if (courseActionBeanList != null && courseActionBeanList.size() > 0) {
                tvExercises.setText("(" + courseActionBeanList.size() + ")");
                actionDetailAdapter.addCourseActionBeanList(courseActionBeanList);
            }
        }

    }

    private Handler handler = new Handler();

    @OnClick({R.id.top_bar_green_img_right, R.id.course_detail_btn_start, R.id.top_bar_green_img_left, R.id.course_detail_lly_music, R.id.course_detail_rel_introduction})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_green_img_left:
                finish();
                break;
            case R.id.top_bar_green_img_right:
                if (courseBean != null && courseBean.isCollect()) {
                    imgCollect.setImageResource(R.mipmap.icon_collect_white);
                    courseBean.setCollect(false);
                    isCollect = false;
                } else {
                    courseBean.setCollect(true);
                    isCollect = true;
                    imgCollect.setImageResource(R.mipmap.icon_collect_yellow);
                    WindowUtils.dismissNODimBack(CourseDetailActivity.this);
                    View view = WindowUtils.noDimBackShow(CourseDetailActivity.this, R.layout.dialog_completed, 0);
                    TextView tv = view.findViewById(R.id.dialog_completed_tv);
                    tv.setText(getResources().getString(R.string.add_favorite));
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            WindowUtils.dismissNODimBack(CourseDetailActivity.this);
                        }
                    }, 2000);
                }
                break;
            case R.id.course_detail_lly_music:
                Intent intent = new Intent(CourseDetailActivity.this, MusicListActivity.class);
                startActivity(intent);
                break;
            case R.id.course_detail_rel_introduction:
                Intent introIntent = new Intent(CourseDetailActivity.this, CourseIntroductionActivity.class);
                introIntent.putExtra(IntentId.COURSE_GRADE, courseBean.getGrade() + "");
                introIntent.putExtra(IntentId.COURSE_TIME, tvTime.getText().toString().replace("min", " "));
                introIntent.putExtra(IntentId.COURSE_GCAL, tvKcal.getText().toString());
                startActivity(introIntent);
                break;
            case R.id.course_detail_btn_start:
                Intent playIntent = new Intent(CourseDetailActivity.this, ActionPlayActivity.class);
                playIntent.putExtra(IntentId.COURSE_BEAN, (Serializable) courseBean);
                playIntent.putExtra(IntentId.MUSIC_NAME, tvMusicName.getText() + "");
                startActivity(playIntent);
                break;
        }
    }


    private void initSeekBar() {
        int num = 0;
        if (courseActionBeanList != null && courseActionBeanList.size() > 0) {
            for (int w = 0; w < courseActionBeanList.size(); w++) {
                CourseActionBean actionBean = courseActionBeanList.get(w);
                if (isLocalSave) {
                    String actionFile = SaveUtils.BASE_FILE_URL + "/course/action/" + StringUtil.getStringName(actionBean.getActionID()) + ".webp";
                    if (SaveUtils.fileIsExists(actionFile)) {

                    } else {

                    }
                }

            }
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    llyDownload.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause: " + false);
        if (isLocalSave) {
            courseBean.save();
        } else {
            if (isCollect) {
                courseBean.setCourseID(courseBean.getId());
                courseBean.setCompleted(false);
                courseBean.setDays(-1);
                String courseBgPath = SaveUtils.BASE_FILE_URL + "/course/" + StringUtil.getStringName(courseBean.getIndexs() + "") + ".jpg";
                if (SaveUtils.fileIsExists(courseBgPath)) {
                    courseBean.setBgUrl(courseBgPath);
                } else {
                    courseBean.setBgUrl(StringUtil.getCourseBgUrl(courseBean.getIndexs()));
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                DownloadFileTaskSync.downloadSync(courseBean.getBgUrl(), new File(courseBgPath), new DownloadFileDelegate() {
                                    @Override
                                    public void onStart(String url) {

                                    }

                                    @Override
                                    public void onDownloading(long currentSizeInByte, long totalSizeInByte, int percentage, String speed, String remainTime) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.e(TAG, "onComplete:courseBg ");
                                        courseBean.setBgUrl(courseBgPath);
                                        courseBean.save();
                                    }

                                    @Override
                                    public void onFail() {

                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                            } catch (Exception e) {
                                Log.e(TAG, "run: 课程bg 下载出错");
                            }
                        }
                    }).start();
                    courseBean.save();
                    List<CourseActionBean> courseActionBeanList = courseBean.getActionTypes();
                    if (courseActionBeanList != null && courseActionBeanList.size() > 0) {
                        for (int i = 0; i < courseActionBeanList.size(); i++) {
                            CourseActionBean actionBean = courseActionBeanList.get(i);
                            CourseActionBean courseActionBean = new CourseActionBean();
                            courseActionBean.setActionID(actionBean.getId());
                            courseActionBean.setName(actionBean.getName());
                            courseActionBean.setDuration(actionBean.getDuration());
                            courseActionBean.setDescription(actionBean.getDescription());
                            courseActionBean.setMp4Time(actionBean.getMp4Time());
                            courseActionBean.setGap(actionBean.getGap());
                            courseActionBean.setPergroup(actionBean.getPergroup());
                            courseActionBean.setType(actionBean.getType());
                            courseActionBean.setCourseBean(courseBean);
                            String courseActionBgPath = SaveUtils.BASE_FILE_URL + "/course/action/men/" + StringUtil.getStringName(actionBean.getId() + "") + ".jpg";
                            if (SaveUtils.fileIsExists(courseActionBgPath)) {
                                courseActionBean.setActionFile(courseActionBgPath);
                            } else {
                                courseActionBean.setActionFile(StringUtil.getActionMenUrl(actionBean.getId()));
                            }
                            courseActionBean.save();
                        }
                    }
                }
            }
        }
    }

    private void initActionDetailDialog(int pos) {
        WindowUtils.dismissBrightness(CourseDetailActivity.this);
        View view = WindowUtils.Show(CourseDetailActivity.this, R.layout.window_course_action_detail, 2);
        NoScrollViewPager viewPager = view.findViewById(R.id.action_detail_viewpager);
        ImageView imPrevious = view.findViewById(R.id.action_detail_img_previous);
        ImageView imNext = view.findViewById(R.id.action_detail_img_next);
        TextView tvPageNum = view.findViewById(R.id.action_detail_tv_page_num);
        int pageNum = pos + 1;
        tvPageNum.setText(pageNum + "/" + courseActionBeanList.size());
        view.findViewById(R.id.action_detail_btn_close).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissBrightness(CourseDetailActivity.this);
            }
        });
        List<View> viewList = new ArrayList<>();
        for (int q = 0; q < courseActionBeanList.size(); q++) {
            LayoutInflater inflater = getLayoutInflater().from(this);
            View childView = inflater.inflate(R.layout.fragment_action_detail, null);
            AnimationImageView imgBg = childView.findViewById(R.id.action_detail_img_bg);
            String bgUrl = SaveUtils.getActionMenFile(courseActionBeanList.get(q).getActionID());
            if (SaveUtils.fileIsExists(bgUrl)) {
                imgBg.setLoopInf();
                //重复行为为指定  跟setLoopCount有关
                imgBg.setLoopFinite();
                Uri uri = Uri.parse("file:" + bgUrl);
                imgBg.setImageURI(uri);
                Log.e(TAG, "onBindViewHolder: bgurl  " + bgUrl);
            } else {
                if (isLocalSave) {
                    bgUrl = StringUtil.getActionMenUrl(courseActionBeanList.get(q).getActionID());
                } else {
                    bgUrl = StringUtil.getActionMenUrl(courseActionBeanList.get(q).getId());
                }
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) imgBg.getLayoutParams();
                Glide.with(CourseDetailActivity.this).load(bgUrl)
                        .apply(HelpUtils.requestOptions(layoutParams.width, layoutParams.height))//
                        .into(imgBg);//
            }

            TextView tvName = childView.findViewById(R.id.action_detail_tv_name);
            TextView tvDescription = childView.findViewById(R.id.action_detail_tv_description);
            tvName.setText(courseActionBeanList.get(q).getName());
            tvDescription.setText(courseActionBeanList.get(q).getDescription());
            viewList.add(childView);
        }
        ActionDetailPageAdapter detailPageAdapter = new ActionDetailPageAdapter(CourseDetailActivity.this, viewList);
        viewPager.setAdapter(detailPageAdapter);
        viewPager.setCurrentItem(pos);
        viewPager.setOffscreenPageLimit(0);
        imPrevious.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int pos = viewPager.getCurrentItem();
                if (pos > 0) {
                    viewPager.setCurrentItem(pos - 1);
                    tvPageNum.setText(pos + "/" + courseActionBeanList.size());
                }
            }
        });
        imNext.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                int pos = viewPager.getCurrentItem();
                if (pos != courseActionBeanList.size() - 1) {
                    viewPager.setCurrentItem(pos + 1);
                    int pageNum = pos + 2;
                    tvPageNum.setText(pageNum + "/" + courseActionBeanList.size());
                }
            }
        });
        viewPager.setOnPageChangeListener(new NoScrollViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int pageNum = position + 1;
                tvPageNum.setText(pageNum + "/" + courseActionBeanList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        getSharedPreferences(PreferencesName.MUSIC_CHECKED, MODE_PRIVATE).edit().putBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED, false).commit();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (WindowUtils.popupWindow != null && WindowUtils.popupWindow.isShowing()) {
            WindowUtils.dismissBrightness(CourseDetailActivity.this);
        } else {
            super.onBackPressed();
        }
    }
}
