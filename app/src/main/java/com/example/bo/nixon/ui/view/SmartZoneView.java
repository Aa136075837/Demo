package com.example.bo.nixon.ui.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bo.nixon.R;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 10:02
 * @说明
 */
public class SmartZoneView extends FrameLayout {

    private SmartClockView mImageView;
    private TextView mZoneTv;
    private AnimatorSet mAnimatorSet;

    public SmartZoneView(Context context) {
        super(context);
        init();
    }

    public SmartZoneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartZoneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.zone_layout, null);
        addView(view);
        mImageView = (SmartClockView) findViewById(R.id.zone_img);
        mZoneTv = (TextView) findViewById(R.id.zone_tv);
    }

    private void createAnimation() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(mImageView, "scaleX", 0.5f, 1f);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(mImageView, "scaleY", 0.5f, 1f);
        ObjectAnimator transY = ObjectAnimator.ofFloat(mZoneTv, "translationY", mZoneTv.getTranslationY(), getHeight() / 2 - mZoneTv.getHeight());
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setInterpolator(new OvershootInterpolator());
        mAnimatorSet.setDuration(800);
        mAnimatorSet.play(animatorX).with(animatorY).with(transY);
    }

    public void startAnimation() {
        if (null == mAnimatorSet) {
            createAnimation();
        }
        mAnimatorSet.start();
    }

    public void setImageRescource(int resId) {
        mImageView.setImageResource(resId);
    }

    public void setHour(int hour) {
        mImageView.setHour(hour);
    }

    public void setMin(int min) {
        mImageView.setMin(min);
    }

    public void setTime(int hour, int min) {
        mImageView.setTime(hour, min);
    }

    public void setText(String text) {
        mZoneTv.setText(text);
    }
}
