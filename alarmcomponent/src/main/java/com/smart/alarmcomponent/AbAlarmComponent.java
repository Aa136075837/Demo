package com.smart.alarmcomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/15 10:45
 * @说明
 */
public abstract class AbAlarmComponent extends AbComponent {


    public AbAlarmComponent(SmartManager smartManager) {
        super(smartManager);
    }

    public void addAlarmListener(IAlarm iAlarm) {
        if (!ListenerInfo.iAlarms.contains(iAlarm)) {
            ListenerInfo.iAlarms.add(iAlarm);
        }
    }

    public void removeAlarmListener(IAlarm iAlarm) {
        if (ListenerInfo.iAlarms.contains(iAlarm)) {
            ListenerInfo.iAlarms.remove(iAlarm);
        }
    }

    public void addAlarmReplayListener(IAlarmReplay iAlarmReplay) {
        if (!ListenerInfo.iAlarmReplays.contains(iAlarmReplay)) {
            ListenerInfo.iAlarmReplays.add(iAlarmReplay);
        }
    }

    public void removeAlarmReplayListener(IAlarmReplay iAlarmReplay) {
        if (ListenerInfo.iAlarmReplays.contains(iAlarmReplay)) {
            ListenerInfo.iAlarmReplays.remove(iAlarmReplay);
        }
    }

    protected void dispatchAlarm(int index, int time, int open, int repeat) {
        for (IAlarm iAlarm : ListenerInfo.iAlarms) {
            iAlarm.alarm(index,time,open,repeat);
        }
    }

    protected void dispatchAlarmReplay(int index) {
        for (IAlarmReplay iAlarmReplay : ListenerInfo.iAlarmReplays) {
            iAlarmReplay.replay(index);
        }
    }

    @Override
    public void registerComponent() {
        mSmartManager.getIService().registerComponent(this);

    }

    @Override
    public void unRegisterComponent() {
        mSmartManager.getIService().unRegisterComponent(this);
    }


    private static class ListenerInfo {
        private static List<IAlarm> iAlarms = new ArrayList<>();
        private static List<IAlarmReplay> iAlarmReplays = new ArrayList<>();
    }

}
