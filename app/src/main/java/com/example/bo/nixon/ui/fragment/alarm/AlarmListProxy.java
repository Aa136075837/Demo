package com.example.bo.nixon.ui.fragment.alarm;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.smartcustomview.views.SmartWheelSeletTimeHour;

/**
 * @author bo.
 * @Date 2017/6/9.
 * @desc
 */

public class AlarmListProxy implements View.OnClickListener {

    private final View mView;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private SmartWheelSeletTimeHour mSmartWheelSeletTimeHour;
    private TextView mActionTv;
    private TextView mRepeatTv;

    public AlarmListProxy() {
        mView = View.inflate(NixonApplication.getContext(), R.layout.fragment_alarm_list, null);
        initView();
        initListener();
    }

    public int getSelectTime() {
        return mSmartWheelSeletTimeHour.getTime();
    }

    private void initView() {
        mSmartWheelSeletTimeHour = (SmartWheelSeletTimeHour) mView.findViewById(R.id.smart_wheel_select_date);
        mActionTv = (TextView) mView.findViewById(R.id.alarm_event_name);
        mRepeatTv = (TextView) mView.findViewById(R.id.alarm_repeat_type);
    }

    private void initListener() {
        mView.findViewById(R.id.alarm_repeat_ll).setOnClickListener(this);
        mView.findViewById(R.id.alarm_event_ll).setOnClickListener(this);
    }

    public View getView() {
        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alarm_repeat_ll:
                if (mListener != null) {
                    mListener.repeatClick();
                }
                break;
            case R.id.alarm_event_ll:
                if (mListener != null) {
                    mListener.eventClick();
                }
                break;
        }
    }

    private AlarmListClickListener mListener;

    public void setAlarmListClickListener(AlarmListClickListener listener) {
        mListener = listener;
    }

    public String getAction() {
        return mActionTv.getText().toString();
    }

    public void setTime(int alarmTime) {
        int hour = alarmTime / 60;
        int min = alarmTime % 60;
        int after = 0;
        if (hour > 12) {
            after = 1;
            hour = hour - 12;
        }
        mSmartWheelSeletTimeHour.setCurrent(after, hour, min);
    }

    public void setRepeat(String repeat) {
        mRepeatTv.setText(repeat);
    }

    public void doScrollView(final int dt ,final int time) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSmartWheelSeletTimeHour.doScrollView(dt);
            }
        },time);
    }

    public interface AlarmListClickListener {
        void repeatClick();

        void eventClick();
    }

    public void setAction(String action) {
        Log.w("AlarmManagerFragment","run-------->setAction" + action);
        mActionTv.setText(action);
    }


}
