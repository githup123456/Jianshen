package com.shyj.jianshen.activity;

import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;

import com.shyj.jianshen.R;
import com.shyj.jianshen.fragment.MyWorkoutFragment;
import com.shyj.jianshen.utils.StatuBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MyFavoriteActivity extends BaseActivity{
    @BindView(R.id.top_bar_green_tv_title)
    TextView tvTitle;

    @Override
    public int layoutId() {
        return R.layout.activity_my_favorite;
    }

    @Override
    public void init() {
        StatuBarUtils.setTranslucentStatus(MyFavoriteActivity.this);
        tvTitle.setText(getString(R.string.favorite));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        MyWorkoutFragment myWorkoutFragment = new MyWorkoutFragment();
        transaction.add(R.id.my_favorite_frame,myWorkoutFragment);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.show(myWorkoutFragment);
        transaction.commitAllowingStateLoss();
    }

    @OnClick({R.id.top_bar_green_img_left})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.top_bar_green_img_left:
                finish();
                break;
        }
    }
}
