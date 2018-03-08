package com.smart.smartble;

import android.bluetooth.BluetoothDevice;
import android.text.TextUtils;

import java.util.Locale;

/**
 * @author ARZE
 * @version 创建时间：2016/12/14 10:16
 * @说明 过滤机芯
 */
public class FetchDevice {

    public static boolean isTargetDevice(BluetoothDevice device) {
        if (TextUtils.isEmpty(device.getName()))
            return true;
        if (
               /* device.getName().toUpperCase(Locale.getDefault()).startsWith("P1") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P2") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P3") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P4") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P5") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P6") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P0033") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P7") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P0") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("P0030") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("PC") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("PK") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("NO") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("24") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("FAMAR") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("ELETTA") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("MAYSUNM") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("T-WATCH") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("D-MAP") || */
                device.getName().toUpperCase(Locale.getDefault()).startsWith("") ||
                        device.getName().toUpperCase(Locale.getDefault()).startsWith("NOERDEN-WS1")
                ) {
            return true;
        }
        return false;
    }
}
