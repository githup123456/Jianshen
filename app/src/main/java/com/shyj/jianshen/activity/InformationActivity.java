package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.LengthBodyBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.utils.StatuBarUtils;

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
    private LengthBodyBean lengthBodyBean;
    
    @Override
    public void init() {
        tvTitle.setText(getString(R.string.information));
        activity = InformationActivity.this;
        StatuBarUtils.setTranslucentStatus(activity);
        try {
            lengthBodyBean  = LitePal.findLast(LengthBodyBean.class);
            if (lengthBodyBean!=null){
                tvChest.setText(lengthBodyBean.getLengthChest()+" cm");
                tvArm.setText(lengthBodyBean.getLengthArm()+" cm");
                tvWaist.setText(lengthBodyBean.getLengthWaist()+" cm");
                tvHip.setText(lengthBodyBean.getLengthHip()+" cm");
                tvThigh.setText(lengthBodyBean.getLengthThigh()+" cm");
                tvCalf.setText(lengthBodyBean.getLengthCalf()+" cm");
            }else {
                tvChest.setText("0 cm");
                tvArm.setText("0 cm");
                tvWaist.setText("0 cm");
                tvHip.setText(0+" cm");
                tvThigh.setText(0+" cm");
                tvCalf.setText(0+" cm");
            }
        }catch (Exception r){
            r.printStackTrace();
        }
        try {
            usersBean = LitePal.findFirst(UsersBean.class);
            if (usersBean!=null){
                if (usersBean.getSex()==1){
                    isMale = true;
                }else {
                    isMale = false;
                }
                tvHeight.setText(usersBean.getHeight());
                tvWeight.setText(usersBean.getWeight());
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
