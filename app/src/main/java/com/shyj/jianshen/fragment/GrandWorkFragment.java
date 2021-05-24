package com.shyj.jianshen.fragment;

import android.os.Bundle;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;


public class GrandWorkFragment extends BaseFragment{

    public static GrandWorkFragment getInstance(int grand){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentId.WORKOUT_GRAND,grand);
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
    private int pageNum =1 ;

    @Override
    public int layoutId() {
        return R.layout.fragment_grand_work;
    }

    @Override
    public void init() {
        grand = getArguments().getInt(IntentId.WORKOUT_GRAND);
        initRcl();
    }

    public void initRcl(){
        courseBeanList = new ArrayList<>();
        planCourseAdapter = new PlanCourseAdapter(getActivity(),courseBeanList,false,false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(planCourseAdapter);
    }

    private void initPOST(){
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("grand",""+grand);
        hashMap.put("pageNum",pageNum);
        RetrofitApi.request(getActivity(), RetrofitApi.createApi(Api.class).getSelectCourseList(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                Log.e(IntentId.TAG, "GrandWork+onSuccess: "+data);
            }

            @Override
            public void onNotNetWork() {

            }

            @Override
            public void onFail(Throwable e) {
                Log.e(IntentId.TAG, "GrandWork+onFail: "+e.getMessage() );
            }
        });
    }
}
