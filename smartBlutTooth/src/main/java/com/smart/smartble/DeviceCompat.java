package com.smart.smartble;

/**
 * @author ARZE
 * @version 创建时间：2017/6/30 16:56
 * @说明  设备兼容类
 */
public class DeviceCompat {

    private static final String C007 = "5037";

    public static boolean isC007() {
        String msg = DeviceMessage.getInstance().getDeviceUUID();
        return true;
    }
}
