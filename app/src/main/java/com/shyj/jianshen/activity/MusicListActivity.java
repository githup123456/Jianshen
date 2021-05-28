package com.shyj.jianshen.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.ShareActionProvider;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.app.ActivityCompat;
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
import com.shyj.jianshen.update.DownloadFile;
import com.shyj.jianshen.update.DownloadFileDelegate;
import com.shyj.jianshen.update.DownloadFileTaskSync;
import com.shyj.jianshen.update.DownloadListener;
import com.shyj.jianshen.utils.HelpUtils;
import com.shyj.jianshen.utils.SaveUtils;
import com.shyj.jianshen.utils.StatuBarUtils;
import com.shyj.jianshen.utils.StringUtil;

import org.json.JSONException;
import org.litepal.LitePal;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
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

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private int REQUEST_PERMISSION_CODE = 64;

    @Override
    public int layoutId() {
        return R.layout.activity_music_list;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.e("WelActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    musicListAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    musicListAdapter.addMusicItemBeanList(musicItemList);
                    break;
            }
        }
    };

    private boolean isFailDownFile;

    @Override
    public void init() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
        StatuBarUtils.setTranslucentStatus(MusicListActivity.this);
        tvTitle.setText(R.string.music_list);
        musicItemList = new ArrayList<>();
        musicListAdapter = new MusicListAdapter(MusicListActivity.this);
        musicListAdapter.setOnMusicClick(new MusicListAdapter.OnMusicClick() {
            @Override
            public void onMusicClick(int pos) {
                SharedPreferences.Editor preferencesEditor = getSharedPreferences(PreferencesName.MUSIC_CHECKED, Context.MODE_PRIVATE).edit();
                preferencesEditor.putString(PreferencesName.MUSIC_CHECKED_NAME, musicItemList.get(pos).getName());
                preferencesEditor.commit();
                finish();
            }

            @Override
            public void onDownload(int pos, String url, MusicListAdapter.MusicListViewHolder musicListViewHolder) {
                isFailDownFile = false;
                String filePath = SaveUtils.BASE_FILE_URL + "/music/";
                Log.e(TAG, "onDownload: " + filePath);
                HelpUtils.createFile(filePath);
                String musicFile = filePath + StringUtil.getStringName(musicItemList.get(pos).getId() + "") + ".mp3";
                if (SaveUtils.fileIsExists(musicFile)) {
                    MusicItemBean musicItemBean = musicItemList.get(pos);
                    musicItemBean.setMusicFile(filePath);
                    musicItemBean.setDownload(true);
                    musicItemBean.saveOrUpdate("id = ?", musicItemBean.getId() + "");
                    Message message = new Message();
                    message.what = 2;
                    handler.sendEmptyMessage(message.what);
                }else {
                    DownloadFile downloadFile = new DownloadFile(url, musicFile);
                    downloadFile.setDownloadListener(new DownloadListener() {
                        @Override
                        public void onProgress(int progress) {
                            musicListViewHolder.colorfulRingProgressView.setPercent(progress);
                        }

                        @Override
                        public void onFinishDownload() {
                            Log.e(TAG, "onFinishDownload: ");
                            if (SaveUtils.fileIsExists(musicFile)) {
                                MusicItemBean musicItemBean = musicItemList.get(pos);
                                musicItemBean.setMusicFile(musicFile);
                                musicItemBean.setDownload(true);
                                musicItemBean.saveOrUpdate("id = ?", musicItemBean.getId() + "");
                            }
                            Message message = new Message();
                            message.what = 2;
                            handler.sendEmptyMessage(message.what);
                        }

                        @Override
                        public void onFail(Throwable throwable) {
                            Log.e(TAG, "onFail: " + throwable.getMessage());
                            isFailDownFile = true;
                            Message message = new Message();
                            message.what = 1;
                            handler.sendEmptyMessage(message.what);
                        }
                    });
                    downloadFile.execute();
                }

            }
        });

        rclMusic.setLayoutManager(new LinearLayoutManager(MusicListActivity.this));
        rclMusic.setAdapter(musicListAdapter);
        if (NetUtils.isConnected(MusicListActivity.this)) {
            initRequest();
        } else {
            try {
                musicItemList = LitePal.findAll(MusicItemBean.class);
                musicListAdapter.addMusicItemBeanList(musicItemList);
            } catch (Exception e) {
                Log.e(TAG, "init:catch " + e.getMessage());
            }
        }
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor preferences = getSharedPreferences(PreferencesName.MUSIC_CHECKED, Context.MODE_PRIVATE).edit();
                if (isChecked) {
                    preferences.putBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED, true);
                    rclMusic.setVisibility(View.VISIBLE);
                } else {
                    preferences.putBoolean(PreferencesName.MUSIC_CHECKED_IS_CHECKED, false);
                    rclMusic.setVisibility(View.GONE);
                }
                preferences.commit();
            }
        });
    }


    @OnClick({R.id.top_bar_green_img_left})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_green_img_left:
                finish();
                break;
        }
    }

    private void initRequest() {
        Map<String, Object> hashMap = new HashMap<>();
        RetrofitApi.request(MusicListActivity.this, RetrofitApi.createApi(Api.class).getSelectMusicList(hashMap), new IResponseListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                Log.e(TAG, "onSuccess: MusicList:" + data);
                if (data != null && data.length() > 0) {
                    try {
                        List<MusicItemBean> musicItemBeanList = new Gson().fromJson(data, new TypeToken<List<MusicItemBean>>() {
                        }.getType());
                        if (musicItemBeanList != null && musicItemBeanList.size() > 0) {
                            musicItemList.clear();
                            for (int i = 0; i < musicItemBeanList.size(); i++) {
                                MusicItemBean musicItemBean = musicItemBeanList.get(i);
                                String imgUrl = StringUtil.getMusicBgUrl(musicItemBean.getId() + "");
                                String imgFile = StringUtil.getMusicFileUrl(musicItemBean.getId() + "");
                                Log.e(TAG, "onSuccess: MusicList " + imgUrl + "\n" + imgFile);
                                String musicPath = SaveUtils.BASE_FILE_URL + "/music/" + StringUtil.getStringName(musicItemBean.getId() + "") + ".mp3";
                                String imgPath = SaveUtils.BASE_FILE_URL + "/music/png/" + StringUtil.getStringName(musicItemBean.getId() + "") + ".jpg";
                                if (SaveUtils.fileIsExists(musicPath)) {
                                    musicItemBean.setDownload(true);
                                    musicItemBean.setMusicFile(musicPath);
                                } else {
                                    musicItemBean.setMusicFile(imgFile);
                                }
                                if (SaveUtils.fileIsExists(imgPath)) {
                                    musicItemBean.setMusicBg(imgPath);
                                } else {
                                    musicItemBean.setMusicBg(imgUrl);
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            DownloadFileTaskSync.downloadSync(imgUrl, new File(imgPath), new DownloadFileDelegate() {
                                                @Override
                                                public void onStart(String url) {

                                                }

                                                @Override
                                                public void onDownloading(long currentSizeInByte, long totalSizeInByte, int percentage, String speed, String remainTime) {

                                                }

                                                @Override
                                                public void onComplete() {
                                                    MusicItemBean itemBean = musicItemBean;
                                                    itemBean.setMusicBg(imgPath);
                                                    itemBean.saveOrUpdate("id = ?", musicItemBean.getId() + "");
                                                }

                                                @Override
                                                public void onFail() {

                                                }

                                                @Override
                                                public void onCancel() {

                                                }
                                            });
                                        }
                                    }).start();
                                }
                                musicItemBean.saveOrUpdate("id = ?", musicItemBean.getId() + "");
                                musicItemList.add(musicItemBean);
                            }
                            musicListAdapter.addMusicItemBeanList(musicItemList);
                        } else {
                            try {
                                musicItemList = LitePal.findAll(MusicItemBean.class);
                                musicListAdapter.addMusicItemBeanList(musicItemList);
                            } catch (Exception e) {
                                Log.e(TAG, "init:catch " + e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "onSuccessCatch:MusicList " + e.getMessage());
                    }
                }
            }

            @Override
            public void onNotNetWork() {

            }

            @Override
            public void hasMore() {

            }

            @Override
            public void onFail(Throwable e) {
                Log.e(TAG, "onFail: MusicList:" + e.getMessage());
                try {
                    musicItemList = LitePal.findAll(MusicItemBean.class);
                    musicListAdapter.addMusicItemBeanList(musicItemList);
                } catch (Exception es) {
                    Log.e(TAG, "init:catch " + es.getMessage());
                }
            }
        });
    }

}
