package com.smart.smartble.smartBle.leScan;

import android.bluetooth.BluetoothDevice;

import com.smart.smartble.smartBle.BleDevice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ARZE
 * @version 创建时间：2016/12/14 10:45
 * @说明
 */
public class BleDeviceImp {

    private Map<String, String> mMapBluetoothAddress = new HashMap<>();
    private List<BleDevice> mBleDeviceList = new CopyOnWriteArrayList<>();

    public void clear() {
        mMapBluetoothAddress.clear();
        mBleDeviceList.clear();
    }

    public void addDevice(BleDevice bleDevice) {
        mBleDeviceList.add(bleDevice);
    }

    public void putAddress(String address, String scanFilter) {
        mMapBluetoothAddress.put(address, scanFilter);
    }

    public BleDevice getDevice(int deviceIndex) {
        return mBleDeviceList.get(deviceIndex);
    }

    public void set(int deviceIndex, BleDevice bleDevice) {
        mBleDeviceList.set(deviceIndex, bleDevice);
    }

    public int isHasDevice(BluetoothDevice device) {
        int deviceIndex = -1;
        for (int i = 0 ;i < mBleDeviceList.size();i++) {
            if (device.getAddress().equals(mBleDeviceList.get(i).getDevice().getAddress())) {
                deviceIndex = i;
                break;
            }
        }
        return deviceIndex;
    }
}
