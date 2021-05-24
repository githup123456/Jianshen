package com.shyj.jianshen.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.RecommendWorkAdapter;
import com.shyj.jianshen.bean.CourseBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ReCommendWorkFragment extends BaseFragment{

    @BindView(R.id.recommend_work_rcl_abs)
    RecyclerView rclAbs;
    @BindView(R.id.recommend_work_rcl_fat)
    RecyclerView rclFat;
    @BindView(R.id.recommend_work_rcl_stretch)
    RecyclerView rclStretch;
    @BindView(R.id.recommend_work_rcl_chest)
    RecyclerView rclChest;
    @BindView(R.id.recommend_work_rcl_warm)
    RecyclerView rclWarm;

    private  RecommendWorkAdapter  adapterAbs,adapterFat,adapterStretch,adapterChest,adapterWarm;
    private List<CourseBean>  listAbs,listFat,listStretch,listChest,listWarm;

    @Override
    public int layoutId() {
        return R.layout.fragment_recommend_work;
    }

    @Override
    public void init() {
        initAbs();
        initFat();
        initStretch();
        initChest();
        initWarm();
    }

    private void initAbs(){
        listAbs = new ArrayList<>();
        adapterAbs = new RecommendWorkAdapter(getActivity(),listAbs);
        LinearLayoutManager absLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclAbs.setLayoutManager(absLayoutManager);
        rclAbs.setAdapter(adapterAbs);
        adapterAbs.setOnRecommendClick(new RecommendWorkAdapter.OnRecommendClick() {
            @Override
            public void onClick(int position) {

            }
        });
    }
    private void initFat(){
        listFat = new ArrayList<>();
        adapterFat = new RecommendWorkAdapter(getActivity(),listFat);
        LinearLayoutManager fatLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclFat.setLayoutManager(fatLayoutManager);
        rclFat.setAdapter(adapterFat);
        adapterFat.setOnRecommendClick(new RecommendWorkAdapter.OnRecommendClick() {
            @Override
            public void onClick(int position) {

            }
        });
    }
    private void initStretch(){
        listStretch = new ArrayList<>();
        adapterStretch = new RecommendWorkAdapter(getActivity(),listStretch);
        LinearLayoutManager stretchLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclStretch.setLayoutManager(stretchLayoutManager);
        rclStretch.setAdapter(adapterStretch);
        adapterStretch.setOnRecommendClick(new RecommendWorkAdapter.OnRecommendClick() {
            @Override
            public void onClick(int position) {

            }
        });
    }
    private void initChest(){
        listChest = new ArrayList<>();
        adapterChest = new RecommendWorkAdapter(getActivity(),listChest);
        LinearLayoutManager chestLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclChest.setLayoutManager(chestLayoutManager);
        rclChest.setAdapter(adapterChest);
        adapterChest.setOnRecommendClick(new RecommendWorkAdapter.OnRecommendClick() {
            @Override
            public void onClick(int position) {

            }
        });
    }
    private void initWarm(){
        listWarm = new ArrayList<>();
        adapterWarm = new RecommendWorkAdapter(getActivity(),listWarm);
        LinearLayoutManager warmLayoutManager = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        rclWarm.setLayoutManager(warmLayoutManager);
        rclWarm.setAdapter(adapterWarm);
        adapterWarm.setOnRecommendClick(new RecommendWorkAdapter.OnRecommendClick() {
            @Override
            public void onClick(int position) {

            }
        });
    }
}
