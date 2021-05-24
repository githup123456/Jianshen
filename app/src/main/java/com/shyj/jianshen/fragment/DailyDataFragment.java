package com.shyj.jianshen.fragment;

import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.charts.BarChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.chartmark.BarChartManager;
import com.shyj.jianshen.chartmark.LineChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DailyDataFragment extends BaseFragment{

    @BindView(R.id.fragment_data_bar_chart)
    BarChart barChart;
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

    private void initChest(){
        chestBodyBeanList = new ArrayList<>();
        strings = new ArrayList<>();
        strings.add(65);
        strings.add(92);
        strings.add(164);
        strings.add(91);
        strings.add(46);
        for (int i = 10;i<30;i++){
            CalorieMouthBean CalorieMouthBean = new CalorieMouthBean();
            CalorieMouthBean.setDay("202105"+i);
            int a = strings.get(i%strings.size());
            CalorieMouthBean.setCalorie((float) a);
            chestBodyBeanList.add(CalorieMouthBean);
        }
        chestLineChartManager = new BarChartManager(barChart);
        chestLineChartManager.showBarChart(chestBodyBeanList,"chest",getActivity().getResources().getColor(R.color.green_19b55e));
/*        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
        chestLineChartManager.setChartFillDrawable(drawable);
        chestLineChartManager.setMarkerView(getActivity());*/
    }


}
