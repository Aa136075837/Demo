package com.example.bo.nixon.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.StringRes;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;

import com.example.bo.nixon.base.NixonApplication;

import java.math.BigDecimal;

/**
 * @author bo.
 * @Date 2017/6/12.
 * @desc
 */

public class StringUtils {

    /**
     * 获取自带属性的文本，解决edittext hint 显示不全的问题
     *
     * @return
     */
    public static SpannedString getSpannableString (@StringRes int stringRes, int size) {

        // 新建一个可以添加属性的文本对象
        SpannableString ss = new SpannableString (NixonApplication.getContext ().getString
                (stringRes));

        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan (size, true);

        // 附加属性到文本
        ss.setSpan (ass, 0, ss.length (), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return new SpannedString (ss);
    }

    public static String getVersion () {
        try {
            PackageManager manager = NixonApplication.getContext ().getPackageManager ();
            PackageInfo info = manager.getPackageInfo (NixonApplication.getContext ()
                    .getPackageName (), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace ();
            return " . ";
        }
    }

    public static boolean isEmail (String email) {
        if (TextUtils.isEmpty (email)) {
            return false;
        }
        return email.matches ("[a-zA-Z0-9_.-]+@\\w+\\.[a-z]+(\\.[a-z]+)?");
    }

    public static boolean isPassword(String password){
        if(TextUtils.isEmpty (password)){
            return false;
        }
        return password.matches ("[a-zA-Z][a-zA-Z0-9]{5,17}");
    }


    /**
     * 从英寸得到英尺
     *
     * @return
     */
    public static String getFt (int in) {

        return (in / 12 + 1) + "";
    }

    public static String getFt (String in) {
        if (TextUtils.isEmpty (in)) {
            in = "0";
        }
        if (in.contains (".")) {
            in = in.split ("\\.")[0];
        }
        int i = Integer.parseInt (in);
        return (i / 12) + "";
    }

    /**
     * 从英寸得到出去英尺后的零头
     *
     * @return
     */
    public static String getIn (int in) {
        in = in % 12;
        return in + "";
    }

    public static String getIn (String in) {
        if (TextUtils.isEmpty (in)) {
            in = "0";
        }
        if (in.contains (".")) {
            in = in.split ("\\.")[0];
        }
        int i = Integer.parseInt (in);
        i = i % 12;
        return i + "";
    }

    /**
     * 厘米转英寸
     *
     * @return
     */
    public static String cm2in (double cm) {
        return cm / 2.54 + "";
    }

    public static String cm2in (String cm) {
        if (TextUtils.isEmpty (cm)) {
            cm = "0";
        }
        //if (cm.contains (".")) {
        //    cm = cm.split ("\\.")[0];
        //}
        double d = Double.parseDouble (cm);
        return d / 2.54 + "";
    }

    /**
     * 英寸转厘米
     *
     * @return
     */
    public static String inch2cm (double in) {
        return ((float) in * 2.54) + "";
    }

    public static String inch2cm (String in) {
        if (TextUtils.isEmpty (in)) {
            in = "0";
        }
        //if (in.contains (".")) {
        //    in = in.split ("\\.")[0];
        //}
        double d = Double.parseDouble (in);
        return d * 2.54 + "";
    }

    /**
     * 英寸转米
     *
     * @return
     */
    public static String inch2m (double in) {
        return ((float) in * 2.54) / 100 + "";
    }

    public static String inch2m (String in) {
        if (TextUtils.isEmpty (in)) {
            in = "0";
        }
        //if (in.contains (".")) {
        //    in = in.split ("\\.")[0];
        //}
        double d = Double.parseDouble (in);
        double v = ((float) d * 2.54) / 100;
        BigDecimal bg = new BigDecimal (v);
        double f1 = bg.setScale (2, BigDecimal.ROUND_HALF_UP).doubleValue ();
        return f1 + "";
    }

    /**
     * 磅转克
     *
     * @return
     */
    public static String lbs2g (double lbs) {
        return (lbs * 454) + "";
    }

    /**
     * 磅转千克
     *
     * @return
     */
    public static String lbs2kg (double lbs) {
        return (double) (lbs * 454) / 1000 + "";
    }

    public static String lbs2kg (String lbs) {
        if (TextUtils.isEmpty (lbs)) {
            lbs = "0";
        }
        //if (lbs.contains (".")) {
        //    lbs = lbs.split ("\\.")[0];
        //}
        double i = Double.parseDouble (lbs);
        double v = (i * 454) / 1000;
        BigDecimal bg = new BigDecimal (v);
        double f1 = bg.setScale (1, BigDecimal.ROUND_HALF_UP).doubleValue ();
        return f1 + "";
    }

    public static String lbs2g (String lbs) throws Exception {
        if (!lbs.matches ("0-9")) {
            throw new Exception ("传入的数据不符合规则");
        }
        double i = Double.parseDouble (lbs);
        return (i * 454) + "";
    }

    /**
     * 克转磅
     *
     * @return
     */
    public static String g2lbs (double g) {
        return g / 454 + "";
    }

    public static String g2lbs (String g) {
        if (TextUtils.isEmpty (g)) {
            g = "0";
        }
        double i = Double.parseDouble (g);
        BigDecimal bg = new BigDecimal (i / 454);
        double f1 = bg.setScale (1, BigDecimal.ROUND_HALF_UP).doubleValue ();
        return f1 + "";
    }

    public static String kmToMiles (double d) {
        double v = d * 0.6;
        BigDecimal b = new BigDecimal (v);
        double v1 = b.setScale (2, BigDecimal.ROUND_HALF_UP).doubleValue ();
        return v1 + "";
    }
}
