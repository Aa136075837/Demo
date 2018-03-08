package com.smart.notifycomponent;

import com.smart.notifycomponent.listener.IDisconnectNotify;
import com.smart.notifycomponent.listener.IFindNotify;
import com.smart.notifycomponent.listener.IPhoneEvent;
import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 17:28
 * @说明
 */
public abstract class AbNotifyComponent extends AbComponent {

    public AbNotifyComponent(SmartManager smartManager) {
        super(smartManager);
    }

    @Override
    public void registerComponent() {
        mSmartManager.getIService().registerComponent(this);

    }

    @Override
    public void unRegisterComponent() {
        mSmartManager.getIService().unRegisterComponent(this);
    }

    public void addFindNotifyListener(IFindNotify notify) {
        if (!Listeners.iFindNotifies.contains(notify)) {
            Listeners.iFindNotifies.add(notify);
        }
    }

    public void removePhoneListener(IPhoneEvent event) {
        if (Listeners.iPhoneEvents.contains(event)) {
            Listeners.iPhoneEvents.remove(event);
        }
    }

    public void addPhoneListener(IPhoneEvent event) {
        if (!Listeners.iPhoneEvents.contains(event)) {
            Listeners.iPhoneEvents.add(event);
        }
    }

    public void removeFindNotifyListener(IFindNotify notify) {
        if (Listeners.iFindNotifies.contains(notify)) {
            Listeners.iFindNotifies.remove(notify);
        }
    }

    public void addDisconnectNotify(IDisconnectNotify iDisconnectNotify) {
        if (!Listeners.iDisconnectNotifies.contains(iDisconnectNotify)) {
            Listeners.iDisconnectNotifies.add(iDisconnectNotify);
        }
    }

    public void removeDisconnectNotify(IDisconnectNotify iDisconnectNotify) {
        if (!Listeners.iDisconnectNotifies.contains(iDisconnectNotify)) {
            Listeners.iDisconnectNotifies.remove(iDisconnectNotify);
        }
    }

    public void dispatchNotifySetting(boolean notify) {
        for (IDisconnectNotify disconnectNotify : Listeners.iDisconnectNotifies) {
            disconnectNotify.disconnectNotify(notify);
        }
    }

    protected void dispatchFindNotify() {
        for (IFindNotify findNotify : Listeners.iFindNotifies) {
            findNotify.findNotify();
        }
    }

    protected void dispatchEndCall() {
        for (IPhoneEvent phoneEvent : Listeners.iPhoneEvents) {
            phoneEvent.endCall();
        }
    }

    protected void dispatchReceiverCall() {
        for (IPhoneEvent phoneEvent : Listeners.iPhoneEvents) {
            phoneEvent.answerCall();
        }
    }

    static class Listeners {

        private static List<IFindNotify> iFindNotifies = new ArrayList<>();

        private static List<IPhoneEvent> iPhoneEvents = new ArrayList<>();

        private static List<IDisconnectNotify> iDisconnectNotifies = new ArrayList<>();

    }

}
