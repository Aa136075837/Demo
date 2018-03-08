package com.example.bo.nixon.base;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.example.bo.nixon.bean.ContactBean;
import com.example.bo.nixon.db.DbManager;
import com.example.bo.nixon.service.NotifyService;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.pgyersdk.crash.PgyCrashManager;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.smartBle.BleService;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.cookie.DBCookieStore;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class NixonApplication extends Application {

    private static Handler mainThreadHandler;
    private static Context mContext;
    private static DBCookieStore mDbCookieStore;

    public static List<ContactBean.ObjectBean> appList = new ArrayList<>();

    public static Handler getmainThreadHandler() {
        return mainThreadHandler;
    }

    public static Context getContext() {
        return mContext;
    }

    public static DBCookieStore getmDbCookieStore() {
        return mDbCookieStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mainThreadHandler = new Handler();
        mContext = this;
        mDbCookieStore = new DBCookieStore (this);
        NoHttp.initialize (this, new NoHttp.Config ().setNetworkExecutor (new OkHttpNetworkExecutor())
            .setReadTimeout (10 * 1000)
            .setConnectTimeout (10 * 1000)
            .setCookieStore (mDbCookieStore));
        DbManager.getInstance(mContext);
        startServices();
        FacebookSdk.setApplicationId ("1773660426258869");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        PgyCrashManager.register(this);
        DeviceMessage.attackBaseContent (this);
    }

    private void startServices() {
        startBle();
        startNotify();
    }

    private void startBle() {
        Intent intent = new Intent(this, BleService.class);
        startService(intent);
    }

    private void startNotify() {
        Intent notifyService = new Intent(this, NotifyService.class);
        startService(notifyService);
        toggleNotification();
    }

    private void toggleNotification() {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

}
