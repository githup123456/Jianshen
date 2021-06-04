package com.shyj.jianshen.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;

import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.InformationActivity;
import com.shyj.jianshen.activity.MyFavoriteActivity;
import com.shyj.jianshen.activity.SettingActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ProfileFragment extends BaseFragment {

    @BindView(R.id.fragment_profile_rel_favorite)
    RelativeLayout relFavorite;
    @BindView(R.id.fragment_profile_rel_information)
    RelativeLayout relInformation;
    @BindView(R.id.fragment_profile_rel_my_program)
    RelativeLayout relMy;


    @Override
    public int layoutId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.fragment_profile_rel_favorite, R.id.fragment_profile_rel_send_feedback, R.id.fragment_profile_rel_setting,
            R.id.fragment_profile_rel_my_program, R.id.fragment_profile_rel_information, R.id.fragment_profile_rel_restore_purchase,
            R.id.fragment_profile_rel_rate_us})
    public void onViewClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.fragment_profile_rel_favorite:
                intent.setClass(getActivity(), MyFavoriteActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_profile_rel_my_program:
            case R.id.fragment_profile_rel_rate_us:
            case R.id.fragment_profile_rel_restore_purchase:
            case R.id.fragment_profile_rel_send_feedback:
                showToast(R.string.function_develop);
                break;
            case R.id.fragment_profile_rel_setting:
                intent.setClass(getActivity(), SettingActivity.class);
                startActivity(intent);
                break;

            case R.id.fragment_profile_rel_information:
                intent.setClass(getActivity(), InformationActivity.class);
                startActivity(intent);
                break;
        }
    }

}
