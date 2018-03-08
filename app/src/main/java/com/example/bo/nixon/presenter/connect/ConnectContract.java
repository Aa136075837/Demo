package com.example.bo.nixon.presenter.connect;

import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.text.TextUtils;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.ui.fragment.alarm.AlarmCacheHelper;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.google.gson.Gson;
import com.smart.alarmcomponent.AlarmComponent;
import com.smart.alarmcomponent.IAlarm;
import com.smart.attributescomponent.AttributesComponent;
import com.smart.attributescomponent.listener.IPower;
import com.smart.connectComponent.ConnectComponent;
import com.smart.connectComponent.IAuthorization;
import com.smart.connectComponent.IConnect;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.SmartManager;
import com.smart.smartble.smartBle.BleDevice;
import com.smart.timecomponent.ISecondTime;
import com.smart.timecomponent.TimeComponent;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Date;
import java.util.Map;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 15:46
 * @说明
 */
public interface ConnectContract {

    interface ConnectNixonView extends BaseNixonView {

        void addNewDevice(BleDevice bleDevice);

        void connectingDevice();

        void connectDevice();

        void authorSuccessfully();

        void authorTimeOut();

        void connectFail();

        void leSanEnd();

        void leSanStart();
    }

    class ConnectPresenter extends BaseBlePresenter<ConnectNixonView>
            implements IConnect, IConnectPresenter, IAuthorization, ISecondTime, IPower, IAlarm {

        private TimeComponent mTimeComponent;
        private AttributesComponent mAttributesComponent;
        private AlarmComponent mAlarmComponent;
        private static final String TAG = "ConnectPresenter";

        private ConnectComponent mConnectComponent;
        private boolean isAuthorzation = false;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mConnectComponent = ConnectComponent.getInstance(smartManager);
            mConnectComponent.registerComponent();
            mConnectComponent.addConnectListener(this);
            mConnectComponent.addAuthorzationListener(this);
            mConnectComponent.startLeScan();

            mTimeComponent = new TimeComponent(smartManager);
            mTimeComponent.registerComponent();
            mTimeComponent.addSecondTimeListener(this);

            mAttributesComponent = AttributesComponent.getInstance(smartManager);
            mAttributesComponent.registerComponent();
            mAttributesComponent.addPowerListener(this);

            mAlarmComponent = AlarmComponent.getInstance(smartManager);
            mAlarmComponent.registerComponent();
            mAlarmComponent.addAlarmListener(this);
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        @Override
        public void findNewDevice(BleDevice bleDevice) {
            //Log.w(TAG, "run------------>" + bleDevice.getRssi());
            if (TextUtils.isEmpty(bleDevice.getKey()) || !bleDevice.getKey().contains("5037"))
                return;
            if (null != getView()) getView().addNewDevice(bleDevice);
        }

        @Override
        public void leSanEnd() {
            if (getView() != null) {
                getView().leSanEnd();
            }
        }

        @Override
        public void leSanStart() {
            if (getView() != null) {
                getView().leSanStart();
            }
        }

        @Override
        public void connectFail() {
            if (null != getView()) {
                DeviceMessage.getInstance().putDeviceUUID("");
                getView().connectFail();
            }
        }

        @Override
        public void connectedDevice() {
            if (null != getView()) {
                getView().connectDevice();
            }
        }

        @Override
        public void connectSuccessful() {
            getView().authorSuccessfully();
        }

        @Override
        public void startSearch() {
            if (null != mConnectComponent) mConnectComponent.startLeScan();
        }

        @Override
        public void refreshSearch() {
            if (null != mConnectComponent) {
                mConnectComponent.stopLeScan();
                mConnectComponent.startLeScan();
            }
        }

        @Override
        public void connectDevice(BleDevice bleDevice) {
            if (null != mConnectComponent) {
                String address = bleDevice.getDevice().getAddress();
                SPUtils.putString(NixonApplication.getContext(), Constant.DEVICE_MAC, address);
                mConnectComponent.connectDevice(bleDevice);
                BluetoothDevice device = bleDevice.getDevice();
                SPUtils.putString(NixonApplication.getContext(), Constant.BLE_NAME_KEY, device.getName());
                getView().connectingDevice();
            }
        }

        @Override
        public void disConnectDevice(BleDevice bleDevice) {
            if (null != mConnectComponent) {
                mConnectComponent.disConnectDevice();
                SPUtils.putString(NixonApplication.getContext(), Constant.BLE_NAME_KEY, "Ambassador");
                SPUtils.putBoolean(NixonApplication.getContext(), Constant.DISCONNECT_ALERT_KEY, false);
            }
        }

        @Override
        public void detachView() {
            Log.w(TAG, "run------------->detachView::");
            removeAllListener();
            super.detachView();
        }

        public void removeAllListener() {
            if (null != mConnectComponent) {
                if (!isAuthorzation) mConnectComponent.disConnectDevice();
                mConnectComponent.stopLeScan();
                mConnectComponent.unRegisterComponent();
                mConnectComponent.removeAuthorzationListener(this);
                mConnectComponent.removeConnectListener(this);
            }
        }

        @Override
        public void authorization(boolean author) {
            getView().authorSuccessfully();
            isAuthorzation = true;
        }

        @Override
        public void AuthorizationTimeOut() {
            if (null != getView()) {
                getView().authorTimeOut();
            }
            if (null != mConnectComponent) {
                mConnectComponent.disConnectDevice();
            }
        }

        private final int BIND_DEVICE = 1111;
        OnResponseListener mListener = new OnResponseListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response response) {
                Gson gson = new Gson();
                LoginResponseBean bean = gson.fromJson(response.get().toString(), LoginResponseBean.class);
                switch (what) {
                    case BIND_DEVICE:
                        switch (bean.getCode()) {
                            case RequestCode.SUCCESS:
                                ToastManager.show(NixonApplication.getContext(), "绑定成功", 0);
                                break;
                            case RequestCode.DEVICE_BINDED:

                                break;
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {

            }

            @Override
            public void onFinish(int what) {

            }
        };

        public void bindDevice(Map<String, String> map) {
            HttpUtils.getInstance().requestCookieJsonObjectPost(ConstantURL.BIND_DEVICE, BIND_DEVICE, map, mListener);
        }

        @Override
        public void secondTime(Date date, int high, int low) {
            String mSecondZone = high + ":0";
            SPUtils.putString(NixonApplication.getContext(), Constant.SECOND_ZONE_KEY, mSecondZone);
        }

        @Override
        public void electricity(int value) {
            SPUtils.putInt(NixonApplication.getContext(), Constant.ELECTRICITY_KEY, value);
        }

        @Override
        public void alarm(int index, int time, int open, int repeat) {
            if (repeat == 0) return;
            AlarmEventBean alarmEventBean =
                    AlarmCacheHelper.getAlarmEventBean(NixonApplication.getContext(), index - 1);
            alarmEventBean.setAlarmTime(time);
            alarmEventBean.setOpenType(open == 1);
            if ((repeat & 128) != 128) {
                alarmEventBean.setRepeatTime(repeat & 128);
                alarmEventBean.setRepeat(false);
            } else {
                alarmEventBean.setRepeatTime(repeat);
                alarmEventBean.setRepeat(true);
            }
            AlarmCacheHelper.setAlarm(NixonApplication.getContext(), index - 1, alarmEventBean);
        }

    }

}
