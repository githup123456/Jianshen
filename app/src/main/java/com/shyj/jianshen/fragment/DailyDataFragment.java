package com.shyj.jianshen.fragment;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.UserInformationActivity;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.bean.CalorieMouthBean;
import com.shyj.jianshen.bean.DailyWorkBean;
import com.shyj.jianshen.bean.DaysWeightBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.chartmark.BarChartManager;
import com.shyj.jianshen.chartmark.LineChartManager;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class DailyDataFragment extends BaseFragment {

    @BindView(R.id.fragment_data_bar_chart)
    BarChart barChart;
    @BindView(R.id.fragment_data_mouth)
    TextView tvMouth;

    @BindView(R.id.fragment_data_tv_weight_current)
    TextView tvCurrent;
    @BindView(R.id.fragment_data_tv_weight_last)
    TextView tvLastTime;
    @BindView(R.id.fragment_data_tv_weight_average)
    TextView tvAverage;
    @BindView(R.id.fragment_data_tv_weight_date)
    TextView tvWeightDate;
    @BindView(R.id.fragment_data_lineChart)
    LineChart lineChart;

    @BindView(R.id.fragment_data_bmi_img_color)
    ImageView imgBmiColor;
    @BindView(R.id.fragment_data_bmi_tv_num)
    TextView tvBmiNum;

    private List<DaysWeightBean> daysWeightBeanList;
    private LineChartManager lineChartManager;
    private BarChartManager barChartManager;
    private List<CalorieMouthBean> calMouthList;
    private List<Integer> strings;

    private  DecimalFormat df = new DecimalFormat("0.0");
    @Override
    public int layoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void init() {
        initChest();
        initWeight();
        initBMI();
    }

    private UsersBean usersBean;

    private void initBMI() {
        try {
            usersBean = LitePal.findFirst(UsersBean.class);
            if (usersBean != null) {
                String weight = StringUtil.getNumbers(usersBean.getWeight());
                String height = StringUtil.getNumbers(usersBean.getHeight());
                float w = Float.valueOf(weight);
                float h = Float.valueOf(height);
                float bmi = (float) ((int) (w / ((h / 100) * h / 100) * 10)) / 10;

                Log.e(TAG, "initBMI: " + bmi);
                tvBmiNum.setText(getString(R.string.your_bmi) + " " + bmi);
                initBMIBar(bmi);
            }

        } catch (Exception r) {
            Log.e(TAG, "initBMI: 00" + r.getMessage());
        }
    }

    private void initBMIBar(float bmi) {
        RelativeLayout.LayoutParams layoutParamsImg = (RelativeLayout.LayoutParams) imgBmiColor.getLayoutParams();
        float pro = 0;
        if (bmi < 15) {
            pro = 0;
            imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_5d9ff8));
        } else if (bmi > 35) {
            pro = 1.0f;
            imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_db3f44));
        } else {
            pro = (bmi - 15) / 21;
            if (bmi < 18.5f) {
                imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_5d9ff8));
            } else if (bmi >= 18.5f && bmi <= 24.9) {
                imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_32de7e));
            } else if (bmi >= 25f && bmi <= 29.9) {
                imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_edb756));
            } else {
                imgBmiColor.setBackgroundColor(getResources().getColor(R.color.color_db3f44));
            }
        }
        int length = HelpUtils.getPhoneWitch(getActivity()) - HelpUtils.dip2px(getActivity(), 45f);
        Log.e(TAG, "initBMIWidow: " + length);
        layoutParamsImg.leftMargin = (int) (pro * length);
        imgBmiColor.setLayoutParams(layoutParamsImg);
    }

    private void initWeight() {
        daysWeightBeanList = new ArrayList<>();
        try {
            daysWeightBeanList = LitePal.findAll(DaysWeightBean.class);
        } catch (Exception e) {
            Log.e(TAG, "initWeight: " + e.getMessage());
        }
        if (daysWeightBeanList != null && daysWeightBeanList.size() > 0) {

            tvWeightDate.setText(DateUtil.formatDateToYMD(daysWeightBeanList.get(0).getDate())+"-"+DateUtil.formatDateToYMD(daysWeightBeanList.get(daysWeightBeanList.size() - 1).getDate()));
            tvCurrent.setText(daysWeightBeanList.get(daysWeightBeanList.size() - 1).getWeight() + "");
            lineChartManager = new LineChartManager(lineChart,30.0f);
            lineChartManager.showLineChartWeight(daysWeightBeanList,"weight",getActivity().getResources().getColor(R.color.green_19b55e));
            float weight= daysWeightBeanList.get(0).getWeight();
            lineChartManager.setYAxisData(weight+30,weight-30,8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            lineChartManager.setChartFillDrawable(drawable);
            lineChartManager.setMarkerView(getActivity(),true);
            float num = 0;
            if (daysWeightBeanList.size()>30){
                for (int i=daysWeightBeanList.size()-30;i<daysWeightBeanList.size();i++){
                    num = num + daysWeightBeanList.get(i).getWeight();
                }

                float nowWeight = daysWeightBeanList.get(daysWeightBeanList.size()-1).getWeight();
                float lastWeight = daysWeightBeanList.get(daysWeightBeanList.size()-30).getWeight();
                float value = nowWeight - lastWeight;
                if (value>0){
                    tvLastTime.setText("+"+df.format(value));
                }else {
                    tvLastTime.setText(""+df.format(value));
                }
                tvAverage.setText(df.format(num/30)+"");
            }else {
                for (int i=0;i<daysWeightBeanList.size();i++){
                    num = num + daysWeightBeanList.get(i).getWeight();
                }
                float nowWeight = daysWeightBeanList.get(daysWeightBeanList.size()-1).getWeight();
                float lastWeight = daysWeightBeanList.get(0).getWeight();
                float value = nowWeight - lastWeight;
                if (value>0){
                    tvLastTime.setText("+"+df.format(value));
                }else {
                    tvLastTime.setText(""+df.format(value));
                }
                tvAverage.setText(df.format(num/daysWeightBeanList.size())+"");
            }
        } else {
            tvCurrent.setText("0");
            tvAverage.setText("0");
            tvLastTime.setText("+0");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume::" );
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            if (calMouthList!=null){
                try {
                    int cal;
                    List<DailyWorkBean> dailyWorkBeanList = LitePal.where("date = ?", DateUtil.getNowStringDate()).find(DailyWorkBean.class);
                    if (dailyWorkBeanList != null && dailyWorkBeanList.size() > 0) {
                        cal = dailyWorkBeanList.get(0).getKcal();
                    } else {
                        cal = 0;
                    }
                    if (cal!=0){
                        if (calMouthList.get(DateUtil.getNowDay-1).getCalorie()!=cal){
                            calMouthList.get(DateUtil.getNowDay-1).setCalorie(cal);
                            barChartManager = new BarChartManager(barChart);
                            barChartManager.showBarChart(calMouthList, "cal_mouth", getActivity().getResources().getColor(R.color.green_19b55e));
                        }
                    }
                }catch (Exception e){

                }
            }



        }
    }

    private int numCal = 0;

    private void initChest() {
        calMouthList = new ArrayList<>();
        int[] date = DateUtil.getNowDate();
        int year = date[0];
        int mouth = date[1];
        int day = date[2];
        int mouthDays = DateUtil.getNowMouthDays();
        tvMouth.setText(DateUtil.MouthEnglish(mouth));
        for (int i = 0; i < mouthDays; i++) {
            CalorieMouthBean calorieMouthBean = new CalorieMouthBean();
            calorieMouthBean.setDay(DateUtil.getStringDate(year, mouth, i + 1));
            int cal = 0;
            if (i >= day) {
                cal = 0;
            } else {
                try {
                    List<DailyWorkBean> dailyWorkBeanList = LitePal.where("date = ?", DateUtil.getStringDate(year, mouth, i + 1)).find(DailyWorkBean.class);
                    if (dailyWorkBeanList != null && dailyWorkBeanList.size() > 0) {
                        cal = dailyWorkBeanList.get(0).getKcal();
                    } else {
                        cal = 0;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "initChest: " + e.getMessage());
                    cal = 0;
                }
            }
            calorieMouthBean.setCalorie((float) cal);
            calMouthList.add(calorieMouthBean);
        }
        for (int j = 0; j < calMouthList.size(); j++) {
            Log.e(TAG, "initChest: " + calMouthList.get(j).getCalorie());
            if (calMouthList.get(j).getCalorie() != 0) {
                numCal++;
            }
        }
        if (numCal > 0) {
            barChartManager = new BarChartManager(barChart);
            barChartManager.showBarChart(calMouthList, "cal_mouth", getActivity().getResources().getColor(R.color.green_19b55e));
        }
    }

    @OnClick({R.id.fragment_data_btn_record, R.id.fragment_data_bmi_btn_edit})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_data_btn_record:
                initDialogRecord();
                break;
            case R.id.fragment_data_bmi_btn_edit:
                initDialogEdit();
                break;
        }
    }

    public void initDialogRecord() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(),R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_weight));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1])+" "+date[2]+","+date[0]);
        tvBomLeft.setText(getString(R.string.weight));
        if (usersBean!=null){
            tvBomRight.setText(usersBean.getWeight());
        }else {
            tvBomRight.setText("0 kg");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showWeightWindow(tvBomRight,null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    if (usersBean!=null){
                        usersBean.setWeight(tvBomRight.getText().toString());
                        usersBean.save();
                    }else {
                        try {
                            usersBean = LitePal.findFirst(UsersBean.class);
                            if (usersBean != null) {
                                usersBean.setWeight(tvBomRight.getText().toString());
                                usersBean.save();
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                        }
                    }
                    DaysWeightBean daysWeightBean = new DaysWeightBean();
                    daysWeightBean.setDate(DateUtil.getNowStringDate());
                    String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                    float w = Float.valueOf(weight);
                    Log.e(TAG, "onNoDoubleClick: "+w +"weight"+weight );
                    daysWeightBean.setWeight(w);
                    daysWeightBean.saveOrUpdate("date = ?",DateUtil.getNowStringDate());
                    initWeight();
                }catch (Exception e){
                    Log.e(TAG, "onNoDoubleClick: "+e.getMessage() );
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogEdit() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(),R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.weight) + " & " + getString(R.string.height));
        tvTopLeft.setText(getString(R.string.height));
        tvBomLeft.setText(getString(R.string.weight));
        if (usersBean != null) {
            tvBomRight.setText(usersBean.getWeight());
            tvTopRight.setText(usersBean.getHeight());
        } else {
            try {
                usersBean = LitePal.findFirst(UsersBean.class);
                if (usersBean != null) {
                    tvBomRight.setText(usersBean.getWeight());
                    tvTopRight.setText(usersBean.getHeight());
                } else {
                    tvBomRight.setText("0 kg");
                    tvTopRight.setText("0 cm");
                }
            } catch (Exception e) {
                Log.e(TAG, "initDialogEdit: ");
            }
        }
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissCenterWindow(getActivity());
                if (usersBean != null) {
                    usersBean.setHeight(tvTopRight.getText().toString());
                    usersBean.setWeight(tvBomRight.getText().toString());
                    usersBean.save();
                } else {
                    try {
                        usersBean = LitePal.findFirst(UsersBean.class);
                        if (usersBean != null) {
                            usersBean.setHeight(tvTopRight.getText().toString());
                            usersBean.setWeight(tvBomRight.getText().toString());
                            usersBean.save();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                    }
                }
                String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                String height = StringUtil.getNumbers(tvTopRight.getText().toString());
                float w = Float.valueOf(weight);
                float h = Float.valueOf(height);
                float bmi = (float) ((int) (w / ((h / 100) * h / 100) * 10)) / 10;
                Log.e(TAG, "initBMI: " + bmi);
                tvBmiNum.setText(getString(R.string.your_bmi) + " " + bmi);
                initBMIBar(bmi);
                DaysWeightBean daysWeightBean = new DaysWeightBean();
                daysWeightBean.setDate(DateUtil.getNowStringDate());
                daysWeightBean.setWeight(w);
                daysWeightBean.saveOrUpdate("date = ?",DateUtil.getNowStringDate());
                initWeight();
            }
        });
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showWeightWindow(tvBomRight, null);
            }
        });
        tvTopRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showHeightWindow(tvTopRight, null);
            }
        });
    }

    private String userWeight;
    private int weightOne = 0, weightTwo = 0, weightThree = 0;

    /**
     * 体重弹框
     */
    private void showWeightWindow(TextView weightTv, ImageView weightImg) {
        try {
            WindowUtils.dismissNODimBack(getActivity());
            View heightView = WindowUtils.noDimBackShow(getActivity(), R.layout.window_bottom_select, 2);
            WheelPicker wheelLeft = heightView.findViewById(R.id.window_bottom_wheel_left);
            List<String> strings = new ArrayList<>();
            for (int q = 2; q < 150; q++) {
                strings.add(q + "");
            }
            String[] stringWeight = {"2", ".0", "kg"};
            wheelLeft.setData(strings);
            wheelLeft.setSelectedItemPosition(weightOne);
            wheelLeft.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    Log.e("TAG", "onItemSelected: " + data.toString() + " position:" + position);
                    stringWeight[0] = data.toString();
                    weightOne = position;
                }
            });

            WheelPicker wheelCenter = heightView.findViewById(R.id.window_bottom_wheel_center);
            ArrayList<String> arrayListTwo = new ArrayList<String>();
            arrayListTwo.add(".0");
            arrayListTwo.add(".1");
            arrayListTwo.add(".2");
            arrayListTwo.add(".3");
            arrayListTwo.add(".4");
            arrayListTwo.add(".5");
            arrayListTwo.add(".6");
            arrayListTwo.add(".7");
            arrayListTwo.add(".8");
            arrayListTwo.add(".9");
            wheelCenter.setData(arrayListTwo);
            wheelCenter.setSelectedItemPosition(weightTwo);
            wheelCenter.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringWeight[1] = data.toString();
                    weightTwo = position;
                }
            });
            List<String> stringListRight = new ArrayList<>();
            if (getActivity().getSharedPreferences(PreferencesName.KG_LB, MODE_PRIVATE).getBoolean(PreferencesName.KG_LB_IS_KG, true)) {
                stringListRight.add("kg");
                stringListRight.add("ibs");
            } else {
                stringListRight.add("ibs");
                stringListRight.add("kg");
            }
            WheelPicker wheelRight = heightView.findViewById(R.id.window_bottom_wheel_right);
            wheelRight.setData(stringListRight);
            wheelRight.setSelectedItemPosition(weightThree);
            wheelRight.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringWeight[2] = data.toString();
                    weightThree = position;
                }
            });
            heightView.findViewById(R.id.window_bottom_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WindowUtils.dismissNODimBack(getActivity());
                }
            });

            heightView.findViewById(R.id.window_bottom_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userWeight = stringWeight[0] + stringWeight[1] + stringWeight[2];
                    if (weightImg != null) {
                        weightImg.setVisibility(View.GONE);
                        weightTv.setVisibility(View.VISIBLE);
                    }
                    weightTv.setText(userWeight);
                    WindowUtils.dismissNODimBack(getActivity());
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "showHeightWindow: " + e.getMessage());
        }
    }

    private String userHeight;
    private int heightOne = 0, heightTwo = 0, heightThree = 0;

    /**
     * 身高弹框
     */
    private void showHeightWindow(TextView heightTv, ImageView heightImg) {
        try {
            WindowUtils.dismissNODimBack(getActivity());
            View heightView = WindowUtils.noDimBackShow(getActivity(), R.layout.window_bottom_select, 2);
            WheelPicker wheelLeft = heightView.findViewById(R.id.window_bottom_wheel_left);
            List<String> strings = new ArrayList<>();
            for (int q = 20; q < 250; q++) {
                strings.add(q + "");
            }
            final String[] stringHeight = {"20", ".0", "cm"};
            wheelLeft.setData(strings);
            wheelLeft.setSelectedItemPosition(heightOne);
            wheelLeft.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    Log.e("TAG", "onItemSelected: " + data.toString() + " position:" + position);
                    stringHeight[0] = data.toString();
                    heightOne = position;
                }
            });

            WheelPicker wheelCenter = heightView.findViewById(R.id.window_bottom_wheel_center);
            ArrayList<String> arrayListTwo = new ArrayList<String>();
            arrayListTwo.add(".0");
            arrayListTwo.add(".1");
            arrayListTwo.add(".2");
            arrayListTwo.add(".3");
            arrayListTwo.add(".4");
            arrayListTwo.add(".5");
            arrayListTwo.add(".6");
            arrayListTwo.add(".7");
            arrayListTwo.add(".8");
            arrayListTwo.add(".9");
            wheelCenter.setData(arrayListTwo);
            wheelCenter.setSelectedItemPosition(heightTwo);
            wheelCenter.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringHeight[1] = data.toString();
                    heightTwo = position;
                }
            });
            List<String> stringListRight = new ArrayList<>();
            if (getActivity().getSharedPreferences(PreferencesName.KG_LB, MODE_PRIVATE).getBoolean(PreferencesName.KG_LB_IS_KG, true)) {
                stringListRight.add("cm");
                stringListRight.add("ft.in");
            } else {
                stringListRight.add("ft.in");
                stringListRight.add("cm");
            }
            WheelPicker wheelRight = heightView.findViewById(R.id.window_bottom_wheel_right);
            wheelRight.setData(stringListRight);
            wheelRight.setSelectedItemPosition(heightThree);
            wheelRight.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
                @Override
                public void onItemSelected(WheelPicker picker, Object data, int position) {
                    stringHeight[2] = data.toString();
                    heightThree = position;
                }
            });
            heightView.findViewById(R.id.window_bottom_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WindowUtils.dismissNODimBack(getActivity());
                }
            });

            heightView.findViewById(R.id.window_bottom_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userHeight = stringHeight[0] + stringHeight[1] + stringHeight[2];
                    if (heightImg != null) {
                        heightImg.setVisibility(View.GONE);
                        heightTv.setVisibility(View.VISIBLE);
                    }
                    heightTv.setText(userHeight);
                    WindowUtils.dismissNODimBack(getActivity());
                }
            });
        } catch (Exception e) {
            Log.e("TAG", "showHeightWindow: " + e.getMessage());
        }
    }

}
