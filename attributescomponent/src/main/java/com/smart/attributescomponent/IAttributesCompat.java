package com.smart.attributescomponent;

import android.support.annotation.NonNull;

import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2017/7/27 14:42
 * @说明
 */
public interface IAttributesCompat {

    int[] getVersion(@NonNull SmartAction action);

    boolean hasElectricity(@NonNull SmartAction action);

    int getElectricity(@NonNull SmartAction action);

    String saveSn(@NonNull SmartAction action);

}
