package com.smart.smartble;

/**
 * @author ARZE
 * @version 创建时间：2017/6/10 18:22
 * @说明
 */
public class ByteToString {


    public  static String byteToString(byte[] bytes) {
        if (bytes == null) {
            throw new IllegalArgumentException(
                    "Argument bytes ( byte array ) is null! ");
        }
        String hs = "";
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

}

