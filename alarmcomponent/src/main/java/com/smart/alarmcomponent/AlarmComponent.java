package com.smart.alarmcomponent;

import android.util.Log;
import com.smart.smartble.SmartManager;
import com.smart.smartble.client.protocolImp.BCDHelper;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2017/6/15 10:47
 * @说明
 */
public class AlarmComponent extends AbAlarmComponent {

    private static final String TAG = "AlarmComponent";

    private static AlarmComponent mAlarmComponent;
    private static SmartManager mSmartManager;

    public static AlarmComponent getInstance(SmartManager smartManager) {
        if (null == mAlarmComponent) {
            synchronized (AlarmComponent.class) {
                if (null == mAlarmComponent) {
                    mAlarmComponent = new AlarmComponent(smartManager);
                }
            }
        }
        return mAlarmComponent;
    }

    private AlarmComponent(SmartManager smartManager) {
        super(smartManager);
        mSmartManager = smartManager;
    }
    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_GET_ALARM:
                int index = bytes[8] & 0xff;
                byte[] hourByte = new byte[1];
                hourByte[0] = bytes[9];
                byte[] minByte = new byte[1];
                minByte[0] = bytes[10];
                String hourString = BCDHelper.bcd2Str(hourByte);
                String minString = BCDHelper.bcd2Str(minByte);
                int hour = Integer.valueOf(hourString);
                int min = Integer.valueOf(minString);
                int time = hour * 60 + min;
                int repeatTime = bytes[11] & 0xff;
                int open = bytes[12] & 0xff;

                dispatchAlarm(index, time, open, repeatTime);
                break;
            case REQUEST_ACTION_ALARM_REPLAY:
                index = (bytes[8] & 0xff);
                dispatchAlarmReplay(index);
                break;
        }
    }

    public void setAlarm(int index, int time, int open, int week) {
        Log.w(TAG,"setAlarm ::");
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_ALARM, index, time, open, week);
    }

    public void getAlarm(int index) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_ALARM, index);
    }

}
