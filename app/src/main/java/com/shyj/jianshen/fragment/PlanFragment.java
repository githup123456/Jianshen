package com.shyj.jianshen.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.AdjustPlanActivity;
import com.shyj.jianshen.adapter.PlanCoursePageAdapter;
import com.shyj.jianshen.adapter.PlanDayAdapter;
import com.shyj.jianshen.key.IntentId;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanFragment extends BaseFragment {

    @BindView(R.id.fragment_plan_tv_week_num)
    TextView tvWeekNum;
    @BindView(R.id.fragment_plan_tv_week)
    TextView tvWeek;
    @BindView(R.id.fragment_plan_im_setting)
    ImageView imSetting;
    @BindView(R.id.fragment_plan_seek_bar)
    SeekBar seekBar;
    @BindView(R.id.fragment_plan_progress)
    TextView tvProgress;
    @BindView(R.id.fragment_plan_tv_seven_num)
    TextView tvSevenNum;
    @BindView(R.id.fragment_plan_rcl_day)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_plan_view_pager_2)
    ViewPager viewPager;

    private PlanDayAdapter planDayAdapter;
    private PlanCoursePageAdapter planCoursePageAdapter;
    @Override
    public int layoutId() {
        return R.layout.fragment_plan;
    }

    @Override
    public void init() {
        planDayAdapter = new PlanDayAdapter(getActivity(),getList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(planDayAdapter);
        planDayAdapter.setPlanAdapterClick(new PlanDayAdapter.PlanAdapterClick() {
            @Override
            public void dayOnClick(int position) {
                planDayAdapter.setSelectPosition(position);
                viewPager.setCurrentItem(position);
            }
        });
        showViewPager();
    }

    public void showViewPager(){
        planCoursePageAdapter = new PlanCoursePageAdapter(getFragmentManager(), getActivity(),getList(),0);
        viewPager.removeAllViews();
        viewPager.setAdapter(planCoursePageAdapter);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(1);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                planDayAdapter.setSelectPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<String> getList(){
     List<String> stringList = new ArrayList<>();
     for (int i=1;i<22;i++){
         stringList.add(i+"");
     }
     return stringList;
    }

    @OnClick(R.id.fragment_plan_im_setting)
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.fragment_plan_im_setting:
                Intent intent = new Intent(getActivity(), AdjustPlanActivity.class);
                startActivity(intent);
                break;
        }
    }
}
