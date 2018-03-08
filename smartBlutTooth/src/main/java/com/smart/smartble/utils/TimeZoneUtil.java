package com.smart.smartble.utils;

import android.text.TextUtils;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author ARZE
 * @version 创建时间：2017/6/19 17:35
 * @说明
 */
public class TimeZoneUtil {

    private static final String TAG = "TimeZoneUtil";

    public static Date getTimeFromThereZone(String zone) {
        zone = deleteInvailChar(zone);
        if (TextUtils.isEmpty(zone))
            return new Date();
        String[] time = zone.split(":");
        if (2 != time.length)
            return new Date();
        Calendar calendar = Calendar.getInstance();
        String cz = getCurrentTimeZone(calendar.getTimeZone());
        cz = deleteInvailChar(cz);
        String[] ct = cz.split(":");
        int per = 1;
        if (Integer.valueOf(time[0]) * 60 < 0) {
            per = -1;
        }
        int tt = Integer.valueOf(time[0]) * 60 + per * Integer.valueOf(time[1]);
        if (Integer.valueOf(ct[0]) * 60 < 0) {
            per = -1;
        } else {
            per = 1;
        }
        int ht = Integer.valueOf(ct[0]) * 60 + per * Integer.valueOf(ct[1]);
        int offTime = tt - ht;
        int hour = offTime / 60;
        int min = offTime % 60;
        Date date = new Date();
        date.setHours(date.getHours() + hour);
        date.setMinutes(date.getMinutes() + min);
        return date;
    }

    private static String deleteInvailChar(String st) {
        String target = st;
        if(target.startsWith ("+")){
            target = target.substring (1);
        }
        return target;
    }

    public static int getMinFromThereZone(String zone) {
        zone = deleteInvailChar (zone);
        if (TextUtils.isEmpty(zone))
            return 0;
        String[] time = zone.split(":");
        if (2 != time.length)
            return 0;
        int per = 1;
        if (Integer.valueOf(time[0]) * 60 < 0) {
            per = -1;
        }
        return Integer.valueOf(time[0]) * 60 + per * Integer.valueOf(time[1]);
    }

    public static String getZoneTimeString(Date date) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(date.getHours() > 12 ? date.getHours() - 12 : date.getHours()).append(":")
                .append(date.getMinutes())
                .append(date.getHours() >= 12 ? "pm" : "am");
        return buffer.toString();
    }

    public static String getCurrentTimeZone(TimeZone timeZone) {
        return createGmtOffsetString(false, true, timeZone.getRawOffset());
    }

    public static String createGmtOffsetString(boolean includeGmt,
                                               boolean includeMinuteSeparator, int offsetMillis) {
        int offsetMinutes = offsetMillis / 60000;
        char sign = '+';
        if (offsetMinutes < 0) {
            sign = '-';
            offsetMinutes = -offsetMinutes;
        }
        StringBuilder builder = new StringBuilder(9);
        if (includeGmt) {
            builder.append("GMT");
        }
        builder.append(sign);
        appendNumber(builder, 2, offsetMinutes / 60);
        if (includeMinuteSeparator) {
            builder.append(':');
        }
        appendNumber(builder, 2, offsetMinutes % 60);

        return builder.toString();
    }

    private static void appendNumber(StringBuilder builder, int count, int value) {
        String string = Integer.toString(value);
        for (int i = 0; i < count - string.length(); i++) {
            builder.append('0');
        }
        builder.append(string);
    }
}
