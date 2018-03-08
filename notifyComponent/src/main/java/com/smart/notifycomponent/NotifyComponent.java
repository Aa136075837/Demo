package com.smart.notifycomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 17:36
 * @说明
 */
public class NotifyComponent extends AbNotifyComponent implements INotify {


    public NotifyComponent(SmartManager smartManager) {
        super(smartManager);
    }

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_FIND_PHONE:
                dispatchFindNotify();
                int flag = bytes[4] & 0xff;
                mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_NORMAL_ACK, flag, 0);
                break;
            case REQUEST_ACTION_PHONE_EVENT:
                int event = bytes[8] & 0xff;
                if (0 == event) {
                    dispatchEndCall();
                } else if (1 == event) {
                    dispatchReceiverCall();
                }
                flag = bytes[4] & 0xff;
                mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_NORMAL_ACK, flag, 0);
                break;
            case REQUEST_ACTION_GET_DISCONNECT_NOTIFY_SETTING:
                int notify = bytes[8] & 0xfff;
                dispatchNotifySetting(1 == notify);
                break;
        }
    }

    @Override
    public void setNotifySetting(int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_NOTIFY_SETTING, value);
    }

    @Override
    public void setNotify(int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_SET_NOTIFY, value);
    }

    @Override
    public void setDisconnectNotify(boolean notify) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_DISCONNECT_NOTIFY, notify);
    }

    @Override
    public void getDisconnectNotifySetting() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_GET_DISCONNECT_NOTIFY_SETTING);
    }

    @Override
    public void cancelNotify(int value) {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CANCEL_NOTIFY, value);
    }
}
