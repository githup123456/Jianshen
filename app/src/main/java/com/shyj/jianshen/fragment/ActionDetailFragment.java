package com.shyj.jianshen.fragment;

import android.os.Bundle;

import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.BaseActivity;
import com.shyj.jianshen.key.IntentId;

public class ActionDetailFragment extends BaseFragment {

    public static  ActionDetailFragment  instance(int pos){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentId.FRAGMENT_POS,pos);
        ActionDetailFragment actionDetailFragment = new ActionDetailFragment();
        actionDetailFragment.setArguments(bundle);
        return actionDetailFragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_action_detail;
    }

    @Override
    public void init() {

    }
}
