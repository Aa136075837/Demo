package com.example.bo.nixon.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.smart.smartble.utils.L;

import java.io.IOException;

/**
 * @author ARZE
 * @version 创建时间：2017/6/23 11:26
 * @说明
 */
public class SmartMediaPlay {

    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean isPlay = false;
    private static final int STOP_RING = 0x0001;

    public SmartMediaPlay(Context context) {
        mContext = context;
    }

    public void ring() {
        if (isPlay)
            return;
        isPlay = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                AudioManager mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                int current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                int haflMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
                int systemCurrent = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
                int halfSystemMax = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM) / 2;
                if (current < haflMax) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 2 * haflMax, 0);
                }
                if (systemCurrent < halfSystemMax) {
                    mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, 2 * halfSystemMax, AudioManager.ADJUST_SAME);
                }
                Uri alarmUri = RingtoneManager.getDefaultUri(AudioManager.STREAM_ALARM);
                try {
                    mMediaPlayer = new MediaPlayer();
                    mMediaPlayer.reset();
                    mMediaPlayer.setDataSource(mContext, alarmUri);
                    mMediaPlayer.prepare();
                    mMediaPlayer.start();
                    isPlay = true;
                    Message msg = mHandler.obtainMessage(STOP_RING);
                    msg.arg1 = current;
                    msg.arg2 = systemCurrent;
                    msg.obj = mAudioManager;
                    L.i("BleService current stop");
                    mHandler.sendMessageDelayed(msg, 10 * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case STOP_RING:
                    if (msg.obj instanceof AudioManager) {
                        AudioManager manager = (AudioManager) msg.obj;
                        int current = msg.arg1;
                        int systemCurrent = msg.arg2;
                        manager.setStreamVolume(AudioManager.STREAM_MUSIC, current, 0);
                        manager.setStreamVolume(AudioManager.STREAM_SYSTEM, systemCurrent, AudioManager.ADJUST_SAME);
                    }
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                    }
                    if (null != mMediaPlayer) {
                        mMediaPlayer.stop();
                        mMediaPlayer.release();
                    }
                    isPlay = false;
                    break;
            }
        }
    };

}
