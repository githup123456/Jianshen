package com.shyj.jianshen.activity;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.IBottomView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.footer.BallPulseView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.PlanCourseAdapter;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.view.DelEditText;
import com.shyj.jianshen.view.RefreshFooterView;
import com.shyj.jianshen.view.RefreshHeadView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseSearchResultActivity extends BaseActivity {
    @BindView(R.id.result_search_et_search)
    DelEditText delEditText;
    @BindView(R.id.result_search_rcl)
    RecyclerView rclResult;
    @BindView(R.id.result_search_lly_null)
    LinearLayout llyNull;
    @BindView(R.id.result_search_trl)
    TwinklingRefreshLayout refreshLayout;

    private List<CourseBean> courseBeanList;
    private PlanCourseAdapter planCourseAdapter;

    private String keyWord = "";
    private int pageNum = 1;

    @Override
    public int layoutId() {
        return R.layout.activity_course_search;
    }

    @Override
    public void init() {
        keyWord = getIntent().getStringExtra(IntentId.SEARCH_KEYWORD);
        delEditText.setText(keyWord);
        refreshLayout.setHeaderView(new RefreshHeadView(CourseSearchResultActivity.this));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                refreshLayout.setBottomView(new BallPulseView(CourseSearchResultActivity.this));
                pageNum = 1;
                initPOST();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                initPOST();
            }
        });
        delEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) delEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CourseSearchResultActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    refreshLayout.startRefresh();
                    return true;
                }
                return false;
            }
        });

        courseBeanList = new ArrayList<>();
        planCourseAdapter = new PlanCourseAdapter(CourseSearchResultActivity.this, courseBeanList, false, false);
        rclResult.setLayoutManager(new LinearLayoutManager(CourseSearchResultActivity.this));
        rclResult.setAdapter(planCourseAdapter);
        if (delEditText.getText().toString() != null && delEditText.getText().toString().length() > 0) {
            showLoading();
            initPOST();
        }
    }

    @OnClick({R.id.result_search_img_search, R.id.result_search_tv_cancel})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.result_search_img_search:
                refreshLayout.startRefresh();
                break;
            case R.id.result_search_tv_cancel:
                finish();
                break;
        }
    }

    private void initPOST() {
        showLoading();
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("keyWord", delEditText.getText().toString());
        hashMap.put("pageNum", pageNum);
        Log.e(TAG, "initPOST: " + keyWord + "\n" + pageNum);
        RetrofitApi.request(CourseSearchResultActivity.this, RetrofitApi.createApi(Api.class).getSearchCourseList(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                hiddenLoadingView();
                refreshLayout.setVisibility(View.VISIBLE);
                llyNull.setVisibility(View.GONE);
                Log.e(IntentId.TAG, "result+onSuccess: " + data);
                try {
                    List<CourseBean> courseList = new Gson().fromJson(data, new TypeToken<List<CourseBean>>() {}.getType());
                    if (courseList != null && courseList.size() > 0) {
                        if (pageNum == 1) {
                            courseBeanList = courseList;
                        } else {
                            courseBeanList.addAll(courseList);
                        }
                        planCourseAdapter.addCourseBeanList(courseBeanList);
                    } else {
                        if (pageNum == 1) {
                            refreshLayout.setVisibility(View.GONE);
                            llyNull.setVisibility(View.VISIBLE);
                        } else {
                            refreshLayout.setBottomView(new RefreshFooterView(CourseSearchResultActivity.this));
                        }
                    }

                } catch (Exception e) {
                    Log.e(TAG, "onSuccess:catch " + e.getMessage());
                }

                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onNotNetWork() {
                hiddenLoadingView();
                if (pageNum == 1) {
                    refreshLayout.setVisibility(View.GONE);
                    llyNull.setVisibility(View.VISIBLE);
                }
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void hasMore() {
                hiddenLoadingView();
                if (pageNum == 1) {
                    refreshLayout.setVisibility(View.GONE);
                    llyNull.setVisibility(View.VISIBLE);
                }
                refreshLayout.setBottomView(new RefreshFooterView(CourseSearchResultActivity.this));
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }

            @Override
            public void onFail(Throwable e) {
                hiddenLoadingView();
                Log.e(IntentId.TAG, "GrandWork+onFail: " + e.getMessage());
                if (pageNum == 1) {
                    refreshLayout.setVisibility(View.GONE);
                    llyNull.setVisibility(View.VISIBLE);
                }
                refreshLayout.finishLoadmore();
                refreshLayout.finishRefreshing();
            }
        });
    }
}
