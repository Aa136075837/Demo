package com.example.bo.nixon.ui.activity.camera;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 20:07
 * @说明
 */
public class SensorHelper implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mSensor;
    private boolean mInitialized = false;
    private boolean isBeginStart = false;
    private boolean mAutoFocus = true;
    private OnFocus mOnFocus;
    private Handler mHandler = new Handler();
    private long mDelayTime = 1000;

    float mLastX = 0f;
    float mLastY = 0f;
    float mLastZ = 0f;
    private List<Float> mDeltaXs = new ArrayList<>();
    private List<Float> mDeltaYs = new ArrayList<> ();
    private List<Float> mDeltaZs = new ArrayList<> ();

    public SensorHelper(SensorManager sensorManager) {
        mSensorManager = sensorManager;
        mSensor = mSensorManager.getDefaultSensor(SensorManager.SENSOR_ORIENTATION);
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        dealEvent(event);
    }

    private void dealEvent(SensorEvent event) {

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        }
        float deltaX = Math.abs(mLastX - x);
        float deltaY = Math.abs(mLastY - y);
        float deltaZ = Math.abs(mLastZ - z);

        Log.w("ARZE34", "run--------------->" + deltaX + "or" + deltaY + "or" + deltaZ);
        if (deltaX > 1 && mAutoFocus) {
            mAutoFocus = false;
            mDeltaYs.clear();
            mDeltaXs.clear();
            mDeltaZs.clear();
            isBeginStart = true;
        }
        if (deltaY > 1 && mAutoFocus) {
            mAutoFocus = false;
            mDeltaYs.clear();
            mDeltaXs.clear();
            mDeltaZs.clear();
            isBeginStart = true;
        }
        if (deltaZ > 1 && mAutoFocus) {
            mAutoFocus = false;
            mDeltaYs.clear();
            mDeltaXs.clear();
            mDeltaZs.clear();
            isBeginStart = true;
        }

        mDeltaXs.add(deltaX);
        mDeltaYs.add(deltaY);
        mDeltaZs.add(deltaZ);

        if (mDeltaYs.size() > 20 && isBeginStart) {
            if (null != mOnFocus) {
                setDelayStatus();
                mOnFocus.focus();
            }
            isBeginStart = false;
        }
        mLastX = x;
        mLastY = y;
        mLastZ = z;
    }

    private void setDelayStatus() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAutoFocus = true;
            }
        },mDelayTime);
    }

    public void setAutoFocus(boolean autoFocus) {
        this.mAutoFocus = autoFocus;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public void unListener() {
        mSensorManager.unregisterListener(this);
        mOnFocus = null;
        mSensorManager = null;
        mHandler.removeCallbacks (null);
    }

    public interface OnFocus {
        void focus();
    }

    public void setFocusListener(OnFocus focus) {
        mOnFocus = focus;
    }

}
