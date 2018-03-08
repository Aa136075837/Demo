package com.example.bo.nixon.presenter;

import android.content.ComponentName;

import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.smart.attributescomponent.AttributesComponent;
import com.smart.smartble.SmartManager;

/**
 * @author bo.
 * @Date 2017/6/20.
 * @desc
 */

public interface DeviceDetailContract {

    interface DeviceDetailNixonView extends BaseNixonView {

    }

    class DeviceDetailPresenter extends BaseBlePresenter<DeviceDetailNixonView> {

        AttributesComponent mAttributesComponent;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mAttributesComponent = AttributesComponent.getInstance(smartManager);
            //  mAttributesComponent.registerComponent();
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        public void changeBleName(String name) {
            if (null != mAttributesComponent) {
                mAttributesComponent.changeBleName(name);
            }
        }

        @Override
        public void detachView() {
            super.detachView();
           /* if (null != mAttributesComponent) {
                mAttributesComponent.unRegisterComponent();
            } */
        }
    }
}
