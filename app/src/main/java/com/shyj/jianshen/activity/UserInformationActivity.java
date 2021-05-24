package com.shyj.jianshen.activity;

import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.aigestudio.wheelpicker.WheelPicker;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.dialog.WindowUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class UserInformationActivity extends BaseActivity {
    /**
     * select sex
     */
    @BindView(R.id.select_sex_radio_group)
    RadioGroup radioGroupSex;
    @BindView(R.id.select_sex_radio_btn_1)
    RadioButton sexWomen;
    @BindView(R.id.select_sex_radio_btn_2)
    RadioButton sexMen;
    /**
     * select goal
     */
    @BindView(R.id.select_goal_radio_group)
    RadioGroup radioGroupGoal;
    @BindView(R.id.select_goal_radio_btn_1)
    RadioButton goalLoseWeight;
    @BindView(R.id.select_goal_radio_btn_2)
    RadioButton goalKeepFit;
    @BindView(R.id.select_goal_radio_btn_3)
    RadioButton goalBuildMuscle;
    /**
     * select position
     */
    @BindView(R.id.select_position_lose_weight)
    TextView tvPositionLostWeight;
    @BindView(R.id.select_position_select_one)
    LinearLayout llyPosition;
    @BindView(R.id.select_position_radio_group)
    RadioGroup radioGroupPosition;
    @BindView(R.id.select_position_radio_btn_1)
    RadioButton positionButtonOne;
    @BindView(R.id.select_position_radio_btn_2)
    RadioButton positionBtnTwo;
    @BindView(R.id.select_position_radio_btn_3)
    RadioButton positionBtnThree;
    @BindView(R.id.select_position_radio_btn_4)
    RadioButton positionButtonFour;
    @BindView(R.id.select_position_radio_btn_5)
    RadioButton positionButtonFive;
    /**
     * select level
     */
    @BindView(R.id.select_level_radio_group)
    RadioGroup radioGroupLevel;
    @BindView(R.id.select_level_radio_btn_one)
    RadioButton levelBtnOne;
    @BindView(R.id.select_level_radio_btn_two)
    RadioButton levelBtnTwo;
    @BindView(R.id.select_level_radio_btn_three)
    RadioButton levelBtnThree;
    @BindView(R.id.select_level_tv_1_top)
    TextView levelTvTopOne;
    @BindView(R.id.select_level_tv_2_top)
    TextView levelTvTopTwo;
    @BindView(R.id.select_level_tv_3_top)
    TextView levelTvTopThree;
    @BindView(R.id.select_level_tv_1_btm)
    TextView levelTvBtmOne;
    @BindView(R.id.select_level_tv_2_btm)
    TextView levelTvBtmTwo;
    @BindView(R.id.select_level_tv_3_btm)
    TextView levelTvBtmThree;
    /**
     * enter weight height
     */
    @BindView(R.id.height_weight_img_height)
    ImageView heightImg;
    @BindView(R.id.height_weight_img_weight)
    ImageView weightImg;
    @BindView(R.id.height_weight_tv_height)
    TextView heightTv;
    @BindView(R.id.height_weight_tv_weight)
    TextView weightTv;

    @BindView(R.id.user_info_btm_btn)
    Button btnNext;
    @BindView(R.id.user_info_page_one)
    LinearLayout llyOne;
    @BindView(R.id.user_info_page_two)
    LinearLayout llyTwo;
    @BindView(R.id.user_info_page_three)
    LinearLayout llyThree;
    @BindView(R.id.user_info_page_four)
    LinearLayout llyFour;
    @BindView(R.id.user_info_page_five)
    LinearLayout llyFive;


    private boolean isIntent = false;

    private UsersBean usersBean;

    @Override
    public int layoutId() {
        return R.layout.activity_user_information;
    }

    @Override
    public void init() {
        LitePal.deleteAll(UsersBean.class);
        usersBean = new UsersBean();
        String str=getString(R.string.third_user_page_lose_weight)+" "+"<font color='#19B55E'>"+R.string.keep_fit+"</font>"+" "+R.string.or+" "+"<font color='#19B55E'>"+R.string.build_muscle+"</font>"+" "+getString(R.string.third_user_page_lose_weight_end);
        tvPositionLostWeight.setText(Html.fromHtml(str.replaceAll("\n", "<br>")));
        setRadioGroupSex();
        setRadioGroupGoal();
        setRadioGroupPosition();
        setRadioGroupLevel();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick({R.id.user_info_btm_btn, R.id.select_goal_back_img, R.id.select_position_back_img, R.id.select_level_back_img, R.id.height_weight_back_img,
                R.id.height_weight_img_weight,R.id.height_weight_img_height,R.id.height_weight_tv_weight,R.id.height_weight_tv_height})
    public void onClickView(View view) {
        switch (view.getId()) {
            case R.id.user_info_btm_btn:
                if (llyOne.getVisibility() == View.VISIBLE) {
                    llyOne.setVisibility(View.GONE);
                    llyTwo.setVisibility(View.VISIBLE);
                    llyThree.setVisibility(View.GONE);
                    llyFour.setVisibility(View.GONE);
                    llyFive.setVisibility(View.GONE);
                } else if (llyTwo.getVisibility() == View.VISIBLE) {
                    llyTwo.setVisibility(View.GONE);
                    llyOne.setVisibility(View.GONE);
                    llyThree.setVisibility(View.VISIBLE);
                    llyFour.setVisibility(View.GONE);
                    llyFive.setVisibility(View.GONE);
                    if (usersBean.getPurpose() == 3) {
                        tvPositionLostWeight.setVisibility(View.VISIBLE);
                        llyPosition.setVisibility(View.GONE);
                        usersBean.setBodyParts(1);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_1));
                    } else {
                        tvPositionLostWeight.setVisibility(View.GONE);
                        llyPosition.setVisibility(View.VISIBLE);
                    }
                } else if (llyThree.getVisibility() == View.VISIBLE) {
                    llyFour.setVisibility(View.VISIBLE);
                    llyTwo.setVisibility(View.GONE);
                    llyThree.setVisibility(View.GONE);
                    llyOne.setVisibility(View.GONE);
                    llyFive.setVisibility(View.GONE);
                } else if (llyFour.getVisibility() == View.VISIBLE) {
                    llyTwo.setVisibility(View.GONE);
                    llyThree.setVisibility(View.GONE);
                    llyFour.setVisibility(View.GONE);
                    llyOne.setVisibility(View.GONE);
                    llyFive.setVisibility(View.VISIBLE);
                    btnNext.setText(getString(R.string.generate_plan));
                } else {
                    setIntent();
                }
                break;
            case R.id.select_goal_back_img:
                llyOne.setVisibility(View.VISIBLE);
                llyTwo.setVisibility(View.GONE);
                llyThree.setVisibility(View.GONE);
                llyFour.setVisibility(View.GONE);
                llyFive.setVisibility(View.GONE);
                break;
            case R.id.select_position_back_img:
                llyOne.setVisibility(View.GONE);
                llyTwo.setVisibility(View.VISIBLE);
                llyThree.setVisibility(View.GONE);
                llyFour.setVisibility(View.GONE);
                llyFive.setVisibility(View.GONE);
                break;
            case R.id.select_level_back_img:
                llyTwo.setVisibility(View.GONE);
                llyOne.setVisibility(View.GONE);
                llyThree.setVisibility(View.VISIBLE);
                llyFour.setVisibility(View.GONE);
                llyFive.setVisibility(View.GONE);
                break;
            case R.id.height_weight_back_img:
                llyFour.setVisibility(View.VISIBLE);
                llyTwo.setVisibility(View.GONE);
                llyThree.setVisibility(View.GONE);
                llyOne.setVisibility(View.GONE);
                llyFive.setVisibility(View.GONE);
                btnNext.setText(getString(R.string.next));
                break;
            case R.id.height_weight_tv_height:
            case R.id.height_weight_img_height:
                showHeightWindow();
                break;
            case R.id.height_weight_tv_weight:
            case R.id.height_weight_img_weight:
                showWeightWindow();
                break;
        }
    }
    private String userWeight;
    private int weightOne=0,weightTwo=0,weightThree=0;
    /** 体重弹框 */
    private void showWeightWindow(){
        try {
            WindowUtils.dismissBrightness(UserInformationActivity.this);
            View heightView = WindowUtils.Show(this,R.layout.window_bottom_select,2);
            WheelPicker wheelLeft = heightView.findViewById(R.id.window_bottom_wheel_left);
            List<String> strings= new ArrayList<>();
            for (int q=2;q<150;q++){
                strings.add(q+"");
            }
            String[] stringWeight = {"2",".0","kg"};
            wheelLeft.setData(strings);
            wheelLeft.setSelectedItemPosition(weightOne);
            wheelLeft.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    Log.e("TAG", "onItemSelected: "+data.toString() + " position:"+position);
                    stringWeight[0] = data.toString();
                    weightOne = position;
                }
            });

            WheelPicker wheelCenter = heightView.findViewById(R.id.window_bottom_wheel_center);
            ArrayList<String> arrayListTwo = new ArrayList<String>();
            arrayListTwo.add(".0");
            arrayListTwo.add(".1");
            arrayListTwo.add(".2");
            arrayListTwo.add(".3");
            arrayListTwo.add(".4");
            arrayListTwo.add(".5");
            arrayListTwo.add(".6");
            arrayListTwo.add(".7");
            arrayListTwo.add(".8");
            arrayListTwo.add(".9");
            wheelCenter.setData(arrayListTwo);
            wheelCenter.setSelectedItemPosition(weightTwo);
            wheelCenter.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringWeight[1] = data.toString();
                    weightTwo = position;
                }
            });
            List<String> stringListRight = new ArrayList<>();
            stringListRight.add("kg");
            stringListRight.add("ibs");
            WheelPicker wheelRight = heightView.findViewById(R.id.window_bottom_wheel_right);
            wheelRight.setData(stringListRight);
            wheelRight.setSelectedItemPosition(weightThree);
            wheelRight.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringWeight[2] = data.toString();
                    weightThree = position;
                }
            });
            heightView.findViewById(R.id.window_bottom_cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WindowUtils.dismissBrightness(UserInformationActivity.this);
                }
            });

            heightView.findViewById(R.id.window_bottom_save).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    userWeight = stringWeight[0]+stringWeight[1]+stringWeight[2];
                    weightImg.setVisibility(View.GONE);
                    weightTv.setVisibility(View.VISIBLE);
                    weightTv.setText(userWeight);
                    WindowUtils.dismissBrightness(UserInformationActivity.this);
                }
            });
        }catch (Exception e){
            Log.e("TAG", "showHeightWindow: "+e.getMessage());
        }
    }

    private String userHeight;
    private int heightOne=0,heightTwo=0,heightThree=0;
    /** 身高弹框 */
    private void showHeightWindow(){
        try {
            WindowUtils.dismissBrightness(UserInformationActivity.this);
            View heightView = WindowUtils.Show(this,R.layout.window_bottom_select,2);
            WheelPicker wheelLeft = heightView.findViewById(R.id.window_bottom_wheel_left);
            List<String> strings= new ArrayList<>();
            for (int q=20;q<250;q++){
                strings.add(q+"");
            }
            final String[] stringHeight = {"20",".0","cm"};
            wheelLeft.setData(strings);
            wheelLeft.setSelectedItemPosition(heightOne);
            wheelLeft.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    Log.e("TAG", "onItemSelected: "+data.toString() + " position:"+position);
                    stringHeight[0] = data.toString();
                    heightOne = position;
                }
            });

            WheelPicker wheelCenter = heightView.findViewById(R.id.window_bottom_wheel_center);
            ArrayList<String> arrayListTwo = new ArrayList<String>();
            arrayListTwo.add(".0");
            arrayListTwo.add(".1");
            arrayListTwo.add(".2");
            arrayListTwo.add(".3");
            arrayListTwo.add(".4");
            arrayListTwo.add(".5");
            arrayListTwo.add(".6");
            arrayListTwo.add(".7");
            arrayListTwo.add(".8");
            arrayListTwo.add(".9");
            wheelCenter.setData(arrayListTwo);
            wheelCenter.setSelectedItemPosition(heightTwo);
            wheelCenter.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringHeight[1] = data.toString();
                    heightTwo = position;
                }
            });
            List<String> stringListRight = new ArrayList<>();
            stringListRight.add("cm");
            stringListRight.add("ft.in");
            WheelPicker wheelRight = heightView.findViewById(R.id.window_bottom_wheel_right);
            wheelRight.setData(stringListRight);
            wheelRight.setSelectedItemPosition(heightThree);
            wheelRight.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringHeight[2] = data.toString();
                    heightThree = position;
                }
            });
            heightView.findViewById(R.id.window_bottom_cancel).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    WindowUtils.dismissBrightness(UserInformationActivity.this);
                }
            });

            heightView.findViewById(R.id.window_bottom_save).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    userHeight = stringHeight[0]+stringHeight[1]+stringHeight[2];
                    heightImg.setVisibility(View.GONE);
                    heightTv.setVisibility(View.VISIBLE);
                    heightTv.setText(userHeight);
                    WindowUtils.dismissBrightness(UserInformationActivity.this);
                }
            });
        }catch (Exception e){
            Log.e("TAG", "showHeightWindow: "+e.getMessage());
        }
    }

    private void setIntent() {
        if (heightTv.getVisibility()==View.GONE || heightTv.getText()==null|| heightTv.getText().length()==0){
            showToast("请输入您的身高");
        }else if (weightTv.getVisibility()==View.GONE || weightTv.getText()==null|| weightTv.getText().length()==0){
            showToast("请输入您的体重");
        }else {
            usersBean.setWeight(weightTv.getText().toString());
            usersBean.setHeight(heightTv.toString());
            usersBean.save();
            Intent intent = new Intent(UserInformationActivity.this, BuildProgressActivity.class);
            isIntent = true;
            startActivity(intent);
            finish();
        }
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
                    llyFive.setVisibility(View.GONE);
                } else if (llyThree.getVisibility() == View.VISIBLE) {
                    llyOne.setVisibility(View.GONE);
                    llyTwo.setVisibility(View.VISIBLE);
                    llyThree.setVisibility(View.GONE);
                    llyFour.setVisibility(View.GONE);
                    llyFive.setVisibility(View.GONE);
                } else if (llyFour.getVisibility() == View.VISIBLE) {
                    llyTwo.setVisibility(View.GONE);
                    llyOne.setVisibility(View.GONE);
                    llyThree.setVisibility(View.VISIBLE);
                    llyFour.setVisibility(View.GONE);
                    llyFive.setVisibility(View.GONE);
                } else if (llyFive.getVisibility() == View.VISIBLE) {
                    if (WindowUtils.popupWindow!=null&&WindowUtils.popupWindow.isShowing()){
                        WindowUtils.dismissBrightness(UserInformationActivity.this);
                    }else {
                        llyFour.setVisibility(View.VISIBLE);
                        llyTwo.setVisibility(View.GONE);
                        llyThree.setVisibility(View.GONE);
                        llyOne.setVisibility(View.GONE);
                        llyFive.setVisibility(View.GONE);
                        btnNext.setText(getString(R.string.next));
                    }
                }
            }
        } catch (Exception e) {
            super.onBackPressed();
        }

    }

    private void setRadioGroupSex() {
        sexWomen.setChecked(true);
        usersBean.setSex(2);
        usersBean.setSexS(getString(R.string.first_user_page_female));
        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_sex_radio_btn_1:
                        usersBean.setSex(2);
                        usersBean.setSexS(getString(R.string.first_user_page_female));
                        break;
                    case R.id.select_sex_radio_btn_2:
                        usersBean.setSex(1);
                        usersBean.setSexS(getString(R.string.first_user_page_male));
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
                    case R.id.select_goal_radio_btn_1:
                        Log.e("TAG", "onCheckedChanged:1 ");
                        usersBean.setPurpose(3);
                        usersBean.setGoal(getString(R.string.lose_weight));
                        goalLoseWeight.setTextColor(getResources().getColor(R.color.white));
                        goalKeepFit.setTextColor(getResources().getColor(R.color.grey_67));
                        goalBuildMuscle.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_goal_radio_btn_2:
                        Log.e("TAG", "onCheckedChanged:2 ");
                        usersBean.setPurpose(1);
                        usersBean.setGoal(getString(R.string.keep_fit));
                        goalLoseWeight.setTextColor(getResources().getColor(R.color.grey_67));
                        goalKeepFit.setTextColor(getResources().getColor(R.color.white));
                        goalBuildMuscle.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_goal_radio_btn_3:
                        Log.e("TAG", "onCheckedChanged: 3");
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

    private void setRadioGroupPosition() {
        positionButtonOne.setChecked(true);
         usersBean.setBodyParts(1);
        usersBean.setFocusParts(getString(R.string.third_user_page_item_1));
        positionButtonOne.setTextColor(getResources().getColor(R.color.white));
        positionBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
        positionBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
        positionButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
        positionButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
        radioGroupPosition.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_position_radio_btn_1:
                        usersBean.setBodyParts(1);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_1));
                        positionButtonOne.setTextColor(getResources().getColor(R.color.white));
                        positionBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_position_radio_btn_2:
                        usersBean.setBodyParts(4);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_2));
                        positionButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnTwo.setTextColor(getResources().getColor(R.color.white));
                        positionBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_position_radio_btn_3:
                        usersBean.setBodyParts(2);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_3));
                        positionButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnThree.setTextColor(getResources().getColor(R.color.white));
                        positionButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_position_radio_btn_4:
                        usersBean.setBodyParts(5);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_4));
                        positionButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFour.setTextColor(getResources().getColor(R.color.white));
                        positionButtonFive.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_position_radio_btn_5:
                        usersBean.setBodyParts(3);
                        usersBean.setFocusParts(getString(R.string.third_user_page_item_5));
                        positionButtonOne.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        positionBtnThree.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFour.setTextColor(getResources().getColor(R.color.grey_67));
                        positionButtonFive.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }

        });
    }

    private void setRadioGroupLevel() {
        levelBtnOne.setChecked(true);
        usersBean.setGrade(1);
        usersBean.setLevel(getString(R.string.beginner));
        levelTvTopOne.setTextColor(getResources().getColor(R.color.white));
        levelTvBtmOne.setTextColor(getResources().getColor(R.color.white));
        levelTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
        levelTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
        levelTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
        levelTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
        radioGroupLevel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.select_level_radio_btn_one:
                        usersBean.setGrade(1);
                        usersBean.setLevel(getString(R.string.beginner));
                        levelTvTopOne.setTextColor(getResources().getColor(R.color.white));
                        levelTvBtmOne.setTextColor(getResources().getColor(R.color.white));
                        levelTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_level_radio_btn_two:
                        usersBean.setGrade(2);
                        usersBean.setLevel(getString(R.string.intermediate));
                        levelTvTopOne.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmOne.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvTopTwo.setTextColor(getResources().getColor(R.color.white));
                        levelTvBtmTwo.setTextColor(getResources().getColor(R.color.white));
                        levelTvTopThree.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmThree.setTextColor(getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.select_level_radio_btn_three:
                        usersBean.setGrade(3);
                        usersBean.setLevel(getString(R.string.advanced));
                        levelTvTopOne.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmOne.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvTopTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvBtmTwo.setTextColor(getResources().getColor(R.color.grey_67));
                        levelTvTopThree.setTextColor(getResources().getColor(R.color.white));
                        levelTvBtmThree.setTextColor(getResources().getColor(R.color.white));
                        break;
                }
            }
        });
    }
}
