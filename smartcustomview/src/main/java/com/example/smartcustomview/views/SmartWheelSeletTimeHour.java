package com.example.smartcustomview.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.smartcustomview.R;
import com.example.smartcustomview.adapter.ArrayWheelAdapter;
import com.example.smartcustomview.listener.OnWheelChangedListener;
import com.example.smartcustomview.manager.FontManager;

public class SmartWheelSeletTimeHour extends FrameLayout implements OnWheelChangedListener {

    private View mContextView;
    private WheelView mAfterView;
    private WheelView mHourtView;
    private WheelView mMinView;
    private final static int MAX_HOUR = 11;
    private final static int MIN_HOUR = 0;
    private final static int MAX_MINUTE = 59;
    private final static int MIN_MINUTE = 0;
    private String[] mAfter = new String[2];
    private String[] mHours = new String[12];
    private String[] mMinutes = new String[60];
    private int mHourColor = 0xff000000;
    private int mMinColor = 0xff1fab7e;
    private int mAfterCurrent = 0;
    private int mHourCurrent = 0;
    private int mMinCurrent = 0;
    private ArrayWheelAdapter<String> mAfterAdapter;
    private ArrayWheelAdapter<String> mHourAdapter;
    private ArrayWheelAdapter<String> mMinAdapter;

    private onSmartTimeChange mSmartTimeChange;

    public SmartWheelSeletTimeHour(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public SmartWheelSeletTimeHour(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SmartWheelSeletTimeHour(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mContextView = LayoutInflater.from(context).inflate(R.layout.smart_wheel_hour_layout, this, true);
        FontManager.changeFonts((ViewGroup) mContextView.findViewById(R.id.smart_wheel_time_root), getContext());
        mAfterView = (WheelView) mContextView.findViewById(R.id.wheel_after);
        mHourtView = (WheelView) mContextView.findViewById(R.id.wheel_left);
        mMinView = (WheelView) mContextView.findViewById(R.id.wheel_right);
        mAfterView.addChangingListener(this);
        mHourtView.addChangingListener(this);
        mMinView.addChangingListener(this);
        mHourColor = getResources().getColor(R.color.text_black);
        mMinColor = getResources().getColor(R.color.text_black);
        setUpWheel();
    }

    @SuppressLint("RtlHardcoded")
    private void setUpWheel() {
        mAfter[0] = getContext().getString(R.string.AM);
        mAfter[1] = getContext().getString(R.string.PM);
        for (int i = MIN_HOUR; i < MAX_HOUR + 1; i++) {
            mHours[i] = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
        }
        for (int i = MIN_MINUTE; i < MAX_MINUTE + 1; i++) {
            mMinutes[i] = i < 10 ? "0" + String.valueOf(i) : String.valueOf(i);
        }
        mAfterView.setmSelectColor(mHourColor)
                .setScaleChange(true)
                .setFillCenterLine(true)
                .setScaleOff(1)
                .setHasItemShape(true)
                .setSelectSize(35)
                .setNormalSize(35)
                .setCenterLineColor(getResources().getColor(R.color.transparent));
        mHourtView.setmSelectColor(mHourColor)
                .setScaleChange(true)
                .setFillCenterLine(true)
                .setScaleOff(2)
                .setHasItemShape(true)
                .setSelectSize(35)
                .setNormalSize(35)
                .setCenterLineColor(getResources().getColor(R.color.transparent));
        mMinView.setmSelectColor(mMinColor)
                .setScaleChange(true)
                .setFillCenterLine(true)
                .setScaleOff(2)
                .setHasItemShape(true)
                .setSelectSize(35)
                .setNormalSize(35)
                .setCenterLineColor(getResources().getColor(R.color.transparent));
        //		mAfterView.setCyclic(true);
        mHourtView.setCyclic(true);
        mMinView.setCyclic(true);

        mAfterAdapter = new ArrayWheelAdapter<String>(getContext(), mAfter);
        mHourAdapter = new ArrayWheelAdapter<String>(getContext(), mHours);
        mMinAdapter = new ArrayWheelAdapter<String>(getContext(), mMinutes);

        mAfterAdapter.setmTextPosition(Gravity.CENTER | Gravity.RIGHT);
        //		mAfterAdapter.setTextSize(15);
        //		mAfterAdapter.setTextRightPadd(30);
        mAfterAdapter.setTextViewHeight(50);

        mHourAdapter.setmTextPosition(Gravity.CENTER);
        mHourAdapter.setTextViewHeight(50);
        //		mHourAdapter.setTextSize(20);
        mHourAdapter.setTextRightPadd(30);

        mMinAdapter.setmTextPosition(Gravity.CENTER | Gravity.LEFT);
        mMinAdapter.setTextViewHeight(50);
        //		mMinAdapter.setTextSize(20);
        mMinAdapter.setTextLeftPadd(30);

        mAfterView.setViewAdapter(mAfterAdapter);
        mHourtView.setViewAdapter(mHourAdapter);
        mMinView.setViewAdapter(mMinAdapter);
        mAfterView.setVisibleItems(5);
        mHourtView.setVisibleItems(5);
        mMinView.setVisibleItems(5);
        mAfterView.setCurrentItem(mAfterCurrent);
        mHourtView.setCurrentItem(mHourCurrent);
        mMinView.setCurrentItem(mMinCurrent);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        int after = mAfterView.getCurrentItem();
        int hour = mHourtView.getCurrentItem();
        int min = mMinView.getCurrentItem();
        //即上午12点时,滚到下午,
        if (R.id.wheel_left == wheel.getId()) {
            if (hour == 11 && after == 0) {
                after = 1;
                mAfterView.setCurrentItem(after, true);
                //                mAfterView.setCurrentItem(after);
            } else if (hour == 11 && after == 1) {
                after = 0;
                mAfterView.setCurrentItem(after, true);
                //                mAfterView.setCurrentItem(after);
            }
        }

        if (null != mSmartTimeChange) {
            mSmartTimeChange.onTimeChage(mAfter[after], Integer.valueOf(mHours[hour]),
                    Integer.valueOf(mMinutes[min]));
        }
    }

    public int getTime() {
        int after = mAfterView.getCurrentItem();
        int hour = mHourtView.getCurrentItem();
        int min = mMinView.getCurrentItem();
        int time = (after * 12 + hour) * 60 + min;
        return time;
    }

    public void setCurrent(int after, int hour, int min) {
        mAfterCurrent = after;
        mHourCurrent = hour;
        mMinCurrent = min;
        mAfterView.setCurrentItem(after);
        mHourtView.setCurrentItem(hour);
        mMinView.setCurrentItem(min);
    }

    public void doScrollView(int dt) {
        mHourtView.doScroll(dt);
        mAfterView.doScroll(dt);
        mMinView.doScroll(dt);
    }

    public interface onSmartTimeChange {
        void onTimeChage(String after, int hour, int min);
    }

    public void setmSmartTimeChange(onSmartTimeChange smartDateChange) {
        mSmartTimeChange = smartDateChange;
    }

    public void setSmartWheelColor(int afterColor, int hourColor, int minColor) {
        mHourColor = afterColor;
        mHourColor = hourColor;
        mMinColor = minColor;
        mAfterView.setmSelectColor(mHourColor);
        mHourtView.setmSelectColor(mHourColor);
        mMinView.setmSelectColor(mMinColor);
    }

    public void setScaleChange(boolean after, boolean hour, boolean min) {
        mAfterView.setScaleChange(after);
        mHourtView.setScaleChange(hour);
        mMinView.setScaleChange(min);
    }

    public void replayDefaultSetting() {
        mAfterView.setSelectSize(30)
                .setNormalSize(25)
                .setFillCenterLine(false)
                .setScaleChange(true)
                .setScaleOff(3);
        mHourtView.setSelectSize(30)
                .setNormalSize(25)
                .setFillCenterLine(false)
                .setScaleChange(true)
                .setScaleOff(3);
        mMinView.setSelectSize(30).setNormalSize(25).setFillCenterLine(false).setScaleChange(true).setScaleOff(3);

        mAfterAdapter.setTextViewHeight(20);
        mAfterAdapter.setTextRightPadd(0);
        mAfterAdapter.setmTextPosition(Gravity.CENTER);
        mAfterAdapter.setTextSize(18);

        mHourAdapter.setTextViewHeight(20);
        mHourAdapter.setTextRightPadd(0);
        mHourAdapter.setmTextPosition(Gravity.CENTER);
        mHourAdapter.setTextSize(18);

        mMinAdapter.setTextViewHeight(20);
        mMinAdapter.setTextLeftPadd(0);
        mMinAdapter.setmTextPosition(Gravity.CENTER);
        mMinAdapter.setTextSize(18);
    }
}
