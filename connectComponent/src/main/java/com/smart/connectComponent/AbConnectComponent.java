package com.smart.connectComponent;

import android.util.Log;

import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;
import com.smart.smartble.smartBle.BleDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/3/30 19:05
 * @说明
 */
public abstract class AbConnectComponent extends AbComponent {

    private static final String TAG = "AbConnectComponent";

    public AbConnectComponent(SmartManager smartManager) {
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

    public void addConnectListener(IConnect iConnect) {
        if (!ListenerInfo.iConnects.contains(iConnect))
            ListenerInfo.iConnects.add(iConnect);
    }

    public void removeConnectListener(IConnect iConnect) {
        if (ListenerInfo.iConnects.contains(iConnect))
            ListenerInfo.iConnects.remove(iConnect);
    }

    public void addAuthorzationListener(IAuthorization iAuthorization) {
        Log.w(TAG, "addAuthorzationListener::" + ListenerInfo.iAuthorizations.size());
        if (!ListenerInfo.iAuthorizations.contains(iAuthorization)) {
            ListenerInfo.iAuthorizations.add(iAuthorization);
            Log.w(TAG, "addAuthorzationListener::" + ListenerInfo.iAuthorizations.size());
        }
    }

    public void removeAuthorzationListener(IAuthorization iAuthorization) {
        Log.w(TAG, "removeAuthorzationListener::" + ListenerInfo.iAuthorizations.size());
        if (ListenerInfo.iAuthorizations.contains(iAuthorization)) {
            ListenerInfo.iAuthorizations.remove(iAuthorization);
            Log.w(TAG, "removeAuthorzationListener::" + ListenerInfo.iAuthorizations.size());
        }
    }

    protected void dispatchConnectFail() {
        for (IConnect iConnect : ListenerInfo.iConnects) {
            iConnect.connectFail();
        }
    }


    protected void dispatchConnectDevice() {
        for (IConnect iConnect : ListenerInfo.iConnects) {
            iConnect.connectedDevice();
        }
    }

    protected void dispatchConnectSuccessful() {
        for (IConnect iConnect : ListenerInfo.iConnects)
            iConnect.connectSuccessful();
    }

    protected void dispatchFindNewDevice(BleDevice bleDevice) {
        for (IConnect iConnect : ListenerInfo.iConnects)
            iConnect.findNewDevice(bleDevice);
    }

    protected void dispatchLeSanEnd() {
        for (IConnect iConnect : ListenerInfo.iConnects)
            iConnect.leSanEnd();
    }

    protected void dispatchLeSanStart() {
        for (IConnect iConnect : ListenerInfo.iConnects)
            iConnect.leSanStart();
    }

    protected void dispatchAuthorization(boolean author) {
        Log.w(TAG, "dispatchAuthorization::" + ListenerInfo.iAuthorizations.size());
        for (IAuthorization iAuthorization : ListenerInfo.iAuthorizations)
            iAuthorization.authorization(author);
    }

    protected void dispatchAuthorizationTimeOut() {
        for (IAuthorization iAuthorization : ListenerInfo.iAuthorizations)
            iAuthorization.AuthorizationTimeOut();
    }

    private static class ListenerInfo {
        private static List<IConnect> iConnects = new ArrayList<>();
        private static List<IAuthorization> iAuthorizations = new ArrayList<>();
    }

}
