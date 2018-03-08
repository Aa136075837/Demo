package com.example.bo.nixon.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SPUtils {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "nixion_date";

    public static boolean getBoolean (Context context, String key) {
        return getBoolean (context, key, false);
    }

    public static boolean getBoolean (Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);

        return sp.getBoolean (key, value);
    }

    public static void putBoolean (Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit ();
        editor.putBoolean (key, value);
        editor.commit ();
    }

    public static String getString (Context context, String key) {
        return getString (context, key, "");
    }

    public static String getString (Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        return sp.getString (key, value);
    }

    public static void putString (Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit ();
        editor.putString (key, value);
        editor.commit ();
    }

    public static int getInt (Context context, String key) {
        return getInt (context, key, 0);
    }

    public static int getInt (Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        return sp.getInt (key, value);
    }

    public static void putInt (Context context, String key, int value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit ();
        editor.putInt (key, value);
        editor.commit ();
    }

    public static long getLong (Context context, String key) {
        return getLong (context, key, 0);
    }

    public static long getLong (Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        return sp.getLong (key, value);
    }

    public static void putLong (Context context, String key, long value) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit ();
        editor.putLong (key, value);
        editor.commit ();
    }

    public static void clear (Context context) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit ();
        editor.clear ();
        editor.commit ();
    }

    public static boolean contains (Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences (FILE_NAME, Context.MODE_PRIVATE);
        return sp.contains (key);
    }
}