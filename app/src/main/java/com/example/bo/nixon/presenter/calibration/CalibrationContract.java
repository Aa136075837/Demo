package com.example.bo.nixon.presenter.calibration;

import android.content.ComponentName;
import android.util.Log;

import com.example.bo.nixon.presenter.BaseNixonView;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.smart.smartble.SmartManager;
import com.smart.timecomponent.TimeComponent;

import java.util.Date;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public interface CalibrationContract {

    interface CalibrationNixonView extends BaseNixonView {

    }

    class CalibrationPresenter extends BaseBlePresenter<CalibrationNixonView> implements ICalibration {

        private TimeComponent mTimeComponent;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mTimeComponent = new TimeComponent(smartManager);
            Log.w("BleService", "run------------>" + smartManager.isDiscovery());
            mTimeComponent.registerComponent();
            inCalibration();
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        @Override
        public void detachView() {
            super.detachView();
            if (null != mTimeComponent) {
                mTimeComponent.unRegisterComponent();
                mTimeComponent.stopTime();
            }
        }

        @Override
        public void inCalibration() {
            if (null != mTimeComponent) {
                mTimeComponent.startTime(10 * 1000);
            }

        }

        @Override
        public void outCalibration() {
            if (null != mTimeComponent)
                mTimeComponent.stopTime();
        }

        @Override
        public void sendTime(int position, int value, Date date) {
            if (null != mTimeComponent) {
                mTimeComponent.setPosition(position, value);
                mTimeComponent.setMcuTime(position, date);
            }
        }

        @Override
        public void intoSmall(int position, int value) {
            if (null != mTimeComponent) {
                mTimeComponent.intoSmallClock(position, value);
            }
        }

    }
}
