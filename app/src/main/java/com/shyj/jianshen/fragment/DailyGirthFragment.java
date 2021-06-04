package com.shyj.jianshen.fragment;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.github.mikephil.charting.charts.LineChart;
import com.shyj.jianshen.R;
import com.shyj.jianshen.bean.DaysWeightBean;
import com.shyj.jianshen.bean.LengthBodyBean;
import com.shyj.jianshen.bean.UsersBean;
import com.shyj.jianshen.chartmark.LineChartManager;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.utils.DateUtil;
import com.shyj.jianshen.utils.StringUtil;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.MODE_PRIVATE;

public class DailyGirthFragment extends BaseFragment {
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

    @BindView(R.id.fragment_girth_lineChart_waist)
    LineChart waistLineChart;
    @BindView(R.id.fragment_girth_btn_waist_record)
    Button waistRecordBtn;
    @BindView(R.id.fragment_girth_tv_waist_date)
    TextView waistDateTv;

    private LineChartManager waistLineChartManager;
    private List<LengthBodyBean> waistBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_hip)
    LineChart hipLineChart;
    @BindView(R.id.fragment_girth_btn_hip_record)
    Button hipRecordBtn;
    @BindView(R.id.fragment_girth_tv_hip_date)
    TextView hipDateTv;

    private LineChartManager hipLineChartManager;
    private List<LengthBodyBean> hipBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_thigh)
    LineChart thighLineChart;
    @BindView(R.id.fragment_girth_btn_thigh_record)
    Button thighRecordBtn;
    @BindView(R.id.fragment_girth_tv_thigh_date)
    TextView thighDateTv;

    private LineChartManager thighLineChartManager;
    private List<LengthBodyBean> thighBodyBeanList;

    @BindView(R.id.fragment_girth_lineChart_calf)
    LineChart calfLineChart;
    @BindView(R.id.fragment_girth_btn_calf_record)
    Button calfRecordBtn;
    @BindView(R.id.fragment_girth_tv_calf_date)
    TextView calfDateTv;

    private LineChartManager calfLineChartManager;
    private List<LengthBodyBean> calfBodyBeanList;


    private List<Float> strings;

    @Override
    public int layoutId() {
        return R.layout.fragment_girth;
    }

    @Override
    public void init() {
        initChest();
        initArm();
        initWaist();
        initHip();
        initCalf();
        initThigh();
    }

    @OnClick({R.id.fragment_girth_btn_waist_record,R.id.fragment_girth_btn_chest_record,R.id.fragment_girth_btn_thigh_record,
               R.id.fragment_girth_btn_hip_record,R.id.fragment_girth_btn_arm_record,R.id.fragment_girth_btn_calf_record})
    public void onClickRecord(View v){
        switch (v.getId()){
            case R.id.fragment_girth_btn_chest_record:
                initDialogRecordChest();
                break;
            case R.id.fragment_girth_btn_arm_record:
                initDialogRecordArm();
                break;
            case R.id.fragment_girth_btn_hip_record:
                initDialogRecordHip();
                break;
            case R.id.fragment_girth_btn_waist_record:
                initDialogRecordWaist();
                break;
            case R.id.fragment_girth_btn_thigh_record:
                initDialogRecordThigh();
                break;
            case R.id.fragment_girth_btn_calf_record:
                initDialogRecordCalf();
                break;
        }
    }

    private void initChest() {
        chestBodyBeanList = new ArrayList<>();
        try {
            chestBodyBeanList = LitePal.where("lengthChest > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "initChest: " + c.getMessage());
        }
        if (chestBodyBeanList != null && chestBodyBeanList.size() > 0) {
            chestDateTv.setText(DateUtil.formatDateToYMD(chestBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(chestBodyBeanList.get(chestBodyBeanList.size() - 1).getDate()));

            chestLineChartManager = new LineChartManager(chestLineChart, 40f);
            chestLineChartManager.showLineChart(chestBodyBeanList, "chest", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_CHEST);
            float length = chestBodyBeanList.get(0).getLengthChest();
            chestLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            chestLineChartManager.setChartFillDrawable(drawable);
            chestLineChartManager.setMarkerView(getActivity(), false);
        }

    }

    private void initArm() {
        armBodyBeanList = new ArrayList<>();
        try {
            armBodyBeanList = LitePal.where("lengtharm > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "initarm: " + c.getMessage());
        }
        if (armBodyBeanList != null && armBodyBeanList.size() > 0) {
            armDateTv.setText(DateUtil.formatDateToYMD(armBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(armBodyBeanList.get(armBodyBeanList.size() - 1).getDate()));

            armLineChartManager = new LineChartManager(armLineChart, 40f);
            armLineChartManager.showLineChart(armBodyBeanList, "arm", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_ARM);
            float length = armBodyBeanList.get(0).getLengthArm();
            armLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            armLineChartManager.setChartFillDrawable(drawable);
            armLineChartManager.setMarkerView(getActivity(), false);
        }

    }

    private void initWaist() {
        waistBodyBeanList = new ArrayList<>();
        try {
            waistBodyBeanList = LitePal.where("lengthwaist > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "initwaist: " + c.getMessage());
        }
        if (waistBodyBeanList != null && waistBodyBeanList.size() > 0) {
            waistDateTv.setText(DateUtil.formatDateToYMD(waistBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(waistBodyBeanList.get(waistBodyBeanList.size() - 1).getDate()));

            waistLineChartManager = new LineChartManager(waistLineChart, 40f);
            waistLineChartManager.showLineChart(waistBodyBeanList, "waist", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_WAIST);
            float length = waistBodyBeanList.get(0).getLengthWaist();
            waistLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            waistLineChartManager.setChartFillDrawable(drawable);
            waistLineChartManager.setMarkerView(getActivity(), false);
        }

    }

    private void initHip() {
        hipBodyBeanList = new ArrayList<>();
        try {
            hipBodyBeanList = LitePal.where("lengthhip > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "inithip: " + c.getMessage());
        }
        if (hipBodyBeanList != null && hipBodyBeanList.size() > 0) {
            hipDateTv.setText(DateUtil.formatDateToYMD(hipBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(hipBodyBeanList.get(hipBodyBeanList.size() - 1).getDate()));

            hipLineChartManager = new LineChartManager(hipLineChart, 40f);
            hipLineChartManager.showLineChart(hipBodyBeanList, "hip", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_HIP);
            float length = hipBodyBeanList.get(0).getLengthHip();
            hipLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            hipLineChartManager.setChartFillDrawable(drawable);
            hipLineChartManager.setMarkerView(getActivity(), false);
        }

    }

    private void initThigh() {
        thighBodyBeanList = new ArrayList<>();
        try {
            thighBodyBeanList = LitePal.where("lengththigh > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "initthigh: " + c.getMessage());
        }
        if (thighBodyBeanList != null && thighBodyBeanList.size() > 0) {
            thighDateTv.setText(DateUtil.formatDateToYMD(thighBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(thighBodyBeanList.get(thighBodyBeanList.size() - 1).getDate()));

            thighLineChartManager = new LineChartManager(thighLineChart, 40f);
            thighLineChartManager.showLineChart(thighBodyBeanList, "thigh", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_THIGH);
            float length = thighBodyBeanList.get(0).getLengthThigh();
            thighLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            thighLineChartManager.setChartFillDrawable(drawable);
            thighLineChartManager.setMarkerView(getActivity(), false);
        }

    }

    private void initCalf() {
        calfBodyBeanList = new ArrayList<>();
        try {
            calfBodyBeanList = LitePal.where("lengthcalf > 0").find(LengthBodyBean.class);
        } catch (Exception c) {
            Log.e(TAG, "initcalf: " + c.getMessage());
        }
        if (calfBodyBeanList != null && calfBodyBeanList.size() > 0) {
            calfDateTv.setText(DateUtil.formatDateToYMD(calfBodyBeanList.get(0).getDate()) + "-" + DateUtil.formatDateToYMD(calfBodyBeanList.get(calfBodyBeanList.size() - 1).getDate()));

            calfLineChartManager = new LineChartManager(calfLineChart, 40f);
            calfLineChartManager.showLineChart(calfBodyBeanList, "calf", getActivity().getResources().getColor(R.color.green_19b55e), IntentId.TYPE_CALF);
            float length = calfBodyBeanList.get(0).getLengthCalf();
            calfLineChartManager.setYAxisData(length + 25, length - 25, 8);
            //设置曲线填充色 以及 MarkerView
            Drawable drawable = getResources().getDrawable(R.drawable.line_chart_fill_color);
            calfLineChartManager.setChartFillDrawable(drawable);
            calfLineChartManager.setMarkerView(getActivity(), false);
        }
    }

    public void initDialogRecordChest() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_chest));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.chest));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthChest>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthChest() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthChest(w);
                        daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthChest(w);
                        daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    }
                    initChest();
                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogRecordArm() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_arm));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.arm));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthArm>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthArm() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthArm(w);
                        daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthArm(w);
                        daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    }
                    initArm();
                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogRecordHip() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_hip));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.hip));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthHip>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthHip() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthHip(w);
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthHip(w);
                    }
                    daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    initHip();
                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogRecordWaist() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_waist));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.waist));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthWaist>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthWaist() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthWaist(w);
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthWaist(w);
                    }
                    daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    initWaist();

                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogRecordThigh() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_thigh));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.thigh));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthThigh>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthThigh() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthThigh(w);
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthThigh(w);
                    }
                    daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    initThigh();
                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    public void initDialogRecordCalf() {
        WindowUtils.dismissCenterWindow(getActivity());
        View view = WindowUtils.centerShow(getActivity(), R.layout.dialog_center_record);
        TextView tvTitle = view.findViewById(R.id.dialog_record_tv_name);
        TextView tvTopLeft = view.findViewById(R.id.dialog_record_tv_top_left);
        TextView tvTopRight = view.findViewById(R.id.dialog_record_tv_top_right);
        TextView tvBomLeft = view.findViewById(R.id.dialog_record_tv_bom_left);
        TextView tvBomRight = view.findViewById(R.id.dialog_record_tv_bom_right);
        view.setOnClickListener(v -> {
            WindowUtils.dismissCenterWindow(getActivity());
        });
        tvTitle.setText(getString(R.string.your_calf));
        tvTopLeft.setText(getString(R.string.date));
        int[] date = DateUtil.getNowDate();
        tvTopRight.setText(DateUtil.MouthToEnglish(date[1]) + " " + date[2] + "," + date[0]);
        tvBomLeft.setText(getString(R.string.calf));
        try {
            LengthBodyBean lengthBodyBean = LitePal.where("lengthCalf>0").findLast(LengthBodyBean.class);
            if (lengthBodyBean != null) {
                tvBomRight.setText(lengthBodyBean.getLengthCalf() + "cm");
            } else {
                tvBomRight.setText("0cm");
            }
        } catch (Exception e) {
            tvBomRight.setText("0cm");
        }
        tvBomRight.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                showLengthWindow(tvBomRight, null);
            }
        });
        view.findViewById(R.id.dialog_record_tv_confirm).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                try {
                    LengthBodyBean daysWeightBean = LitePal.where("date = ?",DateUtil.getNowStringDate()).findFirst(LengthBodyBean.class);
                    if (daysWeightBean!=null){
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthCalf(w);
                    }else {
                        daysWeightBean = new LengthBodyBean();
                        daysWeightBean.setDate(DateUtil.getNowStringDate());
                        String weight = StringUtil.getNumbers(tvBomRight.getText().toString());
                        float w = Float.valueOf(weight);
                        daysWeightBean.setLengthCalf(w);
                    }
                    daysWeightBean.saveOrUpdate("date = ?", DateUtil.getNowStringDate());
                    initCalf();
                } catch (Exception e) {
                    Log.e(TAG, "onNoDoubleClick: " + e.getMessage());
                }
                WindowUtils.dismissCenterWindow(getActivity());
            }
        });
    }

    private String userHeight;
    private int heightOne = 0, heightTwo = 0, heightThree = 0;

    /**
     * 身高弹框
     */
    private void showLengthWindow(TextView heightTv, ImageView heightImg) {
        try {
            WindowUtils.dismissNODimBack(getActivity());
            View heightView = WindowUtils.noDimBackShow(getActivity(), R.layout.window_bottom_select, 2);
            WheelPicker wheelLeft = heightView.findViewById(R.id.window_bottom_wheel_left);
            List<String> strings = new ArrayList<>();
            for (int q = 5; q < 250; q++) {
                strings.add(q + "");
            }
            final String[] stringHeight = {"5", ".0", "cm"};
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
