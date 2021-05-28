package com.shyj.jianshen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.CourseDetailActivity;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.StringUtil;

import java.io.Serializable;
import java.util.List;

public class RecommendWorkAdapter extends RecyclerView.Adapter<RecommendWorkAdapter.RecommendWorkViewHolder> {
    private String TAG = "TAG_RecommendWorkAdapter";
    private Context context;
    private List<CourseBean> courseBeanList;

    public RecommendWorkAdapter(Context context,List<CourseBean> courseBeans){
        this.context = context;
        this.courseBeanList = courseBeans;
    }

    public void addCourseBeanList(List<CourseBean> courseBeanList) {
        this.courseBeanList = courseBeanList;
        notifyDataSetChanged();
    }

    public interface OnRecommendClick{
        void onClick(int position);
    }

    private OnRecommendClick onRecommendClick;

    public void setOnRecommendClick(OnRecommendClick onRecommendClick) {
        this.onRecommendClick = onRecommendClick;
    }

    @NonNull
    @Override
    public RecommendWorkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_recommend_work,parent,false);
        return new RecommendWorkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendWorkViewHolder holder, int position) {
        try {
            if (courseBeanList != null && courseBeanList.size() > position) {
                CourseBean courseBean = courseBeanList.get(position);
                if (courseBean != null) {
                    int grand = courseBean.getGrade();
                    if (grand == 1){
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    }else if (grand == 2){
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_grey);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    }else if (grand == 3){
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_grey);
                    }else {
                        holder.imgGrandOne.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandTwo.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandThree.setImageResource(R.mipmap.grand_sign_green);
                        holder.imgGrandFour.setImageResource(R.mipmap.grand_sign_green);
                    }
                    int min = (int) courseBean.getDuration() / 1000 / 60;
                    holder.tvBody.setText(courseBean.getName());
                    holder.tvTime.setText(min + " min");
                    String bgUrl = StringUtil.getCourseBgUrl(courseBean.getIndexs());
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
                            if (onRecommendClick != null) {
                                onRecommendClick.onClick(position);
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
        intent.putExtra(IntentId.COURSE_BEAN, (Serializable) courseBean);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        if (courseBeanList!=null&&courseBeanList.size()>0){
            return courseBeanList.size();
        }
        return 4;
    }

    class  RecommendWorkViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgGrandOne,imgGrandTwo,imgGrandThree,imgGrandFour;
        public TextView tvTime,tvBody;
        public ImageView imgBg,imgState;

        public RecommendWorkViewHolder(@NonNull View itemView) {
            super(itemView);
            imgGrandOne = itemView.findViewById(R.id.item_recommend_work_img_grand_one);
            imgGrandTwo = itemView.findViewById(R.id.item_recommend_work_img_grand_two);
            imgGrandThree = itemView.findViewById(R.id.item_recommend_work_img_grand_three);
            imgGrandFour = itemView.findViewById(R.id.item_recommend_work_img_grand_four);
            imgBg = itemView.findViewById(R.id.item_recommend_work_img_bg);
            imgState = itemView.findViewById(R.id.item_recommend_work_img_state);
            tvTime = itemView.findViewById(R.id.item_recommend_work_tv_time);
            tvBody = itemView.findViewById(R.id.item_recommend_work_tv_body);
        }
    }
}
