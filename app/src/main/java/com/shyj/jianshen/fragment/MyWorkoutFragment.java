package com.shyj.jianshen.fragment;

import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;

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
    private boolean isNull = true;

    @Override
    public int layoutId() {
        return R.layout.fragment_my_work;
    }

    @Override
    public void init() {
        if (isNull){
            llyNull.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else {
            recyclerView.setVisibility(View.VISIBLE);
            llyNull.setVisibility(View.GONE);
        }
        initRcl();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void initRcl(){
        courseBeanList = new ArrayList<>();
        planCourseAdapter = new PlanCourseAdapter(getActivity(),courseBeanList,false,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(planCourseAdapter);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

}
