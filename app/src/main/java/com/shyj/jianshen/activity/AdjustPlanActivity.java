package com.shyj.jianshen.activity;

import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AdjustPlanActivity extends BaseActivity {
    @BindView(R.id.top_bar_green_img_left)
    ImageView imgTopLeft;
    @BindView(R.id.top_bar_green_tv_right)
    TextView tvTopRight;
    @BindView(R.id.adjust_plan_tv_name)
    TextView tvName;
    @BindView(R.id.adjust_plan_tv_week_grand)
    TextView tvWeekGrand;
    @BindView(R.id.adjust_plan_tv_days)
    TextView tvDays;
    @BindView(R.id.adjust_plan_tv_min)
    TextView tvMinute;
    @BindView(R.id.adjust_plan_tv_lose_weight)
    TextView tvLoseWeight;

    @BindView(R.id.adjust_plan_group)
    RadioGroup radioGroup;
    @BindView(R.id.adjust_plan_group_goal)
    RadioGroup groupGoal;
    @BindView(R.id.adjust_plan_group_body)
    RadioGroup groupBody;
    @BindView(R.id.adjust_plan_group_difficulty)
    RadioGroup groupDifficulty;

    @BindView(R.id.adjust_plan_img_top_bg)
    ImageView imgTopBg;

    @BindView(R.id.adjust_plan_r_btn_goal)
    RadioButton btnGoal;
    @BindView(R.id.adjust_plan_r_btn_goal_one)
    RadioButton btnGoalOne;
    @BindView(R.id.adjust_plan_r_btn_goal_two)
    RadioButton btnGoalTwo;
    @BindView(R.id.adjust_plan_r_btn_goal_three)
    RadioButton btnGoalThree;
    @BindView(R.id.adjust_plan_r_btn_body)
    RadioButton btnBody;
    @BindView(R.id.adjust_plan_r_btn_body_one)
    RadioButton btnBodyOne;
    @BindView(R.id.adjust_plan_r_btn_body_two)
    RadioButton btnBodyTwo;
    @BindView(R.id.adjust_plan_r_btn_body_three)
    RadioButton btnBodyThree;
    @BindView(R.id.adjust_plan_r_btn_body_four)
    RadioButton btnBodyFour;
    @BindView(R.id.adjust_plan_r_btn_body_five)
    RadioButton btnBodyFive;
    @BindView(R.id.adjust_plan_r_btn_difficulty)
    RadioButton btnDifficulty;
    @BindView(R.id.adjust_plan_r_btn_difficulty_one)
    RadioButton btnDifficultyOne;
    @BindView(R.id.adjust_plan_r_btn_difficulty_two)
    RadioButton btnDifficultyTwo;
    @BindView(R.id.adjust_plan_r_btn_difficulty_three)
    RadioButton btnDifficultyThree;

    @Override
    public int layoutId() {
        return R.layout.activity_adjust_plan;
    }

    @Override
    public void init() {
        StatuBarUtils.setTranslucentStatus(AdjustPlanActivity.this);
        String str=getString(R.string.third_user_page_lose_weight)+" "+"<font color='#19B55E'>"+getString(R.string.keep_fit)+"</font>"+" "+getString(R.string.or)+" "+"<font color='#19B55E'>"+getString(R.string.build_muscle)+"</font>"+" "+getString(R.string.third_user_page_lose_weight_end);
        tvLoseWeight.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
        tvTopRight.setText(getResources().getString(R.string.done));
        initPlan();
        initUser();
        initGroup();
        initGoalGroup();
        initBodyGroup();
        initDifficultyGroup();
    }

    private PlanBean planBean;
    private void initPlan(){
        try {
            planBean = LitePal.findFirst(PlanBean.class,true);
            if (planBean!=null&&planBean.getPlanId()!=null){
                tvName.setText(planBean.getName());
                int days = planBean.getDayNum();
                if (days/7==3){
                    tvDays.setText("21");
                    if (planBean.getGrade()==1){
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.beginner));
                    }else if(planBean.getGrade()==2){
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.intermediate));
                    }else {
                        tvWeekGrand.setText(getResources().getString(R.string.three_week)+" · "+getString(R.string.advanced));
                    }
                }else {
                    tvDays.setText("28");
                    if (planBean.getGrade()==1){
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.beginner));
                    }else if(planBean.getGrade()==2){
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.intermediate));
                    }else {
                        tvWeekGrand.setText(getResources().getString(R.string.four_week)+" · "+getString(R.string.advanced));
                    }
                }
                String url = StringUtil.getPlanBgUrl(planBean.getMark());
                Glide.with(AdjustPlanActivity.this).load(url).apply(HelpUtils.getGreyError()).into(imgTopBg);
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



    private int grand = 1;
    private int bodyParts = 1;
    private int purpose = 3;

    private void initUser() {
        try {
            UsersBean usersBean = LitePal.findFirst(UsersBean.class);
            grand = usersBean.getGrade();
            bodyParts = usersBean.getBodyParts();
            purpose = usersBean.getPurpose();
        } catch (Exception r) {
            Log.e(TAG, "initUser() returned: " + r.getMessage());
        }
    }

    private void initGroup() {
        btnGoal.setChecked(true);
        btnGoal.setTextColor(Color.WHITE);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.adjust_plan_r_btn_goal:
                        btnGoal.setTextColor(Color.WHITE);
                        btnBody.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficulty.setTextColor(getResources().getColor(R.color.grey_67));
                        groupGoal.setVisibility(View.VISIBLE);
                        groupBody.setVisibility(View.GONE);
                        tvLoseWeight.setVisibility(View.GONE);
                        groupDifficulty.setVisibility(View.GONE);
                        break;
                    case R.id.adjust_plan_r_btn_body:
                        btnBody.setTextColor(Color.WHITE);
                        btnGoal.setTextColor(getResources().getColor(R.color.grey_67));
                        UsersBean usersBean  = LitePal.findFirst(UsersBean.class);
                        if (usersBean.getPurpose()!=3){
                            groupBody.setVisibility(View.VISIBLE);
                            tvLoseWeight.setVisibility(View.GONE);
                        }else {
                            groupBody.setVisibility(View.GONE);
                            tvLoseWeight.setVisibility(View.VISIBLE);
                            usersBean.setBodyParts(1);
                            usersBean.save();
                        }
                        btnDifficulty.setTextColor(getResources().getColor(R.color.grey_67));
                        groupGoal.setVisibility(View.GONE);
                        groupDifficulty.setVisibility(View.GONE);
                        break;
                    case R.id.adjust_plan_r_btn_difficulty:
                        btnDifficulty.setTextColor(Color.WHITE);
                        btnBody.setTextColor(getResources().getColor(R.color.grey_67));
                        btnGoal.setTextColor(getResources().getColor(R.color.grey_67));
                        groupGoal.setVisibility(View.GONE);
                        groupBody.setVisibility(View.GONE);
                        tvLoseWeight.setVisibility(View.GONE);
                        groupDifficulty.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
    }

    private void initGoalGroup() {
        switch (purpose) {
            case 1:
                btnGoalTwo.setChecked(true);
                btnGoalOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnGoalTwo.setTextColor(getResources().getColor(R.color.white));
                btnGoalThree.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 2:
                btnGoalThree.setChecked(true);
                btnGoalOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnGoalTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnGoalThree.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                btnGoalOne.setChecked(true);
                btnGoalOne.setTextColor(getResources().getColor(R.color.white));
                btnGoalTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnGoalThree.setTextColor(getResources().getColor(R.color.grey_67));
                break;
        }
        groupGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                UsersBean usersBean = LitePal.findFirst(UsersBean.class);
                switch (checkedId) {
                    case R.id.adjust_plan_r_btn_goal_one:
                        Log.e("TAG", "onCheckedChanged:1 ");
                        usersBean.setPurpose(3);
                        usersBean.setGoal(getString(R.string.lose_weight));
                        btnGoalOne.setTextColor(getResources().getColor(R.color.white));
                        btnGoalTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnGoalThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_goal_two:
                        Log.e("TAG", "onCheckedChanged:2 ");
                        usersBean.setPurpose(1);
                        usersBean.setGoal(getString(R.string.keep_fit));
                        btnGoalOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnGoalTwo.setTextColor(getResources().getColor(R.color.white));
                        btnGoalThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_goal_three:
                        Log.e("TAG", "onCheckedChanged: 3");
                        usersBean.setPurpose(2);
                        usersBean.setGoal(getString(R.string.build_muscle));
                        btnGoalOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnGoalTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnGoalThree.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
                usersBean.save();
            }
        });
    }

    private void initBodyGroup() {
        switch (bodyParts) {
            case 1:
                btnBodyOne.setChecked(true);
                btnBodyOne.setTextColor(getResources().getColor(R.color.white));
                btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 4:
                btnBodyTwo.setChecked(true);
                btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyTwo.setTextColor(getResources().getColor(R.color.white));
                btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 2:
                btnBodyThree.setChecked(true);
                btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyThree.setTextColor(getResources().getColor(R.color.white));
                btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 5:
                btnBodyFour.setChecked(true);
                btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFour.setTextColor(getResources().getColor(R.color.white));
                btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 3:
                btnBodyFive.setChecked(true);
                btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                btnBodyFive.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        groupBody.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                UsersBean usersBean = LitePal.findFirst(UsersBean.class);
                switch (checkedId) {
                    case R.id.adjust_plan_r_btn_body_one:
                        usersBean.setBodyParts(1);
                        usersBean.setFocusParts(getString(R.string.whole_body));
                        btnBodyOne.setTextColor(getResources().getColor(R.color.white));
                        btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_body_two:
                        usersBean.setBodyParts(4);
                        usersBean.setFocusParts(getString(R.string.chest_butt));
                        btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyTwo.setTextColor(getResources().getColor(R.color.white));
                        btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_body_three:
                        usersBean.setBodyParts(2);
                        usersBean.setFocusParts(getString(R.string.abs));
                        btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyThree.setTextColor(getResources().getColor(R.color.white));
                        btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_body_four:
                        usersBean.setBodyParts(5);
                        usersBean.setFocusParts(getString(R.string.arm));
                        btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFour.setTextColor(getResources().getColor(R.color.white));
                        btnBodyFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_body_five:
                        usersBean.setBodyParts(3);
                        usersBean.setFocusParts(getString(R.string.leg));
                        btnBodyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFour.setTextColor(getResources().getColor(R.color.grey_67));
                        btnBodyFive.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
                usersBean.save();
            }
        });
    }

    private void initDifficultyGroup() {
        switch (grand) {
            case 1:
                btnDifficultyOne.setChecked(true);
                btnDifficultyOne.setTextColor(getResources().getColor(R.color.white));
                btnDifficultyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnDifficultyThree.setTextColor(getResources().getColor(R.color.grey_67));
                break;
            case 2:
                btnDifficultyTwo.setChecked(true);
                btnDifficultyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnDifficultyThree.setTextColor(getResources().getColor(R.color.grey_67));
                btnDifficultyTwo.setTextColor(getResources().getColor(R.color.white));
                break;
            case 3:
                btnDifficultyThree.setChecked(true);
                btnDifficultyOne.setTextColor(getResources().getColor(R.color.grey_67));
                btnDifficultyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                btnDifficultyThree.setTextColor(getResources().getColor(R.color.white));
                break;
        }
        groupDifficulty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                UsersBean usersBean = LitePal.findFirst(UsersBean.class);
                switch (checkedId) {
                    case R.id.adjust_plan_r_btn_difficulty_one:
                        usersBean.setGrade(1);
                        usersBean.setLevel(getString(R.string.beginner));
                        btnDifficultyOne.setTextColor(getResources().getColor(R.color.white));
                        ;
                        btnDifficultyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficultyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.adjust_plan_r_btn_difficulty_two:
                        usersBean.setGrade(2);
                        usersBean.setLevel(getString(R.string.intermediate));
                        btnDifficultyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficultyThree.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficultyTwo.setTextColor(getResources().getColor(R.color.white));
                        break;
                    case R.id.adjust_plan_r_btn_difficulty_three:
                        usersBean.setGrade(3);
                        usersBean.setLevel(getString(R.string.advanced));
                        btnDifficultyOne.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficultyTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        btnDifficultyThree.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
                usersBean.save();
            }
        });
    }

    @OnClick({R.id.adjust_plan_btn_adjust_end,R.id.top_bar_green_tv_right,R.id.top_bar_green_img_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.adjust_plan_btn_adjust_end:
                showEndDialog();
                break;
            case R.id.top_bar_green_img_left:
                finish();
                break;
            case R.id.top_bar_green_tv_right:
                showDonePlan();
                break;

        }
    }

    private void showEndDialog(){
        WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
        View endView = WindowUtils.centerShow(AdjustPlanActivity.this,R.layout.dialog_center_confirm);
        TextView tvContent = endView.findViewById(R.id.dialog_center_tv_content);
        tvContent.setText(getResources().getString(R.string.sure_end_program));
        TextView tvCancel = endView.findViewById(R.id.dialog_center_tv_cancel);
        tvCancel.setText(getResources().getString(R.string.window_cancel));
        tvCancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
            }
        });
        TextView tvConfirm = endView.findViewById(R.id.dialog_center_tv_confirm);
        tvConfirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
                finish();
            }
        });
    }

    private void showDonePlan(){
        WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
        View endView = WindowUtils.centerShow(AdjustPlanActivity.this,R.layout.dialog_center_confirm);
        TextView tvTitle = endView.findViewById(R.id.dialog_center_tv_title);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.adjust_plan));
        TextView tvContent = endView.findViewById(R.id.dialog_center_tv_content);
        tvContent.setText(getResources().getString(R.string.your_plan_adjust));
        TextView tvCancel = endView.findViewById(R.id.dialog_center_tv_cancel);
        tvCancel.setText(getResources().getString(R.string.no));
        tvCancel.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
            }
        });
        TextView tvConfirm = endView.findViewById(R.id.dialog_center_tv_confirm);
        tvConfirm.setText(getString(R.string.yes));
        tvConfirm.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissCenterWindow(AdjustPlanActivity.this);
                Intent intent = new Intent();
                intent.setClass(AdjustPlanActivity.this,BuildProgressActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

}
