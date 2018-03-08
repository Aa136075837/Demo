package com.example.bo.nixon.presenter;

import android.content.ComponentName;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.ui.fragment.alarm.AlarmCacheHelper;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.alarmcomponent.AlarmComponent;
import com.smart.alarmcomponent.IAlarm;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.SmartManager;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public interface AlarmFragmentContract {

    interface AlarmView extends BaseNixonView {
        void upLoadAlarm();
    }

    class AlarmPresenter extends BaseBlePresenter<AlarmView> implements IAlarm {

        private static final String TAG = "AlarmPresenter";

        private AlarmComponent mAlarmComponent;
        private final int ADD_AlARM = 0x001;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mAlarmComponent = AlarmComponent.getInstance(smartManager);
            mAlarmComponent.registerComponent();
            mAlarmComponent.addAlarmListener(this);
            mAlarmComponent.getAlarm(0);
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        @Override
        public void detachView() {
            Log.w("AlarmComponent", "detachView::");
            super.detachView();
            if (null != mAlarmComponent) {
                setAlarm();
                mAlarmComponent.removeAlarmListener(this);
            }
        }

        private void setAlarm() {
            List<AlarmEventBean> eventBeen = AlarmCacheHelper.getAlarmList(NixonApplication.getContext());
            for (int i = 0; i < eventBeen.size(); i++) {
                AlarmEventBean alarmEventBean = eventBeen.get(i);
                int open = alarmEventBean.isOpenType() ? 1 : 0;
                if ((alarmEventBean.getRepeatTime() & 127) == 0) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(new Date());
                    int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    alarmEventBean.setRepeatTime(1 << week);
                } else {
                    alarmEventBean.setRepeatTime(alarmEventBean.getRepeatTime() | 128);
                }
                mAlarmComponent.setAlarm(i + 1, alarmEventBean.getAlarmTime(), open, alarmEventBean.getRepeatTime());
            }
            if (5 - eventBeen.size() > 0) {
                int count = 5 - eventBeen.size();
                for (int i = 0; i < count; i++) {
                    AlarmEventBean alarmEventBean = AlarmCacheHelper.getEmptyEventBean();
                    mAlarmComponent.setAlarm(i + eventBeen.size() + 1, alarmEventBean.getAlarmTime(), 0, alarmEventBean.getRepeatTime());
                }
            }
            SPUtils.putInt(NixonApplication.getContext(), DeviceMessage.getInstance().getDeviceUUID(), eventBeen.size());
        }

        @Override
        public void alarm(int index, int time, int open, int repeat) {
            if (repeat == 0)
                return;
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
            Log.w("BleService","run------>" + alarmEventBean.toString());
            getView().upLoadAlarm();
        }

        OnResponseListener mListener = new OnResponseListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response response) {
                switch (what) {
                    case ADD_AlARM:

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

        public void upLoadRequest(AlarmEventBean bean) {
            //HttpUtils.getInstance ().requestCookieJsonObjectPost (ConstantURL.ADD_ALARM, ADD_AlARM, map, mListener);
        }

        private Map<String, String> getMap(AlarmEventBean bean) {
            int time = bean.getAlarmTime();
            bean.getRepeatTime();
            bean.getmAction();
            bean.isOpenType();
            Map<String, String> map = new HashMap<>();

            return map;
        }
    }
}
