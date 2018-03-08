package com.smart.smartble.client;

import com.smart.smartble.client.actionFactory.IFactory;
import com.smart.smartble.event.Action;
import com.smart.smartble.smartBle.BleDevice;
import com.smart.smartble.smartBle.BleHelper;
import com.smart.smartble.smartBle.IBleStatus;
import com.smart.smartble.smartBle.ILeListener;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:20
 * @说明
 */
public abstract class IClient {

    protected BleHelper mBleHelper;
    protected IFactory mIFactory;

    public IClient(BleHelper bleHelper) {
        this.mBleHelper = bleHelper;
    }

    public abstract void doAction(Action action);

    public abstract void doAction(Action action,int arg1);

    public abstract void doAction(Action action,Object... objects);

    public abstract void doAction(Action action,boolean b);

    public abstract void doAction(Action action, int arg1, int arg2, Date date);

    public void connectDevice(BleDevice bleDevice) {
        if (null != mBleHelper) {
            mBleHelper.connectDevice(bleDevice);
        }
    }

    public void disConnectDeivce(){
        if(null != mBleHelper) {
            mBleHelper.disConnect();
        }
    }

    public void startLeScan() {
        mBleHelper.startScanLeDevice();
    }

    public void stopLeScan() {
        mBleHelper.stopScanLeDevice();
    }


    public  void setOnScanLeListener(ILeListener listener) {
        mBleHelper.setOnLeSanListener(listener);
    }

    public void addBleStatusListener(IBleStatus listener) {
        mBleHelper.addBleConnectListener(listener);
    }

    public void removeBleStatusListener(IBleStatus listener) {
        mBleHelper.removeBleConnectListener(listener);
    }

    public boolean isAutoConnect() {
        return  mBleHelper.isReconnect();
    }
}
