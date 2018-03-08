package com.smart.smartble.smartBle;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 17:02
 * @说明 蓝牙连接状态
 */
public interface IBleStatus {

    void onBleStatus(Status status);

    enum Status {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        DISCOVER_SERVICES,
        DISCOVER_SERVICES_FAIL,
        BLE_OFF,
        BLE_ON,
        BLE_ERROR
    }

}
