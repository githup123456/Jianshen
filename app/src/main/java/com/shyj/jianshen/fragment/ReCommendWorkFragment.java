package com.shyj.jianshen.fragment;

import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.RecommendWorkAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Retrofit;

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
        showLoading();
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
        rclAbs.post(new Runnable() {
            @Override
            public void run() {
                initRequest("1",adapterAbs);
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
        rclFat.post(new Runnable() {
            @Override
            public void run() {
                initRequest("2",adapterFat);
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
        rclStretch.post(new Runnable() {
            @Override
            public void run() {
                initRequest("3",adapterStretch);
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
        rclChest.post(new Runnable() {
            @Override
            public void run() {
                initRequest("4",adapterChest);
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
        rclWarm.post(new Runnable() {
            @Override
            public void run() {
                initRequest("5",adapterWarm);
            }
        });

    }

    private void initRequest(String type,RecommendWorkAdapter recommendWorkAdapter){
        Map<String,Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("type",type);
        RetrofitApi.request(getActivity(), RetrofitApi.createApi(Api.class).getRecommendCourseList(stringObjectMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                hiddenLoadingView();
                try {
                    List<CourseBean> courseList = new Gson().fromJson(data,new TypeToken<List<CourseBean>>(){}.getType());
                    if (courseList!=null&&courseList.size()>0){
                        recommendWorkAdapter.addCourseBeanList(courseList);
                    }

                }catch (Exception e){
                    Log.e(TAG, "onSuccess:catch " +e.getMessage());
                }
            }

            @Override
            public void onNotNetWork() {
                hiddenLoadingView();
            }

            @Override
            public void hasMore() {
                hiddenLoadingView();
            }

            @Override
            public void onFail(Throwable e) {
                hiddenLoadingView();
            }
        });
    }
}
