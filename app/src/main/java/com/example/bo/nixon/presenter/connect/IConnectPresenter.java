package com.example.bo.nixon.presenter.connect;

import com.smart.smartble.smartBle.BleDevice;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 16:12
 * @说明
 */
public interface IConnectPresenter {

    void startSearch();

    void refreshSearch();

    void connectDevice(BleDevice bleDevice);

    void disConnectDevice(BleDevice bleDevice);

}
