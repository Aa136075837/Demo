package com.smart.smartble.smartBle;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 17:45
 * @说明 蓝牙搜素
 */
public interface ILeListener {

    void onLeScanEnd();

    void onLeScanNewDevice(BleDevice bleDevice);

}
