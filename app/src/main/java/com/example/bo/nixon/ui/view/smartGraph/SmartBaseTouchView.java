package com.example.bo.nixon.ui.view.smartGraph;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author ARZE
 * @version 创建时间：2017/6/5 17:34
 * @说明
 */
public abstract class SmartBaseTouchView extends View {

    private static final String TAG = "SmartBaseTouchView";

    private float mDownX = 0f;
    private float mDownY = 0f;
    private boolean isScrollY = false;
    private long mDownTime = 0;

    public SmartBaseTouchView(Context context) {
        super(context);
    }

    public SmartBaseTouchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartBaseTouchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public abstract void down(float x,float y);

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                down(mDownX,mDownY);
                break;
            case MotionEvent.ACTION_UP:
                cancel(event.getX(),event.getY());
                mDownX = 0f;
                mDownY = 0f;
                break;
            case MotionEvent.ACTION_CANCEL:
                mDownX = 0f;
                mDownY = 0f;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isScrollY) {
                    moveX(mDownX,event.getX());
                } else {
                    moveY(mDownY,event.getY());
                }
                mDownX = event.getX();
                mDownY = event.getY();
                invalidate();
                break;
        }
        return true;
    }

    protected Typeface getCustomTypeface (@NonNull String fontPath) {
        return Typeface.createFromAsset (getContext ().getAssets (), fontPath);
    }

    protected abstract void cancel(float cancelX,float cancelY);

    protected abstract void moveY(float mDownY, float y);

    protected abstract void moveX(float mDownX, float x);

    protected void setScrollY(boolean y) {
        isScrollY = y;
    }

    protected boolean isScrollY () {
        return  isScrollY;
    }

}
