package com.shyj.jianshen.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.humrousz.sequence.AnimationImageView;
import com.shyj.jianshen.R;
import com.shyj.jianshen.activity.MusicListActivity;
import com.shyj.jianshen.bean.CourseActionBean;
import com.shyj.jianshen.eventbus.ActionPlayEvent;
import com.shyj.jianshen.eventbus.MusicPlayEvent;
import com.shyj.jianshen.key.IntentId;
import com.shyj.jianshen.utils.StringUtil;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.OnClick;

public class ActionPlayFragment extends BaseFragment {

    @BindView(R.id.action_play_img_action)
    AnimationImageView imgAction;
    @BindView(R.id.action_play_tv_count_duration)
    TextView tvCountDuration;
    @BindView(R.id.action_play_tv_count_time)
    TextView tvCountTime;
    @BindView(R.id.action_play_rel_playing)
    RelativeLayout relPlaying;
    @BindView(R.id.action_play_rel_suspend)
    RelativeLayout relSuspend;
    @BindView(R.id.action_play_rel_duration)
    RelativeLayout relDuration;
    @BindView(R.id.action_play_rel_count)
    RelativeLayout relCount;
    @BindView(R.id.action_play_progress)
    ColorfulRingProgressView progressView;

    private CourseActionBean actionBean;

    public static ActionPlayFragment getInstance(CourseActionBean actionBean) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IntentId.COURSE_ACTION_BEAN, actionBean);
        ActionPlayFragment playFragment = new ActionPlayFragment();
        playFragment.setArguments(bundle);
        return playFragment;
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_action_play;
    }

    @Override
    public void init() {
        actionBean = (CourseActionBean) getArguments().getSerializable(IntentId.COURSE_ACTION_BEAN);
        String actionFile = null;
        if (actionBean != null && actionBean.getActionID() != null) {
            actionFile = StringUtil.getActionMenUrl(actionBean.getActionID());
        } else {
            actionFile = StringUtil.getActionMenUrl(actionBean.getId());
        }
        Transformation<Bitmap> circleCrop = new CenterInside();
        WebpDrawableTransformation webpDrawableTransformation = new WebpDrawableTransformation(circleCrop);
        Glide.with(getActivity()).load(actionFile).addListener(new RequestListener<Drawable>() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                long duration = 0;
                int delay = getWebpPlayTime(resource);
                Log.e(TAG, "onResourceReady: " + delay + "  gap = "+actionBean.getGap());
                if ("countdown".equals(actionBean.getType())) {
                    relDuration.setVisibility(View.GONE);
                    relCount.setVisibility(View.VISIBLE);
                    setLoopCount((WebpDrawable) resource, actionBean.getPergroup());
                    Log.e(TAG, "onResourceReady: " + actionBean.getPergroup());
                    downTime((int) delay,actionBean.getPergroup());
                } else {
                    relDuration.setVisibility(View.VISIBLE);
                    relCount.setVisibility(View.GONE);
                    int num = (int) actionBean.getDuration() / delay;
                    duration = actionBean.getDuration();
                    setLoopCount((WebpDrawable) resource, num);
                    downTime((int) duration);
                }
                return false;
            }

        }).apply(new RequestOptions().optionalTransform(circleCrop).optionalTransform(WebpDrawable.class, webpDrawableTransformation).skipMemoryCache(true))
                .into(imgAction);
    }

    private WebpDrawable webpDrawable;
    private void setLoopCount(WebpDrawable resource, int count) {
        webpDrawable = resource;
        webpDrawable.setLoopCount(count);
    }

    private int getWebpPlayTime(Drawable resource) {
        WebpDrawable webpDrawable = (WebpDrawable) resource;

        try {
            Field gifStateField = WebpDrawable.class.getDeclaredField("state");
            gifStateField.setAccessible(true);
            Class gifStateClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpDrawable$WebpState");
            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
            gifFrameLoaderField.setAccessible(true);

            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpFrameLoader");
            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("gifDecoder");
            gifDecoderField.setAccessible(true);

            Class gifDecoderClass = Class.forName("com.bumptech.glide.gifdecoder.GifDecoder");
            Object gifDecoder = gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(resource)));
            Method getDelayMethod = gifDecoderClass.getDeclaredMethod("getDelay", int.class);
            getDelayMethod.setAccessible(true);
            // 设置只播放一次
            // 获得总帧数
            int count = webpDrawable.getFrameCount();
            int delay = 0;
            for (int i = 0; i < count; i++) {
                // 计算每一帧所需要的时间进行累加
                delay += (int) getDelayMethod.invoke(gifDecoder, i);
            }
            return delay;

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Long currentTime;
    private Runnable timeDownRunnable;
    private int downTime = 0;
    private int num = 1;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    tvCountTime.setText(msg.arg1+"");
                    break;
            }
        }
    };

    private Runnable countDownRunnable;

    private void downTime(int downTimeMax,int count) {
        downTime = downTimeMax;
        num = count;
        tvCountTime.setText(downTime/1000+"");
        tvCountDuration.setText(num+"/"+actionBean.getPergroup());
        if (timeDownRunnable == null) {
            timeDownRunnable = new Runnable() {
                @Override
                public void run() {
                    Long timeInterval = System.currentTimeMillis() - currentTime;
                    if (timeInterval > 30) {
                        timeInterval = 30L;
                    }
                    downTime = (int) (downTime - timeInterval);
/*                    int percent = (downTimeMax - downTime) * 100 / downTimeMax;
                    progressView.setPercent(percent);*/
                    Message msg = new Message();
                    msg.what = 1;
                    msg.arg1 = downTime/1000;
                    mHandler.sendMessage(msg);
                    if (!isSuspend){
                        if (downTime <= 0) {
                            if (num >1) {
                                num = num - 1;
                                downTime(downTimeMax,num);
                            }else {
                                if (actionBean.getGap() > 0) {
                                    EventBus.getDefault().post(new ActionPlayEvent(true));
                                } else {
                                    EventBus.getDefault().post(new ActionPlayEvent(false));
                                }
                            }

                        } else {
                            currentTime = System.currentTimeMillis();
                            mHandler.postDelayed(this, 1000);
                        }
                    }
                }
            };
        }
        currentTime = System.currentTimeMillis();
        mHandler.postDelayed(timeDownRunnable, 1000);
    }

    private void downTime(int downTimeMax) {
        downTime = downTimeMax;
        tvCountDuration.setText(downTimeMax/1000+"\"");
        if (countDownRunnable == null) {
            countDownRunnable = new Runnable() {
                @Override
                public void run() {
                    Long timeInterval = System.currentTimeMillis() - currentTime;
                    if (timeInterval > 30) {
                        timeInterval = 30L;
                    }
                    downTime = (int) (downTime - timeInterval);
                    int percent = (downTimeMax - downTime) * 100 / downTimeMax;
                    progressView.setPercent(percent);
                    if (!isSuspend){
                        if (downTime <= 0) {
                            if (actionBean.getGap() > 0) {
                                EventBus.getDefault().post(new ActionPlayEvent(true));
                            } else {
                                EventBus.getDefault().post(new ActionPlayEvent(false));
                            }
                        } else {
                            currentTime = System.currentTimeMillis();
                            mHandler.postDelayed(this, 10);
                        }
                    }
                }
            };
        }
        currentTime = System.currentTimeMillis();
        mHandler.postDelayed(countDownRunnable, 10);
    }

    private boolean isSuspend = false;

    @Override
    public void onDestroy() {
        if (countDownRunnable!=null&& mHandler!=null){
            mHandler.removeCallbacks(countDownRunnable);
        }
        if (timeDownRunnable!=null&& mHandler!=null){
            mHandler.removeCallbacks(timeDownRunnable);
        }
        super.onDestroy();
    }

    @OnClick({R.id.action_play_img_play,R.id.action_play_rel_continue,R.id.action_play_rel_end,R.id.action_play_rel_music,R.id.action_play_rel_lock})
    public void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.action_play_img_play:
                EventBus.getDefault().post(new MusicPlayEvent(false));
                relSuspend.setVisibility(View.VISIBLE);
                relPlaying.setVisibility(View.GONE);
                if (webpDrawable!=null){
                    webpDrawable.stop();
                }
                isSuspend = true;
                break;
            case R.id.action_play_rel_continue:
                EventBus.getDefault().post(new MusicPlayEvent(true));
                relSuspend.setVisibility(View.GONE);
                relPlaying.setVisibility(View.VISIBLE);
                if (webpDrawable!=null){
                    webpDrawable.start();
                }
                isSuspend = false;
                currentTime = System.currentTimeMillis();
                mHandler.postDelayed(countDownRunnable,10);
                break;
            case R.id.action_play_rel_end:
                if (webpDrawable!=null){
                    webpDrawable.stop();
                }
                getActivity().finish();
                break;
            case R.id.action_play_rel_lock:
                showLock();
                break;
            case R.id.action_play_rel_music:
                Intent intent = new Intent(getActivity(), MusicListActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void showLock(){

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSuspend){
            if (countDownRunnable!=null){
                isSuspend = false;
                currentTime = System.currentTimeMillis();
                mHandler.postDelayed(countDownRunnable,10);
            }
            if (timeDownRunnable!=null){
                isSuspend = false;
                currentTime = System.currentTimeMillis();
                mHandler.postDelayed(timeDownRunnable,1000);
            }

        }
//        Log.e(TAG, "onResume: "+actionBean.getName() );
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Log.e(TAG, "onHiddenChanged: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        isSuspend = true;
//        Log.e(TAG, "onPause: "+ actionBean.getName());
    }
}

