package com.example.bo.nixon.ui.view.smartGraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.smartcustomview.utils.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/5 18:13
 * @说明
 */
public class SmartGraphView extends SmartBaseView {

    private SmartLine mSmartLine;
    private Paint mLinePaint;
    private RectF mTopRectF;
    private Path path = new Path();
    private Paint mTitlePaint;

    private long mScrollXTime = 800;
    private float mLastX;

    private boolean isStart = false;
    private RectF mRightRectF;

    private String[] mTitle = {"Day", "Week", "Month"};
    private String mTitleShow = "Today";
    private String mBackTop = "Back to";
    private String mToday = "today";
    private RectF[] mRectFs = new RectF[3];

    private EventAction mEventAction = new EventAction();

    private static float RIGHT_PADDING = 30;
    private static float BOTTOM_PADDING = 10;
    private static final int TEXT_SIZE = 10;
    private List<SmartPoint> mSmartPoints = new ArrayList<>();
    private OnEventActionListener mOnEventActionListener;
    private OnSmartXChangeListener mOnSmartXChangeListener;
    private SmartPoint mTempSmartPoint = new SmartPoint.Builder().build();
    private RectF mClickBackRectF;
    private Paint mTextPaint;
    private RectF mRightLineRectF;

    private Handler mHandler = new Handler(Looper.getMainLooper());


    public SmartGraphView(Context context) {
        super(context);
    }

    public SmartGraphView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartGraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSmartPoints(List<SmartPoint> smartPoints) {
        mSmartPoints.clear();
        mSmartPoints.addAll(smartPoints);
        mSmartLine.setetPoints(mSmartPoints);
        initPosition(getWidth(), getHeight());
        initTitleRectF();
        invalidate();
    }

    @Override
    protected void init() {
        super.init();
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#BBBBBB"));
        mLinePaint.setAntiAlias(true);
        mSmartLine = new SmartLine();
        mTitlePaint = new Paint();
        mTitlePaint.setTextSize(DisplayUtil.sp2px(getContext(), TEXT_SIZE));
        mTitlePaint.setTypeface(getCustomTypeface("fonts/Montserrat-Regular.ttf"));
        mTitlePaint.setColor(Color.BLACK);
        mTitlePaint.setStrokeWidth(DisplayUtil.dip2px(getContext(), 2));
        mTitlePaint.setAntiAlias(true);
        RIGHT_PADDING = DisplayUtil.dip2px(getContext(), 20);
        BOTTOM_PADDING = DisplayUtil.dip2px(getContext(), 30);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTypeface(getCustomTypeface("fonts/Montserrat-Regular.ttf"));
        mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(), 36));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPoint(canvas);
        if (null != mRightRectF)
            drawRightRectF(canvas);
        super.onDraw(canvas);
        drawTitle(canvas);
        drawRightText(canvas);
        if (null != mClickBackRectF) {
            drawBackRectF(canvas);
        }
    }

    private void drawBackRectF(Canvas canvas) {
        canvas.drawRect(mClickBackRectF, mLinePaint);
        mTextPaint.setStrokeWidth(3f);
        float width = mTextPaint.measureText(mBackTop);
        float height = DisplayUtil.getFontHeight(DisplayUtil.sp2px(getContext(), 40));
        float x = mClickBackRectF.centerX() - width / 2;
        float y1 = mClickBackRectF.centerY() - height / 4;
        float y2 = mClickBackRectF.centerY() + height * 3 / 4;
        canvas.drawText(mBackTop, x, y1, mTextPaint);
        canvas.drawText(mToday, x, y2, mTextPaint);
    }

    private void drawRightText(Canvas canvas) {
        mTitlePaint.setColor(Color.BLACK);
        canvas.save();
        canvas.rotate(270, getWidth() / 2, getHeight() / 2);
        canvas.translate((getWidth() - getHeight()) / 2, (getWidth() - getHeight()) / 2);
        float x = DisplayUtil.dip2px(getContext(), 10);
        float y = getHeight() - DisplayUtil.dip2px(getContext(), 5);
        canvas.drawText(mTitleShow, x, y, mTitlePaint);
        canvas.restore();
    }

    private void drawTitle(Canvas canvas) {
        for (int i = 0; i < mTitle.length; i++) {
            if (i == mEventAction.position) {
                mTitlePaint.setColor(Color.BLACK);
            } else {
                mTitlePaint.setColor(Color.GRAY);
            }
            canvas.drawText(mTitle[i], mRectFs[i].left, mRectFs[i].bottom, mTitlePaint);
        }
    }

    private void drawRightRectF(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#eaeaea"));
        canvas.drawRect(mRightRectF, paint);
        RectF rectF = new RectF(mRightRectF.left, 2, mRightRectF.right, mRightRectF.top);
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawRect(rectF, paint);
        mRightLineRectF = new RectF(mRightRectF.left, 2, mRightRectF.left + 4, mRightRectF.bottom);
        paint.setColor(Color.parseColor("#c4c4c4"));
        canvas.drawRect(mRightLineRectF, paint);
        Path path = new Path();
        path.moveTo(mRightRectF.left - DisplayUtil.dip2px(getContext(), 6), 0);
        path.lineTo(mRightRectF.left, DisplayUtil.dip2px(getContext(), 9));
        path.lineTo(mRightRectF.left + DisplayUtil.dip2px(getContext(), 6), 0);
        path.close();
        canvas.drawPath(path, paint);
        path.reset();
    }

    private void drawPoint(Canvas canvas) {
        for (int i = 0; i < mSmartLine.getmList().size(); i++) {
            SmartPoint smartPoint = mSmartLine.getmList().get(i);
            canvas.drawRect(smartPoint.getmPointLine(), mLinePaint);
            Log.w("SmartGraphView", smartPoint.getmPointLine().toString());
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            if (smartPoint.getmValue() / 250 >= getTargetValue()) {
                paint.setColor(Color.GRAY);
            } else {
                paint.setColor(Color.RED);
            }
            canvas.drawCircle(smartPoint.getmPointX(), smartPoint.getmPointY(), 6, paint);
            if (0 != i) {
                canvas.save();
                float w = mLinePaint.measureText(smartPoint.getmDate());
                float h = DisplayUtil.getFontHeight(DisplayUtil.sp2px(getContext(), TEXT_SIZE));
                float x = smartPoint.getmPointLine().centerX() - w / 2;
                float y = getHeight();
                canvas.rotate(270, smartPoint.getmPointLine().centerX(), y);
                canvas.translate(h / 2, w / 2);
                canvas.drawText(smartPoint.getmDate(), x, y, mTitlePaint);
                //  canvas.rotate(90,smartPoint.getmPointLine().centerX(),y);
                canvas.restore();
            }
        }
        canvas.drawRect(mTopRectF, mLinePaint);
        if (mSmartLine.isHasShape()) {
            drawShape(canvas);
        }
    }

    private void drawShape(Canvas canvas) {
        drawLine(canvas);
        drawArea(canvas);
        path.reset();
    }

    private void drawLine(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        canvas.save();
        final int lineSize = mSmartLine.getmList().size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        float nextPointX = Float.NaN;
        float nextPointY = Float.NaN;
        List<SmartPoint> list = mSmartLine.getmList();
        for (int valueIndex = 0; valueIndex < lineSize; ++valueIndex) {
            if (Float.isNaN(currentPointX)) {
                SmartPoint point = list.get(valueIndex);
                currentPointX = point.getmPointX();
                currentPointY = point.getmPointY();
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    SmartPoint point = list.get(valueIndex - 1);
                    previousPointX = point.getmPointX();
                    previousPointY = point.getmPointY();
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }

            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    SmartPoint point = list.get(valueIndex - 2);
                    prePreviousPointX = point.getmPointX();
                    prePreviousPointY = point.getmPointY();
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }
            if (valueIndex < lineSize - 1) {
                SmartPoint point = list.get(valueIndex + 1);
                nextPointX = point.getmPointX();
                nextPointY = point.getmPointY();
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }

            if (valueIndex == 0) {
                path.moveTo(currentPointX, currentPointY);
            } else {
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                final float firstControlPointX = previousPointX + (0.16f * firstDiffX);
                final float firstControlPointY = previousPointY + (0.16f * firstDiffY);
                final float secondControlPointX = currentPointX - (0.16f * secondDiffX);
                final float secondControlPointY = currentPointY - (0.16f * secondDiffY);
                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        canvas.restore();
    }

    private void drawArea(Canvas canvas) {
        canvas.save();
        if (mSmartLine.getmList().size() < 1)
            return;
        List<SmartPoint> points = mSmartLine.getmList();
        final float baseRawValue = getHeight();
        final float left = points.get(0).getmPointLine().left;
        final float right = points.get(points.size() - 1).getmPointLine().right;
        path.lineTo(right, baseRawValue);
        path.lineTo(left, baseRawValue);
        path.close();

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#eaeaea"));
        paint.setAlpha(155);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initPosition(w, h);
        initTitleRectF();
    }

    private void initTitleRectF() {
        for (int i = 0; i < mTitle.length; i++) {
            float offX = DisplayUtil.dip2px(getContext(), 10);
            if (0 != i) {
                offX = mRectFs[i - 1].right;
            }
            float left = offX;
            float top = DisplayUtil.dip2px(getContext(), 5);
            float textLength = mTitlePaint.measureText(mTitle[i]);
            float right = left + textLength + DisplayUtil.dip2px(getContext(), 20);
            float bottom = DisplayUtil.dip2px(getContext(), 20);
            RectF rectF = new RectF(left, top, right, bottom);
            mRectFs[i] = rectF;
        }
    }


    private void initPosition(int w, int h) {
        float dt = mSmartLine.getmDistance();
        List<SmartPoint> list = mSmartLine.getmList();
        float width = 2;
        float perY = (float) h / getMaxValue();
        mTopRectF = new RectF(0, 0, w, width);
        for (int i = 0; i < list.size(); i++) {
            SmartPoint smartPoint = list.get(i);
            float left = w - RIGHT_PADDING - i * dt - i * width;
            float top = 0;
            float right = left + width;
            float bottom = h - BOTTOM_PADDING;
            smartPoint.setmPointX(left + (right - left) / 2);
            smartPoint.setmPointY(h - perY * (smartPoint.getmValue() / 250));
            smartPoint.setmPointLine(new RectF(left, top, right, bottom));
            if (0 == i) {
                mRightRectF = new RectF(w - RIGHT_PADDING, smartPoint.getmPointY(), w, h);
            }
            if (i == list.size() - 1) {
                mClickBackRectF = new RectF(left - DisplayUtil.dip2px(getContext(), 320), 0, left, h);
            }
        }
    }

    @Override
    protected void moveX(float oldX, float x) {
        float offX = x - oldX;
        mLastX = offX;
        changePointX(offX);
    }

    @Override
    protected void cancel(float cancelX, float cancelY) {
        super.cancel(cancelX, cancelY);
        if (null != mClickBackRectF && mClickBackRectF.contains(cancelX, cancelY)) {
            moveBack();
        } else {
            if (!isScrollY()) {
                List<SmartPoint> points = mSmartLine.getmList();
                if (points.size() > 0) {
                    float right = getWidth() - RIGHT_PADDING + 5;
                    float trueRight = points.get(0).getmPointLine().right;
                    float offX = right - trueRight;
                    if (offX > 0) {
                        scrollXSmooth(offX);
                    } else {
                        if (Math.abs(mLastX) > 0f) {
                            float x = 15 * mLastX;
                            if (x > 0) {
                                x = x + 10;
                            } else {
                                x = x - 10;
                            }
                            scrollXSmooth(x);
                        } else {

                        }
                    }
                }
            }
        }
    }

    private void moveBack() {
        List<SmartPoint> points = mSmartLine.getmList();
        float offX = 0;
        for (int i = 0; i < points.size(); i++) {
            SmartPoint smartPoint = points.get(i);
            if (0 == i) {
                offX = mRightRectF.left - smartPoint.getmPointLine().right;
                if (null != mClickBackRectF) {
                    mClickBackRectF.left = mClickBackRectF.left + offX;
                    mClickBackRectF.right = mClickBackRectF.right + offX;
                }
            }
            smartPoint.getmPointLine().right = smartPoint.getmPointLine().right + offX;
            smartPoint.getmPointLine().left = smartPoint.getmPointLine().left + offX;
            smartPoint.setmPointX(smartPoint.getmPointX() + offX);
        }
        invalidate();
    }

    private void scrollXSmooth(float x) {
        if (isStart)
            return;
        isStart = true;
        final float s = x;
        new Thread(new Runnable() {
            @Override
            public void run() {
                long time = 0;
                float last = 0;
                while (time < mScrollXTime) {
                    try {
                        Thread.sleep(10);
                        time = time + 10;
                        float interpolation = getInterpolation((float) time / mScrollXTime);
                        float distance = interpolation * s;
                        changePointX(distance - last);
                        last = distance;
                        postInvalidate();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                isStart = false;
            }
        }).start();
    }

    private synchronized void changePointX(float v) {
        if (null == mClickBackRectF || mClickBackRectF.left + v > 0)
            return;
        if (null != mClickBackRectF) {
            mClickBackRectF.left = mClickBackRectF.left + v;
            mClickBackRectF.right = mClickBackRectF.right + v;
        }
        List<SmartPoint> points = mSmartLine.getmList();
        boolean isListener = false;

        for (int i = points.size() - 1; i >= 0; i--) {
            SmartPoint smartPoint = points.get(i);
            smartPoint.getmPointLine().right = smartPoint.getmPointLine().right + v;
            smartPoint.getmPointLine().left = smartPoint.getmPointLine().left + v;
            smartPoint.setmPointX(smartPoint.getmPointX() + v);
            if (null != mOnSmartXChangeListener && !isListener &&
                    null != mRightLineRectF && smartPoint.getmPointLine().right > mRightLineRectF.left) {
                isListener = true;
                if (mTempSmartPoint.getmValue() != smartPoint.getmValue()) {
                    Log.w("SmartGraphView", "run--------->" + smartPoint.getmValue());
                    mTempSmartPoint = new SmartPoint.Builder().build();
                    mTempSmartPoint.setmObjectBean(smartPoint.getmObjectBean());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mOnSmartXChangeListener.onSmart(mTempSmartPoint);
                        }
                    });
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float downX = event.getX();
                float downY = event.getY();
                mEventAction.initDefault();
                for (int i = 0; i < mRectFs.length; i++) {
                    RectF rectF = mRectFs[i];
                    if (rectF.contains(downX, downY)) {
                        mEventAction.isDownSelect = true;
                        mEventAction.position = i;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                for (int i = 0; i < mRectFs.length; i++) {
                    RectF rectF = mRectFs[i];
                    if (rectF.contains(upX, upY) && i == mEventAction.position
                            && mEventAction.isDownSelect) {
                        select(mEventAction);
                        break;
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void select(EventAction action) {
        switch (action.position) {
            case 0:
                setHasTargetLine(true);
                mTitleShow = "Today";
                mToday = "today";
                break;
            case 1:
                setHasTargetLine(false);
                mTitleShow = "This Week";
                mToday = "This Week";
                break;
            case 2:
                mTitleShow = "This Month";
                mToday = "This Month";
                setHasTargetLine(false);
                break;
        }
        invalidate();
        if (null != mOnEventActionListener) {
            mOnEventActionListener.onEvent(action);
        }
    }


    public static class EventAction {
        boolean isDownSelect = false;
        int position = 0;

        private void initDefault() {
            isDownSelect = false;
        }

        public boolean isDownSelect() {
            return isDownSelect;
        }

        public void setDownSelect(boolean downSelect) {
            isDownSelect = downSelect;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }

    public interface OnEventActionListener {

        void onEvent(EventAction eventAction);
    }

    public void setOnEventActionListener(OnEventActionListener listener) {
        mOnEventActionListener = listener;
    }


    public EventAction getmEventAction() {
        return mEventAction;
    }

    public interface OnSmartXChangeListener {
        void onSmart(SmartPoint smartPoint);
    }

    public void setOnSmartXChangeListener(OnSmartXChangeListener listener) {
        mOnSmartXChangeListener = listener;
    }


}
