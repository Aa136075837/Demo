package com.example.bo.nixon.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.smartcustomview.utils.DisplayUtil;

/**
 * @author ARZE
 * @version 创建时间：2017/6/8 14:45
 * @说明
 */
public class SmartPopWindow extends PopupWindow {

    private Context mContext;

    private View mRootView;
    private TextView mTitleTv;
    private ImageView mImageView;

    private final static int ALPHA_CHANGE = 0X0001;
    private boolean isStart = false;

    public SmartPopWindow(Context context,@LayoutRes int layout) {
        mContext = context;
        mRootView = LayoutInflater.from(mContext).inflate(layout, null);
        mTitleTv = (TextView) mRootView.findViewById(R.id.smart_pop_title_tv);
        mImageView = (ImageView) mRootView.findViewById(R.id.smart_pop_img);
        setContentView(mRootView);
        setWidth(DisplayUtil.dip2px(context,220));
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setAnimationStyle(R.style.pop_anim_style);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchable(true);
        setOutsideTouchable(true);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        changePopupWindow(1f,0.5f,100);
    }

    private void changePopupWindow(final float startAlpha, final float targetAlpha, long time) {
        final float valueAlpha = (targetAlpha - startAlpha) / (time / 10);
        isStart = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                float currentAlpha = startAlpha;
                while (targetAlpha < currentAlpha && isStart) {
                    try {
                        Thread.sleep(10);
                        currentAlpha = currentAlpha + valueAlpha;
                        sendMessage(currentAlpha);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!isStart) {
                    sendMessage(1f);
                }
            }
        }).start();
    }

    private void changePopupWindow(float alpha) {
        if ( mContext instanceof Activity) {
            Activity activity = (Activity) mContext;
            WindowManager.LayoutParams params = activity.getWindow().getAttributes();
            params.alpha = alpha;
            activity.getWindow().setAttributes(params);
        }
    }

    private void sendMessage(float alpha) {
        Message message = mHandler.obtainMessage();
        message.what = ALPHA_CHANGE;
        message.obj = alpha;
        mHandler.sendMessage(message);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ALPHA_CHANGE:
                    changePopupWindow((Float) msg.obj);
                    break;
            }
        }
    };

    @Override
    public void dismiss() {
        isStart = false;
        changePopupWindow(1f);
        super.dismiss();
    }


}
