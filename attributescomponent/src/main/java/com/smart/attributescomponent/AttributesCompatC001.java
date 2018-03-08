package com.smart.attributescomponent;

import android.support.annotation.NonNull;

import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2017/7/27 14:44
 * @说明
 */
public class AttributesCompatC001 implements IAttributesCompat {

    @Override
    public int[] getVersion(@NonNull SmartAction action) {
        byte[] bytes = action.getBytes();
        int[] version = new int[3];
        version[0] = Integer.valueOf(new String(new byte[]{bytes[6]}));
        version[1] = Integer.valueOf(new String(new byte[]{bytes[8]}));
        version[2] = Integer.valueOf(new String(new byte[]{bytes[10]}));
        return version;
    }

    @Override
    public boolean hasElectricity(SmartAction action) {
        return true;
    }

    @Override
    public int getElectricity(@NonNull SmartAction action) {
        byte[] bytes = action.getBytes();
        return bytes[13] & 0xff;
    }

    @Override
    public String saveSn(@NonNull SmartAction action) {
        return null;
    }

}
