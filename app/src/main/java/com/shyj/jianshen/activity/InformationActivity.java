package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.key.PreferencesName;

import org.litepal.LitePal;

import butterknife.BindView;

public class InformationActivity extends BaseActivity{
    @BindView(R.id.top_bar_green_tv_title)
    TextView tvTitle;
    @BindView(R.id.information_tv_arm)
    TextView tvArm;
    @BindView(R.id.information_tv_calf)
    TextView tvCalf;
    @BindView(R.id.information_tv_chest)
    TextView tvChest;
    @BindView(R.id.information_tv_height)
    TextView tvHeight;
    @BindView(R.id.information_tv_weight)
    TextView tvWeight;
    @BindView(R.id.information_tv_waist)
    TextView tvWaist;
    @BindView(R.id.information_tv_thigh)
    TextView tvThigh;
    @BindView(R.id.information_tv_hip)
    TextView tvHip;
    
    @BindView(R.id.information_radio_group)
    RadioGroup group;
    @BindView(R.id.information_radio_btn_male)
    RadioButton btnMale;
    @BindView(R.id.information_radio_btn_female)
    RadioButton btnFemale;
    
    @Override
    public int layoutId() {
        return R.layout.activity_information;
    }

    private Activity activity;
    
    private boolean isMale;
    private UsersBean usersBean;
    
    @Override
    public void init() {
        activity = InformationActivity.this;
        try {
            usersBean = LitePal.findFirst(UsersBean.class);
            if (usersBean!=null){
                if (usersBean.getSex()==1){
                    isMale = true;
                }else {
                    isMale = false;
                }
                tvHeight.setText(usersBean.getHeight()+" cm");
                tvWeight.setText(usersBean.getWeight()+" cm");
            }
        }catch (Exception e){
            Log.e(TAG, "init: "+e.getMessage());
            isMale = false;
        }
        initGroup();
    }

    private void initGroup(){
        if (isMale){
            btnMale.setChecked(true);
            btnMale.setTextColor(Color.WHITE);
            btnFemale.setTextColor(Color.parseColor("#fefffe"));
        }else {
            btnFemale.setChecked(true);
            btnFemale.setTextColor(Color.WHITE);
            btnMale.setTextColor(Color.parseColor("#fefffe"));
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = getSharedPreferences(PreferencesName.KG_LB,MODE_PRIVATE).edit();
                switch (checkedId){
                    case R.id.setting_radio_btn_kg:
                        btnMale.setChecked(true);
                        btnMale.setTextColor(Color.WHITE);
                        btnFemale.setTextColor(Color.parseColor("#fefffe"));
                        editor.putBoolean(PreferencesName.KG_LB_IS_KG,true);
                        break;
                    case R.id.setting_radio_btn_lb:
                        btnFemale.setChecked(true);
                        btnFemale.setTextColor(Color.WHITE);
                        btnMale.setTextColor(Color.parseColor("#fefffe"));
                        editor.putBoolean(PreferencesName.KG_LB_IS_KG,false);
                        break;
                }
                editor.commit();
            }
        });
    }
}