package com.example.bo.nixon.manager;

import android.util.Log;

/**
 * @author ARZE
 * @version 创建时间：2017/7/5 9:55
 * @说明
 */
public class L {

    public static boolean DEBUG = true;
    private static final String TAG = "NIXON";

    public static void w(String tag, String msg) {
        if (DEBUG)
            Log.w(tag, msg);
    }

    public static void w(String msg) {
        if (DEBUG)
            Log.w(TAG, msg);
    }
}
