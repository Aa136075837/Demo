package com.example.bo.nixon.utils;

import android.content.Context;
import android.util.Log;
import java.util.Locale;

public class LanguageUtil {

    private static final String TAG = "LanguageUtil";

    /**
     * 判断语言状态
     */
    public static boolean isEnglish () {
        if (Locale.getDefault ().getLanguage ().contains ("en")) {
            return true;
        }
        return false;
    }

    public static boolean isChinese () {

        if (Locale.getDefault ().getLanguage ().contains ("zh")) {
            return true;
        }
        return false;
    }

    public static boolean isJapan (Context context) {
        Log.w (TAG, context.getResources ().getConfiguration ().locale.getCountry ());
        return context.getResources ().getConfiguration ().locale.getCountry ().equals ("JP");
    }

    public static boolean isTraditionalChinese () {
        if (Locale.getDefault ().getLanguage ().contains ("tw") || Locale.getDefault ()
            .getLanguage ()
            .contains ("hk")) {
            return true;
        }
        return false;
    }

    public static boolean isTraditionalChinese (Context context) {
        Log.w (TAG, context.getResources ().getConfiguration ().locale.getCountry ());
        return context.getResources ().getConfiguration ().locale.getCountry ().equals ("TW") || context.getResources ()
            .getConfiguration ().locale.getCountry ().equals ("HK");
    }

    public static boolean isSimpleChinese (Context context) {
        Log.w (TAG, context.getResources ().getConfiguration ().locale.getCountry ());
        return context.getResources ().getConfiguration ().locale.getCountry ().equals ("CN");
    }

    /**
     * 获取语言编码和国家编码
     *
     * @return
     */
    public static String getLanguageAndCountry (Context context) {
        return getLanguage (context) + "_" + getCountry (context);
    }

    /**
     * 获取当前国家编码
     *
     * @return
     */
    public static String getCountry (Context context) {
        return context.getResources ().getConfiguration ().locale.getCountry ();
    }

    /**
     * 获取当前语言编码
     *
     * @return
     */
    public static String getLanguage (Context context) {
        return context.getResources ().getConfiguration ().locale.getLanguage ();
    }
}
