package com.shyj.jianshen.fragment;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.viewpager.widget.ViewPager;

import com.github.mikephil.charting.charts.LineChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.DailyDataAdapter;
import com.shyj.jianshen.bean.LengthBodyBean;
import com.shyj.jianshen.chartmark.LineChartManager;
import com.shyj.jianshen.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;

public class DailyFragment extends BaseFragment {
    @BindView(R.id.fragment_daily_radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.fragment_daily_radio_btn_calendar)
    RadioButton btnCalender;
    @BindView(R.id.fragment_daily_radio_btn_data)
    RadioButton btnData;
    @BindView(R.id.fragment_daily_radio_btn_girth)
    RadioButton btnGirth;
    @BindView(R.id.fragment_daily_frame)
    FrameLayout frameLayout;

    private DailyDataAdapter dailyDataAdapter;
    private SelectDailyFragment selectDailyFragment;

    @Override
    public int layoutId() {
        return R.layout.fragment_daily;
    }

    @Override
    public void init() {
        selectDailyFragment = new SelectDailyFragment(getFragmentManager(),getActivity());
        btnCalender.setChecked(true);
        btnCalender.setTextColor(Color.WHITE);
        selectDailyFragment.chooseFragment(0);
        btnData.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
        btnGirth.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fragment_daily_radio_btn_calendar:
                        selectDailyFragment.chooseFragment(0);
                        btnCalender.setTextColor(Color.WHITE);
                        btnData.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        btnGirth.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.fragment_daily_radio_btn_data:
                        selectDailyFragment.chooseFragment(1);
                        btnData.setTextColor(Color.WHITE);
                        btnCalender.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        btnGirth.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        break;
                    case R.id.fragment_daily_radio_btn_girth:
                        selectDailyFragment.chooseFragment(2);
                        btnGirth.setTextColor(Color.WHITE);
                        btnData.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        btnCalender.setTextColor(getActivity().getResources().getColor(R.color.grey_67));
                        break;
                    default:break;
                }
            }
        });
    }
}
