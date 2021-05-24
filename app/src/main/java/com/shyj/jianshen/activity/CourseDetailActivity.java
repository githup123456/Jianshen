package com.shyj.jianshen.activity;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowAnimationFrameStats;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.ActionDetailAdapter;
import com.shyj.jianshen.adapter.ActionDetailPageAdapter;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.utils.StatuBarUtils;

import org.litepal.LitePal;

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

    @BindView(R.id.course_detail_rcl)
    RecyclerView rclExercise;

    private ActionDetailAdapter actionDetailAdapter;

    private String courseId = "";
    private List<CourseActionBean> courseActionBeanList;
    private CourseBean courseBean;
    private UsersBean usersBean;

    @Override
    public int layoutId() {
        return R.layout.activity_course_detail;
    }

    @Override
    public void init() {
        StatuBarUtils.setTranslucentStatus(CourseDetailActivity.this);
        imgCollect.setImageResource(R.mipmap.icon_collect_white);
        courseActionBeanList = new ArrayList<>();
        try {
            /*usersBean = LitePal.findFirst(UsersBean.class);
            if (usersBean.getSex() == 1){
                relMen.setVisibility(View.VISIBLE);
                relWomen.setVisibility(View.GONE);
            }else {
                relWomen.setVisibility(View.VISIBLE);
                relMen.setVisibility(View.GONE);
            }*/
        } catch (Exception e) {
            Log.e(TAG, "init: " + e.getMessage());
        }

        try {
            courseBean = LitePal.where("id = ?", courseId).find(CourseBean.class).get(0);
            if (courseBean != null) {
                tvTitle.setText(courseBean.getName());
                tvKcal.setText(courseBean.getCalorie() + "");
                int min = (int) courseBean.getDuration() / 1000 / 60;
                tvTime.setText(min + "min");
                tvGrand.setText("L" + courseBean.getGrade());
                if (courseBean.getActionTypes() != null && courseBean.getActionTypes().size() > 0) {
                    courseActionBeanList = courseBean.getActionTypes();
                    tvExercises.setText("(" + courseActionBeanList + ")");
                }
                if (courseBean.isCollect()) {
                    imgCollect.setImageResource(R.mipmap.icon_collect_yellow);
                } else {
                    imgCollect.setImageResource(R.mipmap.icon_collect_white);
                }
                initAction(courseBean);
            }
        } catch (Exception e) {
            Log.e(TAG, "init:catch= " + e.getMessage());
        }

    }


    private void initAction(CourseBean courseBean) {
        if (courseBean != null) {
            courseActionBeanList = courseBean.getActionTypes();
            if (courseActionBeanList!=null&&courseActionBeanList.size()>0){
                tvExercises.setText("(" + courseActionBeanList.size() + ")");
                actionDetailAdapter = new ActionDetailAdapter(CourseDetailActivity.this, courseActionBeanList);
                actionDetailAdapter.setActionDetailListener(new ActionDetailAdapter.ActionDetailListener() {
                    @Override
                    public void onActionDetailClick(int pos) {
                        initActionDetailDialog(pos);
                    }
                });
                rclExercise.setLayoutManager(new LinearLayoutManager(CourseDetailActivity.this));
                rclExercise.setAdapter(actionDetailAdapter);
            }
        }
    }

    private Handler handler = new Handler();

    @OnClick({R.id.top_bar_green_img_right, R.id.top_bar_green_img_left, R.id.course_detail_lly_music})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.top_bar_green_img_left:
                finish();
                break;
            case R.id.top_bar_green_img_right:
                if (courseBean != null && courseBean.isCollect()) {
                    imgCollect.setImageResource(R.mipmap.icon_collect_white);
                } else {
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
        }
    }


    private void initActionDetailDialog(int pos) {
        WindowUtils.dismissNODimBack(CourseDetailActivity.this);
        View view = WindowUtils.Show(CourseDetailActivity.this, R.layout.window_course_action_detail, 2);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.action_detail_viewpager);
        ImageView imPrevious = view.findViewById(R.id.action_detail_img_previous);
        ImageView imNext = view.findViewById(R.id.action_detail_img_next);
        TextView tvPageNum = view.findViewById(R.id.action_detail_tv_page_num);
        int pageNum = pos + 1;
        tvPageNum.setText(pageNum + "/" + courseActionBeanList.size());
        view.findViewById(R.id.action_detail_btn_close).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissNODimBack(CourseDetailActivity.this);
            }
        });
        ActionDetailPageAdapter detailPageAdapter = new ActionDetailPageAdapter(getSupportFragmentManager(), CourseDetailActivity.this, courseActionBeanList);
        viewPager.setAdapter(detailPageAdapter);
        viewPager.setCurrentItem(pos);
        viewPager.setOffscreenPageLimit(1);
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
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

}
