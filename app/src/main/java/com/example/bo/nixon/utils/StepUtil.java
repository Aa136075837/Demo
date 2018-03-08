package com.example.bo.nixon.utils;

import android.util.Log;
import java.math.BigDecimal;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 17:54
 * @说明
 */
public class StepUtil {

    public static double getStepWidthByHeight(double height) {// m
        return height * 0.45 / 100;
    }

    //
    // // 根据身高、体重，分钟算消耗的卡路里
    // public static double getCalorieByWeightAndDistance(double weight,
    // double distance) {// w kg， height cm
    // // w60×d8×1.036
    // return weight * distance / 1000 * 1.036;
    // }

    public static double getCalorieByWeightAndStep(double weight, int stepCount, int count) {//
        BigDecimal bigDecimal = new BigDecimal(stepCount * ((weight - 13.63636) * 0.000693 + 0.00495));
        return bigDecimal.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double getDistanceByHeightAndStep(double dh, int step, int count) {
        Log.e ("DISTANCE"," 身高 = " + dh +" 步数 = " + step + " count = " + count);
        BigDecimal bigDecimal = new BigDecimal(getStepWidthByHeight(dh) * step);
        return bigDecimal.setScale(count, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
