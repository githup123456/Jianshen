package com.shyj.jianshen.fragment;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.chartmark.BarChartManager;
import com.shyj.jianshen.chartmark.LineChartManager;
import com.shyj.jianshen.utils.DateUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DailyDataFragment extends BaseFragment{

    @BindView(R.id.fragment_data_bar_chart)
    BarChart barChart;
    @BindView(R.id.fragment_data_mouth)
    TextView tvMouth;


    private BarChartManager chestLineChartManager;
    private List<CalorieMouthBean> chestBodyBeanList;
    private List<Integer> strings;
    
    @Override
    public int layoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void init() {
        initChest();
    }

    private  int  numCal = 0;
    private void initChest(){
        chestBodyBeanList = new ArrayList<>();
        int[] date  = DateUtil.getNowDate();
        int year = date[0];
        int mouth = date[1];
        int day = date[2];
        int mouthDays = DateUtil.getNowMouthDays();
        tvMouth.setText(DateUtil.MouthEnglish(mouth));
        for (int i = 0;i<mouthDays;i++){
            CalorieMouthBean CalorieMouthBean = new CalorieMouthBean();
            CalorieMouthBean.setDay(DateUtil.getStringDate(year,mouth,i+1));
            int cal = 0;
            if (i>=day){
                cal = 0;
            }else {
                try {
                    List<DailyWorkBean> dailyWorkBeanList = LitePal.where("date = ?",DateUtil.getStringDate(year,mouth,i+1)).find(DailyWorkBean.class);
                    if (dailyWorkBeanList!=null&&dailyWorkBeanList.size()>0){
                        cal = dailyWorkBeanList.get(0).getKcal();
                    }else {
                        cal =0;
                    }
                }catch (Exception e){
                    Log.e(TAG, "initChest: "+e.getMessage() );
                    cal = 0;
                }
            }
            CalorieMouthBean.setCalorie((float) cal);
            chestBodyBeanList.add(CalorieMouthBean);
        }
        for (int j= 0;j<chestBodyBeanList.size();j++){
            Log.e(TAG, "initChest: "+chestBodyBeanList.get(j).getCalorie() );
            if (chestBodyBeanList.get(j).getCalorie()!=0){
                numCal++;
            }
        }
        if (numCal > 0) {
            chestLineChartManager = new BarChartManager(barChart);
            chestLineChartManager.showBarChart(chestBodyBeanList,"chest",getActivity().getResources().getColor(R.color.green_19b55e));
        }
/*        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
        chestLineChartManager.setChartFillDrawable(drawable);
        chestLineChartManager.setMarkerView(getActivity());*/
    }


}
