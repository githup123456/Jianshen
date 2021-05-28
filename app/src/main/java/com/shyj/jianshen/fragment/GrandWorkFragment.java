package com.shyj.jianshen.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.CourseSearchResultActivity;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.view.RefreshFooterView;
import com.shyj.jianshen.view.RefreshHeadView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;


public class GrandWorkFragment extends BaseFragment {

    public static GrandWorkFragment getInstance(int grand) {
        Bundle bundle = new Bundle();
        bundle.putInt(IntentId.WORKOUT_GRAND, grand);
        GrandWorkFragment fragment = new GrandWorkFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.grand_work_trl)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.grand_work_rcl)
    RecyclerView recyclerView;

    private PlanCourseAdapter planCourseAdapter;
    private List<CourseBean> courseBeanList;
    private int grand;
    private int pageNum = 1;

    @Override
    public int layoutId() {
        return R.layout.fragment_grand_work;
    }

    @Override
    public void init() {
        grand = getArguments().getInt(IntentId.WORKOUT_GRAND, 1);
        initRcl();
    }

    public void initRcl() {
        courseBeanList = new ArrayList<>();
        refreshLayout.setHeaderView(new RefreshHeadView(getActivity()));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                pageNum = 1;
                refreshLayout.setBottomView(new BallPulseView(getActivity()));
                initPOST();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                initPOST();
            }
        });
        planCourseAdapter = new PlanCourseAdapter(getActivity(), courseBeanList, false, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(planCourseAdapter);
        initPOST();
    }

    private void initPOST() {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("grade", "" + grand);
        hashMap.put("pageNum", pageNum);
        Log.e(TAG, "initPOST: " + grand + "\n" + pageNum);
        RetrofitApi.request(getActivity(), RetrofitApi.createApi(Api.class).getSearchCourseList(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                Log.e(IntentId.TAG, "GrandWork+onSuccess: " + data);
                try {
                    List<CourseBean> courseList = new Gson().fromJson(data,new TypeToken<List<CourseBean>>(){}.getType());
                    if (courseList!=null&&courseList.size()>0){
                        if (pageNum==1){
                            courseBeanList = courseList;
                        }else {
                            courseBeanList.addAll(courseList);
                        }
                        planCourseAdapter.addCourseBeanList(courseBeanList);
                    }else {
                        refreshLayout.setBottomView(new RefreshFooterView(getActivity()));
                    }

                }catch (Exception e){
                    Log.e(TAG, "onSuccess:catch " +e.getMessage());
                }

                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onNotNetWork() {

                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void hasMore() {
                refreshLayout.setBottomView(new RefreshFooterView(getActivity()));
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onFail(Throwable e) {
                Log.e(IntentId.TAG, "GrandWork+onFail: " + e.getMessage());
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }
        });
    }
}
