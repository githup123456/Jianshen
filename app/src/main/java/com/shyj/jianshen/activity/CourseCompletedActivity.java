package com.shyj.jianshen.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CourseCompletedActivity extends BaseActivity{
    @BindView(R.id.course_complete_img_bg)
    ImageView imgBg;
    @BindView(R.id.course_complete_tv_action_num)
    TextView tvActionNum;
    @BindView(R.id.course_complete_tv_kcal)
    TextView tvKcal;
    @BindView(R.id.course_complete_tv_name)
    TextView tvName;
    @BindView(R.id.course_complete_tv_time)
    TextView tvTime;
    @BindView(R.id.course_complete_rel_course)
    RelativeLayout relCourse;

    private Activity activity;
    private int index;
    private CourseBean courseBean;

    @Override
    public int layoutId() {
        return R.layout.activity_course_completed;
    }

    @Override
    public void init() {
        activity = CourseCompletedActivity.this;
        StatuBarUtils.setWhiteTop(activity, Color.WHITE,true);
        index = getIntent().getIntExtra(IntentId.COURSE_INDEX,1);
        try {
            List<CourseBean> courseBeanList = LitePal.where("indexs = ?", (index+1)+"").find(CourseBean.class,true);
            if (courseBeanList!=null&&courseBeanList.size()>0){
                courseBean = courseBeanList.get(0);
            }else {
                courseBean = LitePal.find(CourseBean.class,index+1,true);
            }
        }catch (Exception e){
            Log.e(TAG, "init:Completed "+e.getMessage() );
        }
        if (courseBean!=null){
            relCourse.setVisibility(View.VISIBLE);
            tvActionNum .setText(courseBean.getActionTypes().size()+"");
            tvName.setText(courseBean.getName());
            tvKcal.setText(courseBean.getCalorie()+"");
            float min = (float) courseBean.getDuration()/1000/60;
            tvTime.setText(Math.round(min*100)/100+ " min");
            if (SaveUtils.fileIsExists(SaveUtils.getCourseBgFile(courseBean.getIndexs()))){
                Glide.with(activity).load(SaveUtils.getCourseBgFile(courseBean.getIndexs())).into(imgBg);
            }else {
                Glide.with(activity).load(StringUtil.getCourseBgUrl(courseBean.getIndexs())).into(imgBg);
            }
        }else {
            relCourse.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.course_complete_btn_finish,R.id.course_complete_rel_course})
    public void OnClick(View view){
        Intent intent = new Intent();
        switch (view.getId()){
            case R.id.course_complete_btn_finish:
                intent.setClass(activity,MainActivity.class);
                break;
            case R.id.course_complete_rel_course:
                intent.setClass(activity,CourseDetailActivity.class);
                intent.putExtra(IntentId.COURSE_ID,courseBean.getCourseID());
                intent.putExtra(IntentId.DAYS_ID,courseBean.getDays());
               break;
        }
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(activity,MainActivity.class);
        startActivity(intent);
    }
}
