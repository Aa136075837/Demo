package com.example.bo.nixon.presenter;

import android.content.ComponentName;

import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.smart.smartble.SmartManager;
import com.smart.timecomponent.ISecondTime;
import com.smart.timecomponent.TimeComponent;

import java.util.Date;

/**
 * @author bo.
 * @Date 2017/8/14.
 * @desc
 */

public interface TimeZoneContract {
    interface TimeZoneNixonView extends BaseNixonView {

    }

    class TimeZonePresenter extends BaseBlePresenter<TimeZoneNixonView> implements ISecondTime {

        private TimeComponent mTimeComponent;

        @Override
        protected void serviceConnected (SmartManager smartManager) {
            mTimeComponent = new TimeComponent (smartManager);
            mTimeComponent.registerComponent();
            mTimeComponent.addSecondTimeListener(this);
        }

        @Override
        protected void serviceDisconnected (ComponentName name) {

        }

        public void sendSecondTime (Date date, int i, int i1) {
            if (null != mTimeComponent) mTimeComponent.setSecondCity(date, i, i1);
        }

        @Override
        public void secondTime (Date date, int high, int low) {

        }

        @Override
        public void detachView () {
            super.detachView ();
        }
    }
}
