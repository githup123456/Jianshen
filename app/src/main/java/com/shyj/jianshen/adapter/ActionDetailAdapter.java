package com.shyj.jianshen.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;

import java.util.List;

public class ActionDetailAdapter extends RecyclerView.Adapter<ActionDetailAdapter.MyActionViewHolder> {

    private Context context;
    private List<CourseActionBean> courseActionBeanList;

    public ActionDetailAdapter(Context context, List<CourseActionBean> courseActionBeanList) {
        this.context = context;
        this.courseActionBeanList = courseActionBeanList;
    }

    public interface ActionDetailListener {
        void onActionDetailClick(int pos);
    }

    private ActionDetailListener actionDetailListener;

    public void setActionDetailListener(ActionDetailListener actionDetailListener) {
        this.actionDetailListener = actionDetailListener;
    }

    @NonNull
    @Override
    public MyActionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_action_detail, parent, false);
        return new MyActionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyActionViewHolder holder, int position) {
        try {
            if (courseActionBeanList != null && courseActionBeanList.size() > 0) {
                CourseActionBean courseActionBean = courseActionBeanList.get(position);
                Glide.with(context).load(StringUtil.getActionWomenUrl(courseActionBean.getId()))
                        .apply(HelpUtils.setImgRadius(context, 5.0f)).into(holder.imgBg);
                holder.tvName.setText(courseActionBean.getName());
                holder.tvTime.setText(HelpUtils.getMSTime(courseActionBean.getDuration()));
                holder.tvRest.setText(courseActionBean.getGap() + "\"");
                holder.itemView.setOnClickListener(new NoDoubleClickListener() {
                    @Override
                    public void onNoDoubleClick(View v) {
                        if (actionDetailListener != null) {
                            actionDetailListener.onActionDetailClick(position);
                        }
                    }
                });
            }

        } catch (Exception e) {
            Log.e("ActionDetailAdapter", "onBindViewHolder:catch " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (courseActionBeanList != null && courseActionBeanList.size() > 0) {
            return courseActionBeanList.size();
        }
        return 0;
    }

    public class MyActionViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgBg;
        public TextView tvRest, tvName, tvTime;

        public MyActionViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBg = itemView.findViewById(R.id.item_action_detail_img_bg);
            tvName = itemView.findViewById(R.id.item_action_detail_tv_name);
            tvRest = itemView.findViewById(R.id.item_action_detail_tv_rest_time);
            tvTime = itemView.findViewById(R.id.item_action_detail_tv_time);
        }
    }
}
