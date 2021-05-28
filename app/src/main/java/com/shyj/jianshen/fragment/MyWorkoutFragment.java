package com.shyj.jianshen.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyWorkoutFragment extends BaseFragment {

    @BindView(R.id.my_work_lly_null)
    LinearLayout llyNull;
    @BindView(R.id.my_work_rcl)
    RecyclerView recyclerView;

    private PlanCourseAdapter planCourseAdapter;
    private List<CourseBean> courseBeanList;
    private boolean isFirst = true;

    @Override
    public int layoutId() {
        return R.layout.fragment_my_work;
    }

    @Override
    public void init() {
        courseBeanList = new ArrayList<>();
        initCourseList();
        if (courseBeanList!=null &&courseBeanList.size()>0){
            recyclerView.setVisibility(View.VISIBLE);
            llyNull.setVisibility(View.GONE);
        }else {
            llyNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
        initRcl();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirst){
            isFirst = false;
        }else {
            initCourseList();
            planCourseAdapter.addCourseBeanList(courseBeanList);
        }

    }

    private void initCourseList(){
        try {
            List<CourseBean>  courseBeanList = LitePal.where("isCollect = 1").find(CourseBean.class);
            if (courseBeanList!=null&& courseBeanList.size()>0){
                this.courseBeanList = courseBeanList;
            }
        }catch (Exception e){
            Log.e(TAG, "initCourseList:获取收藏异常 "+e.getMessage() );
        }
    }

    public void initRcl(){
        planCourseAdapter = new PlanCourseAdapter(getActivity(),courseBeanList,false,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(planCourseAdapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: "+hidden );
    }

}
