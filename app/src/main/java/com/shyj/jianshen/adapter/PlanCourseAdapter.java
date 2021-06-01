package com.shyj.jianshen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.request.RequestOptions;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.CourseDetailActivity;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class PlanCourseAdapter extends RecyclerView.Adapter<PlanCourseAdapter.CourseViewHolder> {
    private String TAG = "TAG_PlanCourseAdapter";
    private Context context;
    private List<CourseBean> courseBeanList;
    private boolean isLock;
    private boolean isPlan;

    public PlanCourseAdapter(Context context, List<CourseBean> courseBeans, boolean isLock, boolean isPlan) {
        this.context = context;
        this.courseBeanList = courseBeans;
        this.isLock = isLock;
        this.isPlan = isPlan;
    }

    public void addCourseBeanList(List<CourseBean> courseBeanList) {
        this.courseBeanList = courseBeanList;
        notifyDataSetChanged();
    }

    public interface OnCourseClick {
        void onCourseClick(int pos);
    }

    private OnCourseClick onCourseClick;

    public void setOnCourseClick(OnCourseClick onCourseClick) {
        this.onCourseClick = onCourseClick;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_plan_course, parent, false);
        return new CourseViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        try {
            if (courseBeanList != null && courseBeanList.size() > position) {
                CourseBean courseBean = courseBeanList.get(position);
                if (courseBean != null) {
                    if (isPlan) {
                        if (isLock) {
                            holder.imgState.setVisibility(View.VISIBLE);
                            holder.imgState.setImageResource(R.mipmap.course_lock);
                        } else {
                            if (courseBean.isCompleted()) {
                                holder.imgState.setVisibility(View.VISIBLE);
                                holder.imgState.setImageResource(R.mipmap.course_end);
                            } else {
                                holder.imgState.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        holder.imgState.setVisibility(View.GONE);
                    }
                    int grand = courseBean.getGrade();
                    if (grand == 1) {
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    } else if (grand == 2) {
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    } else if (grand == 3) {
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    } else {
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_green);
                    }
                    int min = (int) courseBean.getDuration() / 1000 / 60;
                    holder.tvBody.setText(courseBean.getName());
                    holder.tvTime.setText(min + " min");
                    String bgUrl = SaveUtils.getCourseBgFile(courseBean.getIndexs());
                    if (!SaveUtils.fileIsExists(bgUrl)) {
                        bgUrl = StringUtil.getCourseBgUrl(courseBean.getIndexs());
                    }
                    Resources resources = context.getResources();
                    resources.getDrawable(R.drawable.grey_radius_2);
                    RequestOptions requestOptions = new RequestOptions()
                            .placeholder(R.color.black)
                            .error(R.color.black);
                    Glide.with(context).load(bgUrl).apply(requestOptions).into(holder.imgBg);
                    holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                        @Override
                        public void onNoDoubleClick(View v) {
                            initIntent(courseBean);
                            if (onCourseClick != null) {
                                onCourseClick.onCourseClick(position);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "catch: " + e.getMessage());
        }

    }

    private void initIntent(CourseBean courseBean) {
        Intent intent = new Intent((Activity) context, CourseDetailActivity.class);
        if (isPlan) {
            intent.putExtra(IntentId.DAYS_ID, courseBean.getDays());
            intent.putExtra(IntentId.COURSE_ID, courseBean.getCourseID());
        } else {
            if (courseBean.isCollect()) {
                intent.putExtra(IntentId.DAYS_ID, courseBean.getDays());
                intent.putExtra(IntentId.COURSE_ID, courseBean.getCourseID());
            } else {
                intent.putExtra(IntentId.COURSE_BEAN, (Serializable) courseBean);
            }
        }
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        if (courseBeanList != null && courseBeanList.size() > 0) {
            return courseBeanList.size();
        }
        return 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgGrandOne, imgGrandTwo, imgGrandThree, imgGrandFour;
        public TextView tvTime, tvBody;
        public ImageView imgBg, imgState;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGrandOne = itemView.findViewById(R.id.item_plan_course_img_grand_one);
            imgGrandTwo = itemView.findViewById(R.id.item_plan_course_img_grand_two);
            imgGrandThree = itemView.findViewById(R.id.item_plan_course_img_grand_three);
            imgGrandFour = itemView.findViewById(R.id.item_plan_course_img_grand_four);
            imgBg = itemView.findViewById(R.id.item_plan_course_img_bg);
            imgState = itemView.findViewById(R.id.item_plan_course_img_state);
            tvTime = itemView.findViewById(R.id.item_plan_course_tv_time);
            tvBody = itemView.findViewById(R.id.item_plan_course_tv_body);
        }
    }
}
