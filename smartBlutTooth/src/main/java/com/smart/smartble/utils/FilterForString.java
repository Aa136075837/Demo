package com.smart.smartble.utils;

import java.util.Locale;

/**
 * @author ARZE
 * @version 创建时间：2016/12/14 10:27
 * @说明
 */
public class FilterForString {

    public static String filter(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        byte tempByte;
        String tempString;
        for (int j = 0; j < bytes.length; j++) {
            tempByte = bytes[j];
            if (tempByte >= 0 && tempByte < 16) {
                sb.append("0");
            }
            tempString = Integer.toHexString(bytes[j]);
            sb.append(tempString.length() == 1 ? tempString : tempString.subSequence(0, 2));
        }
        String sanRecordString = sb.toString().toUpperCase(Locale.getDefault());

        return sanRecordString;
    }
}
