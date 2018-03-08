package com.smart.attributescomponent;

import android.support.annotation.NonNull;

import com.smart.smartble.DeviceMessage;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.utils.ByteHelper;

/**
 * @author ARZE
 * @version 创建时间：2017/7/27 14:44
 * @说明
 */
public class AttributesCompatC007 implements IAttributesCompat {

    @Override
    public int[] getVersion(SmartAction action) {
        int[] version = new int[3];
        byte[] bytes = action.getBytes();
        version[0] = ByteHelper.calculateHigh(bytes[8]);
        version[1] = ByteHelper.calculateHigh(bytes[9]);
        version[2]= ByteHelper.calculateHigh(bytes[10]);
        return version;
    }

    @Override
    public boolean hasElectricity(SmartAction action) {
        return false;
    }

    @Override
    public int getElectricity(SmartAction action) {
        return 0;
    }

    @Override
    public String saveSn(@NonNull SmartAction action) {
        byte[] bytes = action.getBytes();
        if (0 == ByteHelper.calculateHigh(bytes[8])) {
            DeviceMessage.getInstance().putDeviceLowSn(new String(ByteHelper.cutBytes(bytes, 9, 19)));
            return "";
        } else if (1 == ByteHelper.calculateHigh(bytes[8])) {
            DeviceMessage.getInstance().putDeviceHighSn(new String(ByteHelper.cutBytes(bytes, 9, 19)));
            return DeviceMessage.getInstance().getDeviceSn();
        }
        return "";
    }
}
