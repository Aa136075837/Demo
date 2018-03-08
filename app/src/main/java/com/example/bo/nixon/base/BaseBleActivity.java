package com.example.bo.nixon.base;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;

import com.smart.smartble.SmartManager;
import com.smart.smartble.smartBle.BleService;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 15:28
 * @说明
 */
public abstract class BaseBleActivity extends BaseActivity {

    private BleService.SmartBinder mSmartBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindManagerService(this);
    }

    private boolean bindManagerService(Context context) {
        Intent intent = new Intent(this, BleService.class);
        return context.bindService(intent, bleConnection,
                Context.BIND_AUTO_CREATE);
    }

    public void unbindManagerService() {
        unbindService(bleConnection);
    }

    private ServiceConnection bleConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mSmartBinder = (BleService.SmartBinder) service;
            if (null != mSmartBinder) {
                serviceConnected(mSmartBinder.getSmartManager());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceDisconnected(name);
        }
    };

    protected abstract void serviceConnected(SmartManager smartManager);

    protected abstract void serviceDisconnected(ComponentName name);

}
