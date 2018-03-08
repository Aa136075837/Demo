package com.smart.connectComponent;

import android.os.Handler;
import android.util.Log;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleDevice;
import com.smart.smartble.smartBle.IBleStatus;
import com.smart.smartble.smartBle.ILeListener;

/**
 * @author ARZE
 * @version 创建时间：2017/3/30 19:01
 * @说明
 */
public class ConnectComponent extends AbConnectComponent implements IBleStatus, ILeListener {

    private final static String TAG = "ConnectComponent";

    private static ConnectComponent mConnectComponent;
    private static SmartManager mSmartManager;
    private Handler mHandler = new Handler();
    private Runnable mRunnable;

    public static ConnectComponent getInstance(SmartManager smartManager) {
        if (null == mConnectComponent) {
            synchronized (ConnectComponent.class) {
                if (null == mConnectComponent) {
                    mConnectComponent = new ConnectComponent(smartManager);
                }
            }
        }
        mSmartManager.getIClient().setOnScanLeListener(mConnectComponent);
        mSmartManager.getIClient().addBleStatusListener(mConnectComponent);
        return mConnectComponent;
    }

    private ConnectComponent(SmartManager smartManager) {
        super(smartManager);
        mSmartManager = smartManager;
    }

    @Override
    public void registerComponent() {
        super.registerComponent();
    }

    @Override
    public void start() {
        mSmartManager.getIClient().startLeScan();
    }

    public void stopLeScan() {
        mSmartManager.getIClient().stopLeScan();
    }

    public void startLeScan() {
        start();
    }

    public void connectDevice(BleDevice bleDevice) {
        mSmartManager.getIClient().connectDevice(bleDevice);
    }

    public void disConnectDevice() {
        mSmartManager.getIClient().disConnectDeivce();
    }

    @Override
    public void dealAction(SmartAction action) {
        if (null == action.getAction())
            return;
        byte[] bytes = action.getBytes();
        switch (action.getAction()) {
            case REQUEST_ACTION_AUTHORIZATION:
                Log.w(TAG, "REQUEST_ACTION_AUTHORIZATION::");
                if (!mSmartManager.getIClient().isAutoConnect()) {
                    Log.w(TAG, "REQUEST_ACTION_AUTHORIZATION::");
                    int succ = bytes[8] & 0xff;
                    dispatchAuthorization(1 == succ);
                    mHandler.removeCallbacks(mRunnable);
                }
                break;
        }
    }

    @Override
    public void onBleStatus(Status status) {
        Log.w(TAG, "onBleStatus::" + status);
        switch (status) {
            case CONNECTED:
                break;
            case DISCONNECTED:
                dispatchConnectFail();
                break;
            case DISCOVER_SERVICES:
                if (mSmartManager.getIClient().isAutoConnect()) {
                    mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_AUTHORIZATION, true);
                } else {
                    mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_AUTHORIZATION, false);
                }
                dispatchConnectDevice();
                mHandler.removeCallbacks(mRunnable);
                mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        dispatchAuthorizationTimeOut();
                    }
                };
                mHandler.postDelayed(mRunnable, 12 * 1000);
                break;
            case DISCOVER_SERVICES_FAIL:
                dispatchConnectFail();
                break;
        }
    }

    @Override
    public void onLeScanEnd() {
        dispatchLeSanEnd();
    }

    @Override
    public void onLeScanNewDevice(BleDevice bleDevice) {
        dispatchFindNewDevice(bleDevice);
    }

    @Override
    public void unRegisterComponent() {
        super.unRegisterComponent();
        mSmartManager.getIClient().removeBleStatusListener(this);
        mHandler.removeCallbacks(mRunnable);
    }



}
