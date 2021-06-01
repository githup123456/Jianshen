package com.shyj.jianshen.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.CourseSearchResultActivity;
import com.shyj.jianshen.adapter.WorkoutPageAdapter;
import com.shyj.jianshen.click.NoDoubleClickListener;
import com.shyj.jianshen.dialog.WindowUtils;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.view.DelEditText;


import butterknife.BindView;
import butterknife.OnClick;

public class WorkOutFragment extends BaseFragment {

    @BindView(R.id.fragment_workout_radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.fragment_workout_radio_btn_one)
    RadioButton btnOne;
    @BindView(R.id.fragment_workout_radio_btn_two)
    RadioButton btnTwo;
    @BindView(R.id.fragment_workout_radio_btn_three)
    RadioButton btnThree;
    @BindView(R.id.fragment_workout_radio_btn_four)
    RadioButton btnFour;
    @BindView(R.id.fragment_workout_radio_btn_five)
    RadioButton btnFive;
    @BindView(R.id.fragment_workout_view_page)
    ViewPager noScrollViewPager;

    private WorkoutPageAdapter workoutPageAdapter;

    private int[] radioIds = {R.id.fragment_workout_radio_btn_one,R.id.fragment_workout_radio_btn_two,R.id.fragment_workout_radio_btn_three,R.id.fragment_workout_radio_btn_four,R.id.fragment_workout_radio_btn_five};

    @Override
    public int layoutId() {
        return R.layout.fragment_workout;
    }

    @Override
    public void init() {
        btnOne.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.fragment_workout_radio_btn_one:
                        noScrollViewPager.setCurrentItem(0);
                        break;
                    case R.id.fragment_workout_radio_btn_two:
                        noScrollViewPager.setCurrentItem(1);
                        break;
                    case R.id.fragment_workout_radio_btn_three:
                        noScrollViewPager.setCurrentItem(2);
                        break;
                    case R.id.fragment_workout_radio_btn_four:
                        noScrollViewPager.setCurrentItem(3);
                        break;
                    case R.id.fragment_workout_radio_btn_five:
                        noScrollViewPager.setCurrentItem(4);
                        break;
                    default:break;
                }
            }
        });
        workoutPageAdapter = new WorkoutPageAdapter(getFragmentManager(),getActivity());
        noScrollViewPager.setAdapter(workoutPageAdapter);
        noScrollViewPager.setCurrentItem(0);
        noScrollViewPager.setOffscreenPageLimit(1);
        noScrollViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioGroup.check(radioIds[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.fragment_workout_img_select})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.fragment_workout_img_select:
                showWindow();
                break;
        }
    }

    private void showWindow(){
        WindowUtils.dismissBrightness(getActivity());
        PopupWindow popupWindow = WindowUtils.cancelShow(getActivity(),R.layout.dialog_search_top,0);
        View view = popupWindow.getContentView();
        view.findViewById(R.id.window_search_tv_cancel).setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                WindowUtils.dismissNODimBack(getActivity());
            }
        });
        DelEditText delEditText = view.findViewById(R.id.window_search_et_search);
        delEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    switch (event.getAction()) {
                        case KeyEvent.ACTION_UP:
                            setIntent(delEditText.getText().toString());
                            WindowUtils.dismissBrightness(getActivity());
                            // 先隐藏键盘
                            hiddenKeyboard();
                            return true;
                        default: return true;
                    }
                }
                return false;
            }
        });
        showKeyboard(delEditText);
    }

    private void setIntent(String keyword){
        if (keyword!=null&&keyword.length()>0){
            Intent intent = new Intent(getActivity(),CourseSearchResultActivity.class);
            intent.putExtra(IntentId.SEARCH_KEYWORD,keyword);
            getActivity().startActivity(intent);
        }else {
            showToast(R.string.keyword_null);
        }
    }

    public void hiddenKeyboard() {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView()
                        .getApplicationWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
