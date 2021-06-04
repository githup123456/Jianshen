package com.shyj.jianshen.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

public class BmiBuildProgressActivity extends BaseActivity {


    @BindView(R.id.build_progress_tv_content)
    TextView tvContent;
    @BindView(R.id.build_progress_colorful_view)
    ColorfulRingProgressView colorfulRingProgressView;
    @BindView(R.id.build_progress_tv_progress)
    TextView tvProgress;
    @BindView(R.id.build_progress_btn_start)
    Button btnStart;
    @BindView(R.id.build_progress_tv_end_title)
    TextView tvEndTitle;
    @BindView(R.id.build_progress_tv_title)
    TextView tvTitle;

    @Override
    public int layoutId() {
        return R.layout.activity_build_progress;
    }

    @Override
    public void init() {
        StatuBarUtils.setWhiteTop(BmiBuildProgressActivity.this,R.color.white,true);
        sendRequest();
        downTime();
    }

    private Long currentTime;
    private Runnable countDownRunnable;
    private int downTime = 6 * 1000;
    private int downTimeMax = 6 * 1000;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
        }
    };

    private void downTime() {
        if (countDownRunnable == null) {
            countDownRunnable = new Runnable() {
                @Override
                public void run() {
                    Long timeInterval = System.currentTimeMillis() - currentTime;
                    if (timeInterval > 30) {
                        timeInterval = 30L;
                    }
                    downTime = (int) (downTime - timeInterval);
                    int percent = (downTimeMax - downTime) * 100 / downTimeMax;
                    colorfulRingProgressView.setPercent(percent);
                    tvProgress.setText(percent + "%");
                    if (percent < 25) {
                        tvContent.setText(getString(R.string.bmi_progress_one));
                    } else {
                        tvContent.setText(getString(R.string.bmi_progress_two));
                    }
                    if (downTime <= 0) {
                        showEnd();
                    } else {
                        currentTime = System.currentTimeMillis();
                        mHandler.postDelayed(this, 10);
                    }
                }
            };
        }
        currentTime = System.currentTimeMillis();
        mHandler.postDelayed(countDownRunnable, 10);
    }

    private void showEnd() {
        Intent intent = new Intent(BmiBuildProgressActivity.this,BMIResultActivity.class);
        startActivity(intent);
        finish();
    }

    public void sendRequest() {
        UsersBean usersBean = LitePal.findFirst(UsersBean.class);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sex", usersBean.getSex());
        hashMap.put("grade", usersBean.getGrade());
        hashMap.put("purpose", usersBean.getPurpose());
        hashMap.put("bodyParts", usersBean.getBodyParts());
        Log.e("TAG", "sendRequest: " + usersBean.getSex() + "\n" + usersBean.getGrade() + "\n" + usersBean.getPurpose());
        RetrofitApi.request(BmiBuildProgressActivity.this, RetrofitApi.createApi(Api.class).getSelectPlanSmartRecommend(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                try {
                    Log.e("TAG", "onSuccess: " + data);
                    List<PlanBean> planBeanList = new Gson().fromJson(data, new TypeToken<List<PlanBean>>() {
                    }.getType());
//                    LitePal.saveAll(planBeanList);
                    if (planBeanList != null && planBeanList.size() > 0) {
                        LitePal.deleteAll(PlanBean.class);
                        PlanBean planBean = planBeanList.get(0);
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
                        LitePal.deleteAll(CourseBean.class,"days > 0");
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
                            } catch (Exception e) {
                                Log.e("TAG", "getSelectPlanSmartRecommend:message days ::" + e.getMessage());
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e("TAG", "getSelectPlanSmartRecommend:message ::" + e.getMessage());
                }
            }

            @Override
            public void onNotNetWork() {
            }

            @Override
            public void hasMore() {

            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }
}
