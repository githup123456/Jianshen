package com.shyj.jianshen.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lzy.widget.HexagonView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.DaysCourseBean;
import com.shyj.jianshen.fragment.PlanFragment;

import java.util.ArrayList;
import java.util.List;

public class PlanDayAdapter extends RecyclerView.Adapter<PlanDayAdapter.MyViewHolder> {
    private Context context;
    private List<DaysCourseBean> list;
    private String TAG = "PlanDayAdapter";
    private int selectPosition = 0;
    private int nowDay;

    public PlanDayAdapter(Context context, List<DaysCourseBean> stringList,int nowDay){
        this.context =context;
        list = new ArrayList<>();
        this.list.addAll(stringList);
        this.nowDay = nowDay;
    }


    public void addPlanDayList(List<DaysCourseBean> stringList){
        list = new ArrayList<>();
        this.list.addAll(stringList);
        notifyDataSetChanged();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        if (selectPosition  != getSelectPosition()){
            this.selectPosition = selectPosition;
            notifyDataSetChanged();
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_plan_day,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        try {
            try {
                if (list!=null&& list.size()>position){
                    int day = list.get(position).getMouthDay();
                    int weekday = list.get(position).getWeekDay();
                    HexagonView hexagonView = holder.hexagonView;
                    hexagonView.setText(""+day);
                    if (position==nowDay){
                        hexagonView.setTextColorResource(R.color.white);
                        hexagonView.setBorderColorResource(R.color.green_19b55e);
                        hexagonView.setImageResource(R.color.green_19b55e);
                    }else {
                        if (position == selectPosition){
                            hexagonView.setTextColorResource(R.color.green_19b55e);
                            hexagonView.setBorderColorResource(R.color.green_19b55e);
                        }else {
                            hexagonView.setTextColorResource(R.color.grey_ae);
                            hexagonView.setBorderColorResource(R.color.grey_ae);
                        }
                        hexagonView.setBorderOverlay(false);
                        hexagonView.setImageResource(R.color.white);
                    }
                    if (weekday == 1){
                        holder.tvWeek.setText("M");
                    }else if (weekday == 2){
                        holder.tvWeek.setText("T");
                    }else if (weekday == 3){
                        holder.tvWeek.setText("W");
                    }else if (weekday == 4){
                        holder.tvWeek.setText("T");
                    }else if (weekday == 5){
                        holder.tvWeek.setText("F");
                    }else if (weekday == 6){
                        holder.tvWeek.setText("S");
                    }else{
                        holder.tvWeek.setText("S");
                    }

                    holder.hexagonView.setOnHexagonClickListener(new HexagonView.OnHexagonViewClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (planAdapterClick!=null){
                                planAdapterClick.dayOnClick(position);
                            }
                        }
                    });
                }

            }catch (Exception e){

            }

        }catch (Exception e){
            Log.e(TAG, "Exception: "+e.getMessage() );
        }
    }

    public interface PlanAdapterClick{
        void dayOnClick(int position);
    }

    private PlanAdapterClick planAdapterClick;

    public void setPlanAdapterClick(PlanAdapterClick planAdapterClick) {
        this.planAdapterClick = planAdapterClick;
    }

    @Override
    public int getItemCount() {
        if (list!=null&&list.size()>0){
            return list.size();
        }
       return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView tvWeek;
        public HexagonView hexagonView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvWeek = itemView.findViewById(R.id.item_plan_day_week_what);
            hexagonView = itemView.findViewById(R.id.item_plan_day_date);
        }
    }
}
