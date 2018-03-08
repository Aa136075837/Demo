package com.smart.notifycomponent;

import android.content.Context;
import android.os.RemoteException;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;

import java.lang.reflect.Method;

/**
 * @author ARZE
 * @version 创建时间：2017/6/23 9:55
 * @说明
 */
public class PhoneHelper {

    private static ITelephony getITelephony(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        ITelephony iTelephony = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony");
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            iTelephony = (ITelephony) getITelephonyMethod.invoke(mTelephonyManager);
            return iTelephony;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iTelephony;
    }

    public static void endCall(Context context) {
        ITelephony telephony = getITelephony(context);
        try {
            telephony.endCall();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    public static void answerCall(Context context) {
        ITelephony telephony = getITelephony(context);
        try {
            telephony.answerRingingCall();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
