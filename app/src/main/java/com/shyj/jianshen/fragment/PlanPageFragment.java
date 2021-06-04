package com.shyj.jianshen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.update.DownloadFileDelegate;
import com.shyj.jianshen.update.DownloadFileTaskSync;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlanPageFragment extends BaseFragment{

    public static PlanPageFragment getInstance(String day, int nowDay){
        Bundle bundle = new Bundle();
        bundle.putString(IntentId.PLAN_PAGE_NUM, day);
        bundle.putInt(IntentId.PLAN_PAGE_NOW_DAY,nowDay);
        PlanPageFragment fragment = new PlanPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.fragment_plan_page_lly_off)
    LinearLayout lly_off;
    @BindView(R.id.fragment_plan_page_lly_work)
    LinearLayout lly_work;
    @BindView(R.id.fragment_plan_page_rcl_course)
    RecyclerView rclCourse;

    private PlanCourseAdapter planCourseAdapter;
    private boolean isNull = true;

    private boolean isLock  = false;

    private List<CourseBean> courseBeanList = new ArrayList<>();
    private String day;
    private int nowDay;
    private boolean isFirst = true;

    @Override
    public int layoutId() {
        return  R.layout.fragment_plan_page;
    }

    @Override
    public void init() {
        day = getArguments().getString(IntentId.PLAN_PAGE_NUM);
        nowDay = getArguments().getInt(IntentId.PLAN_PAGE_NOW_DAY)+1;
        Log.e(TAG, "init:getIntent "+day+"\n"+nowDay);
       if (day!=null){
           initList();
           int da = Integer.valueOf(day);
           if (da>nowDay){
               isLock =true;
           }else {
               isLock = false;
           }

           initRecyclerView();
           if (courseBeanList.size()==0){
               isNull = true;
               lly_off.setVisibility(View.VISIBLE);
               lly_work.setVisibility(View.GONE);
           }else {
               isNull = false;
               lly_off.setVisibility(View.GONE);
               lly_work.setVisibility(View.VISIBLE);
           }
       }else {
           isNull = true;
           lly_off.setVisibility(View.VISIBLE);
           lly_work.setVisibility(View.GONE);
       }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst){
            isFirst = false;
        }else {
            initList();
            if (planCourseAdapter!=null&&courseBeanList.size()>0){
                planCourseAdapter.addCourseBeanList(courseBeanList);
                Log.e(TAG, "onResume:planPage " );
            }
        }

    }

    private void initRecyclerView(){
        planCourseAdapter = new PlanCourseAdapter(getActivity(),courseBeanList,isLock,true);
        rclCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclCourse.setAdapter(planCourseAdapter);
    }

    private void initList(){
        try {
            DaysCourseBean daysCourseBean = LitePal.where("day = ?",day).findFirst(DaysCourseBean.class,true);
            if (daysCourseBean!=null){
                List<CourseBean> courseBeans = daysCourseBean.getCourseList(); //LitePal.where("dayscoursebean = ?", nowDay+"").find(CourseBean.class);
                Log.e(TAG, "initList: "+courseBeans.size() );
                courseBeanList = courseBeans;
            }
        }catch (Exception e){
            Log.e(TAG, "initList: "+e.getMessage());
        }
    }



}
