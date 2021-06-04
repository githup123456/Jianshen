package com.shyj.jianshen.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.PlanBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class PlanTypeAdapter extends RecyclerView.Adapter<PlanTypeAdapter.PlanTypeViewHolder> {

    private String TAG = "PlanTypeAdapter";
    private Context context;
    private List<PlanBean> planBeanList ;

    public PlanTypeAdapter(Context context){
        this.context = context;
        this.planBeanList = new ArrayList<>();
    }

    public void setPlanBeanList(List<PlanBean> planBeanList) {
        this.planBeanList = planBeanList;
        notifyDataSetChanged();
    }

    public void addPlanBeanList(List<PlanBean> planBeanList) {
        this.planBeanList.addAll(planBeanList);
        notifyDataSetChanged();
    }

    public List<PlanBean> getPlanBeanList() {
        return planBeanList;
    }

    public interface OnItemClick{
        void onItemClick(int position);
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @NonNull
    @Override
    public PlanTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_plan_type,parent,false);
        PlanTypeViewHolder planTypeViewHolder = new PlanTypeViewHolder(view);
        return planTypeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PlanTypeViewHolder holder, int position) {
        try {
            if (planBeanList!=null&& planBeanList.size()>0){
                PlanBean planBean = planBeanList.get(position);
                int week = planBean.getDayNum()/7;
                holder.tvWeeks.setText(week + " "+context.getString(R.string.weeks));
                holder.tvName.setText(planBean.getName());
                Glide.with(context).load(StringUtil.getPlanBgUrl(planBean.getMark())).
                        apply(HelpUtils.setImgRadius(context,3.0f))
                        .into(holder.imgBg);
                if (planBean.getIsVip()== 1){
                    holder.imgLock.setVisibility(View.VISIBLE);
                }else {
                    holder.imgLock.setVisibility(View.GONE);
                }
                holder.itemView.setOnClickListener(new NoDoubleClickListener() {

                    @Override
                    public void onNoDoubleClick(View v) {
                        if (onItemClick!=null){
                            onItemClick.onItemClick(position);
                        }
                    }
                });
            }
        }catch (Exception e){
            Log.e(TAG, "catch: "+e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        if (planBeanList!=null&&planBeanList.size()>0){
            return planBeanList.size();
        }
        return 0;
    }

    class PlanTypeViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgBg;
        public ImageView imgLock;
        public TextView tvName,tvWeeks;

        public PlanTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBg = itemView.findViewById(R.id.item_plan_type_img_bg);
            imgLock = itemView.findViewById(R.id.item_plan_type_img_lock);
            tvName = itemView.findViewById(R.id.item_plan_type_tv_name);
            tvWeeks = itemView.findViewById(R.id.item_plan_type_tv_weeks);
        }
    }
}

