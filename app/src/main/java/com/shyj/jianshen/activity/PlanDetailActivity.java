package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.RecommendWorkAdapter;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanDetailActivity extends BaseActivity {

    @BindView(R.id.plan_detail_tv_name)
    TextView tvName;
    @BindView(R.id.plan_detail_tv_week_grand)
    TextView tvWeekGrand;
    @BindView(R.id.plan_detail_tv_days)
    TextView tvDays;
    @BindView(R.id.plan_detail_tv_min)
    TextView tvMinute;
    @BindView(R.id.plan_detail_img_top_bg)
    ImageView imgTopBg;

    @BindView(R.id.plan_detail_rcl)
    RecyclerView recyclerView;

    private Activity activity;
    private PlanBean planBean;

    private List<CourseBean> courseBeanList;

    private RecommendWorkAdapter workAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_plan_detail;
    }

    @Override
    public void init() {
        courseBeanList = new ArrayList<>();
        planBean = (PlanBean) getIntent().getSerializableExtra(IntentId.PLAN_BEAN);
        activity = PlanDetailActivity.this;
        StatuBarUtils.setTranslucentStatus(activity);
        workAdapter = new RecommendWorkAdapter(activity, courseBeanList);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        recyclerView.setAdapter(workAdapter);
        if (planBean != null) {
            int days = planBean.getDayNum();
            if (days / 7 == 3) {
                tvDays.setText("21");
                if (planBean.getGrade() == 1) {
                    tvWeekGrand.setText(getResources().getString(R.string.three_week) + " · " + getString(R.string.beginner));
                } else if (planBean.getGrade() == 2) {
                    tvWeekGrand.setText(getResources().getString(R.string.three_week) + " · " + getString(R.string.intermediate));
                } else {
                    tvWeekGrand.setText(getResources().getString(R.string.three_week) + " · " + getString(R.string.advanced));
                }
            } else {
                tvDays.setText("28");
                if (planBean.getGrade() == 1) {
                    tvWeekGrand.setText(getResources().getString(R.string.four_week) + " · " + getString(R.string.beginner));
                } else if (planBean.getGrade() == 2) {
                    tvWeekGrand.setText(getResources().getString(R.string.four_week) + " · " + getString(R.string.intermediate));
                } else {
                    tvWeekGrand.setText(getResources().getString(R.string.four_week) + " · " + getString(R.string.advanced));
                }
            }
            String url = StringUtil.getPlanBgUrl(planBean.getMark());
            Glide.with(activity).load(url).apply(HelpUtils.getGreyError()).into(imgTopBg);
            for (int i = 0; i < planBean.getCourses().size(); i++) {
                if (courseBeanList.size() >= 5) {
                    int minute = (int) courseBeanList.get(0).getDuration() / 10 / 60;
                    tvMinute.setText((float) minute / 100 + " min");
                    workAdapter.addCourseBeanList(courseBeanList);
                    return;
                } else {
                    DaysCourseBean daysCourseBean = planBean.getCourses().get(i);
                    if (daysCourseBean != null && daysCourseBean.getCourseList().size() > 0) {
                        for (int j = 0; j < daysCourseBean.getCourseList().size(); j++) {
                            if (courseBeanList.size() >= 5) {
                                int minute = (int) daysCourseBean.getCourseList().get(0).getDuration() / 10 / 60;
                                tvMinute.setText((float) minute / 100 + " min");
                                workAdapter.addCourseBeanList(courseBeanList);
                                return;
                            } else {
                                courseBeanList.add(daysCourseBean.getCourseList().get(j));
                            }

                        }
                    }
                }
            }
        }
    }
    @OnClick({R.id.top_bar_green_img_left,R.id.plan_detail_btn_join_plan})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_green_img_left:
                finish();
                break;
            case R.id.plan_detail_btn_join_plan:
                showLoading();
                savePlan();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hiddenLoadingView();
                        Intent i = new Intent(PlanDetailActivity.this,PlanDetailActivity.class);
                        i.putExtra(IntentId.PLAN_SWITCH, true);
                        startActivity(i);
                    }
                },3000);
                break;
        }
    }

    private Handler handler = new Handler();

    public void savePlan(){
        if (planBean!=null){
            LitePal.deleteAll(PlanBean.class);
            PlanBean newPlan = new PlanBean();
            newPlan.setPlanId(planBean.getId());
            newPlan.setName(planBean.getName());
            newPlan.setDayNum(planBean.getDayNum());
            newPlan.setGrade(planBean.getGrade());
            newPlan.setIsVip(planBean.getIsVip());
            newPlan.setMark(planBean.getMark());
            newPlan.setCourses(planBean.getCourses());
            newPlan.save();
            Log.e("TAG", "onSuccess: " + newPlan.getId() + "\n" + newPlan.getIsVip());
            LitePal.deleteAll(DaysCourseBean.class);
            LitePal.deleteAll(CourseBean.class, "days > 0");
            List<DaysCourseBean> daysCourseBeans = planBean.getCourses();
            List<Integer> integerList = DateUtil.getPlanDayList(daysCourseBeans);
            for (int i = 0; i < daysCourseBeans.size(); i++) {
                try {
                    DaysCourseBean daysCourseBean = daysCourseBeans.get(i);
                    DaysCourseBean daysBean = new DaysCourseBean();
                    daysBean.setId(i);
                    daysBean.setDay(daysCourseBean.getDay());
                    daysBean.setDate(DateUtil.getPlanDate(daysCourseBean.getDay()));
                    daysBean.setPlanBean(newPlan);
                    daysBean.setMouthDay(integerList.get(i));
                    int weekDay = DateUtil.getNowDate()[3] + i;
                    daysBean.setWeekDay(weekDay % 7);
                    daysBean.setCourseList(daysCourseBean.getCourseList());
                    daysBean.save();
                    List<CourseBean> courseBeanList = daysCourseBean.getCourseList();
                    if (courseBeanList != null && courseBeanList.size() > 0) {
                        for (int j = 0; j < courseBeanList.size(); j++) {
                            CourseBean courseBean = new CourseBean();
                            CourseBean course = courseBeanList.get(j);
                            courseBean.setCourseID(course.getId());
                            courseBean.setCalorie(course.getCalorie());
                            courseBean.setCollect(false);
                            courseBean.setCompleted(false);
                            courseBean.setDays(daysBean.getDay());
                            courseBean.setDuration(course.getDuration());
                            courseBean.setDaysCourseBean(daysBean);
                            courseBean.setGrade(course.getGrade());
                            courseBean.setIndexs(course.getIndexs());
                            courseBean.setName(course.getName());
                            courseBean.setActionTypes(course.getActionTypes());
                            courseBean.setEquipments(course.getEquipments());
                            courseBean.setBgUrl(StringUtil.getCourseBgUrl(course.getIndexs()));
                            courseBean.save();
                            List<CourseActionBean> actionBeanList = course.getActionTypes();
                            if (actionBeanList != null && actionBeanList.size() > 0) {
                                for (int k = 0; k < actionBeanList.size(); k++) {
                                    CourseActionBean actionBean = actionBeanList.get(k);
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
                                    courseActionBean.setActionFile(StringUtil.getActionMenUrl(actionBean.getId()));
                                    courseActionBean.save();
                                }
                            }
                        }
                    }
                }catch (Exception e){
                    Log.e(TAG, "savePlan: "+e.getMessage() );
                }
            }
        }

    }

}
