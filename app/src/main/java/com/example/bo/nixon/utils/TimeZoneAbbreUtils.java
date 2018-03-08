package com.example.bo.nixon.utils;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/7/1.
 * @desc 时区缩写
 */

public class TimeZoneAbbreUtils {
    private Map<String, String> map = new HashMap<> ();
    private Map<String, String> map1 = new HashMap<> ();
    private static TimeZoneAbbreUtils mUtils;

    private TimeZoneAbbreUtils () {
        map.put ("+00:00", "GTM");
        map.put ("+01:00", "CET");
        map.put ("+02:00", "EET");
        map.put ("+03:00", "EAT");
        map.put ("+04:00", "GST");
        map.put ("+05:00", "MVT");
        map.put ("+06:00", "BST");
        map.put ("+07:00", "ICT");
        map.put ("+08:00", "HKT");
        map.put ("+09:00", "JST");
        map.put ("+10:00", "AEST");
        map.put ("+11:00", "VUT");
        map.put ("+12:00", "NZST");
        map.put ("-11:00", "SST");
        map.put ("-10:00", "HADT");
        map.put ("-09:00", "AKST");
        map.put ("-08:00", "PST");
        map.put ("-07:00", "MST");
        map.put ("-06:00", "CST");
        map.put ("-05:00", "EST");
        map.put ("-04:00", "AST");
        map.put ("-03:00", "BRT");
        map.put ("-02:00", "FNT");
        map.put ("-01:00", "AZOT");

        map.put ("0:0", "GTM");
        map.put ("1:0", "CET");
        map.put ("2:0", "EET");
        map.put ("3:0", "EAT");
        map.put ("4:0", "GST");
        map.put ("5:0", "MVT");
        map.put ("6:0", "BST");
        map.put ("7:0", "ICT");
        map.put ("8:0", "HKT");
        map.put ("9:0", "JST");
        map.put ("10:0", "AEST");
        map.put ("11:0", "VUT");
        map.put ("12:0", "NZST");
        map.put ("-11:0", "SST");
        map.put ("-10:0", "HADT");
        map.put ("-9:0", "AKST");
        map.put ("-8:0", "PST");
        map.put ("-7:0", "MST");
        map.put ("-6:0", "CST");
        map.put ("-5:0", "EST");
        map.put ("-4:0", "AST");
        map.put ("-3:0", "BRT");
        map.put ("-2:0", "FNT");
        map.put ("-1:0", "AZOT");

        map1.put ("+00:00", "London");
        map1.put ("+01:00", "Central European");
        map1.put ("+02:00", "Eastern European");
        map1.put ("+03:00", "Arabia Standard Time");
        map1.put ("+04:00", "Gulf Standard Time");
        map1.put ("+05:00", "Maldives/Pakistan Standard Time");
        map1.put ("+06:00", "Bangladesh Standard Time");
        map1.put ("+07:00", "Indochina Standard Time");
        map1.put ("+08:00", "Hong Kong Standard Time");
        map1.put ("+09:00", "Japan Standard Time");
        map1.put ("+10:00", "Australian Eastern Standard Time");
        map1.put ("+11:00", "Vanuatu Standard Time");
        map1.put ("+12:00", "New Zealand Standard Time");
        map1.put ("-11:00", "Samoa Standard Time");
        map1.put ("-10:00", "Hawaii-Aleutian Standard Time");
        map1.put ("-09:00", "Alaskan Standard Time");
        map1.put ("-08:00", "Pacific Standard Time");
        map1.put ("-07:00", "Mountain Standard Time");
        map1.put ("-06:00", "Central Standard Time");
        map1.put ("-05:00", "Eastern Standard Time");
        map1.put ("-04:00", "Atlantic Standard Time");
        map1.put ("-03:00", "Brazil Standard Time");
        map1.put ("-02:00", "Fernando de Noronha Time");
        map1.put ("-01:00", "Azores Time");

        map1.put ("0:0", "London");
        map1.put ("1:0", "Central European");
        map1.put ("2:0", "Eastern European");
        map1.put ("3:0", "Arabia Standard Time");
        map1.put ("4:0", "Gulf Standard Time");
        map1.put ("5:0", "Maldives/Pakistan Standard Time");
        map1.put ("6:0", "Bangladesh Standard Time");
        map1.put ("7:0", "Indochina Standard Time");
        map1.put ("8:0", "Hong Kong Standard Time");
        map1.put ("9:0", "Japan Standard Time");
        map1.put ("10:0", "Australian Eastern Standard Time");
        map1.put ("11:0", "Vanuatu Standard Time");
        map1.put ("12:0", "New Zealand Standard Time");
        map1.put ("-11:0", "Samoa Standard Time");
        map1.put ("-10:0", "Hawaii-Aleutian Standard Time");
        map1.put ("-9:0", "Alaskan Standard Time");
        map1.put ("-8:0", "Pacific Standard Time");
        map1.put ("-7:0", "Mountain Standard Time");
        map1.put ("-6:0", "Central Standard Time");
        map1.put ("-5:0", "Eastern Standard Time");
        map1.put ("-4:0", "Atlantic Standard Time");
        map1.put ("-3:0", "Brazil Standard Time");
        map1.put ("-2:0", "Fernando de Noronha Time");
        map1.put ("-1:0", "Azores Time");
    }

    public static TimeZoneAbbreUtils getInstance () {
        if (null == mUtils) {
            synchronized (TimeZoneAbbreUtils.class) {
                if (null == mUtils) {
                    mUtils = new TimeZoneAbbreUtils ();
                }
            }
        }
        return mUtils;
    }

    public String getAbbreName (String key) {
        if (TextUtils.isEmpty (key)) {
            return "---";
        }
        return map.get (key);
    }

    public String getTineZoneName (String key) {
        if (TextUtils.isEmpty (key)) {
            return "---";
        }
        return map1.get (key);
    }
}
