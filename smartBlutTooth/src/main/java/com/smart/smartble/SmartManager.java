package com.smart.smartble;

import com.smart.smartble.client.IClient;
import com.smart.smartble.event.ISmartActionOperator;
import com.smart.smartble.service.IService;

/**
 * @author ARZE
 * @version 创建时间：2016/12/14 9:25
 * @说明
 */
public class SmartManager {

    private IClient mIClient;
    private IService mIService;
    private ISmartActionOperator mISmartActionOperator;
    private static boolean isDiscovery = false;
    private static boolean isBluetoothOpen = false;

    public IClient getIClient() {
        return mIClient;
    }

    public void setIClient(IClient iClient) {
        this.mIClient = iClient;
    }

    /**
     * @return
     * @hide
     */
    public IService getIService() {
        return mIService;
    }

    /**
     * @param iService
     * @hide
     */
    public void setIService(IService iService) {
        this.mIService = iService;
    }

    public static boolean isDiscovery() {
        return isDiscovery;
    }

    public static void setBluetoothOpen(boolean isBluetoothOpen) {
        SmartManager.isBluetoothOpen = isBluetoothOpen;
    }

    public static boolean isBluetoothOpen() {
        return isBluetoothOpen;
    }

    public void setDiscovery(boolean discovery) {
        isDiscovery = discovery;
    }

    public ISmartActionOperator getISmartActionOperator() {
        return mISmartActionOperator;
    }

    public void setISmartActionOperator(ISmartActionOperator mISmartActionOperator) {
        this.mISmartActionOperator = mISmartActionOperator;
    }


}
