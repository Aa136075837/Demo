package com.example.bo.nixon.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class SimpleDateUtils {

    private static final String DATE_LATE = "yyyy-MM-dd";
    private static final String TIME_LATE = "HH:mm:ss:SSS";
    private static final String DATE_TIME_LATE = "yyyy-MM-dd HH:mm:ss.SSS";
    private static final String DATE_TIME_LATE2 = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_TIME_LATE3 = "yyyyMMddHHmmss";
    private static final String DATE_TIME_LATE4 = "MM-dd HH:mm:ss";
    private static final String Y_M_LATE = "yyyy-MM";
    private static final String Y_LATE = "yyyy";
    private static final String M_D_LATE = "MM-dd";
    private static final String M_D_LATE1 = "MM.dd";
    private static final String M_D_LATE2 = "M.d";
    private static final String H_M_LATE = "HH:mm";

    private static final String M_D_H_M_LATE = "MM-dd HH:mm";
    private static final String H_M_S_LATE = "HH:mm:ss";

    private static final String Y_M_D_H_M = "yyyy-MM-dd HH:mm";

    //  private static final String DATE_LATE_CH = "yyyy年MM月dd日";
    //   private static final String Y_M_LATE_CH = "yyyy年MM月";
    //   private static final String Y_LATE1_CH = "yyyy年";

    public static String getDateString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_LATE,
                Locale.getDefault());
        return dateFormat.format(date);
    }

    public static Date getDateByString(String date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_LATE,
                Locale.getDefault());
        try {
            return timeFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Date getYMDHMDate(String date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(Y_M_D_H_M,
                Locale.getDefault());
        try {
            return timeFormat.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    public static String getTimeString(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_LATE,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static Date getTimeByString(String time) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat(TIME_LATE,
                Locale.getDefault());
        return timeFormat.parse(time);
    }

    public static String getDateTimeString(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_LATE,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getDateTimeString2(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_LATE2,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getDateTimeMDHMS(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_LATE4,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getDateTimeString3(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_LATE3,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static Date getDateTimeByString(String time) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_TIME_LATE,
                Locale.getDefault());
        return timeFormat.parse(time);
    }

    public static Date getDateTime(String time) throws ParseException {
        SimpleDateFormat timeFormat = new SimpleDateFormat(DATE_LATE,
                Locale.getDefault());
        return timeFormat.parse(time);
    }

    public static String changeFormatString(String str) {
        Date date = new Date();
        try {
            date = getDateTime(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat timeFormat = new SimpleDateFormat(M_D_LATE1,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getMDString(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(M_D_LATE,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static String getYMString(Date date) {
        SimpleDateFormat ymFormat = new SimpleDateFormat(Y_M_LATE,
                Locale.getDefault());
        return ymFormat.format(date);
    }

    public static Date getDateYMByString(String time) throws ParseException {
        SimpleDateFormat ymFormat = new SimpleDateFormat(Y_M_LATE,
                Locale.getDefault());
        return ymFormat.parse(time);
    }

    public static String getYString(Date date) {
        SimpleDateFormat yFormat = new SimpleDateFormat(Y_LATE,
                Locale.getDefault());
        return yFormat.format(date);
    }

    public static Date getDateYByString(String time) throws ParseException {
        SimpleDateFormat yFormat = new SimpleDateFormat(Y_LATE,
                Locale.getDefault());
        return yFormat.parse(time);
    }

    public static String getHMString(Date date) {
        SimpleDateFormat yFormat = new SimpleDateFormat(H_M_LATE,
                Locale.getDefault());
        return yFormat.format(date);
    }

    public static Date getDateByHMString(String time) throws ParseException {
        SimpleDateFormat yFormat = new SimpleDateFormat(H_M_LATE,
                Locale.getDefault());
        return yFormat.parse(time);
    }

    public static String getHMSString(Date date) {
        SimpleDateFormat yFormat = new SimpleDateFormat(H_M_S_LATE,
                Locale.getDefault());
        return yFormat.format(date);
    }

    public static String getMDHMSString(Date date) {
        SimpleDateFormat yFormat = new SimpleDateFormat(M_D_H_M_LATE,
                Locale.getDefault());
        return yFormat.format(date);
    }

    public static Date getDateHMSString(String time) throws ParseException {
        SimpleDateFormat yFormat = new SimpleDateFormat(H_M_S_LATE,
                Locale.getDefault());
        return yFormat.parse(time);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH);
        int day1 = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.setTime(date2);
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH);
        int day2 = calendar.get(Calendar.DAY_OF_MONTH);
        return year1 == year2 && month1 == month2 && day1 == day2;
    }

    public static Date getDateByTimeZone(String fromTimeZong,
                                         String toTimeZone, Date fromDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_LATE);
        format.setTimeZone(TimeZone.getTimeZone(fromTimeZong));
        String tempString = format.format(fromDate);
        format.setTimeZone(TimeZone.getTimeZone(toTimeZone));
        tempString = format.format(fromDate);

        SimpleDateFormat format2 = new SimpleDateFormat(DATE_TIME_LATE);
        Date toDate = format2.parse(tempString);
        return toDate;
    }

    public static Date getDate() {
        Date date = new Date(System.currentTimeMillis());
        return date;
    }

    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    public static String getMDString1(Date date) {
        SimpleDateFormat timeFormat = new SimpleDateFormat(M_D_LATE2,
                Locale.getDefault());
        return timeFormat.format(date);
    }

    public static int compareDate(Date var1, Date var2) {
        if (var1.getYear() > var2.getYear()) {
            return 1;
        } else if (var1.getYear() < var2.getYear()) {
            return -1;
        } else {
            if (var1.getMonth() > var2.getMonth()) {
                return 1;
            } else if (var1.getMonth() < var2.getMonth()) {
                return -1;
            } else {
                if (var1.getDate() > var2.getDate()) {
                    return 1;
                } else if (var1.getDate() < var2.getDate()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        }
    }

    public static boolean isAfterDate(Date date, Date listenerDate) {
        if (date.getYear() > listenerDate.getYear()) {
            return true;
        }
        if (date.getMonth() > listenerDate.getMonth()) {
            return true;
        }
        if (date.getDate() > listenerDate.getDate()) {
            return true;
        }
        return false;
    }

    public static int getDateDistance(Date var1, Date var2) {
        if (null == var1 || null == var2 || var1.getDate() < var2.getDate())
            return 0;
        if (var1.getDate() == var2.getDate()) {
            return var1.getHours() * 60 + var1.getMinutes() -
                    var2.getHours() * 60 - var2.getMinutes();
        } else {
            return var1.getHours() * 60 + var1.getMinutes() + 24 * 60 -
                    var2.getHours() * 60 - var2.getMinutes();
        }
    }

    public static String getStringFromMin(int value) {
        int hour = value / 60;
        int min = value % 60;
        String hourString = String.valueOf(hour);
        String minString = String.valueOf(min);
        if (hour < 10) {
            hourString = "0" + hour;
        }
        if (min < 10) {
            minString = "0" + min;
        }
        return hourString + ":" + minString;
    }

}
