package com.shyj.jianshen.fragment;

import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.viewpager.widget.ViewPager;

import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.WorkoutPageAdapter;
import com.shyj.jianshen.view.NoScrollViewPager;

import butterknife.BindView;

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
}
