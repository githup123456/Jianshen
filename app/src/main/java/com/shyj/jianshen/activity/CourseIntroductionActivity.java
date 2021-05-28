package com.shyj.jianshen.activity;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.StatuBarUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseIntroductionActivity extends BaseActivity{
    @BindView(R.id.top_bar_green_tv_title)
    TextView tvTitle;
    @BindView(R.id.introduction_tv_introduction)
    TextView tvIntro;
    private Activity activity;

    @Override
    public int layoutId() {
        return R.layout.activity_introduction;
    }

    @Override
    public void init() {
        activity = CourseIntroductionActivity.this;
        StatuBarUtils.setTranslucentStatus(activity);
        tvTitle.setText(getString(R.string.introduction));
        String grade = getIntent().getStringExtra(IntentId.COURSE_GRADE);
        String time = getIntent().getStringExtra(IntentId.COURSE_TIME);
        String kcal = getIntent().getStringExtra(IntentId.COURSE_GCAL);
        if (grade!=null && time!=null && kcal!=null){
            if ("1".equals(grade)){
                tvIntro.setText(getString(R.string.introduction_one)+" L"+grade+" - "+getString(R.string.beginner)+" "+getString(R.string.introduction_two)+
                        time+" "+getString(R.string.minute)+" "+getString(R.string.introduction_three)+" "+kcal);
            }else if ("2".equals(grade)){
                tvIntro.setText(getString(R.string.introduction_one)+" L"+grade+" - "+getString(R.string.intermediate)+" "+getString(R.string.introduction_two)+
                        time+" "+getString(R.string.minute)+" "+getString(R.string.introduction_three)+" "+kcal);
            }else {
                tvIntro.setText(getString(R.string.introduction_one)+" L"+grade+" - "+getString(R.string.advanced)+" "+getString(R.string.introduction_two)+
                        time+" "+getString(R.string.minute)+" "+getString(R.string.introduction_three)+" "+kcal);
            }
        }else {
            tvIntro.setText(getString(R.string.introduction_one)+" L"+1+" - "+getString(R.string.beginner)+" "+getString(R.string.introduction_two)+
                    12+" "+getString(R.string.minute)+" "+getString(R.string.introduction_three)+" "+80+" "+getString(R.string.kcal));
        }
    }

    @OnClick({R.id.top_bar_green_img_left})
    public void onClickView(View view){
        switch (view.getId()){
            case R.id.top_bar_green_img_left:
                finish();
                break;
        }
    }
}
