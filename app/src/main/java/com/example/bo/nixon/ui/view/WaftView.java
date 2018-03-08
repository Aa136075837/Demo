package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.bo.nixon.R;

/**
 * @author bo.
 * @Date 2017/6/10.
 * @desc
 */

public class WaftView extends View {
    /**
     * 信号强度
     */
    private int mWaftStrength;
    /**
     * 满格信号强度
     */
    private int mWaftSum;

    public int getWaftStrength () {
        return mWaftStrength;
    }

    public void setWaftStrength (int waftStrength) {
        mWaftStrength = waftStrength;
    }

    public int getWaftSum () {
        return mWaftSum;
    }

    public void setWaftSum (int waftSum) {
        mWaftSum = waftSum;
    }

    public int getNorColor () {
        return mNorColor;
    }

    public void setNorColor (int norColor) {
        mNorColor = norColor;
    }

    public int getHasColor () {
        return mHasColor;
    }

    public void setHasColor (int hasColor) {
        mHasColor = hasColor;
    }

    /**
     * 没信号部分的颜色
     */
    private int mNorColor;
    /**
     * 有信号部分的颜色
     */
    private int mHasColor;
    private final int mDefWaftSum = 5; //默认总信号强度
    /**
     * 默认没信号部分的颜色
     */
    @ColorRes private final int mDefNorColor = R.color.white_dialog_line;
    @ColorRes private final int mDefHasColor = R.color.main_text_color;
    @ColorRes private final int mBgColor = R.color.white;
    private Paint mNorPaint;
    private Paint mHasPaint;
    private Paint mBgPaint;

    public WaftView (Context context) {
        this (context, null);
    }

    public WaftView (Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public WaftView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context, attrs, defStyleAttr);
        initPaint ();
    }

    private void init (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes (attrs, R.styleable.WaftView);
        mWaftSum = typedArray.getInt (R.styleable.WaftView_smart_waft_sum, mDefWaftSum);
        mWaftStrength = typedArray.getInt (R.styleable.WaftView_smart_waft_strength, 1);
        mNorColor =
            typedArray.getColor (R.styleable.WaftView_smart_waft_color_no, getResources ().getColor (mDefNorColor));
        mHasColor =
            typedArray.getColor (R.styleable.WaftView_smart_waft_color_has, getResources ().getColor (mDefHasColor));
        typedArray.recycle ();
    }

    private void initPaint () {
        mNorPaint = new Paint ();
        mNorPaint.setColor (mNorColor);
        mNorPaint.setStrokeWidth (10);

        mHasPaint = new Paint ();
        mHasPaint.setColor (mHasColor);
        mHasPaint.setStrokeWidth (10);

        mBgPaint = new Paint ();
        mBgPaint.setColor (getResources ().getColor (mBgColor));
    }

    @Override protected void onDraw (Canvas canvas) {
        drawBg (canvas);
        super.onDraw (canvas);
    }

    private void drawBg (Canvas canvas) {
        int width = getWidth ();
        int height = getHeight ();
        int startWid = width / 5;
        int startHei = height / 10;
        int spacing = 5; //间隔
        float[] pts = {
            startWid, startHei + spacing * 8, startWid, startHei + spacing * 10,//
            startWid + spacing * 3, startHei + spacing * 6, startWid + spacing * 3, startHei + spacing * 10,//
            startWid + spacing * 5 + 5, startHei + spacing * 4, startWid + spacing * 5 + 5, startHei + spacing * 10,//
            startWid + spacing * 7 + 10, startHei + spacing * 2, startWid + spacing * 7 + 10, startHei + spacing * 10,//
            startWid + spacing * 9 + 15, startHei , startWid + spacing * 9 + 15, startHei + spacing * 10,//
        };
        //canvas.drawLines (pts, mHasPaint);
        switch (mWaftStrength) {
            case 0:
                break;
            case 1:
                canvas.drawLine (pts[0], pts[1], pts[2], pts[3], mHasPaint);
                canvas.drawLine (pts[4], pts[5], pts[6], pts[7], mNorPaint);
                canvas.drawLine (pts[8], pts[9], pts[10], pts[11], mNorPaint);
                canvas.drawLine (pts[12], pts[13], pts[14], pts[15], mNorPaint);
                canvas.drawLine (pts[16], pts[17], pts[18], pts[19], mNorPaint);
                break;
            case 2:
                canvas.drawLine (pts[0], pts[1], pts[2], pts[3], mHasPaint);
                canvas.drawLine (pts[4], pts[5], pts[6], pts[7], mHasPaint);
                canvas.drawLine (pts[8], pts[9], pts[10], pts[11], mNorPaint);
                canvas.drawLine (pts[12], pts[13], pts[14], pts[15], mNorPaint);
                canvas.drawLine (pts[16], pts[17], pts[18], pts[19], mNorPaint);

                break;
            case 3:
                canvas.drawLine (pts[0], pts[1], pts[2], pts[3], mHasPaint);
                canvas.drawLine (pts[4], pts[5], pts[6], pts[7], mHasPaint);
                canvas.drawLine (pts[8], pts[9], pts[10], pts[11], mHasPaint);
                canvas.drawLine (pts[12], pts[13], pts[14], pts[15], mNorPaint);
                canvas.drawLine (pts[16], pts[17], pts[18], pts[19], mNorPaint);
                break;
            case 4:
                canvas.drawLine (pts[0], pts[1], pts[2], pts[3], mHasPaint);
                canvas.drawLine (pts[4], pts[5], pts[6], pts[7], mHasPaint);
                canvas.drawLine (pts[8], pts[9], pts[10], pts[11], mHasPaint);
                canvas.drawLine (pts[12], pts[13], pts[14], pts[15], mHasPaint);
                canvas.drawLine (pts[16], pts[17], pts[18], pts[19], mNorPaint);
                break;
            case 5:
                canvas.drawLine (pts[0], pts[1], pts[2], pts[3], mHasPaint);
                canvas.drawLine (pts[4], pts[5], pts[6], pts[7], mHasPaint);
                canvas.drawLine (pts[8], pts[9], pts[10], pts[11], mHasPaint);
                canvas.drawLine (pts[12], pts[13], pts[14], pts[15], mHasPaint);
                canvas.drawLine (pts[16], pts[17], pts[18], pts[19], mHasPaint);
                break;
        }
    }
}
