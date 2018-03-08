package com.example.bo.nixon.utils;

/**
 * @author ARZE
 * @version 创建时间：2016年5月16日 下午5:30:23
 * @说明
 */

public class MonthUtil {


    public static String getMon(int i) {
        String result = "";
        switch (i) {
            case 1:
                result = "Jan";
                break;
            case 2:
                result = "Feb";
                break;
            case 3:
                result = "Mar";
                break;
            case 4:
                result = "Apr";
                break;
            case 5:
                result = "May";
                break;
            case 6:
                result = "Jun";
                break;
            case 7:
                result = "Jul";
                break;
            case 8:
                result = "Aug";
                break;
            case 9:
                result = "Sep";
                break;
            case 10:
                result = "Oct";
                break;
            case 11:
                result = "Nov";
                break;
            case 12:
                result = "Dec";
                break;
        }
        return result;
    }

    public static String getDay(int i) {
        String result = "";
        switch (i) {
            case 1:
                result = "1st";
                break;
            case 2:
                result = "2nd";
                break;
            case 3:
                result = "3rd";
                break;
            case 4:
                result = "4th";
                break;
            case 5:
                result = "5th";
                break;
            case 6:
                result = "6th";
                break;
            case 7:
                result = "7th";
                break;
            case 8:
                result = "8th";
                break;
            case 9:
                result = "9th";
                break;
            case 10:
                result = "10th";
                break;
            case 11:
                result = "11th";
                break;
            case 12:
                result = "12th";
                break;
            case 13:
                result = "13th";
                break;
            case 14:
                result = "14th";
                break;
            case 15:
                result = "15th";
                break;
            case 16:
                result = "16th";
                break;
            case 17:
                result = "17th";
                break;
            case 18:
                result = "18th";
                break;
            case 19:
                result = "19th";
                break;
            case 20:
                result = "20th";
                break;
            case 21:
                result = "21st";
                break;
            case 22:
                result = "22nd";
                break;
            case 23:
                result = "23rd";
                break;
            case 24:
                result = "24th";
                break;
            case 25:
                result = "25th";
                break;
            case 26:
                result = "26th";
                break;
            case 27:
                result = "27th";
                break;
            case 28:
                result = "28th";
                break;
            case 29:
                result = "29th";
                break;
            case 30:
                result = "30th";
                break;
            case 31:
                result = "31st";
                break;
        }
        return result;

    }

    public static String getNumber(int i) {
        String result = "";
        int num = i % 10;
        switch (num) {
            case 1:
                result = i + "st";
                break;
            case 2:
                result = i + "nd";
                break;
            case 3:
                result = i + "rd";
                break;
            default:
                result = i + "th";
        }
        return result;
    }
}
