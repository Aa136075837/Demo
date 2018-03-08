package com.example.bo.nixon.ui.view.smartGraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.example.smartcustomview.utils.DisplayUtil;
import com.smart.smartble.SmartManager;

/**
 * @author ARZE
 * @version 创建时间：2017/6/5 17:21
 * @说明
 */
public abstract class SmartBaseView extends SmartBaseTouchView {

    private static final String TAG = "SmartBaseView";
    private int mMaxValue = 40000 / 250;
    private int mTargetValue = 6000 / 250;
    private float mTargetLineHeight = 4f;
    private float mTargetLineColor = 0x000000;
    private RectF mLineRectF;
    private Paint mLinePaint;
    private RectF mCenterRectF;
    private Paint mTextPaint;
    private float mLastY = 0;
    private final long DURATION = 800;
    private final float V = 100;
    private boolean hasTargetLine = true;
    private boolean isDownClick = false;
    private boolean hasMove = false;
    private IEditSelect mIEditSelect;
    private String btnContent;

    public SmartBaseView (Context context) {
        super (context);
        init ();
    }

    public SmartBaseView (Context context, AttributeSet attrs) {
        super (context, attrs);
        init ();
    }

    public SmartBaseView (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init ();
    }

    protected void init () {
        mLinePaint = new Paint ();
        mLinePaint.setColor (Color.BLACK);
        mLinePaint.setAntiAlias (true);
        mTextPaint = new Paint ();
        mTextPaint.setColor (Color.BLACK);
        mTextPaint.setTextSize (DisplayUtil.sp2px (getContext (), 12));
        mTextPaint.setTypeface (getCustomTypeface ("fonts/Montserrat-Regular.ttf"));
        mTextPaint.setAntiAlias (true);
    }

    @Override
    protected void onDraw (Canvas canvas) {
        super.onDraw (canvas);
        if (hasTargetLine)
            drawTargetLine (canvas);
    }

    protected void drawTargetLine (Canvas canvas) {
        if (null != mLineRectF) {
            if (isDownClick && SmartManager.isDiscovery ()) {
                mLinePaint.setColor (Color.RED);
                btnContent = "OK";
            } else {
                mLinePaint.setColor (Color.BLACK);
                btnContent = "Edit";
            }
            canvas.drawRect (mLineRectF, mLinePaint);
            if (null != mCenterRectF) {
                canvas.drawRoundRect (mCenterRectF, 30, 30, mLinePaint);
                drawText (canvas);
            }
        }
    }

    private void drawText (Canvas canvas) {
        float h = DisplayUtil.getFontHeight (DisplayUtil.sp2px (getContext (), 12));
        mTextPaint.setColor (Color.parseColor ("#BBBBBB"));
        float x1 = mCenterRectF.right + 15;
        float y1 = mCenterRectF.top + 15;
        if (y1 < h + 15) {
            y1 = mCenterRectF.centerY () + h;
        }
        canvas.drawText ("Goal:", x1, y1, mTextPaint);
        mTextPaint.setColor (Color.parseColor ("#000000"));
        float w = mTextPaint.measureText ("Goal:");
        float x2 = x1 + w + 10;
        float y2 = y1;
        if (y2 < h + 15) {
            y2 = mCenterRectF.centerY () + h;
        }
        canvas.drawText (((mTargetValue + 2) * 250 > mMaxValue * 250 ? mMaxValue * 250 :
                (mTargetValue + 2) * 250) + "  " + "steps", x2, y2, mTextPaint);
        mTextPaint.setColor (Color.WHITE);
        w = mTextPaint.measureText (btnContent);
        float x3 = mCenterRectF.centerX () - w / 2;
        float y3 = mCenterRectF.centerY () + h / 4;
        canvas.drawText (btnContent, x3, y3, mTextPaint);
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged (w, h, oldw, oldh);
        initLineRectF (w, h);
    }

    private void initLineRectF (int w, int h) {
        float left = 0;
        float top = h - (float) (mTargetValue + 2) / mMaxValue * h;
        top = top < 2 ? 2 : top;
        float right = w;
        float bottom = top + mTargetLineHeight;
        mLineRectF = new RectF (left, top, right, bottom);

        float cL = mLineRectF.centerX () - DisplayUtil.dip2px (getContext (), 20);
        float cT = mLineRectF.centerY () - DisplayUtil.dip2px (getContext (), 10);
        float cR = mLineRectF.centerX () + DisplayUtil.dip2px (getContext (), 20);
        float cB = mLineRectF.centerY () + DisplayUtil.dip2px (getContext (), 10);
        mCenterRectF = new RectF (cL, cT, cR, cB);
    }

    public void setMaxValue (int max) {
        mMaxValue = max;
        initLineRectF (getWidth (), getHeight ());
        invalidate ();
    }

    public void setTargetValue (int target) {
        mTargetValue = target / 250;
        initLineRectF (getWidth (), getHeight ());
        invalidate ();
    }

    protected int getMaxValue () {
        return mMaxValue;
    }

    protected int getTargetValue () {
        return mTargetValue;
    }

    @Override
    public void down (float x, float y) {
        if (mCenterRectF.contains (x, y)) {
            setScrollY (true);
            hasMove = false;
        } else {
            setScrollY (false);
        }
    }

    @Override
    protected void moveY (float lastY, float y) {
        if (hasTargetLine && isDownClick) {
            if (lastY != y)
                hasMove = true;
            float offY = (lastY - y);
            mLastY = offY;
            changeByY (offY);
        }
    }

    private void changeByY (float offY) {
        float per = (float) mMaxValue / getHeight ();
        int value = (int) (mTargetValue + offY * per);
        if (value > mMaxValue | value < 0) {
            return;
        }
        mTargetValue = value;
        initLineRectF (getWidth (), getHeight ());
    }

    @Override
    protected void cancel (float x, float y) {
       /* if (Math.abs (mLastY) > 0f && isScrollY () && hasTargetLine && isDownClick) {
            scrollYSmooth (10 * mLastY);
        } */
        int dp = DisplayUtil.dip2px (getContext (), 20);
        RectF rectF = new RectF (mCenterRectF.left - dp, mCenterRectF.top - dp, mCenterRectF
                .right + dp, mCenterRectF.bottom + dp);
        if (rectF.contains (x, y) && !hasMove && SmartManager.isDiscovery ()) {
            isDownClick = !isDownClick;
        }
        if (null != mIEditSelect) {
            mIEditSelect.onEditSelected (isDownClick, (mTargetValue + 2) * 250);
        }
    }

    private void scrollYSmooth (float y) {
        final float s = y;
        new Thread (new Runnable () {
            @Override
            public void run () {
                long time = 0;
                float last = 0;
                float v = s / DURATION;
                boolean isChange = false;
                while (time < DURATION && isDownClick) {
                    try {
                        Thread.sleep (5);
                        time = time + 5;
                        float distance;
                        float interpolation = getInterpolation ((float) time / DURATION);
                        distance = interpolation * s;
                        float per = (float) mMaxValue / getHeight ();
                        float value = mTargetValue + (distance - last) * per;
                        // mMaxValue + 20微调。
                        if (value > mMaxValue || value < 0) {
                            isChange = true;
                        }
                        if (isChange) {
                            changeByY (last - distance);
                        } else {
                            changeByY (distance - last);
                        }
                        last = distance;
                        postInvalidate ();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                }
            }
        }).start ();
    }

    protected float getInterpolation (float input) {
        return (1.0f - (1.0f - input) * (1.0f - input));
    }

    protected void setHasTargetLine (boolean target) {
        hasTargetLine = target;
    }

    public interface IEditSelect {
        void onEditSelected (boolean select, int target);
    }

    public void setIEditSelect (IEditSelect select) {
        mIEditSelect = select;
    }

    public RectF getmLineRectF () {
        return mLineRectF;
    }

    public void setmLineRectF (RectF mLineRectF) {
        this.mLineRectF = mLineRectF;
    }
}
