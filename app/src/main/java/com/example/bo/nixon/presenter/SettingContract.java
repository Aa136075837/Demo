package com.example.bo.nixon.presenter;

import android.content.ComponentName;

import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.smart.notifycomponent.NotifyComponent;
import com.smart.smartble.SmartManager;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 14:58
 * @说明
 */
public interface SettingContract {

    interface SettingNixonView extends BaseNixonView {

    }

    class SettingPresenter extends BaseBlePresenter<SettingNixonView> {

        private NotifyComponent mNotifyComponent;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mNotifyComponent = new NotifyComponent(smartManager);
            mNotifyComponent.registerComponent();
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        @Override
        public void detachView() {
            super.detachView();
            if (null != mNotifyComponent)
                mNotifyComponent.unRegisterComponent();
        }
    }

}
