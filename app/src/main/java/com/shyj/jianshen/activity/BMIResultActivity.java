package com.shyj.jianshen.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

public class BMIResultActivity extends BaseActivity{

    @BindView(R.id.bmi_result_plan_tv_name)
    TextView tvName;
    @BindView(R.id.bmi_result_plan_tv_week_grand)
    TextView tvWeekGrand;
    @BindView(R.id.bmi_result_plan_tv_days)
    TextView tvDays;
    @BindView(R.id.bmi_result_plan_tv_min)
    TextView tvMinute;
    @BindView(R.id.bmi_result_plan_img_top_bg)
    ImageView imgTopBg;

    @Override
    public int layoutId() {
        return R.layout.activity_bmi_result;
    }

    @Override
    public void init() {
        initPlan();
    }

    private PlanBean planBean;
    private void initPlan(){
        try {
            planBean = LitePal.findFirst(PlanBean.class,true);
            if (planBean!=null&&planBean.getPlanId()!=null){
                int days = planBean.getDayNum();
                tvName.setText(planBean.getName());
                tvDays.setText(days+"");
                if (days/7==3){
                    if (planBean.getGrade()==1){
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.beginner));
                    }else if(planBean.getGrade()==2){
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.intermediate));
                    }else {
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.advanced));
                    }
                }else {
                    if (planBean.getGrade()==1){
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.beginner));
                    }else if(planBean.getGrade()==2){
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.intermediate));
                    }else {
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.advanced));
                    }
                }

                String url = StringUtil.getPlanBgUrl(planBean.getMark());
                Glide.with(BMIResultActivity.this).load(url).apply(HelpUtils.getGreyError()).into(imgTopBg);

                CourseBean courseBean = LitePal.findFirst(CourseBean.class);
                if (courseBean!=null){
                    int minute = (int)courseBean.getDuration()/10/60;
                    tvMinute .setText((float)minute/100 +" min");
                }
            }
        }catch (Exception e){
            Log.e(TAG, "initPlan: "+e.getMessage() );
        }
    }

    @OnClick({R.id.top_bar_green_img_left,R.id.bmi_result_plan_btn_start})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bmi_result_plan_btn_start:
            case R.id.top_bar_green_img_left:
                mainIntent();
                break;
        }
    }

    private void mainIntent(){
        Intent intent = new Intent(BMIResultActivity.this,MainActivity.class);
        intent.putExtra(IntentId.PLAN_SWITCH,true);
        startActivity(intent);
        finish();
    }

}
