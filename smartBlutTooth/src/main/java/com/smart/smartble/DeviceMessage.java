package com.smart.smartble;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:31
 * @说明  设备信息
 */
public class DeviceMessage {

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEdit;
    private static DeviceMessage mDeviceMessage;
    private static Context mContext;

    private static final String DEVICE_UUID = "DEVICE_UUID";
    private static final String DEVICE_LOW_SN = "DEVICE_LOW_SN";
    private static final String DEVICE_HIGH_SN = "DEVICE_HIGH_SN";

    private DeviceMessage() {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mEdit = mSharedPreferences.edit();
    }

    public static void attackBaseContent(Context context) {
        mContext = context;
    }

    public static DeviceMessage getInstance() {
        if (null == mDeviceMessage) {
            synchronized (DeviceMessage.class) {
                if (null == mDeviceMessage) {
                    mDeviceMessage = new DeviceMessage();
                }
            }
        }
        return mDeviceMessage;
    }

    public String getDeviceUUID() {
        return mSharedPreferences.getString(DEVICE_UUID, "");
    }

    public void putDeviceUUID(String value) {
        mEdit.putString(DEVICE_UUID, value).commit();
    }

    public String getDeviceSn() {
        return mSharedPreferences.getString(DEVICE_HIGH_SN, "") + mSharedPreferences.getString(DEVICE_LOW_SN, "");
    }

    public void putDeviceLowSn(String low) {
        mEdit.putString(DEVICE_LOW_SN, low).commit();
    }

    public void putDeviceHighSn(String high) {
        mEdit.putString(DEVICE_LOW_SN, high).commit();
    }

}
