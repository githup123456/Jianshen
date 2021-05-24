package com.shyj.jianshen.fragment;

import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanTypeAdapter;
import com.shyj.jianshen.bean.PlanBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlanNullFragment extends BaseFragment{

    @BindView(R.id.null_plan_rcl_beginner)
    RecyclerView rclBeginner;
    @BindView(R.id.null_plan_rcl_popular)
    RecyclerView rclPopular;
    @BindView(R.id.null_plan_rcl_intermediate)
    RecyclerView rclIntermediate;
    @BindView(R.id.null_plan_rcl_advanced)
    RecyclerView rclAdvanced;

    private PlanTypeAdapter adapterPopular,adapterBeginner,adapterInter,adapterAdvanced;
    private List<PlanBean> listPopular,listBeginner,listInter,listAdvanced;

    @Override
    public int layoutId() {
        return R.layout.fragment_null_plan;
    }

    @Override
    public void init() {
        listPopular = new ArrayList<>();
        listBeginner = new ArrayList<>();
        listInter = new ArrayList<>();
        listAdvanced = new ArrayList<>();
        initPopular();
        initBeginner();
        initInter();
        initAdvanced();
    }

    private void initPopular(){
        adapterPopular = new PlanTypeAdapter(getActivity());
        LinearLayoutManager popLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclPopular.setLayoutManager(popLayoutManager);
        rclPopular.setAdapter(adapterPopular);
        adapterPopular.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    private void initBeginner(){
        adapterBeginner = new PlanTypeAdapter(getActivity());
        LinearLayoutManager begLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclBeginner.setLayoutManager(begLayoutManager);
        rclBeginner.setAdapter(adapterBeginner);
        adapterBeginner.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    private void initInter(){
        adapterInter = new PlanTypeAdapter(getActivity());
        LinearLayoutManager intLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclIntermediate.setLayoutManager(intLayoutManager);
        rclIntermediate.setAdapter(adapterInter);
        adapterInter.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }

    private void initAdvanced(){
        adapterAdvanced = new PlanTypeAdapter(getActivity());
        LinearLayoutManager adLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclAdvanced.setLayoutManager(adLayoutManager);
        rclAdvanced.setAdapter(adapterAdvanced);
        adapterAdvanced.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {

            }
        });
    }
}
