package com.shyj.jianshen.fragment;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.LengthBodyBean;
import com.shyj.jianshen.chartmark.LineChartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DailyGirthFragment extends BaseFragment{
    @BindView(R.id.fragment_girth_lineChart_chest)
    LineChart chestLineChart;
    @BindView(R.id.fragment_girth_btn_chest_record)
    Button chestRecordBtn;
    @BindView(R.id.fragment_girth_tv_chest_date)
    TextView chestDateTv;

    private LineChartManager chestLineChartManager;
    private List<LengthBodyBean> chestBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_arm)
    LineChart armLineChart;
    @BindView(R.id.fragment_girth_btn_arm_record)
    Button armRecordBtn;
    @BindView(R.id.fragment_girth_tv_arm_date)
    TextView armDateTv;

    private LineChartManager armLineChartManager;
    private List<LengthBodyBean> armBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_leg)
    LineChart legLineChart;
    @BindView(R.id.fragment_girth_btn_leg_record)
    Button legRecordBtn;
    @BindView(R.id.fragment_girth_tv_leg_date)
    TextView legDateTv;

    private LineChartManager legLineChartManager;
    private List<LengthBodyBean> legBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_butt)
    LineChart buttLineChart;
    @BindView(R.id.fragment_girth_btn_butt_record)
    Button buttRecordBtn;
    @BindView(R.id.fragment_girth_tv_butt_date)
    TextView buttDateTv;

    private LineChartManager buttLineChartManager;
    private List<LengthBodyBean> buttBodyBeanList;

    private List<Integer> strings;

    @Override
    public int layoutId() {
        return R.layout.fragment_girth;
    }

    @Override
    public void init() {
        initChest();
        initArm();
        initLeg();
        initButt();
    }

    private void initChest(){
        chestBodyBeanList = new ArrayList<>();
        strings = new ArrayList<>();
        strings.add(65);
        strings.add(59);
        strings.add(52);
        strings.add(61);
        strings.add(56);
        for (int i = 10;i<30;i++){
            LengthBodyBean lengthBodyBean = new LengthBodyBean();
            lengthBodyBean.setDate("202105"+i);
            int a = strings.get(i%strings.size());
            lengthBodyBean.setLength((float) a);
            chestBodyBeanList.add(lengthBodyBean);
        }
        chestLineChartManager = new LineChartManager(chestLineChart,40f);
        chestLineChartManager.showLineChart(chestBodyBeanList,"chest",getActivity().getResources().getColor(R.color.green_19b55e));
        //设置曲线填充色 以及 MarkerView
        Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
        chestLineChartManager.setChartFillDrawable(drawable);
        chestLineChartManager.setMarkerView(getActivity());
    }

    private void initArm(){

    }

    private void initLeg(){

    }

    private void initButt(){

    }
}
