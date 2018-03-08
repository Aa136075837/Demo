package com.example.bo.nixon.utils;

/**
 * @author ARZE
 * @version 创建时间：2016年9月12日 下午5:07:56
 * @说明
 */

public class AsciiToString {

    public static String ascii16ToString(String ascii) {
        StringBuilder sb = new StringBuilder();
        StringBuilder temp = new StringBuilder();

        for (int i = 0; i < ascii.length() - 1; i += 2) {
            String output = ascii.substring(i, (i + 2));
            int decimal = Integer.parseInt(output, 16);
            sb.append((char) decimal);
            temp.append(decimal);
        }
        return sb.toString();
    }
}
