package com.smart.timecomponent;

import com.smart.smartble.DeviceCompat;
import com.smart.smartble.SmartManager;
import com.smart.smartble.client.protocolImp.BCDHelper;
import com.smart.smartble.event.SmartAction;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/6/8 18:24
 * @说明
 */
public class TimeComponent extends AbTimeComponent {

    private static final String TAG = "TimeComponent";

    private ITime mITime;

    public TimeComponent(SmartManager smartManager) {
        super(smartManager);
    }

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_GET_SECOND_TIME:
                Date date = getDate(bytes);
                int highZone = BCDHelper.bcd2Int(bytes[14]);
                int lowZone = BCDHelper.bcd2Int(bytes[15]);
                dispatchSecondTime(date, highZone, lowZone);
                break;
        }
    }

    private Date getDate(byte[] bytes) {
        int year = BCDHelper.bcd2Int(bytes[8]) + 2000;
        int month = BCDHelper.bcd2Int(bytes[9]);
        int day = BCDHelper.bcd2Int(bytes[10]);
        int hour = BCDHelper.bcd2Int(bytes[11]);
        int min = BCDHelper.bcd2Int(bytes[12]);
        int sec = BCDHelper.bcd2Int(bytes[13]);

        Date date = new Date();
        date.setYear(year);
        date.setMonth(month);
        date.setDate(day);
        date.setHours(hour);
        date.setMinutes(min);
        date.setSeconds(sec);
        return date;
    }

    @Override
    public void startTime(long time) {
        compat().startTime(time);
    }

    @Override
    public void stopTime() {
        compat().stopTime();
    }

    @Override
    public void intoSmallClock(int position, int value) {
        compat().intoSmallClock(position, value);
    }

    @Override
    public void setMcuTime(int position, Date date) {
        compat().setMcuTime(position, date);
    }

    @Override
    public void setPosition(int position, int value) {
        compat().setPosition(position, value);
    }

    @Override
    public void setSecondCity(Date date, int arg1, int arg2) {
        compat().setSecondCity(date, arg1, arg2);
    }

    @Override
    public void getSecondCity() {
        compat().getSecondCity();
    }

    public ITime compat() {
        if (DeviceCompat.isC007()) {
            if (mITime instanceof ITimeC007) {
                return mITime;
            } else {
                mITime = new ITimeC007(mSmartManager);
            }
        }
        if (null == mITime) {
            mITime = new ITimeC007(mSmartManager);
        }
        return mITime;
    }

}
