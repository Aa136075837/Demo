package com.smart.connectComponent;

import com.smart.smartble.smartBle.BleDevice;

/**
 * @author ARZE
 * @version 创建时间：2017/3/30 19:08
 * @说明
 */
public interface IConnect {

    void findNewDevice(BleDevice bleDevice);

    void leSanEnd();

    void leSanStart();

    void connectFail();

    void connectedDevice();

    void connectSuccessful();

}
