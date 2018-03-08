package com.smart.timecomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ARZE
 * @version 创建时间：2017/6/30 16:48
 * @说明
 */
public class ITimeC007 implements ITime {

    private SmartManager mSmartManager;
    private Timer mTimer;

    public ITimeC007 (SmartManager smartManager) {
        mSmartManager = smartManager;
    }

    @Override
    public void dealAction(SmartAction smartAction) {

    }

    @Override
    public void startTime(long time) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_TIME, true);
        if (null != mTimer) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimeHeart(this), 0, time);
    }

    @Override
    public void stopTime() {
        if (null != mTimer)
            mTimer.cancel();
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_TIME, false);
    }

    @Override
    public void intoSmallClock(int position, int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_TIME_SMALL_POSITION, position, value);
    }

    @Override
    public void setMcuTime(int position, Date date) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_MCU_TIME, position, date);
    }

    @Override
    public void setPosition(int position, int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_POSITION_TIME, position, value);
    }

    @Override
    public void setSecondCity(Date date, int arg1, int arg2) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_SECOND_TIME, date, arg1, arg2);
    }

    @Override
    public void getSecondCity() {
       mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_SECOND_TIME);
    }


    private static class TimeHeart extends TimerTask {

        public WeakReference<ITimeC007> mTarget;

        public TimeHeart(ITimeC007 iTime) {
            mTarget = new WeakReference<>(iTime);
        }

        @Override
        public void run() {
            if (null != mTarget.get()) {
                mTarget.get().mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_HEART_TIME);
            }
        }
    }
}
