package com.shyj.jianshen.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.key.IntentId;

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

    @Override
    public int layoutId() {
        return  R.layout.fragment_plan_page;
    }

    @Override
    public void init() {
       String day = getArguments().getString(IntentId.PLAN_PAGE_NUM);
       int nowDay = getArguments().getInt(IntentId.PLAN_PAGE_NOW_DAY) +1;
       if (day!=null){
           int da = Integer.valueOf(day);
           if (da>nowDay){
               isLock =true;
           }else {
               isLock = false;
           }
           if (da%5==0){
               isNull = true;
               lly_off.setVisibility(View.VISIBLE);
               lly_work.setVisibility(View.GONE);
           }else {
               isNull = false;
               lly_off.setVisibility(View.GONE);
               lly_work.setVisibility(View.VISIBLE);
               initRecyclerView();
           }
       }else {
           isNull = true;
           lly_off.setVisibility(View.VISIBLE);
           lly_work.setVisibility(View.GONE);
       }

    }

    private void initRecyclerView(){
        planCourseAdapter = new PlanCourseAdapter(getActivity(),courseBeanList,isLock,true);
        rclCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        rclCourse.setAdapter(planCourseAdapter);
    }
}
