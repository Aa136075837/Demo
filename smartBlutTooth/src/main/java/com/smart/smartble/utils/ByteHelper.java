package com.smart.smartble.utils;

import android.support.annotation.NonNull;

/**
 * @author ARZE
 * @version 创建时间：2017/8/5 14:27
 * @说明
 */
public class ByteHelper {

    public static int calculateHigh(byte... bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result += (bytes[i] & 0xff) << ((bytes.length - 1 - i) * 8);
        }
        return result;
    }

    public static int calculateLow(byte... bytes) {
        int result = 0;
        for (int i = 0; i < bytes.length; i++) {
            result += (bytes[i] & 0xff) <<  (i * 8);
        }
        return result;
    }

    public static byte[] cutBytes(@NonNull byte[] src , int start , int end) {
        if (start < 0)
            start = 0;
        if (end > src.length)
            end = src.length;
        byte[] des = new byte[end - start];
        for (int i = start; i < end ; i ++) {
            des[i - start] = src[i];
        }
        return des;
    }

}
