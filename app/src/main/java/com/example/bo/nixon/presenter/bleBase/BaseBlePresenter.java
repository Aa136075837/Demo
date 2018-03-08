package com.example.bo.nixon.presenter.bleBase;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.bo.nixon.presenter.BasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.smart.smartble.SmartManager;
import com.smart.smartble.smartBle.BleService;

import java.lang.ref.WeakReference;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 15:58
 * @说明
 */
public abstract class BaseBlePresenter<V extends MvpView> extends BasePresenter<V> {

    private WeakReference<V> mWeakReference;
    private BleService.SmartBinder mSmartBinder;

    public V getView() {
        return mWeakReference == null ? null : mWeakReference.get();
    }

    public boolean isViewAttach() {
        return mWeakReference != null && mWeakReference.get() != null;
    }

    public void attachView(V view) {
        mWeakReference = new WeakReference<>(view);
        if (mWeakReference.get() instanceof Context) {
            bindManagerService((Context) mWeakReference.get());
        } else if (mWeakReference.get() instanceof Fragment) {
            Fragment fragment = (Fragment) mWeakReference.get();
            bindManagerService(fragment.getActivity());
        }
    }

    public void detachView() {
        if (mWeakReference != null) {
            if (mWeakReference.get() instanceof Context) {
                unbindManagerService((Context) mWeakReference.get());
            }  else if (mWeakReference.get() instanceof Fragment) {
                Fragment fragment = (Fragment) mWeakReference.get();
                unbindManagerService(fragment.getActivity());
            }
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    private boolean bindManagerService(Context context) {
        Intent intent = new Intent(context, BleService.class);
        return context.bindService(intent, mBleConnect,
                Context.BIND_AUTO_CREATE);
    }

    private void unbindManagerService(Context context) {
        context.unbindService(mBleConnect);
    }

    private ServiceConnection mBleConnect = new ServiceConnection() {
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
