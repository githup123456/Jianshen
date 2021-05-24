package com.shyj.jianshen.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.StringRes;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shyj.jianshen.R;
import com.shyj.jianshen.adapter.MusicListAdapter;
import com.shyj.jianshen.bean.MusicItemBean;
import com.shyj.jianshen.key.PreferencesName;
import com.shyj.jianshen.network.Api;
import com.shyj.jianshen.network.IResponseListener;
import com.shyj.jianshen.network.NetUtils;
import com.shyj.jianshen.network.RetrofitApi;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MusicListActivity extends BaseActivity {
    @BindView(R.id.top_bar_green_img_left)
    ImageView imgLeft;
    @BindView(R.id.top_bar_green_tv_title)
    TextView tvTitle;
    @BindView(R.id.music_list_switch)
    CheckBox mCheckBox;
    @BindView(R.id.music_list_rcl)
    RecyclerView rclMusic;

    private List<MusicItemBean> musicItemList;
    private MusicListAdapter musicListAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    public void init() {
        StatuBarUtils.setTranslucentStatus(MusicListActivity.this);
        tvTitle.setText(R.string.music_list);
        musicItemList = new ArrayList<>();
        musicListAdapter = new MusicListAdapter(MusicListActivity.this);
        musicListAdapter.setOnMusicClick(new MusicListAdapter.OnMusicClick() {
            @Override
            public void onMusicClick(int pos) {
                SharedPreferences.Editor preferencesEditor = getSharedPreferences(PreferencesName.MUSIC_CHECKED, Context.MODE_PRIVATE).edit();
                preferencesEditor.putString(PreferencesName.MUSIC_CHECKED_NAME,musicItemList.get(pos).getName());
                preferencesEditor.commit();
            }

            @Override
            public void onDownload(int pos, String url, MusicListAdapter.MusicListViewHolder musicListViewHolder) {

            }
        });
        rclMusic.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
        rclMusic.setAdapter(musicListAdapter);
        if (NetUtils.isConnected(MusicListActivity.this)){
            initRequest();
        }else {
            try {
                musicItemList = LitePal.findAll(MusicItemBean.class);
                musicListAdapter.addMusicItemBeanList(musicItemList);
            }catch (Exception e){
                Log.e(TAG, "init:catch "+e.getMessage() );
            }
        }
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor preferences = getSharedPreferences(PreferencesName.MUSIC_CHECKED, Context.MODE_PRIVATE).edit();
                if (isChecked){
                    preferences.putBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED,true);
                    rclMusic.setVisibility(View.VISIBLE);
                }else {
                    preferences.putBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED,false);
                    rclMusic.setVisibility(View.GONE);
                }
                preferences.commit();
            }
        });
    }

    @OnClick({R.id.top_bar_green_img_left})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.top_bar_green_img_left:
                finish();
                break;
        }
    }

    private void initRequest(){
        Map<String,Object> hashMap = new HashMap<>();
        RetrofitApi.request(MusicListActivity.this, RetrofitApi.createApi(Api.class).getSelectMusicList(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                Log.e(TAG, "onSuccess: MusicList:"+data );
                if (data!=null&&data.length()>0){
                    try {
                        List<MusicItemBean> musicItemBeanList = new Gson().fromJson(data,new TypeToken<List<MusicItemBean>>(){}.getType());
                        if (musicItemBeanList!=null&&musicItemBeanList.size()>0){
                            musicItemList = musicItemBeanList;
                            musicListAdapter.addMusicItemBeanList(musicItemList);
                            for (int i= 0; i< musicItemBeanList.size();i++){
                                MusicItemBean musicItemBean =  musicItemBeanList.get(i);
                                String imgUrl = StringUtil.getMusicBgUrl(musicItemBean.getId());
                                String imgFile = StringUtil.getMusicFileUrl(musicItemBean.getId());
                                Log.e(TAG, "onSuccess: MusicList "+imgUrl + "\n"+imgFile );
                                musicItemBean.setMusicBg(imgUrl);
                                musicItemBean.setMusicFile(imgFile);
                                musicItemBean.save();
                            }
                        }else {
                            try {
                                musicItemList = LitePal.findAll(MusicItemBean.class);
                                musicListAdapter.addMusicItemBeanList(musicItemList);
                            }catch (Exception e){
                                Log.e(TAG, "init:catch "+e.getMessage() );
                            }
                        }
                    }catch (Exception e){
                        Log.e(TAG, "onSuccessCatch:MusicList " +e.getMessage() );
                    }
                }
            }

            @Override
            public void onNotNetWork() {

            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "onFail: MusicList:"+e.getMessage() );
            }
        });
    }

}
