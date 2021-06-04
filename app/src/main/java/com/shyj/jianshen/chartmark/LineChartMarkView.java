package com.shyj.jianshen.chartmark;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.MPPointF;
import com.shyj.jianshen.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by xhu_ww on 2018/6/1.
 * description:
 */
public class LineChartMarkView extends MarkerView {

    private TextView tvValue;
    private IAxisValueFormatter xAxisValueFormatter;
    DecimalFormat df = new DecimalFormat("0.0");

    private boolean isWeight = false;

    public LineChartMarkView(Context context, IAxisValueFormatter xAxisValueFormatter,boolean isWeight) {
        super(context, R.layout.line_chart_mark_view);
        this.xAxisValueFormatter = xAxisValueFormatter;
        tvValue = (TextView) findViewById(R.id.line_chart_mark_tv);
        this.isWeight = isWeight;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        Chart chart = getChartView();
        if (chart instanceof LineChart) {
            LineData lineData = ((LineChart) chart).getLineData();
            //获取到图表中的所有曲线
            List<ILineDataSet> dataSetList = lineData.getDataSets();
            for (int i = 0; i < dataSetList.size(); i++) {
                LineDataSet dataSet = (LineDataSet) dataSetList.get(i);
                //获取到曲线的所有在Y轴的数据集合，根据当前X轴的位置 来获取对应的Y轴值
                float y = dataSet.getValues().get((int) e.getX()).getY();
                if (i == 0) {
                    if (isWeight){
                        tvValue.setText(df.format(y)+"kg");
                    }else {
                        tvValue.setText(df.format(y)+"cm");
                    }
                }
            }
        }
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
