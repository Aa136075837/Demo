package com.example.bo.nixon.presenter;

import android.content.ComponentName;
import android.content.Context;

import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.utils.CacheUtils;
import com.smart.connectComponent.ConnectComponent;
import com.smart.smartble.SmartManager;

/**
 * @author bo.
 * @Date 2017/6/19.
 * @desc
 */

public interface DeviceInfoContract {
    interface DeviceInfoNixonView extends BaseNixonView {
        void disConnected ();
        void showToast ();
    }

    class DeviceInfoPresenter extends BaseBlePresenter<DeviceInfoNixonView> {

        private ConnectComponent mConnectComponent;
        public void deleteFile () {
            getView ().showToast ();
        }

        public void clearCache(Context context){
            CacheUtils.getInstance (context).clearCache ();
        }

        @Override protected void serviceConnected (SmartManager smartManager) {
            mConnectComponent = ConnectComponent.getInstance(smartManager);
        }

        @Override protected void serviceDisconnected (ComponentName name) {

        }

        public void disConnectDevice () {
            mConnectComponent.disConnectDevice ();
            getView ().disConnected ();
        }

        @Override
        public void detachView() {
            super.detachView();
            if (null != mConnectComponent) {
                mConnectComponent.unRegisterComponent();
            }
        }
    }
}
