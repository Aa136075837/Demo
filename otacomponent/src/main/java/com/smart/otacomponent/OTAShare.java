package com.smart.otacomponent;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author ARZE
 * @version 创建时间：2017/6/24 15:09
 * @说明
 */
public class OTAShare {

    private static final String  OTA_SAVE = "OTA_SAVE";

    public static void putString (Context context,String key ,String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(OTA_SAVE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,data).commit();
    }

    public static String getString(Context context,String key,String defaultString) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(OTA_SAVE,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultString);
    }

}
