package com.smart.smartble.client.actionFactory;

import com.smart.smartble.event.Action;
import com.smart.smartble.event.AlarmEvent;
import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2016/12/19 18:06
 * @说明
 */
public interface IFactory {

    SmartAction buildSmartAction(Action action);

    SmartAction buildSmartAction(Action action, boolean b);

    SmartAction buildSmartAction(Action action, int arg1);

    SmartAction buildSmartAction(Action action, Object... objects);

    SmartAction buildSmartAction(Action action, AlarmEvent alarmEvent);

    SmartAction buildSmartAction(Action action, int arg1, int arg2, Object obj);

    void reBuildAskedAction(SmartAction smartAction);
}
