package com.smart.smartble.smartBle;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.smart.smartble.ByteToString;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.FetchDevice;
import com.smart.smartble.LooperHelper;
import com.smart.smartble.SmartManager;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.leScan.BleDeviceImp;
import com.smart.smartble.utils.FilterForString;
import com.smart.smartble.utils.L;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.UUID;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:09
 * @说明 处理蓝牙
 */
public class BleHelper implements BluetoothAdapter.LeScanCallback {

    private static final String TAG = "BleHelper";
    private Context mContext;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice mTargetDevice;
    private BluetoothGatt mBluetoothGatt;
    private BluetoothManager mBluetoothManager;
    private BluetoothGattService mBluetoothGattServer;
    private ListenerInfo mListenerInfo;

    private int SCAN_PERIOD = 15000;

    private boolean mScanning = false;
    private BleDeviceImp mBleDeviceImp = new BleDeviceImp();
    private HandlerImp mHandlerImp;
    private SmartManager mSmartManager;

    private static final int FIND_NEW = 0x1001;

    private static final int STATE_CHANGE = 0x0001;
    private static final int DISCOVER_SERVICE = 0x0002;
    private static final int CHARACTERISTIC_WRITE = 0x0003;
    private static final int CHARACTERISTIC_CHANGE = 0x0004;

    private static final int WRITE_DATA = 0x2001;
    private static final int WRITRE_OTA_DATA = 0x2002;

    private Queue<SmartAction> mSmartDataPool = new LinkedList<>();
    private Queue<SmartAction> mOTAPool = new LinkedList<>();
    private boolean isReconnect = false;


    public BleHelper(Context context, SmartManager manager) {
        mContext = context;
        mSmartManager = manager;
        mHandlerImp = new HandlerImp(mContext, this);
        register();
        initBle();
    }

    private void register() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        mContext.registerReceiver(mBlueToothReceiver, filter);
    }

    public void destroy() {
        if (null != mBlueToothReceiver) {
            mContext.unregisterReceiver(mBlueToothReceiver);
        }
    }

    private void initBle() {
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter.isEnabled()) {
            mSmartManager.setBluetoothOpen(true);
        } else {
            mSmartManager.setBluetoothOpen(false);
        }
    }

    public void reConnectDevice() {
        if (TextUtils.isEmpty(DeviceMessage.getInstance().getDeviceUUID()))
            return;
        mTargetDevice = mBluetoothAdapter.getRemoteDevice(DeviceMessage.getInstance().getDeviceUUID());
        isReconnect = true;
        L.i("BleHelperConnect", "runte------------>write :::reConnectDevice");
        connectDevice();
    }

    private synchronized void connectDevice() {
        new LooperHelper(mContext).post(new Runnable() {
            @Override
            public void run() {
                clearGatt();
                Log.w("BleHelperConnect", "run----------------->onCharacteristicChanged  " + mTargetDevice.getAddress());
                //  Toast.makeText(mContext,mTargetDevice.getAddress(),Toast.LENGTH_LONG).show();
                DeviceMessage.getInstance().putDeviceUUID(mTargetDevice.getAddress());
                Log.e ("NIXONLOGIN","  保存mac地址 ：----》" + mTargetDevice.getAddress());
                mBluetoothGatt = mTargetDevice.connectGatt(mContext, false, mBluetoothGattCallback);
            }
        });
    }

    public synchronized void connectDevice(BleDevice bleDevice) {
        BluetoothDevice bluetoothDevice = bleDevice.getDevice();
        if (null == bluetoothDevice) {
            bluetoothDevice = mBluetoothAdapter.getRemoteDevice(bleDevice.getDevice().getAddress());
        }
        if (null != bluetoothDevice) {
            mTargetDevice = bluetoothDevice;
            isReconnect = false;
            L.i("BleHelperConnect", "runte------------>write :::connectDevice");
            connectDevice();
        }
    }


    public void disConnect() {
        DeviceMessage.getInstance().putDeviceUUID("");
        clearGatt();
        onReplyStatus(IBleStatus.Status.DISCONNECTED);
    }

    /**
     * 此处异常需要处理
     */
    private void clearGatt() {
        if (null != mBluetoothGatt) {
            try {
                mBluetoothGatt.close();
                mBluetoothGatt.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private final BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.w("BleHelperConnect", "run----------------->onCharacteristicChanged" + status + "or  " + newState);
            mHandlerImp.sendMessage(Message.obtain(mHandlerImp, STATE_CHANGE, status, newState, gatt));
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            mHandlerImp.sendMessage(Message.obtain(mHandlerImp, DISCOVER_SERVICE, 0, status, gatt));
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            mHandlerImp.sendMessage(Message.obtain(mHandlerImp, CHARACTERISTIC_WRITE, 0, status, characteristic));
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            BluetoothGattCharacteristic bluetoothGattCharacteristic =
                    new BluetoothGattCharacteristic(characteristic.getUuid(), characteristic.getProperties(), characteristic.getPermissions());
            byte[] bytes = characteristic.getValue().clone();
            bluetoothGattCharacteristic.setValue(bytes);
            Message message = new Message();
            message.what = CHARACTERISTIC_CHANGE;
            message.arg1 = 0;
            message.arg2 = 0;
            message.obj = bluetoothGattCharacteristic;
            mHandlerImp.sendMessage(message);
        }
    };

    private Runnable mLeScanRunnable;

    private synchronized void scanLeDevice(final boolean enable) {
        if (null != mLeScanRunnable)
            mHandlerImp.removeCallbacks(mLeScanRunnable);
        if (enable) {
            mBleDeviceImp.clear();
            mScanning = true;
            mHandlerImp.postDelayed(mLeScanRunnable = new Runnable() {
                @Override
                public void run() {
                    scanLeDevice(false);
                }
            }, SCAN_PERIOD);
            mBluetoothAdapter.startLeScan(this);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(this);
        }
    }

    public void startScanLeDevice() {
        scanLeDevice(true);
    }

    public void stopScanLeDevice() {
        scanLeDevice(false);
    }

    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
        if (null == device.getName() && null == device.getAddress())
            return;
        if (null == device.getAddress())
            return;
        if (!FetchDevice.isTargetDevice(device))
            return;
        L.i(TAG, "onLeScan:" + rssi + device.getAddress());
        int deviceIndex;
        if ((deviceIndex = mBleDeviceImp.isHasDevice(device)) == -1) {
            BleDevice bleDevice = new BleDevice(device, rssi);
            mBleDeviceImp.addDevice(bleDevice);
            String scanFilter = FilterForString.filter(scanRecord);
            bleDevice.setRssi(rssi);
            bleDevice.setKey(scanFilter);
            mBleDeviceImp.putAddress(device.getAddress(), scanFilter);
            mHandlerImp.sendMessage(Message.obtain(mHandlerImp, FIND_NEW, 0, 0, bleDevice));
        } else {
            String scanFilter = FilterForString.filter(scanRecord);
            BleDevice bleDevice = mBleDeviceImp.getDevice(deviceIndex);
            bleDevice.setRssi(rssi);
            bleDevice.setKey(scanFilter);
            mBleDeviceImp.set(deviceIndex, bleDevice);
        }
    }

    public void setOnLeSanListener(@NonNull ILeListener iLeListener) {
        getListenerInfo().mILeListener = iLeListener;
    }

    public void addBleConnectListener(@NonNull IBleStatus iBleStatus) {
        if (!getListenerInfo().mIBleStatuses.contains(iBleStatus))
            getListenerInfo().mIBleStatuses.add(iBleStatus);
    }

    public void removeBleConnectListener(@NonNull IBleStatus iBleStatus) {
        if (getListenerInfo().mIBleStatuses.contains(iBleStatus))
            getListenerInfo().mIBleStatuses.remove(iBleStatus);
    }

    public void addBleWrite(IBleWrite bleWrite) {
        if (!getListenerInfo().mIBleWrites.contains(bleWrite))
            getListenerInfo().mIBleWrites.add(bleWrite);
    }

    private ListenerInfo getListenerInfo() {
        if (null == mListenerInfo) {
            mListenerInfo = new ListenerInfo();
        }
        return mListenerInfo;
    }

    public void bleWrite() {
        for (IBleWrite bleWrite : getListenerInfo().mIBleWrites) {
            bleWrite.write(mSmartDataPool);
        }
    }

    public void bleWriteSuccessfully(byte[] bytes, String uuid) {
        for (IBleWrite bleWrite : getListenerInfo().mIBleWrites) {
            bleWrite.writeSuccessfully(mSmartDataPool, bytes, uuid);
        }
    }

    public void bleWriteReply(byte[] bytes, String uuid) {
        for (IBleWrite bleWrite : getListenerInfo().mIBleWrites) {
            bleWrite.writeReply(mSmartDataPool, bytes, uuid);
        }
    }

    public void bleWriteError() {
        for (IBleWrite bleWrite : getListenerInfo().mIBleWrites) {
            bleWrite.writeError(mSmartDataPool);
        }
    }

    private static class ListenerInfo {
        private ILeListener mILeListener;
        private List<IBleStatus> mIBleStatuses = new ArrayList<>();
        private List<IBleWrite> mIBleWrites = new ArrayList<>();
    }

    public synchronized void toWriteAction(SmartAction smartAction) {
        if (null == smartAction || null == mSmartManager || !mSmartManager.isDiscovery())
            return;
        Log.w(TAG, "toWriteAction::" + ByteToString.byteToString(smartAction.getBytes()) + "  or " + mSmartManager.isDiscovery());
        if (smartAction.isOTA()) {
            toSendCharacteristicOTA(smartAction);
        } else {
            if (smartAction.isHead()) {
                Queue<SmartAction> queue = new LinkedList<>();
                queue.add(smartAction);
                queue.addAll(mSmartDataPool);
                mSmartDataPool = queue;
            } else {
                mSmartDataPool.add(smartAction);
            }
            if (1 == mSmartDataPool.size()) {
                toSendCharacteristic(mSmartDataPool.element());
            }
        }
    }

    private synchronized void writeData() {
        if (mSmartDataPool.size() > 0) {
            toSendCharacteristic(mSmartDataPool.element());
        }
    }

    private void toSendCharacteristicOTA(SmartAction action) {
        if (null != action) {
            BluetoothGattCharacteristic characteristic =
                    mBluetoothGattServer.getCharacteristic(UUID.fromString(action.getCharacteristicUUID()));
            characteristic.setValue(action.getBytes());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mBluetoothGatt.writeCharacteristic(characteristic);
            bleWrite();
        }
    }

    private void toSendCharacteristic(SmartAction action) {
        if (null != action) {
            action.setSendCount(action.getResendCount() + 1);
            if (action.isImmediate()) {
                action = mSmartManager.getISmartActionOperator().reBuildSmartAction(action);
            }
            if (null == action || null == action.getCharacteristicUUID() || null == mBluetoothGattServer || null == action.getBytes()) {
                mHandlerImp.sendMessageDelayed(Message.obtain(mHandlerImp, WRITE_DATA), BleConfig.WRITE_TIME);
                return;
            }
            BluetoothGattCharacteristic characteristic =
                    mBluetoothGattServer.getCharacteristic(UUID.fromString(action.getCharacteristicUUID()));
            if (null == characteristic) {
                mHandlerImp.sendMessageDelayed(Message.obtain(mHandlerImp, WRITE_DATA), BleConfig.WRITE_TIME);
                return;
            }
            characteristic.setValue(action.getBytes());
            mBluetoothGatt.writeCharacteristic(characteristic);
            bleWrite();
            mHandlerImp.removeMessages(WRITE_DATA);
        }
        mHandlerImp.sendMessageDelayed(Message.obtain(mHandlerImp, WRITE_DATA), BleConfig.WRITE_TIME);
    }

    private void onConnectionStateChange(int status, BluetoothGatt gatt) {
        if (BluetoothProfile.STATE_CONNECTED == status) {
            onReplyStatus(IBleStatus.Status.CONNECTED);
            setDiscoverService(gatt);
        } else if (BluetoothProfile.STATE_DISCONNECTED == status) {
            onReplyStatus(IBleStatus.Status.DISCONNECTED);
        } else {
            onReplyStatus(IBleStatus.Status.DISCONNECTED);
            onReplyStatus(IBleStatus.Status.BLE_ERROR);
        }
    }

    private void setDiscoverService(final BluetoothGatt gatt) {

        mHandlerImp.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gatt.getDevice().getBondState() != BluetoothProfile.STATE_DISCONNECTED) {
                    if (gatt.discoverServices()) {
                        return;
                    }
                }
                onReplyStatus(IBleStatus.Status.DISCOVER_SERVICES_FAIL);
            }
        }, 600);
    }

    private void onServicesDiscovered(int status, BluetoothGatt gatt) {
        if (BluetoothGatt.GATT_SUCCESS == status) {
            if (null != mBluetoothGatt) {
                mBluetoothGattServer = mBluetoothGatt.getService(UUID.fromString(BleChannel.MAIN_SERVICE));
                initNotification(mBluetoothGattServer);
            }
            onReplyStatus(IBleStatus.Status.DISCOVER_SERVICES);
        } else {
            onReplyStatus(IBleStatus.Status.DISCOVER_SERVICES_FAIL);
        }
    }

    private void initNotification(final BluetoothGattService mBluetoothGattServer) {
        setNotification(mBluetoothGattServer, BleChannel.WRITE_CODE_A801, BleChannel.NOTIFY_DD, true);
        mHandlerImp.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotification(mBluetoothGattServer, BleChannel.WRITE_CODE_A802, BleChannel.NOTIFY_DD, true);
            }
        }, 1 * 200);
        mHandlerImp.postDelayed(new Runnable() {
            @Override
            public void run() {
                setNotification(mBluetoothGattServer, BleChannel.WRITE_CODE_A803, BleChannel.NOTIFY_DD, true);
            }
        }, 2 * 200);
    }

    public void setNotification(BluetoothGattService gattService, String characteristicUUID,
                                String descriptorUUID, boolean enable) {
        if (mBluetoothGatt != null && gattService != null) {
            BluetoothGattCharacteristic characteristic = gattService
                    .getCharacteristic(UUID.fromString(characteristicUUID));
            if (characteristic != null) {
                mBluetoothGatt.setCharacteristicNotification(characteristic, true);
                BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID.fromString(descriptorUUID));
                if (descriptor != null) {
                    descriptor.setValue(enable ? BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
                    mBluetoothGatt.writeDescriptor(descriptor);
                }
            }
        }
    }

    private void onCharacteristicWrite(BluetoothGattCharacteristic characteristic, int status) {
        if (BluetoothGatt.GATT_SUCCESS == status) {
            bleWriteSuccessfully(characteristic.getValue(), characteristic.getUuid().toString());
        } else {
            bleWriteError();
        }
    }

    private void onCharacteristicChanged(BluetoothGattCharacteristic characteristic) {
        bleWriteReply(characteristic.getValue(), characteristic.getUuid().toString());
    }

    private void onLeScan(BleDevice bleDevice) {
        if (null != getListenerInfo().mILeListener) {
            getListenerInfo().mILeListener.onLeScanNewDevice(bleDevice);
        }
    }

    private void onReplyStatus(IBleStatus.Status status) {
        if (null != getListenerInfo().mIBleStatuses) {
            for (IBleStatus iBleStatus : getListenerInfo().mIBleStatuses) {
                iBleStatus.onBleStatus(status);
            }
        }
    }

    private static class HandlerImp extends LooperHelper {

        private WeakReference<BleHelper> mBleHelper;

        public HandlerImp(Context context, BleHelper bleHelper) {
            super(context);
            mBleHelper = new WeakReference<>(bleHelper);
        }

        @Override
        public void handleMessage(Message msg) {
            final BleHelper bleHelper = mBleHelper.get();
            if (null == bleHelper)
                return;
            switch (msg.what) {
                case STATE_CHANGE:
                    bleHelper.onConnectionStateChange(msg.arg2, (BluetoothGatt) msg.obj);
                    break;
                case DISCOVER_SERVICE:
                    bleHelper.onServicesDiscovered(msg.arg2, (BluetoothGatt) msg.obj);
                    break;
                case CHARACTERISTIC_WRITE:
                    bleHelper.onCharacteristicWrite((BluetoothGattCharacteristic) msg.obj, msg.arg2);
                    break;
                case CHARACTERISTIC_CHANGE:
                    bleHelper.onCharacteristicChanged((BluetoothGattCharacteristic) msg.obj);
                    break;
                case FIND_NEW:
                    bleHelper.onLeScan((BleDevice) msg.obj);
                    break;
                case WRITE_DATA:
                    bleHelper.writeData();
                    break;
            }
        }
    }

    private final BroadcastReceiver mBlueToothReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON:
                            break;
                        case BluetoothAdapter.STATE_ON:
                            mSmartManager.setBluetoothOpen(true);
                            reConnectDevice();
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            mSmartManager.setBluetoothOpen(false);
                            break;
                    }
                    break;
            }
        }
    };

    public void clearPool() {
        if (null != mSmartDataPool) {
            mSmartDataPool.clear();
        }
    }

    public boolean isReconnect() {
        return isReconnect;
    }

    public void setReconnect(boolean reconnect) {
        isReconnect = reconnect;
    }

    public BluetoothGattService getBluetoothGattServer() {
        return mBluetoothGattServer;
    }
}
