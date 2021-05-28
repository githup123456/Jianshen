package com.shyj.jianshen.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.update.DownloadFileDelegate;
import com.shyj.jianshen.update.DownloadFileTaskSync;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class WelComeActivity extends AppCompatActivity {

    private UsersBean usersBean;
    private List<PlanBean> planBean;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_PERMISSION_CODE = 64;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        usersBean = LitePal.findFirst(UsersBean.class);
        planBean = LitePal.findAll(PlanBean.class);
        Log.e("TAG", "onCreate: " + planBean.size());
        if (usersBean != null && (planBean == null || planBean.size() == 0)) {
            sendRequest();
        }

    }

    private String TAG = "Welcome";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.e("WelActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void startAct(View view) {
        if (usersBean != null && usersBean.getFocusParts() != null) {
            Intent intent = new Intent(WelComeActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(WelComeActivity.this, UserInformationActivity.class);
            startActivity(intent);
        }
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
        RetrofitApi.request(WelComeActivity.this, RetrofitApi.createApi(Api.class).getSelectPlanSmartRecommend(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                try {
                    Log.e("TAG", "onSuccess: " + data);
                    List<PlanBean> planBeanList = new Gson().fromJson(data, new TypeToken<List<PlanBean>>() {
                    }.getType());
//                    LitePal.saveAll(planBeanList);
                    if (planBeanList != null && planBeanList.size() > 0) {
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
                        Log.e("TAG", "onSuccess: " + newPlan.getPlanId() + "\n" + newPlan.getIsVip());
                        LitePal.deleteAll(DaysCourseBean.class);
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
                                        String courseBgPath = SaveUtils.BASE_FILE_URL + "/course/" + StringUtil.getStringName(course.getIndexs() + "") + ".jpg";
                                        if (SaveUtils.fileIsExists(courseBgPath)) {
                                            courseBean.setBgUrl(courseBgPath);
                                        } else {
                                            courseBean.setBgUrl(StringUtil.getCourseBgUrl(course.getIndexs()));
                                        }
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

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(final Runnable runnable) {
            Runnable wrapperRunnable = new Runnable() {
                @Override
                public void run() {
                    try {
//                        设置线程优先级
                        android.os.Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                        Thread.currentThread().setPriority(Thread.NORM_PRIORITY - 1);
                        runnable.run();
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            };
            return new Thread(wrapperRunnable, "AsyncTask #" + mCount.getAndIncrement());
        }
    };
    private static final ExecutorService THREAD_POOL_CLICK_EXECUTOR = Executors.newCachedThreadPool(sThreadFactory);


}
