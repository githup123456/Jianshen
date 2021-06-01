package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.utils.StatuBarUtils;

import butterknife.BindView;

public class SettingActivity extends BaseActivity{
    @BindView(R.id.setting_clear_download)
    LinearLayout llyClearDownload;
    @BindView(R.id.setting_radio_group)
    RadioGroup group;
    @BindView(R.id.setting_radio_btn_kg)
    RadioButton btnKg;
    @BindView(R.id.setting_radio_btn_lb)
    RadioButton btnLb;
    @BindView(R.id.top_bar_green_tv_title)
    TextView tvTitle;

    private boolean isKg = true;

    @Override
    public int layoutId() {
        return R.layout.activity_setting;
    }

    private Activity activity;
    @Override
    public void init() {
        activity = SettingActivity.this;
        StatuBarUtils.setTranslucentStatus(activity);
        tvTitle.setText(getString(R.string.settings));
        isKg = getSharedPreferences(PreferencesName.KG_LB,MODE_PRIVATE).getBoolean(PreferencesName.KG_LB_IS_KG,true);
        initGroup();
    }

    private void initGroup(){
        if (isKg){
            btnKg.setChecked(true);
            btnKg.setTextColor(Color.WHITE);
            btnLb.setTextColor(Color.parseColor("#fefffe"));
        }else {
            btnLb.setChecked(true);
            btnLb.setTextColor(Color.WHITE);
            btnKg.setTextColor(Color.parseColor("#fefffe"));
        }
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                SharedPreferences.Editor editor = getSharedPreferences(PreferencesName.KG_LB,MODE_PRIVATE).edit();
                switch (checkedId){
                    case R.id.setting_radio_btn_kg:
                        btnKg.setChecked(true);
                        btnKg.setTextColor(Color.WHITE);
                        btnLb.setTextColor(Color.parseColor("#fefffe"));
                        editor.putBoolean(PreferencesName.KG_LB_IS_KG,true);
                        break;
                    case R.id.setting_radio_btn_lb:
                        btnLb.setChecked(true);
                        btnLb.setTextColor(Color.WHITE);
                        btnKg.setTextColor(Color.parseColor("#fefffe"));
                        editor.putBoolean(PreferencesName.KG_LB_IS_KG,false);
                        break;
                }
                editor.commit();
            }
        });
    }
}
