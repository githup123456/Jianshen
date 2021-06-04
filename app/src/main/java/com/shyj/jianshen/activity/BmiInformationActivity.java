package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.OnClick;

public class BmiInformationActivity extends BaseActivity {

    /**
     * bmi goal
     */
    @BindView(R.id.bmi_goal_radio_group)
    RadioGroup radioGroupGoal;
    @BindView(R.id.bmi_goal_radio_btn_1)
    RadioButton goalLoseWeight;
    @BindView(R.id.bmi_goal_radio_btn_2)
    RadioButton goalKeepFit;
    @BindView(R.id.bmi_goal_radio_btn_3)
    RadioButton goalBuildMuscle;
    /**
     * bmi body
     */
    @BindView(R.id.bmi_body_lose_weight)
    TextView tvBodyLostWeight;
    @BindView(R.id.bmi_body_select_one)
    LinearLayout llyBody;
    @BindView(R.id.bmi_body_radio_group)
    RadioGroup radioGroupBody;
    @BindView(R.id.bmi_body_radio_btn_1)
    RadioButton bodyButtonOne;
    @BindView(R.id.bmi_body_radio_btn_2)
    RadioButton bodyBtnTwo;
    @BindView(R.id.bmi_body_radio_btn_3)
    RadioButton bodyBtnThree;
    @BindView(R.id.bmi_body_radio_btn_4)
    RadioButton bodyButtonFour;
    @BindView(R.id.bmi_body_radio_btn_5)
    RadioButton bodyButtonFive;
    /**
     * bmi grade
     */
    @BindView(R.id.bmi_grade_radio_group)
    RadioGroup radioGroupGrade;
    @BindView(R.id.bmi_grade_radio_btn_one)
    RadioButton gradeBtnOne;
    @BindView(R.id.bmi_grade_radio_btn_two)
    RadioButton gradeBtnTwo;
    @BindView(R.id.bmi_grade_radio_btn_three)
    RadioButton gradeBtnThree;
    @BindView(R.id.bmi_grade_tv_1_top)
    TextView gradeTvTopOne;
    @BindView(R.id.bmi_grade_tv_2_top)
    TextView gradeTvTopTwo;
    @BindView(R.id.bmi_grade_tv_3_top)
    TextView gradeTvTopThree;
    @BindView(R.id.bmi_grade_tv_1_btm)
    TextView gradeTvBtmOne;
    @BindView(R.id.bmi_grade_tv_2_btm)
    TextView gradeTvBtmTwo;
    @BindView(R.id.bmi_grade_tv_3_btm)
    TextView gradeTvBtmThree;

    @BindView(R.id.bmi_info_btm_btn)
    Button btnNext;
    @BindView(R.id.bmi_info_page_one)
    LinearLayout llyOne;
    @BindView(R.id.bmi_info_page_two)
    LinearLayout llyTwo;
    @BindView(R.id.bmi_info_page_three)
    LinearLayout llyThree;
    @BindView(R.id.bmi_info_page_four)
    LinearLayout llyFour;

    private UsersBean usersBean;

    @Override
    public int layoutId() {
        return R.layout.activity_bmi_information;
    }

    private float bmi = 0.0f;

    @Override
    public void init() {
        StatuBarUtils.setWhiteTop(BmiInformationActivity.this, Color.WHITE, true);
        String str = getString(R.string.third_user_page_lose_weight) + " " + "<font color='#19B55E'>" + getString(R.string.keep_fit) + "</font>" + " " + getString(R.string.or) + " " + "<font color='#19B55E'>" + getString(R.string.build_muscle) + "</font>" + " " + getString(R.string.third_user_page_lose_weight_end);
        tvBodyLostWeight.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
        try {
            usersBean = LitePal.findFirst(UsersBean.class);
            if (usersBean == null) {
                usersBean = new UsersBean();
            } else {
                String weight = StringUtil.getNumbers(usersBean.getWeight());
                String height = StringUtil.getNumbers(usersBean.getHeight());
                float w = Float.valueOf(weight);
                float h = Float.valueOf(height);
                bmi = (float) ((int) (w / ((h / 100) * h / 100) * 10)) / 10;
                initBMIWidow(bmi);
            }
        } catch (Exception e) {
            if (usersBean == null) {
                usersBean = new UsersBean();
            }
            Log.e(TAG, "init:catch " + e.getMessage());
        }
        setRadioGroupGoal();
        setRadioGroupBody();
        setRadioGroupGrade();
    }

    private Activity getActivity() {
        return BmiInformationActivity.this;
    }

    private boolean isPlus = false;

    public void initBMIWidow(float bmi) {
        findViewById(R.id.bmi_recommend_close).setOnClickListener(v -> {
            finish();
        });
        TextView tvTitle = findViewById(R.id.bmi_recommend_tv_title);
        tvTitle.setText(getString(R.string.your_bmi) + " " + bmi + "" + getString(R.string.we_recommend_program));
        TextView tvBmiNum = findViewById(R.id.bmi_recommend_tv_bmi_num);
        tvBmiNum.setText(getString(R.string.your_bmi) + " " + bmi);
        ImageView imgBmi = findViewById(R.id.bmi_recommend_img_color);
        RelativeLayout.LayoutParams layoutParamsImg = (RelativeLayout.LayoutParams) imgBmi.getLayoutParams();
        float pro = 0;
        if (bmi < 15) {
            pro = 0f;
            imgBmi.setBackgroundColor(getResources().getColor(R.color.color_5d9ff8));
        } else if (bmi > 35) {
            pro = 1.0f;
            imgBmi.setBackgroundColor(getResources().getColor(R.color.color_db3f44));
        } else {
            pro = (bmi - 15) / 21;
            if (bmi < 18.5f) {
                imgBmi.setBackgroundColor(getResources().getColor(R.color.color_5d9ff8));
            } else if (bmi >= 18.5f && bmi <= 24.9) {
                imgBmi.setBackgroundColor(getResources().getColor(R.color.color_32de7e));
            } else if (bmi >= 25f && bmi <= 29.9) {
                imgBmi.setBackgroundColor(getResources().getColor(R.color.color_edb756));
            } else {
                imgBmi.setBackgroundColor(getResources().getColor(R.color.color_db3f44));
            }
        }
        int length = HelpUtils.getPhoneWitch(getActivity()) - HelpUtils.dip2px(getActivity(), 45f);
        Log.e(TAG, "initBMIWidow: " + length);
        layoutParamsImg.leftMargin = (int) (pro * length);
        imgBmi.setLayoutParams(layoutParamsImg);
        RadioGroup group = findViewById(R.id.bmi_recommend_radio_group);
        RadioButton btnPlus = findViewById(R.id.bmi_recommend_radio_btn_plus);
        RadioButton btnRegular = findViewById(R.id.bmi_recommend_radio_btn_regular);
        TextView tvPlusUp = findViewById(R.id.bmi_recommend_tv_plus_up);
        TextView tvPlusUnder = findViewById(R.id.bmi_recommend_tv_plus_under);
        TextView tvRegularUp = findViewById(R.id.bmi_recommend_tv_regular_up);
        TextView tvRegularUnder = findViewById(R.id.bmi_recommend_tv_regular_under);

        if (bmi > 28) {
            isPlus = true;
            btnPlus.setChecked(true);
            tvPlusUp.setTextColor(Color.WHITE);
            tvPlusUnder.setTextColor(Color.WHITE);
            tvRegularUp.setTextColor(getResources().getColor(R.color.grey_67));
            tvRegularUnder.setTextColor(getResources().getColor(R.color.grey_67));
        } else {
            isPlus = false;
            btnRegular.setChecked(true);
            tvRegularUp.setTextColor(Color.WHITE);
            tvRegularUnder.setTextColor(Color.WHITE);
            tvPlusUp.setTextColor(getResources().getColor(R.color.grey_67));
            tvPlusUnder.setTextColor(getResources().getColor(R.color.grey_67));
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bmi_recommend_radio_btn_plus:
                        isPlus = true;
                        btnPlus.setChecked(true);
                        tvPlusUp.setTextColor(Color.WHITE);
                        tvPlusUnder.setTextColor(Color.WHITE);
                        tvRegularUp.setTextColor(getResources().getColor(R.color.grey_67));
                        tvRegularUnder.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_recommend_radio_btn_regular:
                        isPlus = false;
                        btnRegular.setChecked(true);
                        tvRegularUp.setTextColor(Color.WHITE);
                        tvRegularUnder.setTextColor(Color.WHITE);
                        tvPlusUp.setTextColor(getResources().getColor(R.color.grey_67));
                        tvPlusUnder.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                }
            }
        });
    }

    private void setRadioGroupGoal() {
        radioGroupGoal.check(goalBuildMuscle.getId());
        usersBean.setPurpose(2);
        usersBean.setGoal(getString(R.string.build_muscle));
        goalLoseWeight.setTextColor(getResources().getColor(R.color.grey_67));
        goalKeepFit.setTextColor(getResources().getColor(R.color.grey_67));
        goalBuildMuscle.setTextColor(getResources().getColor(R.color.white));
        radioGroupGoal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bmi_goal_radio_btn_1:
                        usersBean.setPurpose(3);
                        usersBean.setGoal(getString(R.string.lose_weight));
                        goalLoseWeight.setTextColor(getResources().getColor(R.color.white));
                        goalKeepFit.setTextColor(getResources().getColor(R.color.grey_67));
                        goalBuildMuscle.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_goal_radio_btn_2:
                        usersBean.setPurpose(1);
                        usersBean.setGoal(getString(R.string.keep_fit));
                        goalLoseWeight.setTextColor(getResources().getColor(R.color.grey_67));
                        goalKeepFit.setTextColor(getResources().getColor(R.color.white));
                        goalBuildMuscle.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_goal_radio_btn_3:
                        usersBean.setPurpose(2);
                        usersBean.setGoal(getString(R.string.build_muscle));
                        goalLoseWeight.setTextColor(getResources().getColor(R.color.grey_67));
                        goalKeepFit.setTextColor(getResources().getColor(R.color.grey_67));
                        goalBuildMuscle.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        });
    }

    private void setRadioGroupBody() {
        bodyButtonOne.setChecked(true);
        usersBean.setBodyParts(1);
        usersBean.setFocusParts(getString(R.string.whole_body));
        bodyButtonOne.setTextColor(getResources().getColor(R.color.white));
        bodyBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
        bodyBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
        bodyButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
        bodyButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
        radioGroupBody.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bmi_body_radio_btn_1:
                        usersBean.setBodyParts(1);
                        usersBean.setFocusParts(getString(R.string.whole_body));
                        bodyButtonOne.setTextColor(getResources().getColor(R.color.white));
                        bodyBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_body_radio_btn_2:
                        usersBean.setBodyParts(4);
                        usersBean.setFocusParts(getString(R.string.chest_butt));
                        bodyButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnTwo.setTextColor(getResources().getColor(R.color.white));
                        bodyBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_body_radio_btn_3:
                        usersBean.setBodyParts(2);
                        usersBean.setFocusParts(getString(R.string.abs));
                        bodyButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnThree.setTextColor(getResources().getColor(R.color.white));
                        bodyButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_body_radio_btn_4:
                        usersBean.setBodyParts(5);
                        usersBean.setFocusParts(getString(R.string.arm));
                        bodyButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFour.setTextColor(getResources().getColor(R.color.white));
                        bodyButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_body_radio_btn_5:
                        usersBean.setBodyParts(3);
                        usersBean.setFocusParts(getString(R.string.leg));
                        bodyButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        bodyButtonFive.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }

        });
    }

    private void setRadioGroupGrade() {
        gradeBtnOne.setChecked(true);
        usersBean.setGrade(1);
        usersBean.setLevel(getString(R.string.beginner));
        gradeTvTopOne.setTextColor(getResources().getColor(R.color.white));
        gradeTvBtmOne.setTextColor(getResources().getColor(R.color.white));
        gradeTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
        gradeTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
        gradeTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
        gradeTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
        radioGroupGrade.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bmi_grade_radio_btn_one:
                        usersBean.setGrade(1);
                        usersBean.setLevel(getString(R.string.beginner));
                        gradeTvTopOne.setTextColor(getResources().getColor(R.color.white));
                        gradeTvBtmOne.setTextColor(getResources().getColor(R.color.white));
                        gradeTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_grade_radio_btn_two:
                        usersBean.setGrade(2);
                        usersBean.setLevel(getString(R.string.intermediate));
                        gradeTvTopOne.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmOne.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvTopTwo.setTextColor(getResources().getColor(R.color.white));
                        gradeTvBtmTwo.setTextColor(getResources().getColor(R.color.white));
                        gradeTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.bmi_grade_radio_btn_three:
                        usersBean.setGrade(3);
                        usersBean.setLevel(getString(R.string.advanced));
                        gradeTvTopOne.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmOne.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        gradeTvTopThree.setTextColor(getResources().getColor(R.color.white));
                        gradeTvBtmThree.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        });
    }

    @OnClick({R.id.bmi_info_btm_btn, R.id.bmi_goal_back_img, R.id.bmi_body_back_img, R.id.bmi_grade_back_img})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.bmi_info_btm_btn:
                if (isPlus) {
                    usersBean.setGrade(1);
                    usersBean.setPurpose(0);
                    usersBean.setLevel(getString(R.string.beginner));
                    usersBean.setBodyParts(0);
                    usersBean.setFocusParts("null");
                    usersBean.setGoal("null");
                    setIntent();
                } else {
                    if (llyOne.getVisibility() == View.VISIBLE) {
                        llyOne.setVisibility(View.GONE);
                        llyTwo.setVisibility(View.VISIBLE);
                        llyThree.setVisibility(View.GONE);
                        llyFour.setVisibility(View.GONE);
                    } else if (llyTwo.getVisibility() == View.VISIBLE) {
                        llyTwo.setVisibility(View.GONE);
                        llyOne.setVisibility(View.GONE);
                        llyThree.setVisibility(View.VISIBLE);
                        llyFour.setVisibility(View.GONE);
                        if (usersBean.getPurpose() == 3) {
                            tvBodyLostWeight.setVisibility(View.VISIBLE);
                            llyBody.setVisibility(View.GONE);
                            usersBean.setBodyParts(1);
                            usersBean.setFocusParts(getString(R.string.whole_body));
                        } else {
                            tvBodyLostWeight.setVisibility(View.GONE);
                            llyBody.setVisibility(View.VISIBLE);
                        }
                    } else if (llyThree.getVisibility() == View.VISIBLE) {
                        llyFour.setVisibility(View.VISIBLE);
                        llyTwo.setVisibility(View.GONE);
                        llyThree.setVisibility(View.GONE);
                        llyOne.setVisibility(View.GONE);
                        btnNext.setText(getString(R.string.generate_plan));
                    } else {
                        setIntent();
                    }
                }

                break;
            case R.id.bmi_goal_back_img:
                llyOne.setVisibility(View.VISIBLE);
                llyTwo.setVisibility(View.GONE);
                llyThree.setVisibility(View.GONE);
                llyFour.setVisibility(View.GONE);
                break;
            case R.id.bmi_body_back_img:
                llyOne.setVisibility(View.GONE);
                llyTwo.setVisibility(View.VISIBLE);
                llyThree.setVisibility(View.GONE);
                llyFour.setVisibility(View.GONE);
                break;
            case R.id.bmi_grade_back_img:
                llyTwo.setVisibility(View.GONE);
                llyOne.setVisibility(View.GONE);
                llyThree.setVisibility(View.VISIBLE);
                llyFour.setVisibility(View.GONE);
                break;

        }
    }

    private boolean isIntent = false;

    private void setIntent() {
        usersBean.save();
        Intent intent = new Intent(BmiInformationActivity.this, BmiBuildProgressActivity.class);
        isIntent = true;
        startActivity(intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        try {
            if (llyOne.getVisibility() == View.VISIBLE) {
                LitePal.deleteAll(UsersBean.class);
                super.onBackPressed();
            } else {
                if (llyTwo.getVisibility() == View.VISIBLE) {
                    llyOne.setVisibility(View.VISIBLE);
                    llyTwo.setVisibility(View.GONE);
                    llyThree.setVisibility(View.GONE);
                    llyFour.setVisibility(View.GONE);
                } else if (llyThree.getVisibility() == View.VISIBLE) {
                    llyOne.setVisibility(View.GONE);
                    llyTwo.setVisibility(View.VISIBLE);
                    llyThree.setVisibility(View.GONE);
                    llyFour.setVisibility(View.GONE);
                } else if (llyFour.getVisibility() == View.VISIBLE) {
                    llyTwo.setVisibility(View.GONE);
                    llyOne.setVisibility(View.GONE);
                    llyThree.setVisibility(View.VISIBLE);
                    llyFour.setVisibility(View.GONE);
                    btnNext.setText(getString(R.string.next));
                }
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }
}
