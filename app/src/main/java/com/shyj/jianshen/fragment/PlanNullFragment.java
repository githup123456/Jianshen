package com.shyj.jianshen.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.BmiInformationActivity;
import com.shyj.jianshen.activity.PlanDetailActivity;
import com.shyj.jianshen.adapter.PlanTypeAdapter;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.utils.HelpUtils;

import org.json.JSONException;
import org.litepal.LitePal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PlanNullFragment extends BaseFragment {

    @BindView(R.id.null_plan_rcl_beginner)
    RecyclerView rclBeginner;
    @BindView(R.id.null_plan_rcl_popular)
    RecyclerView rclPopular;
    @BindView(R.id.null_plan_rcl_intermediate)
    RecyclerView rclIntermediate;
    @BindView(R.id.null_plan_rcl_advanced)
    RecyclerView rclAdvanced;

    private PlanTypeAdapter adapterPopular, adapterBeginner, adapterInter, adapterAdvanced;

    @Override
    public int layoutId() {
        return R.layout.fragment_null_plan;
    }

    @Override
    public void init() {
        initPopular();
        initBeginner();
        initInter();
        initAdvanced();
    }

    @OnClick({R.id.null_plan_try_now,R.id.null_plan_start})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.null_plan_try_now:
                getActivity().getSharedPreferences(PreferencesName.VIP, Context.MODE_PRIVATE).edit().putBoolean(PreferencesName.IS_VIP,true).commit();
                break;
            case R.id.null_plan_start:
                Intent intent = new Intent(getActivity(), BmiInformationActivity.class);
                startActivity(intent);
                break;
        }
    }

    private int pagePop = 1;
    private void initPopular() {
        adapterPopular = new PlanTypeAdapter(getActivity());
        LinearLayoutManager popLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rclPopular.setLayoutManager(popLayoutManager);
        rclPopular.setAdapter(adapterPopular);
        rclPopular.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        pagePop++;
                        requestRecommend(pagePop);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //dx>0:向右滑动,dx<0:向左滑动
                //dy>0:向下滑动,dy<0:向上滑动
                if (dx > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        adapterPopular.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                intentDetail(adapterPopular.getPlanBeanList().get(position));
            }
        });
        requestRecommend(pagePop);
    }


    private int pageBegin = 1;

    private void initBeginner() {
        adapterBeginner = new PlanTypeAdapter(getActivity());
        LinearLayoutManager begLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rclBeginner.setLayoutManager(begLayoutManager);
        rclBeginner.setAdapter(adapterBeginner);
        rclBeginner.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        pageBegin++;
                        requestGrade(1,pageBegin);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //dx>0:向右滑动,dx<0:向左滑动
                //dy>0:向下滑动,dy<0:向上滑动
                if (dx > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        adapterBeginner.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                intentDetail(adapterBeginner.getPlanBeanList().get(position));
            }
        });
        requestGrade(1, pageBegin);
    }

    private int pageInter = 1;

    private void initInter() {
        adapterInter = new PlanTypeAdapter(getActivity());
        LinearLayoutManager intLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rclIntermediate.setLayoutManager(intLayoutManager);
        rclIntermediate.setAdapter(adapterInter);
        rclIntermediate.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        pageInter++;
                        requestGrade(2,pageInter);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //dx>0:向右滑动,dx<0:向左滑动
                //dy>0:向下滑动,dy<0:向上滑动
                if (dx > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        adapterInter.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                intentDetail(adapterInter.getPlanBeanList().get(position));
            }
        });
        requestGrade(2, pageInter);
    }

    private int pageAd = 1;

    private void initAdvanced() {
        adapterAdvanced = new PlanTypeAdapter(getActivity());
        LinearLayoutManager adLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        rclAdvanced.setLayoutManager(adLayoutManager);
        rclAdvanced.setAdapter(adapterAdvanced);
        rclAdvanced.setOnScrollListener(new RecyclerView.OnScrollListener() {
            //用来标记是否正在向最后一个滑动
            boolean isSlidingToLast = false;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //设置什么布局管理器,就获取什么的布局管理器
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当停止滑动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    //获取最后一个完全显示的ItemPosition ,角标值
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    //所有条目,数量值
                    int totalItemCount = manager.getItemCount();

                    // 判断是否滚动到底部，并且是向右滚动
                    if (lastVisibleItem == (totalItemCount - 1) && isSlidingToLast) {
                        //加载更多功能的代码
                        pageAd++;
                        requestGrade(3,pageAd);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //dx用来判断横向滑动方向，dy用来判断纵向滑动方向
                //dx>0:向右滑动,dx<0:向左滑动
                //dy>0:向下滑动,dy<0:向上滑动
                if (dx > 0) {
                    isSlidingToLast = true;
                } else {
                    isSlidingToLast = false;
                }
            }
        });
        adapterAdvanced.setOnItemClick(new PlanTypeAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                intentDetail(adapterAdvanced.getPlanBeanList().get(position));
            }
        });
        requestGrade(3, pageAd);
    }

    private void intentDetail(PlanBean planBean) {
        Intent intent = new Intent(getActivity(), PlanDetailActivity.class);
        intent.putExtra(IntentId.PLAN_BEAN, (Serializable) planBean);
        startActivity(intent);
    }

    public void requestRecommend(int pageNum) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("pageNum", pageNum);
        RetrofitApi.request(getActivity(), RetrofitApi.createApi(Api.class).getSelectPlanRecommend(stringObjectMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                try {
                    Log.e(TAG, "onSuccess: "+data );
                    List<PlanBean> planBeanList = new Gson().fromJson(data, new TypeToken<List<PlanBean>>() {
                    }.getType());
                    if (planBeanList != null) {
                        if (pageNum == 1) {
                            adapterPopular.setPlanBeanList(planBeanList);
                        } else {
                            adapterPopular.addPlanBeanList(planBeanList);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ");
                }
            }

            @Override
            public void onNotNetWork() {

            }

            @Override
            public void hasMore() {
                showToast(R.string.no_more_data);
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }

    public void requestGrade(int grade, int pageNum) {
        Map<String, Object> stringObjectMap = new HashMap<>();
        stringObjectMap.put("grade", grade);
        stringObjectMap.put("pageNum", pageNum);
        RetrofitApi.request(getActivity(), RetrofitApi.createApi(Api.class).getSelectPlanGrade(stringObjectMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                try {
                    Log.e(TAG, "onSuccess: "+data );
                    List<PlanBean> planBeanList = new Gson().fromJson(data, new TypeToken<List<PlanBean>>() {
                    }.getType());
                    Log.e(TAG, "onSuccess: "+planBeanList.size() );
                    if (planBeanList != null) {
                        if (pageNum == 1) {
                            if (grade == 1) {
                                adapterBeginner.setPlanBeanList(planBeanList);
                            } else if (grade == 2) {
                                adapterInter.setPlanBeanList(planBeanList);
                            } else {
                                adapterAdvanced.setPlanBeanList(planBeanList);
                            }
                        } else {
                            if (grade == 1) {
                                adapterBeginner.addPlanBeanList(planBeanList);
                            } else if (grade == 2) {
                                adapterInter.addPlanBeanList(planBeanList);
                            } else {
                                adapterAdvanced.addPlanBeanList(planBeanList);
                            }
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onSuccess: ");
                }
            }

            @Override
            public void onNotNetWork() {

            }

            @Override
            public void hasMore() {
                showToast(R.string.no_more_data);
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }
}
