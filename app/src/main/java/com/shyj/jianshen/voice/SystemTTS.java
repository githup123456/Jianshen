package com.shyj.jianshen.voice;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.shyj.jianshen.R;

import java.util.Locale;

public class SystemTTS  extends UtteranceProgressListener implements TTS, TextToSpeech.OnUtteranceCompletedListener {
    private Context mContext;
    private static SystemTTS singleton;
    private TextToSpeech textToSpeech; // 系统语音播报类
    private boolean isSuccess = true;

    public static SystemTTS getInstance(Context context) {
        if (singleton == null) {
            synchronized (SystemTTS.class) {
                if (singleton == null) {
                    singleton = new SystemTTS(context);
                }
            }
        }
        return singleton;
    }

    private SystemTTS(Context context) {
        this.mContext = context.getApplicationContext();
        textToSpeech = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                //系统语音初始化成功
                if (i == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setPitch(0.1f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                    textToSpeech.setSpeechRate(1.5f);
                    textToSpeech.setOnUtteranceProgressListener(SystemTTS.this);
                    textToSpeech.setOnUtteranceCompletedListener(SystemTTS.this);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //系统不支持中文播报
                        isSuccess = false;
                    }
                }

            }
        });
    }


    public void setPitchRate(float pitch){
        textToSpeech.setPitch(pitch);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        textToSpeech.setSpeechRate(1.0f);
    }

    public void playText(String playText) {
        if (!isSuccess) {
            Toast.makeText(mContext, mContext.getString(R.string.no_support_voice), Toast.LENGTH_SHORT).show();
            return;
        }
        if (textToSpeech != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                textToSpeech.speak(playText,
                        TextToSpeech.QUEUE_FLUSH, null, null);
            }
        }
    }

    public void stopSpeak() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
    }

    public boolean isSpeaking() {
        if (textToSpeech.isSpeaking()) {
            return true;
        }
        return false;
    }


    //播报完成回调
    @Override
    public void onUtteranceCompleted(String utteranceId) {

    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {
    }

    @Override
    public void onError(String utteranceId) {

    }
}
