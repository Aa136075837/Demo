package com.example.bo.nixon.ui.activity.connect;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectingActivity extends BaseActivity {

    @BindView (R.id.connecting_title) TextView mConnectingTitle;
    @BindView (R.id.connecting_img1) ImageView mConnectingImg1;
    @BindView (R.id.connecting_img2) ImageView mConnectingImg2;
    @BindView (R.id.connecting_img3) ImageView mConnectingImg3;
    @BindView (R.id.connecting_img4) ImageView mConnectingImg4;
    @BindView (R.id.connecting_img5) ImageView mConnectingImg5;
    @BindView (R.id.connecting_img6) ImageView mConnectingImg6;
    @BindView (R.id.connecting_img7) ImageView mConnectingImg7;
    @BindView (R.id.conn_connecting) TextView mConnConnecting;
    @BindView (R.id.connecting_anim_img_rel) RelativeLayout mConnectingAnimImgRel;
    @BindView (R.id.connecting_activity) RelativeLayout mConnectingActivity;
    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;
    private static final int MSG_WAVE4_ANIMATION = 4;
    private static final int MSG_WAVE5_ANIMATION = 5;
    private static final int MSG_WAVE6_ANIMATION = 6;
    private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3, mAnimationSet4, mAnimationSet5;

    Handler mHandler = new Handler () {
        @Override public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mConnectingImg2.setVisibility (View.VISIBLE);
                    mConnectingImg2.startAnimation (mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mConnectingImg3.setVisibility (View.VISIBLE);
                    mConnectingImg3.startAnimation (mAnimationSet3);
                    break;
                case MSG_WAVE4_ANIMATION:
                    mConnectingImg4.setVisibility (View.VISIBLE);
                    mConnectingImg4.startAnimation (mAnimationSet4);
                    break;
                case MSG_WAVE5_ANIMATION:
                    mConnectingImg5.setVisibility (View.VISIBLE);
                    mConnectingImg5.startAnimation (mAnimationSet5);
                    break;
                case MSG_WAVE6_ANIMATION:
                    mConnectingImg6.setVisibility (View.VISIBLE);
                    mConnectingImg6.startAnimation (mAnimationSet5);
                    break;
            }
        }
    };

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_connecting);
        ButterKnife.bind (this);
        initAnim();
    }

    private void initAnim () {
        mAnimationSet1 = initAnimationSet (5);
        mAnimationSet2 = initAnimationSet (4);
        mAnimationSet3 = initAnimationSet (3);
        mAnimationSet4 = initAnimationSet (2);
        mAnimationSet5 = initAnimationSet (1);
        showWaveAnimation ();
    }

    private AnimationSet initAnimationSet (int i) {
        AnimationSet as = new AnimationSet (true);
        ScaleAnimation sa = new ScaleAnimation (1f, i * 20f, 1f, i * 20f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
            ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration (OFFSET * i);
        sa.setRepeatCount (Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation (1, 0.1f);
        aa.setDuration (OFFSET * i);
        aa.setRepeatCount (Animation.INFINITE);//设置循环
        as.addAnimation (sa);
        as.addAnimation (aa);
        return as;
    }

    private AlphaAnimation getAlphaAnim () {
        AlphaAnimation alphaAnimation = new AlphaAnimation (1, 0.0f);
        return alphaAnimation;
    }

    private void showWaveAnimation () {
        mConnectingImg7.startAnimation (getAlphaAnim ());
        mConnectingImg1.startAnimation (mAnimationSet1);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE2_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE3_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE4_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE5_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE6_ANIMATION, OFFSET);
    }

    private void clearWaveAnimation () {
        mConnectingImg1.clearAnimation ();
        mConnectingImg2.clearAnimation ();
        mConnectingImg3.clearAnimation ();
        mConnectingImg4.clearAnimation ();
        mConnectingImg5.clearAnimation ();
        mConnectingImg6.clearAnimation ();
        mConnectingImg7.clearAnimation ();
    }

    private void connecting () {
        showWaveAnimation ();
        mConnectingImg7.setImageResource (R.drawable.img_point);
        mConnectingTitle.setVisibility (View.VISIBLE);
        mConnConnecting.setText (R.string.connect_connecting);
    }

    private void connectFailed () {
        clearWaveAnimation ();
        mConnectingImg7.setImageResource (R.drawable.img_bg_finish);
        mConnectingTitle.setVisibility (View.GONE);
        mConnConnecting.setText (R.string.failed);
    }

    private void connectSuccessed () {
        clearWaveAnimation ();
        mConnectingImg7.setImageResource (R.drawable.img_bg_finish);
        mConnectingTitle.setVisibility (View.GONE);
        mConnConnecting.setText (R.string.paired);
    }

}
