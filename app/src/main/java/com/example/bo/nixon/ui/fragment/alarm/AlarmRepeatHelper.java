package com.example.bo.nixon.ui.fragment.alarm;

import android.content.Context;
import android.util.Log;

import com.example.bo.nixon.R;

/**
 * @author ARZE
 * @version 创建时间：2017/6/16 16:09
 * @说明
 */
public class AlarmRepeatHelper {

    private static final String SPIT_STRING = "/";

    public static String getRepeatString(Context context, int value) {
        Log.w("AlarmRepeatHelper","Run------->" + value);
        boolean bool1 = ((value >> 1) & 0x01) == 1;
        boolean bool2 = ((value >> 2) & 0x01) == 1;
        boolean bool3 = ((value >> 3) & 0x01) == 1;
        boolean bool4 = ((value >> 4) & 0x01) == 1;
        boolean bool5 = ((value >> 5) & 0x01) == 1;

        boolean bool6 = ((value >> 6) & 0x01) == 1;
        boolean bool7 = ((value >> 0) & 0x01) == 1;
            if (bool1 && bool2 && bool3 && bool4 && bool5 && bool6 && bool7) {
                return context.getResources().getString(R.string.alarm_item_title_4);
            } else if (bool1 && bool2 && bool3 && bool4 && bool5) {
                StringBuilder stringBuilder =
                        new StringBuilder(context.getResources().getString(R.string.alarm_item_title_2));
                if (bool6 || bool7) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(bool6 ? context.getResources().getString(R.string.alarm_item_subtitle_6)
                            : context.getResources().getString(R.string.alarm_item_subtitle_7));
                }
                return stringBuilder.toString();
            } else if (bool6 && bool7) {
                StringBuilder stringBuilder =
                        new StringBuilder(context.getResources().getString(R.string.alarm_item_title_3));
                if (bool1) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_1));
                }
                if (bool2) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_2));
                }
                if (bool3) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_3));
                }
                if (bool4) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_4));
                }
                if (bool5) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_5));
                }
                return stringBuilder.toString();
            } else {
                StringBuilder stringBuilder = new StringBuilder("");
                if (bool1) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_1));
                }
                if (bool2) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_2));
                }
                if (bool3) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_3));
                }
                if (bool4) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_4));
                }
                if (bool5) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_5));
                }
                if (bool6) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_6));
                }
                if (bool7) {
                    stringBuilder.append(SPIT_STRING);
                    stringBuilder.append(context.getResources().getString(R.string.alarm_item_subtitle_7));
                }
                if (stringBuilder.toString().startsWith("/")) {
                    stringBuilder.deleteCharAt(0);
                }
                return stringBuilder.toString();
            }
    }
}
