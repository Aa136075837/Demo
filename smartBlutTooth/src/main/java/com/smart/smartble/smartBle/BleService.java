package com.smart.smartble.smartBle;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.smart.smartble.ByteToString;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.FetchSDK;
import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.utils.ByteHelper;
import com.smart.smartble.utils.L;

import java.lang.ref.WeakReference;
import java.util.Date;
import java.util.Queue;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:09
 * @说明
 */
public class BleService extends Service implements IBleStatus, IBleWrite {

    private static final String TAG = "BleService";

    private SmartManager mSmartManager;

    private SmartBinder mSmartBinder = new SmartBinder (this);
    private BleHelper mBleHelper;

    @Override
    public void onCreate () {
        super.onCreate ();
        //DeviceMessage.attackBaseContent(this);
        mSmartManager = new SmartManager ();
        L.i ("ConnectComponent", "run----------->" + mSmartManager.toString ());
        mBleHelper = new BleHelper (getBaseContext (), mSmartManager);
        mBleHelper.addBleConnectListener (this);
        mBleHelper.addBleWrite (this);
        FetchSDK.fetch (mSmartManager, mBleHelper, true);
        if (BleConfig.hasReConnect) {
            mBleHelper.reConnectDevice ();
        }
    }

    @Nullable
    @Override
    public IBinder onBind (Intent intent) {
        return mSmartBinder;
    }

    @Override
    public void onBleStatus (Status status) {
        if (Status.DISCOVER_SERVICES == status) {
            FetchSDK.fetch (mSmartManager, mBleHelper, false);
            mSmartManager.setDiscovery (true);
            mBleHelper.clearPool ();
            if (mBleHelper.isReconnect ()) {
                mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_AUTHORIZATION, true);
            }
        } else if (Status.DISCONNECTED == status) {
            mSmartManager.setDiscovery (false);
            if (BleConfig.hasReConnect) {
                mBleHelper.reConnectDevice ();
            }
        } else if (Status.DISCOVER_SERVICES_FAIL == status) {
            if (null != mBleHelper)
                mBleHelper.disConnect ();
        }
    }

    @Override
    public void write (Queue<SmartAction> dataPool) {
        if (dataPool.size () > 0 && null != dataPool.element ()) {
            if (dataPool.element ().isInterruption ()) {
                dataPool.remove ();
            }
        }
    }

    @Override
    public void writeSuccessfully (Queue<SmartAction> dataPool, byte[] bytes, String uuid) {
        if (null == bytes)
            return;
        Log.w ("BleService", "writeSuccessfully::" + ByteToString.byteToString (bytes));
        if (dataPool.size () > 0) {
            SmartAction smartAction = dataPool.element ();
            if (smartAction.isNoReply ()) {
                if (mSmartManager.getISmartActionOperator ().isSameInstruction (dataPool, bytes))
                    dataPool.remove ();
            }
        }

        if (mSmartManager.getISmartActionOperator ().isOTAData (bytes, uuid)) {
            Log.w ("OTAComponent", "OTAComponent------------>writeSuccessfully" + ByteToString
                    .byteToString (bytes));
            SmartAction action = new SmartAction.SmartBuilder ().action (Action
                    .REQUEST_ACTION_ASK_MCU_DATA_OK).value (bytes).build ();
            mSmartManager.getIService ().dealActon (action);
        }
    }

    @Override
    public void writeReply (Queue<SmartAction> dataPool, byte[] bytes, String uuid) {
        Log.w ("BleService", "writeReply::" + ByteToString.byteToString (bytes));
        if (mSmartManager.getISmartActionOperator ().isNormalAck (bytes) && mSmartManager
                .getISmartActionOperator ().isSameInstruction (dataPool, bytes)) {
            dataPool.remove ();
            return;
        } else {
            if (mSmartManager.getISmartActionOperator ().isSameInstruction (dataPool, bytes)) {
                dataPool.remove ();
            }
        }
        SmartAction smartAction = mSmartManager.getISmartActionOperator ().createSmartAction
                (bytes, uuid);
        mSmartManager.getIService ().dealActon (smartAction);
        dealEvent (smartAction);
    }

    private void dealEvent (SmartAction smartAction) {
        byte[] bytes = smartAction.getBytes ();
        switch (smartAction.getAction ()) {
            case REQUEST_ACTION_AUTHORIZATION:
                if (1 == (bytes[8] & 0xff)) {
                    mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_GET_SN);
                    mSmartManager.getIClient ().doAction (Action
                            .REQUEST_ACTION_SET_NOTIFY_SETTING, 1);
                    mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_GET_ELECTRICITY);
                    mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_GET_SECOND_TIME);
                    mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_GET_TARGET);
                    mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_GET_ALARM, 0);
                }
                break;
            case REQUEST_ACTION_GET_TIME:
                mSmartManager.getIClient ().doAction (Action.REQUEST_ACTION_ASK_TIME, 1, new Date
                        ());
                break;
            case REQUEST_ACTION_GET_SN:
                if (0 == ByteHelper.calculateHigh (bytes[8])) {
                    DeviceMessage.getInstance ().putDeviceLowSn (new String (ByteHelper.cutBytes
                            (bytes, 9, 19)));
                } else if (1 == ByteHelper.calculateHigh (bytes[8])) {
                    DeviceMessage.getInstance ().putDeviceHighSn (new String (ByteHelper.cutBytes
                            (bytes, 9, 19)));
                    Log.w (TAG, "REQUEST_ACTION_GET_SN" + DeviceMessage.getInstance ()
                            .getDeviceSn ());
                }
                break;
        }
    }

    @Override
    public void writeError (Queue<SmartAction> dataPool) {

    }

    public static class SmartBinder extends Binder {

        private WeakReference<BleService> mTarget;

        public SmartBinder (BleService bleService) {
            mTarget = new WeakReference<> (bleService);
        }

        public SmartManager getSmartManager () {
            if (null != mTarget.get ())
                return mTarget.get ().mSmartManager;
            return null;
        }
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        mBleHelper.destroy ();
    }

}
