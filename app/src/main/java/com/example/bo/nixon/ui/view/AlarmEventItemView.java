package com.example.bo.nixon.ui.view;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import com.example.bo.nixon.R;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.fragment.alarm.AlarmRepeatHelper;

public class AlarmEventItemView extends FrameLayout
    implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    private TextView alarmText;
    private TextView repeatTimeText;
    private TextView mNameTV;
    private SmartSwitchView mSmartSwitchView;
    private LayoutInflater inflater;
    private boolean clickType = false;
    private AlarmEventBean alarmBean;
    private AlarmEventCallBack callBack;
    private final String SPIT_STRING = "/";
    private Context mContext;
    private ImageView mArrow;
    private CheckBox mCheckBox;
    private View mSuperView;
    private TextView mDelete;
    private boolean isSlide;
    private TextView mRepeatTv;

    public AlarmEventItemView (Context context) {
        this (context, null);
        // TODO Auto-generated constructor stub
    }

    public AlarmEventItemView (@NonNull Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public AlarmEventItemView (@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        mContext = context;
        inflater = LayoutInflater.from (context);
        initView ();
    }

    private void initView () {
        mSuperView = inflater.inflate (R.layout.alarm_event_item, this, false);
        this.addView (mSuperView);

        alarmText = (TextView) mSuperView.findViewById (R.id.alarm_event_item_time);
        repeatTimeText = (TextView) mSuperView.findViewById (R.id.alarm_event_item_repeat_time);
        mNameTV = (TextView) mSuperView.findViewById (R.id.alarm_event_item_time_after);
        mSmartSwitchView = (SmartSwitchView) findViewById (R.id.alarm_event_item_open_switch);
        mArrow = (ImageView) findViewById (R.id.alarm_event_item_arr);
        mCheckBox = (CheckBox) findViewById (R.id.alarm_event_item_check);
        mSmartSwitchView.setOnClickListener (this);
        mDelete = (TextView) mSuperView.findViewById (R.id.alarm_event_item_delete);
        mRepeatTv = (TextView) mSuperView.findViewById (R.id.alarm_event_item_repeat);

        alarmText.setTypeface (FontManager.mTf);
        repeatTimeText.setTypeface (FontManager.mTf);
        mRepeatTv.setTypeface (FontManager.mTf);
        mNameTV.setTypeface (FontManager.mTf);
        mDelete.setOnClickListener (this);
        mCheckBox.setOnCheckedChangeListener (this);
        //设置字体大小
        //		alarmText.setTextSize(DisplayUtil.sp2px(mContext,30));
    }

    private OnClickListener onClickListener = new OnClickListener () {

        @Override public void onClick (View v) {
            switch (v.getId ()) {
                case R.id.alarm_event_item_time: {
                    int hour = alarmBean.getAlarmTime () / 60;
                    int min = alarmBean.getAlarmTime () % 60;
                    new TimePickerDialog (getContext (), AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener () {

                            @Override public void onTimeSet (TimePicker view, int hourOfDay, int minute) {
                                // TODO Auto-generated method stub
                                setAlarmTime (hourOfDay * 60 + minute);
                            }
                        }, hour, min, true).show ();
                }
                break;
                case R.id.alarm_event_item_delete:
                    if (null != mlistener) {
                        mlistener.deleteItem ();
                    }
                    break;
            }
        }
    };

    public TextView getDeleteBtn () {
        return mDelete;
    }

    public boolean isSlide () {
        return isSlide;
    }

    public void setSlide (boolean slide) {
        isSlide = slide;
    }

    public void setOpenType (boolean openType) {
        alarmBean.setOpenType (openType);
        mSmartSwitchView.setCheck (alarmBean.isOpenType ());
        if (null != callBack) {
            callBack.onDataChange (alarmBean);
        }
    }

    public void setCallBack (AlarmEventCallBack callBack) {
        this.callBack = callBack;
    }

    public boolean isClickType () {
        return clickType;
    }

    public void setClickType (boolean clickType) {
        this.clickType = clickType;
    }

    public void setAlarmTime (int alarmTime) {
        this.alarmBean.setAlarmTime (alarmTime);
        StringBuilder stringBuilder = new StringBuilder ("");
        int hour = alarmTime / 60;
        int min = alarmTime % 60;
        if (hour >= 12) {
            mNameTV.setText (R.string.PM);
        } else {
            mNameTV.setText (R.string.AM);
        }

        if (hour > 12) {
            hour = hour - 12;
        }

        if (hour < 10) {
            //stringBuilder.append ("0");
        }
        stringBuilder.append (hour);
        stringBuilder.append (":");
        if (min < 10) {
            stringBuilder.append ("0");
        }
        stringBuilder.append (min);
        alarmText.setText (stringBuilder.toString ());
        if (null != callBack) {
            callBack.onDataChange (alarmBean);
        }
    }

    public void setAlarmBean (AlarmEventBean alarmBean) {
        if (null == alarmBean) {
            return;
        }
        this.alarmBean = alarmBean;

        setOpenType (this.alarmBean.isOpenType ());
        setAlarmTime (this.alarmBean.getAlarmTime ());
        String text = AlarmRepeatHelper.getRepeatString (getContext (), this.alarmBean.getRepeatTime ());
        if (!TextUtils.isEmpty (alarmBean.getmAction ()) && !TextUtils.isEmpty (text)) {
            mRepeatTv.setText ("/" + text);
            repeatTimeText.setText (alarmBean.getmAction ());
        } else if (!TextUtils.isEmpty (alarmBean.getmAction ()) && TextUtils.isEmpty (text)) {
            mRepeatTv.setText ("/" + getContext ().getString (R.string.alarm_item_tip));
            repeatTimeText.setText (alarmBean.getmAction ());
        } else if (TextUtils.isEmpty (alarmBean.getmAction ()) && !TextUtils.isEmpty (text)) {
            mRepeatTv.setText ("/" + text);
            repeatTimeText.setText (getResources ().getString (R.string.event_title));
        } else {
            mRepeatTv.setText ("/" + getResources ().getString (R.string.alarm_item_tip));
            repeatTimeText.setText (getResources ().getString (R.string.event_title));
        }
        if (text.length () > 13) {
            text = setAlarmTextBelow13 (text);
        }
        //repeatTimeText.setText (text);
    }

    private String setAlarmTextBelow13 (String text) {
        text = text.substring (0, 12);
        String newText = text + "...";
        return newText;
    }

    private void setAction (String getmAction) {
        repeatTimeText.setText (getmAction);
    }

    private int getRepeatTimeValue (boolean index1, boolean index2, boolean index3, boolean index4, boolean index5,
        boolean index6, boolean index7) {// index7 为星期天 index1为星期一
        return ((getBooleanValue (index1) << 6) + (getBooleanValue (index2) << 5) + (getBooleanValue (index3) << 4) + (
            getBooleanValue (index4)
                << 3) + (getBooleanValue (index5) << 2) + (getBooleanValue (index6) << 1) + (getBooleanValue (index7)
            << 6));
    }

    private int getBooleanValue (boolean bool) {
        return bool ? 1 : 0;
    }

    public interface AlarmEventCallBack {
        void onDataChange (AlarmEventBean alarmBean);

        void onOpenTypeClick (AlarmEventBean alarmBean);
    }

    public void setTextColor (@ColorRes int color) {
        alarmText.setTextColor (getResources ().getColor (color));
        repeatTimeText.setTextColor (getResources ().getColor (color));
        mRepeatTv.setTextColor (getResources ().getColor (color));
        mNameTV.setTextColor (getResources ().getColor (color));
    }

    @Override public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.alarm_event_item_open_switch:
                setOpenType (!alarmBean.isOpenType ());
                Log.w ("AlarmFragment", "run---> onClick");
                if (null != callBack) {
                    callBack.onOpenTypeClick (alarmBean);
                }
                break;
            default:
                break;
        }
    }

    public boolean getCheckState () {
        return mCheckBox.isChecked ();
    }

    public void setEditState (boolean isShowCheck) {
        if (isShowCheck) {
            mArrow.setVisibility (View.VISIBLE);
            mCheckBox.setVisibility (View.VISIBLE);
            mSmartSwitchView.setVisibility (View.INVISIBLE);
        } else {
            mArrow.setVisibility (View.INVISIBLE);
            mCheckBox.setVisibility (View.GONE);
            mSmartSwitchView.setVisibility (View.VISIBLE);
        }
    }

    float y;
    float x;
    boolean isOpen = false;

    @Override public boolean onTouchEvent (MotionEvent event) {
        switch (event.getAction ()) {
            case MotionEvent.ACTION_DOWN:

                y = event.getY ();
                x = event.getX ();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!clickType) {
                    float mY = event.getY ();
                    float mX = event.getX ();
                    int disX = (int) (mX - x);
                    if (Math.abs (y - mY) < Math.abs (x - mX) && mX > x) {  //横向右滑 打开
                        if (Math.abs (mSuperView.getScrollX ()) >= mDelete.getWidth ()) {
                            mSuperView.scrollTo (-mDelete.getWidth (), 0);
                        } else {
                            mSuperView.scrollTo (-disX, 0);
                        }
                    } else if (Math.abs (y - mY) < Math.abs (x - mX) && mX < x) {  //横向左滑 关闭
                        if (mSuperView.getScrollX () >= mDelete.getWidth ()) {
                            mSuperView.scrollTo (0, 0);
                        } else {
                            if (isOpen) {
                                mSuperView.scrollTo ((int) -(mDelete.getWidth () - (x - mX)), 0);
                            }
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs (x - event.getX ()) <= 3) {
                    Log.e ("DELETE", " 222222222222222222222");
                    if (null != mlistener && clickType) {
                        mlistener.click (this);
                    }
                    return super.onTouchEvent (event);
                }
                if (Math.abs (mSuperView.getScrollX ()) < mDelete.getWidth () / 2) {
                    mSuperView.scrollTo (0, 0);
                    isOpen = false;
                } else {
                    mSuperView.scrollTo (-mDelete.getWidth (), 0);
                    isOpen = true;
                }
                break;
        }
        //return super.onTouchEvent (event);
        return isSlide;
    }

    public void turnNoamal () {
        mSuperView.scrollTo (0, 0);
    }

    @Override public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
        if (b) {
            setTextColor (R.color.main_text_color);
        } else {
            setTextColor (R.color.sup_text_color);
        }
        if (mlistener != null) {
            mlistener.isDelete (alarmBean.getIndex (), b);
        }
    }

    public void setCheckboxState (boolean isCheck) {
        mCheckBox.setChecked (isCheck);
    }

    private OnDeleteListener mlistener;

    public void setOnDeleteListener (OnDeleteListener listener) {
        mlistener = listener;
    }

    /**
     * 将被点击的CheckBox回调给adapter
     */
    public interface OnDeleteListener {
        void isDelete (int index, boolean b);

        /**
         * 策划删除按钮 点击
         */
        void deleteItem ();

        void click (AlarmEventItemView view);
    }
}
