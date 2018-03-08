package com.example.bo.nixon.service;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.ContactBean;
import com.example.bo.nixon.bean.DbWatchBean;
import com.example.bo.nixon.bean.NetSportStepBean;
import com.example.bo.nixon.bean.UploadStepsBean;
import com.example.bo.nixon.db.DbManager;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.SmartMediaPlay;
import com.google.gson.Gson;
import com.smart.alarmcomponent.AlarmComponent;
import com.smart.alarmcomponent.IAlarmReplay;
import com.smart.notifycomponent.NotifyComponent;
import com.smart.notifycomponent.PhoneHelper;
import com.smart.notifycomponent.listener.IFindNotify;
import com.smart.notifycomponent.listener.IPhoneEvent;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.SmartManager;
import com.smart.smartble.smartBle.BleService;
import com.smart.timecomponent.TimeComponent;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 19:00
 * @说明
 */
public class NotifyService extends NotificationListenerService implements IFindNotify,
        IPhoneEvent, IAlarmReplay {

    private static final String TAG = "NotifyService";
    private NotifyComponent mNotifyComponent;
    private final static String B_PHONE_STATE = TelephonyManager.ACTION_PHONE_STATE_CHANGED;
    private BroadcastReceiverMgr mBroadcastReceiver;
    private boolean isCall = false;
    private SmartMediaPlay mSmartMediaPlay;
    private SmartManager mSmartManager;
    private long mTime = 0;
    private AlarmComponent mAlarmComponent;
    private String mDeviceUUID;

    @Override
    public void onCreate () {
        super.onCreate ();
        bindManagerService (getBaseContext ());
        mBroadcastReceiver = new BroadcastReceiverMgr ();
        IntentFilter filter = new IntentFilter ();
        filter.addAction (B_PHONE_STATE);
        filter.addAction (Intent.ACTION_TIMEZONE_CHANGED);
        registerReceiver (mBroadcastReceiver, filter);
        mSmartMediaPlay = new SmartMediaPlay (NixonApplication.getContext ());
        String cookie = SPUtils.getString (NixonApplication.getContext (), Constant.COOKIE_KEY);
        if (0 == NixonApplication.appList.size () && !TextUtils.isEmpty (cookie)) {
            getContactFromServer ();
        }
    }

    @Override
    public void onNotificationPosted (StatusBarNotification sbn) {
        doNotify (sbn);
        if (NetUtil.getNetworkState (NixonApplication.getContext ()) != NetUtil.NETWORN_NONE) {
            uploadDbSportData ();
        }
    }

    private void doNotify (StatusBarNotification sbn) {
        Log.w (TAG, "doNotify::" + sbn.toString ());
        if (System.currentTimeMillis () - mTime < 3 * 1000) {
            return;
        }
        mTime = System.currentTimeMillis ();
        String pkg = sbn.getPackageName ();
        if (TextUtils.isEmpty (pkg))
            return;
        if (ConstantPackageName.WX.contains (sbn.getPackageName ()) && (sbn.getId () == 40 || sbn
                .getId () == 43)) {
            return;
        }
        if (ConstantPackageName.QQ.contains (sbn.getPackageName ()) && (sbn.getId () ==
                2130839418 || sbn.getId () == 2130839170))
            return;
        if (ConstantPackageName.WHAT_APP.contains (sbn.getPackageName ()) && sbn.getId () == 4)
            return;
        if (ConstantPackageName.SYSTEM.equals (sbn.getPackageName ()))
            return;
        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.MESSAGE_SWITCH_KEY) &&
                null != mNotifyComponent) {
            Log.w (TAG, "doNotify::" + sbn.toString ());
            if (ConstantPackageName.QQ.contains (pkg)) {
                mNotifyComponent.setNotify (1);
            } else if (ConstantPackageName.WX.contains (pkg)) {
                mNotifyComponent.setNotify (2);
            } else if (ConstantPackageName.TX_WB.contains (pkg)) {
                mNotifyComponent.setNotify (4);
            } else if (ConstantPackageName.SKYPE.contains (pkg)) {
                mNotifyComponent.setNotify (8);
            } else if (ConstantPackageName.SINA_WB.contains (pkg)) {
                mNotifyComponent.setNotify (16);
            } else if (ConstantPackageName.FACKBOOK.contains (pkg)) {
                mNotifyComponent.setNotify (32);
            } else if (ConstantPackageName.TWITTER.contains (pkg)) {
                mNotifyComponent.setNotify (64);
            } else if (ConstantPackageName.WHAT_APP.contains (pkg)) {
                mNotifyComponent.setNotify (128);
            } else if (ConstantPackageName.LINE.contains (pkg)) {
                mNotifyComponent.setNotify (256);
            } else {
                //   mNotifyComponent.setNotify(512);
            }
        }
    }

    private boolean bindManagerService (Context context) {
        Intent intent = new Intent (context, BleService.class);
        return context.bindService (intent, mBleConnect, Context.BIND_AUTO_CREATE);
    }

    private void unbindManagerService (Context context) {
        context.unbindService (mBleConnect);
    }

    private ServiceConnection mBleConnect = new ServiceConnection () {

        @Override
        public void onServiceConnected (ComponentName name, IBinder service) {
            BleService.SmartBinder smartBinder = (BleService.SmartBinder) service;
            if (null != smartBinder) {
                mSmartManager = smartBinder.getSmartManager ();
                mNotifyComponent = new NotifyComponent (mSmartManager);
                mNotifyComponent.registerComponent ();
                mNotifyComponent.addPhoneListener (NotifyService.this);
                mNotifyComponent.addFindNotifyListener (NotifyService.this);

                mAlarmComponent = AlarmComponent.getInstance (mSmartManager);
                mAlarmComponent.registerComponent ();
                mAlarmComponent.addAlarmReplayListener (NotifyService.this);

            }
        }

        @Override
        public void onServiceDisconnected (ComponentName name) {
        }
    };

    @Override
    public void onNotificationRemoved (StatusBarNotification sbn) {
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        unbindManagerService (getBaseContext ());
        toggleNotification ();
        unregisterReceiver (mBroadcastReceiver);
        mNotifyComponent.removeFindNotifyListener (this);
        mNotifyComponent.removePhoneListener (this);
        mAlarmComponent.removeAlarmReplayListener (this);
        mAlarmComponent.unRegisterComponent ();
        Log.w (TAG, "onDestroy::");
    }

    @Override
    public void findNotify () {
        Log.w (TAG, "RUN------------>查找手机");
        mSmartMediaPlay.ring ();
        ToastManager.show (NixonApplication.getContext (), "查找手机", Toast.LENGTH_LONG);
    }

    private void toggleNotification () {
        PackageManager pm = getPackageManager ();
        pm.setComponentEnabledSetting (new ComponentName (this, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting (new ComponentName (this, NotifyService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    public void endCall () {
        if (isCall) {
            PhoneHelper.endCall (getBaseContext ());
        }
    }

    @Override
    public void answerCall () {
        if (isCall) {
            PhoneHelper.answerCall (getBaseContext ());
        }
    }

    public void sendCurrentZoneTime () {
        boolean autoTime = SPUtils.getBoolean (NixonApplication.getContext (), Constant
                .IS_AUTO_TIME_KEY);
        if (mSmartManager == null) {
            return;
        }
        if (autoTime && mSmartManager.isDiscovery ()) {
            TimeComponent timeComponent = new TimeComponent (mSmartManager);
            timeComponent.setMcuTime (1, new Date ());
        }
    }

    @Override
    public void replay (int index) {
        Log.w (TAG, "replay::" + index);
        mSmartMediaPlay.ring ();
    }

    public class BroadcastReceiverMgr extends BroadcastReceiver {

        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            if (action.equals (B_PHONE_STATE)) {
                Log.i (TAG, "[Broadcast]PHONE_STATE");
                doReceivePhone (context, intent);
            } else if (Intent.ACTION_TIMEZONE_CHANGED.equals (intent.getAction ())) {
                sendCurrentZoneTime ();
            }
        }

        /**
         * 处理电话广播.
         */
        public void doReceivePhone (Context context, Intent intent) {
            TelephonyManager telephony = (TelephonyManager) context.getSystemService (Context
                    .TELEPHONY_SERVICE);
            int state = telephony.getCallState ();
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String phoneNumber = intent.getStringExtra (TelephonyManager
                            .EXTRA_INCOMING_NUMBER);
                    Log.w (TAG, phoneNumber + " ");
                    boolean b = isContact (phoneNumber);
                    isCall = true;
                    if (null != mNotifyComponent && SPUtils.getBoolean (context, Constant.CALL)) {
                        if (b) {
                            Log.w (TAG, "chang yong lian xi ren ");
                            mNotifyComponent.setNotify (65536);
                        } else {
                            Log.w (TAG, "!chang yong lian xi ren ");
                            mNotifyComponent.setNotify (1024);
                        }
                    }
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    isCall = false;
                    if (null != mNotifyComponent) {
                        mNotifyComponent.cancelNotify (1024);
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (null != mNotifyComponent) {
                        mNotifyComponent.cancelNotify (1024);
                    }
                    isCall = false;
                    break;
            }
        }
    }

    private boolean isContact (String number) {
        if (0 == NixonApplication.appList.size ()) {
            Log.w (TAG, " null == mContactList");
            getContactFromServer ();
        }
        for (ContactBean.ObjectBean b : NixonApplication.appList) {
            String contactNumber = dealContactNumber (b.getPhoneNumber ());
            if (TextUtils.isEmpty (contactNumber) || TextUtils.isEmpty (number))
                return false;
            Log.w (TAG, " 来电号码 " + number + "  常用联系人列表 " + contactNumber);
            if (contactNumber.contains (number)) {
                Log.w (TAG, "contactNumber.contains(number)");
                return true;
            }
        }
        Log.w (TAG, "mContactList " + NixonApplication.appList.size ());
        return false;
    }

    private String dealContactNumber (String phoneNumber) {
        if (phoneNumber.contains (" ")) {
            Log.w (TAG, " contains");
            phoneNumber.replaceAll (" ", "");
        }
        if (phoneNumber.contains ("-")) {
            phoneNumber.replaceAll ("-", "");
        }
        return phoneNumber;
    }

    private void uploadDbSportData () {
        String customerId = SPUtils.getString (NixonApplication.getContext (), Constant
                .CUSTOMER_ID);
        mDeviceUUID = DeviceMessage.getInstance ().getDeviceUUID ();
        if (TextUtils.isEmpty (customerId) || TextUtils.isEmpty (mDeviceUUID)) {
            return;
        }
        List<DbWatchBean> dbWatchBean = DbManager.getDbWatchBean (customerId, mDeviceUUID);
        if (dbWatchBean != null && dbWatchBean.size () > 0) {
            updateListSteps (dbWatchBean);
        }
    }

    private void updateListSteps (List<DbWatchBean> watchBeens) {
        List<Map<String, String>> parmas = new ArrayList<> ();
        Log.e ("UPDATELISTSTEPS", "  watchBeens.size () = " + watchBeens.size ());
        for (int i = 0; i < watchBeens.size (); i++) {
            Map<String, String> element = new HashMap<> ();
            DbWatchBean bean = watchBeens.get (i);
            element.put ("customerId", bean.getUserId ());
            element.put ("timestamp", bean.getTime () + "");
            element.put ("step", bean.getValue () + "");
            element.put ("deviceId", bean.getDeivceMac ());
            parmas.add (element);
        }
        HttpUtils.getInstance ().autoLogin ();

        HttpUtils.getInstance ().requestJsonArrayPost (ConstantURL.UPDATE_LIST_STEPS, 121232,
                parmas, new SimpleResponseListener<JSONObject> () {
            @Override
            public void onSucceed (int what, Response<JSONObject> response) {
                super.onSucceed (what, response);
                Gson gson = new Gson ();
                NetSportStepBean bean = gson.fromJson (response.get ().toString (),
                        NetSportStepBean.class);
                if (RequestCode.SUCCESS.equals (bean.getCode ())) {
                    Log.e ("UPDATELISTSTEPS", "  上传数据库 数据成功 删除数据库数据" + what);
                    DbManager.delete (SPUtils.getString (NixonApplication.getContext (), Constant
                            .CUSTOMER_ID), mDeviceUUID);
                }
            }

            @Override
            public void onFailed (int what, Response<JSONObject> response) {
                super.onFailed (what, response);
                Log.e ("UPDATELISTSTEPS", "  上传数据库 数据失败 " + what);
            }
        });
    }

    private void updateSportData (String step, final String customerID, final String deviceID) {
        Map<String, String> map = new HashMap<> ();
        map.put ("customerId", customerID);
        map.put ("timestamp", System.currentTimeMillis () + "");
        map.put ("step", step);
        map.put ("deviceId", deviceID);
        HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.UPDATE_STEPS, 1313, map, new
                SimpleResponseListener<JSONObject> () {
            @Override
            public void onSucceed (int what, Response<JSONObject> response) {
                super.onSucceed (what, response);
                Gson gson = new Gson ();
                UploadStepsBean bean = gson.fromJson (response.get ().toString (),
                        UploadStepsBean.class);
                if (RequestCode.SUCCESS.equals (bean.getCode ())) {
                    DbManager.delete (customerID, deviceID);
                }
            }

            @Override
            public void onFailed (int what, Response<JSONObject> response) {
                super.onFailed (what, response);
            }
        });
    }

    private void getContactFromServer () {
        final Gson mGson = new Gson ();
        Map<String, String> map = new HashMap<> ();
        map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant
                .CUSTOMER_ID));
        HttpUtils.getInstance ().requestCookieJsonObjectPost (ConstantURL.GET_FAVORITE_CONTACT,
                0x123, map, new OnResponseListener<JSONObject> () {
            @Override
            public void onStart (int what) {
            }

            @Override
            public void onSucceed (int what, Response<JSONObject> response) {
                ContactBean bean = mGson.fromJson (response.get ().toString (), ContactBean.class);
                List<ContactBean.ObjectBean> beanList = bean.getObject ();
                if (null == beanList)
                    return;
                NixonApplication.appList.clear ();
                NixonApplication.appList.addAll (beanList);
            }

            @Override
            public void onFailed (int what, Response<JSONObject> response) {

            }

            @Override
            public void onFinish (int what) {

            }
        });
    }
}
