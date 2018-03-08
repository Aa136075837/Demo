package com.example.smartcustomview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.example.smartcustomview.R;
import com.example.smartcustomview.adapter.ArrayWheelAdapter;
import com.example.smartcustomview.utils.DisplayUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/3/20 19:21
 * @说明
 */
public class TimeWheelView extends FrameLayout {

    private View mDecorView;
    private WheelView mHourWheelView;
    private WheelView mMinWheelView;
    private WheelView mSecWheelView;

    private static final int MIN_MIN = 0;
    private static final int MAX_MIN = 59;
    private static final int MIN_SEC = 0;
    private static final int MAX_SEC = 59;
    private static final int MIN_HOUR = 0;
    private static final int MAX_HOUR = 11;
    private List<String> mHours = new ArrayList<> ();
    private List<String> mMins = new ArrayList<> ();
    private List<String> mSecs = new ArrayList<> ();
    private ArrayWheelAdapter<Object> mHourAdapter;
    private ArrayWheelAdapter<Object> mMinAdapter;
    private ArrayWheelAdapter<Object> mSecAdapter;
    private int mLineColor = 0x00ffffff;

    public TimeWheelView (Context context) {
        super (context);
        init (context, null, 0);
    }

    public TimeWheelView (Context context, AttributeSet attrs) {
        super (context, attrs);
        init (context, attrs, 0);
    }

    public TimeWheelView (Context context, AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init (context, attrs, defStyleAttr);
    }

    private void init (Context context, AttributeSet attrs, int defStyleAttr) {
        mDecorView = LayoutInflater.from (context).inflate (R.layout.time_wheel_view, null);
        addView (mDecorView);
        initValue ();
        initView ();
    }

    private void initView () {
        mHourWheelView = (WheelView) mDecorView.findViewById (R.id.wheel_view_hour);
        mMinWheelView = (WheelView) mDecorView.findViewById (R.id.wheel_view_min);
        mSecWheelView = (WheelView) mDecorView.findViewById (R.id.wheel_view_sec);
        mHourWheelView.setmSelectColor (getResources ().getColor (R.color.text_black)).
            setScaleChange (true).setDrawShadows (false).setRightExtraString (getResources ().
            getString (R.string.day_hour)).setScaleOff (3).setFillCenterLine (true).
            setHasItemShape (true).setSelectSize (32).setRightExtraString (":").
            setmRightExtraSize (DisplayUtil.sp2px (getContext (), 14)).setCenterLineColor (mLineColor).
            setNormalSize (29).setCyclic (true);
        mMinWheelView.setmSelectColor (getResources ().getColor (R.color.text_black)).setCenterLineColor (mLineColor).
            setScaleChange (true).setDrawShadows (false).setRightExtraString (getResources ().
            getString (R.string.day_min)).setScaleOff (3).setFillCenterLine (true).setRightExtraString (":").
            setHasItemShape (true).setSelectSize (32).setmRightExtraSize (DisplayUtil.sp2px (getContext (), 14)).
            setNormalSize (29).setCyclic (true);
        mSecWheelView.setmSelectColor (getResources ().getColor (R.color.text_black)).setCenterLineColor (mLineColor).
            setScaleChange (true).setDrawShadows (false).setRightExtraString (getResources ().
            getString (R.string.day_sec)).setScaleOff (3).setFillCenterLine (true).setRightExtraString ("").
            setHasItemShape (true).setSelectSize (32).setmRightExtraSize (DisplayUtil.sp2px (getContext (), 14)).
            setNormalSize (29).setCyclic (true);
        show ();
    }

    private void initValue () {
        mSecs.clear ();
        for (int i = MIN_SEC; i <= MAX_SEC; i++) {
            String min = i + "";
            if (min.length () < 2) {
                min = "0" + min;
            }
            mSecs.add (min);
        }
        mMins.clear ();
        for (int i = MIN_MIN; i <= MAX_MIN; i++) {
            String min = i + "";
            if (min.length () < 2) {
                min = "0" + min;
            }
            mMins.add (min);
        }
        mHours.clear ();
        for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
            String hour = i + "";
            if (hour.length () < 2) {
                hour = "0" + hour;
            }
            mHours.add (hour);
        }
    }

    public void show () {
        mHourAdapter = new ArrayWheelAdapter<> (getContext (), mHours.toArray ());
        mMinAdapter = new ArrayWheelAdapter<> (getContext (), mMins.toArray ());
        mSecAdapter = new ArrayWheelAdapter<> (getContext (), mSecs.toArray ());
        mHourAdapter.setTextViewHeight (60);
        mMinAdapter.setTextViewHeight (60);
        mSecAdapter.setTextViewHeight (60);
        Date date = new Date ();
        int hour = date.getHours ();
        int min = date.getMinutes ();
        int sec = date.getSeconds ();
        if (hour >= 12) {
            hour = hour - 12;
        }
        String hourString = hour + "";
        String minString = min + "";
        String secString = sec + "";
        if (hourString.length () < 2) {
            hourString = "0" + hourString;
        }
        if (minString.length () < 2) {
            minString = "0" + minString;
        }
        if (secString.length () < 2) {
            secString = "0" + secString;
        }
        mHourWheelView.setViewAdapter (mHourAdapter);
        mMinWheelView.setViewAdapter (mMinAdapter);
        mSecWheelView.setViewAdapter (mSecAdapter);
        mHourWheelView.setCurrentItem (mHours.indexOf (hourString));
        mMinWheelView.setCurrentItem (mMins.indexOf (minString));
        mSecWheelView.setCurrentItem (mSecs.indexOf (secString));
        mHourWheelView.setVisibleItems (3);
        mMinWheelView.setVisibleItems (3);
        mSecWheelView.setVisibleItems (3);
    }

    public int getMinTime () {
        return (MIN_HOUR + mHourWheelView.getCurrentItem ()) * 60 + (MIN_MIN + mMinWheelView.getCurrentItem ());
    }

    public int getSecTime() {
        return (MIN_HOUR + mHourWheelView.getCurrentItem ()) * 60  * 60 +
                (MIN_MIN + mMinWheelView.getCurrentItem ()) * 60 +  MIN_SEC +mSecWheelView.getCurrentItem() ;
    }
}
