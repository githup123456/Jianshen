package com.shyj.jianshen.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.BaseActivity;
import com.shyj.jianshen.key.IntentId;

public class ActionDetailFragment extends DialogFragment {

    public static  ActionDetailFragment  instance(int pos){
        Bundle bundle = new Bundle();
        bundle.putInt(IntentId.FRAGMENT_POS,pos);
        ActionDetailFragment actionDetailFragment = new ActionDetailFragment();
        actionDetailFragment.setArguments(bundle);
        return actionDetailFragment;
    }

    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_action_detail,container,false);
        return rootView;
    }

}
