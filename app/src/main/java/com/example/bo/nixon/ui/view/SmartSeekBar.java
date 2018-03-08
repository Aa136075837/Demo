package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v7.widget.AppCompatSeekBar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.SeekBar;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public class SmartSeekBar extends AppCompatSeekBar {

    private boolean mIsDragging;
    private float mTouchDownY;
    private int mScaledTouchSlop;
    private boolean isInScrollingContainer = false;

    public SmartSeekBar (Context context) {
        this (context, null);
    }

    public SmartSeekBar (Context context, AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public SmartSeekBar (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        mScaledTouchSlop = ViewConfiguration.get (context).getScaledTouchSlop ();
    }

    public boolean isInScrollingContainer () {
        return isInScrollingContainer;
    }

    public void setInScrollingContainer (boolean isInScrollingContainer) {
        this.isInScrollingContainer = isInScrollingContainer;
    }

    /**
     * On touch, this offset plus the scaled value from the position of the
     * touch will form the progress value. Usually 0.
     */
    float mTouchProgressOffset;

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged (h, w, oldh, oldw);
        Log.w ("onSizeChanged", "run---------->" + w);
    }

    @Override
    protected synchronized void onMeasure (int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure (heightMeasureSpec, widthMeasureSpec);
        setMeasuredDimension (getMeasuredHeight (), getMeasuredWidth ());
    }

    @Override
    protected synchronized void onDraw (Canvas canvas) {
        canvas.rotate (-90);
        canvas.translate (-getHeight (), 0);
        super.onDraw (canvas);
    }

    @Override
    public boolean onTouchEvent (MotionEvent event) {
        if (!isEnabled ()) {
            return false;
        }

        switch (event.getAction ()) {
            case MotionEvent.ACTION_DOWN:
                if (isInScrollingContainer ()) {

                    mTouchDownY = event.getY ();
                } else {
                    setPressed (true);

                    invalidate ();
                    onStartTrackingTouch ();
                    trackTouchEvent (event);
                    attemptClaimDrag ();

                    onSizeChanged (getWidth (), getHeight (), 0, 0);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (mIsDragging) {
                    trackTouchEvent (event);
                } else {
                    final float y = event.getY ();
                    if (Math.abs (y - mTouchDownY) > mScaledTouchSlop) {
                        setPressed (true);

                        invalidate ();
                        onStartTrackingTouch ();
                        trackTouchEvent (event);
                        attemptClaimDrag ();
                    }
                }
                onSizeChanged (getWidth (), getHeight (), 0, 0);
                break;

            case MotionEvent.ACTION_UP:
                if (mIsDragging) {
                    trackTouchEvent (event);
                    onStopTrackingTouch ();
                    setPressed (false);
                } else {
                    // Touch up when we never crossed the touch slop threshold
                    // should
                    // be interpreted as a tap-seek to that location.
                    onStartTrackingTouch ();
                    trackTouchEvent (event);
                    onStopTrackingTouch ();
                }
                onSizeChanged (getWidth (), getHeight (), 0, 0);
                // ProgressBar doesn't know to repaint the thumb drawable
                // in its inactive state when the touch stops (because the
                // value has not apparently changed)
                invalidate ();
                break;
        }
        return true;
    }

    private void trackTouchEvent (MotionEvent event) {
        final int height = getHeight ();
        final int top = getPaddingTop ();
        final int bottom = getPaddingBottom ();
        final int available = height - top - bottom;

        int y = (int) event.getY ();

        float scale;
        float progress = 0;

        // 下面是最小值
        if (y > height - bottom) {
            scale = 0.0f;
        } else if (y < top) {
            scale = 1.0f;
        } else {
            scale = (float) (available - y + top) / (float) available;
            progress = mTouchProgressOffset;
        }

        final int max = getMax ();
        progress += scale * max;

        setProgress ((int) progress);
        if (mListener != null){
            mListener.seekBarProgressChanged (this,(int) progress, true);
        }

    }

    /**
     * This is called when the user has started touching this widget.
     */
    void onStartTrackingTouch () {
        mIsDragging = true;
    }

    /**
     * This is called when the user either releases his touch or the touch is
     * canceled.
     */
    void onStopTrackingTouch () {
        mIsDragging = false;
    }

    private void attemptClaimDrag () {
        ViewParent p = getParent ();
        if (p != null) {
            p.requestDisallowInterceptTouchEvent (true);
        }
    }

    @Override
    public synchronized void setProgress (int progress) {

        super.setProgress (progress);
        onSizeChanged (getWidth (), getHeight (), 0, 0);
    }

    private ProgressChangedListener mListener;

    public void setProgressChangedListener (ProgressChangedListener listener) {
        mListener = listener;
    }

    public interface ProgressChangedListener {
        void seekBarProgressChanged (SeekBar seekBar, int progress, boolean fromeUser);
    }
}
