package com.shyj.jianshen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseBean;
import com.shyj.jianshen.click.NoDoubleClickListener;

import java.util.List;

public class RecommendWorkAdapter extends RecyclerView.Adapter<RecommendWorkAdapter.RecommendWorkViewHolder> {
    private String TAG = "TAG_RecommendWorkAdapter";
    private Context context;
    private List<CourseBean> courseBeanList;
    private boolean isLock;

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
            holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                @Override
                public void onNoDoubleClick(View v) {
                    if (onRecommendClick!=null){
                        onRecommendClick.onClick(position);
                    }
                }
            });
        }catch (Exception e){
            Log.e(TAG, "catch: "+e.getMessage() );
        }
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
