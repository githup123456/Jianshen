package com.shyj.jianshen.activity;

import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.RetrofitApi;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.HashMap;
import java.util.logging.LogRecord;

import butterknife.BindView;
import butterknife.OnClick;

public class BuildProgressActivity extends BaseActivity{
    private static final String TAG = "jianShen";
    //等级，目标, 身体部位
    private String grand, purpose,bodyParts,sex;
    private int bodyPart;

    @BindView(R.id.build_progress_tv_content)
    TextView tvContent;
    @BindView(R.id.build_progress_colorful_view)
    ColorfulRingProgressView colorfulRingProgressView;
    @BindView(R.id.build_progress_tv_progress)
    TextView tvProgress;
    @BindView(R.id.build_progress_btn_start)
    Button btnStart;
    @BindView(R.id.build_progress_tv_end_title)
    TextView tvEndTitle;
    @BindView(R.id.build_progress_tv_title)
    TextView tvTitle;

    @Override
    public int layoutId() {
        return R.layout.activity_build_progress;
    }

    @Override
    public void init() {
        UsersBean usersBean = LitePal.findFirst(UsersBean.class);
        grand = usersBean.getLevel();
        purpose = usersBean.getGoal();
        bodyParts = usersBean.getFocusParts();
        sex = usersBean.getSexS();
        bodyPart = usersBean.getBodyParts();
        Log.e(TAG, "init: "+grand+"\n"+purpose );
        sendRequest();
        downTime();
    }

    private Long currentTime;
    private Runnable countDownRunnable;
    private int downTime = 6 * 1000;
    private int downTimeMax = 6 * 1000;
    private Handler mHandler = new Handler();

    private void downTime() {
        if (countDownRunnable == null) {
            countDownRunnable = new Runnable() {
                @Override
                public void run() {
                    Long timeInterval = System.currentTimeMillis() - currentTime;
                    if (timeInterval > 30) {
                        timeInterval = 30L;
                    }
                    downTime = (int) (downTime - timeInterval);
                    int percent = (downTimeMax-downTime)*100/downTimeMax;
                    colorfulRingProgressView.setPercent(percent);
                    tvProgress.setText(percent+"%");
                    Log.e(TAG, "run:"+percent );
                    if (percent<25){
                        String str=getString(R.string.build_progress_content_one)+" <font color='#19B55E'>"+purpose+"</font>...";
                        tvContent.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
                    }else if (percent>=25&&percent<50){
                        if (bodyPart != 3){
                            String str=getString(R.string.build_progress_content_two)+" <font color='#19B55E'>"+bodyParts+"</font>...";
                            tvContent.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
                        }
                    }else if (percent>=50&&percent<75){
                        String str=getString(R.string.build_progress_content_three_start)+" <font color='#19B55E'>"+grand+"</font> "+getString(R.string.build_progress_content_three_end);
                        tvContent.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
                    }else {
                        String str=getString(R.string.build_progress_content_four)+" <font color='#19B55E'>"+sex+"</font>...";
                        tvContent.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
                    }
                    if (downTime <= 0) {
                        showEnd();
                    } else {
                        currentTime = System.currentTimeMillis();
                        mHandler.postDelayed(this, 10);
                    }
                }
            };
        }
        currentTime = System.currentTimeMillis();
        mHandler.postDelayed(countDownRunnable, 10);
    }

    private void showEnd(){
        tvEndTitle.setText(purpose);
        tvEndTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.build_progress_end_title));
        tvContent.setText(getString(R.string.build_progress_end_content));
        btnStart.setVisibility(View.VISIBLE);
    }

    public void sendRequest(){
        UsersBean usersBean = LitePal.findFirst(UsersBean.class);
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sex",usersBean.getSex());
        hashMap.put("grade",usersBean.getGrade());
        hashMap.put("purpose",usersBean.getPurpose());
        hashMap.put("bodyParts",usersBean.getBodyParts());
        RetrofitApi.request(BuildProgressActivity.this, RetrofitApi.createApi(Api.class).getSelectPlanSmartRecommend(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                try {
                    Log.e(TAG, "onSuccess: "+data );

                }catch (Exception e){
                    showToast(e.getMessage());
                    Log.e(TAG, "getSelectPlanSmartRecommend:message ::"+e.getMessage() );
                }
            }

            @Override
            public void onNotNetWork() {
                showToast("网络连接失败");
            }

            @Override
            public void onFail(Throwable e) {

            }
        });
    }


    @OnClick({R.id.build_progress_btn_start})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.build_progress_btn_start:
                if (mHandler!=null&&countDownRunnable!=null){
                    mHandler.removeCallbacks(countDownRunnable);
                }
                Intent intent = new Intent(BuildProgressActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler!=null&&countDownRunnable!=null){
            mHandler.removeCallbacks(countDownRunnable);
        }
    }
}
