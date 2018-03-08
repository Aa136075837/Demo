package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.bo.nixon.R;

/**
 * @author ARZE
 * @version 创建时间：2017/6/17 15:32
 * @说明
 */
public class SmartClockView extends FrameLayout {

    private ImageView mClockBgView;
    private ImageView mHourView;
    private ImageView mMinView;
    private int mHour = 9;
    private int mMin = 23;

    public SmartClockView(Context context) {
        super(context);
        init();
    }

    public SmartClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.colock_view, null);
        addView(view);
        mClockBgView = (ImageView) view.findViewById(R.id.smart_clock_view);
        mHourView = (ImageView) view.findViewById(R.id.smart_clock_hour_view);
        mMinView = (ImageView) view.findViewById(R.id.smart_clock_min_view);
        dealClock();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void dealClock() {
        float mHourDegree = (float) mHour / 12 * 360;
        float mMinDegree = (float) mMin / 60 * 360;

        mHourView.setRotation(mHourDegree);
        mMinView.setRotation(mMinDegree );
    }

    public void setImageResource(int resId) {
        mClockBgView.setImageResource(resId);
    }

    public void setHour(int hour) {
        mHour = hour;
        dealClock();
    }

    public void setMin(int min) {
        mMin = min;
        dealClock();
    }

    public void setTime(int hour,int min) {
        mHour = hour;
        mMin = min;
        dealClock();
    }
}
