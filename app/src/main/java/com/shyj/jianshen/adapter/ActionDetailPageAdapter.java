package com.shyj.jianshen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;


import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.fragment.ActionDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class ActionDetailPageAdapter  extends PagerAdapter {

    ArrayList<View> views ;
    Context context;

    public ActionDetailPageAdapter(Context context,List list){
        this.views = (ArrayList<View>) list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == (arg1);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
        System.out.println("--------------------remove position"+position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        System.out.println("--------------------add position"+position);
        return views.get(position);
    }

}


