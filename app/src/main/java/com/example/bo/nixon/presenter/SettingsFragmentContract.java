package com.example.bo.nixon.presenter;

import android.content.ComponentName;

import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.smart.notifycomponent.NotifyComponent;
import com.smart.notifycomponent.listener.IDisconnectNotify;
import com.smart.smartble.SmartManager;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public interface SettingsFragmentContract {

    interface SettingsView extends BaseNixonView {
        void disconnectNotifySetting(boolean notify);
    }

    class SettingsPresenter extends BaseBlePresenter<SettingsView> implements IDisconnectNotify {

        private NotifyComponent mNotifyComponent;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mNotifyComponent = new NotifyComponent(smartManager);
            mNotifyComponent.registerComponent();
            mNotifyComponent.addDisconnectNotify(this);
            mNotifyComponent.getDisconnectNotifySetting();
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        public void setDisconnectNotify(boolean b) {
            if (null != mNotifyComponent) {
                mNotifyComponent.setDisconnectNotify(b);
            }
        }

        @Override
        public void detachView() {
            super.detachView();
            if (null != mNotifyComponent)
                mNotifyComponent.unRegisterComponent();
                mNotifyComponent.removeDisconnectNotify(this);
        }

        @Override
        public void disconnectNotify(boolean notify) {
            if (null != getView()) {
                getView().disconnectNotifySetting(notify);
            }
        }
    }

}
