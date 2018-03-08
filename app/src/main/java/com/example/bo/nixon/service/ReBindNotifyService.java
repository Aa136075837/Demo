package com.example.bo.nixon.service;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;

/**
 * @author ARZE
 * @version 创建时间：2017/6/26 11:43
 * @说明
 */
public class ReBindNotifyService {

    public static boolean isNotificationListenerEnable(Context context) {
        NotificationManagerCompat compat = NotificationManagerCompat.from(context);
        compat.cancelAll();
        return compat.areNotificationsEnabled();
    }
}
