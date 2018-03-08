package com.example.bo.nixon.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.utils.SmartPopWindow;
import com.smart.smartble.smartBle.BleDevice;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/6/10.
 * @desc
 */

public class ConnectFragment extends BaseFragment implements BleNearByFragment.BleItemClickListener {
    @BindView (R.id.img1) ImageView mImg1;
    @BindView (R.id.img2) ImageView mImg2;
    @BindView (R.id.img3) ImageView mImg3;
    @BindView (R.id.img4) ImageView mImg4;
    @BindView (R.id.img5) ImageView mImg5;
    @BindView (R.id.img6) ImageView mImg6;
    @BindView (R.id.img7) ImageView mImg7;
    @BindView (R.id.conn_connecting) TextView mConnConnecting;
    @BindView (R.id.anim_img_rel) RelativeLayout mAnimImgRel;
    @BindView (R.id.conn_frag_title) TextView mConnFragTitle;
    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;
    private static final int MSG_WAVE4_ANIMATION = 4;
    private static final int MSG_WAVE5_ANIMATION = 5;
    private static final int MSG_WAVE6_ANIMATION = 6;
    private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3, mAnimationSet4, mAnimationSet5;
    private BleNearByFragment mBleNearByFragment;
    private SmartPopWindow mSmartPopWindow;

    Handler mHandler = new Handler () {
        @Override public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mImg2.setVisibility (View.VISIBLE);
                    mImg2.startAnimation (mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mImg3.setVisibility (View.VISIBLE);
                    mImg3.startAnimation (mAnimationSet3);
                    break;
                case MSG_WAVE4_ANIMATION:
                    mImg4.setVisibility (View.VISIBLE);
                    mImg4.startAnimation (mAnimationSet4);
                    break;
                case MSG_WAVE5_ANIMATION:
                    mImg5.setVisibility (View.VISIBLE);
                    mImg5.startAnimation (mAnimationSet5);
                    break;
                case MSG_WAVE6_ANIMATION:
                    mImg6.setVisibility (View.VISIBLE);
                    mImg6.startAnimation (mAnimationSet5);
                    break;
            }
        }
    };

    @Override public int getLayoutResId () {
        return R.layout.fragment_connect;
    }

    @Override protected void initView () {
        super.initView ();
        mBleNearByFragment = new BleNearByFragment ();
        mBleNearByFragment.setBleItemClickListener (this);
        mSmartPopWindow = new SmartPopWindow (NixonApplication.getContext (),R.layout.pop_layout_white);
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
        mImg7.startAnimation (getAlphaAnim ());
        mImg1.startAnimation (mAnimationSet1);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE2_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE3_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE4_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE5_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE6_ANIMATION, OFFSET);
    }

    private void clearWaveAnimation () {
        mImg1.clearAnimation ();
        mImg2.clearAnimation ();
        mImg3.clearAnimation ();
        mImg4.clearAnimation ();
        mImg5.clearAnimation ();
        mImg6.clearAnimation ();
        mImg7.clearAnimation ();
    }

    @Override public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (hidden){
            clearWaveAnimation ();
        }else{
            connecting();
        }
    }

    @Override public void onDestroyView () {
        super.onDestroyView ();
        //clearWaveAnimation ();
    }

    @OnClick (R.id.conn_frag_title) public void onViewClicked () {

    }

    private void connecting () {
        showWaveAnimation ();
        mImg7.setImageResource (R.drawable.img_point);
        mConnFragTitle.setVisibility (View.VISIBLE);
        mConnConnecting.setText (R.string.connect_connecting);
    }

    private void connectFailed () {
        clearWaveAnimation ();
        mImg7.setImageResource (R.drawable.img_bg_finish);
        mConnFragTitle.setVisibility (View.GONE);
        mConnConnecting.setText (R.string.failed);
    }

    private void connectSuccessed () {
        if (mListener != null) {
            mListener.connectSucc ();
        }
        clearWaveAnimation ();
        mImg7.setImageResource (R.drawable.img_bg_finish);
        mConnFragTitle.setVisibility (View.GONE);
        mConnConnecting.setText (R.string.paired);
    }

    private ConnectResultListener mListener;

    public void setConnectResultListener (ConnectResultListener listener) {
        mListener = listener;
    }

    @Override public void toConnectBle (BleDevice bleNearBean) {

    }

    @Override public void connectBleDevice () {
        if (!mSmartPopWindow.isShowing ()) {
            mSmartPopWindow.showAtLocation (mAnimImgRel, Gravity.CENTER, 0, 0);
        }

    }

    @Override public void authorBleSuccessfully () {
        mSmartPopWindow.dismiss ();
        ToastManager.show (NixonApplication.getContext (), "授权成功", Toast.LENGTH_SHORT);
        connectSuccessed();
    }

    @Override public void authorTimeOut () {

    }

    public interface ConnectResultListener {
        void connectSucc ();

        void connectFail ();
    }
}
