package com.example.bo.nixon.presenter;

import android.content.ComponentName;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.bean.DbWatchBean;
import com.example.bo.nixon.bean.NetSportStepBean;
import com.example.bo.nixon.bean.UploadStepsBean;
import com.example.bo.nixon.db.DbManager;
import com.example.bo.nixon.db.DbWatchBeanCompat;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.presenter.ota.IOtaPresenter;
import com.example.bo.nixon.ui.activity.main.IMain;
import com.example.bo.nixon.ui.fragment.alarm.AlarmCacheHelper;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.version.DownLoaderTask;
import com.example.bo.nixon.version.FileUtil;
import com.example.bo.nixon.version.VersionControl;
import com.example.bo.nixon.version.ZIP;
import com.google.gson.Gson;
import com.smart.alarmcomponent.AlarmComponent;
import com.smart.alarmcomponent.IAlarm;
import com.smart.attributescomponent.AttributesComponent;
import com.smart.attributescomponent.listener.IPower;
import com.smart.attributescomponent.listener.IVersion;
import com.smart.connectComponent.ConnectComponent;
import com.smart.connectComponent.IAuthorization;
import com.smart.dataComponent.DataComponent;
import com.smart.dataComponent.DataStyle;
import com.smart.dataComponent.WatchBean;
import com.smart.dataComponent.listener.IRequestData;
import com.smart.dataComponent.listener.ITarget;
import com.smart.dataComponent.listener.IUpdateSumStep;
import com.smart.otacomponent.OTAComponent;
import com.smart.otacomponent.listener.IOTAListener;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.SmartManager;
import com.smart.smartble.utils.TimeZoneUtil;
import com.smart.timecomponent.ISecondTime;
import com.smart.timecomponent.TimeComponent;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public interface MainContract {

    interface MainNixonView extends BaseNixonView {

        void updateComplete(int step);

        void electricity(int electricity);

        void stepsDay(NetSportStepBean bean);

        void stepsWeek(NetSportStepBean bean);

        void stepsMonth(NetSportStepBean bean);

        void onComplete(boolean isComplete);

        void secondDate(Date date, int zone);

        void showVersion(int main, int minor, int test);

        void target(int target);
    }

    class MainPresenter extends BaseBlePresenter<MainNixonView> implements IUpdateSumStep, IPower, IMain, IRequestData,
            VersionControl.VersionListener, DownLoaderTask.LoaderResultListener, IOtaPresenter, IOTAListener,
            IVersion, ISecondTime, ITarget, IAuthorization ,IAlarm{

        private static final String TAG = "MainPresenter";
        private TimeComponent mTimeComponent;
        private DataComponent mDataComponent;
        private AttributesComponent mAttributesComponent;
        private AlarmComponent mAlarmComponent;
        private Handler mHandler = new Handler(Looper.getMainLooper());
        private Gson gson = new Gson();
        private List<DbWatchBean> dbWatchBeens;
        private Map<String, String> map = new HashMap<> ();
        private String deviceUUID;
        private String cuntomerId = SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID);
        private final int UPLOAD_WHAT = 777;
        private final int GET_DAY_STEP = 12123;
        private final int GET_WEEK_STEP = 12124;
        private final int GET_MONTH_STEP = 12125;
        private VersionControl mVersionControl;
        private DownLoaderTask mDownLoaderTask;
        private static final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/OTA/";
        private OTAComponent mOtaComponent;
        private ConnectComponent mConnectComponent;
        private Timer mTimer;

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mTimeComponent = new TimeComponent(smartManager);
            mTimeComponent.registerComponent();
            mTimeComponent.addSecondTimeListener(this);

            mDataComponent = new DataComponent(smartManager);
            mDataComponent.registerComponent();
            mDataComponent.addSumStepListener(this);
            mDataComponent.addRequestDataListener(this);
            mDataComponent.addTargetListener(this);
            mDataComponent.requestSumStep();

            mAlarmComponent = AlarmComponent.getInstance(smartManager);
            mAlarmComponent.registerComponent();
            mAlarmComponent.addAlarmListener(this);
            mAttributesComponent = AttributesComponent.getInstance(smartManager);
            mAttributesComponent.registerComponent();
            mAttributesComponent.addPowerListener(this);
            mAttributesComponent.addVersionListener(this);
            mAttributesComponent.getVersion();
            mConnectComponent = ConnectComponent.getInstance(smartManager);
            mConnectComponent.registerComponent();
            mConnectComponent.addAuthorzationListener(this);

            mOtaComponent =  OTAComponent.getInstance(smartManager);
            mOtaComponent.registerComponent();
            mOtaComponent.addOTAListener(this);
            startTimer();
        }

        private void startTimer() {
            if (null != mTimer)
                mTimer.cancel();
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    uploadAllData();
                }
            }, 0, 5 * 60 * 1000);
        }

        private void destroyTimer() {
            if (null != mTimer)
                mTimer.cancel();
        }

        @Override
        public void attachView(MainNixonView view) {
            super.attachView(view);
            Log.e("DownLoaderTask", " Main  attachView");
            mVersionControl = new VersionControl();
            mVersionControl.setVersionListener(this);
        }

        private void setAlarm() {
            List<AlarmEventBean> eventBeen = AlarmCacheHelper.getAlarmList(NixonApplication.getContext());
            for (int i = 0; i < eventBeen.size(); i++) {
                AlarmEventBean alarmEventBean = eventBeen.get(i);
                int open = alarmEventBean.isOpenType() ? 1 : 0;
                if (0 != (alarmEventBean.getRepeatTime() & 128)) {
                    alarmEventBean.setRepeatTime(alarmEventBean.getRepeatTime() | 128);
                }
                mAlarmComponent.setAlarm(i + 1, alarmEventBean.getAlarmTime(), open, alarmEventBean.getRepeatTime());
            }
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }

        public void sendSecondTime(Date date, int arg1, int arg2) {
            if (null != mTimeComponent) mTimeComponent.setSecondCity(date, arg1, arg2);
        }

        public void getStepValue() {
            if (null != mDataComponent) mDataComponent.requestSumStep();
        }

        @Override
        public void detachView() {
            if (null != mDataComponent) {
                mDataComponent.removeSumStepListener(this);
                mDataComponent.removeTargetListener(this);
                mDataComponent.removeSumStepListener(this);
                mDataComponent.unRegisterComponent();
            }
            if (null != mAttributesComponent) {
                mAttributesComponent.unRegisterComponent();
                mAttributesComponent.removePowerListener(this);
            }
            if (null != mOtaComponent) {
                mOtaComponent.unRegisterComponent();
            }
            if (null != mTimeComponent) {
                mTimeComponent.removeSecondTimeListener(this);
                mTimeComponent.unRegisterComponent();
            }

            if (null != mConnectComponent) {
                mConnectComponent.unRegisterComponent();
                mConnectComponent.removeAuthorzationListener(this);
            }
            if (null != mAlarmComponent) {
                mAlarmComponent.removeAlarmListener(this);
                mAlarmComponent.unRegisterComponent();
            }
            destroyTimer();
            super.detachView();
        }


        @Override
        public void sumStep(int step) {
            if (null != getView()) getView().updateComplete(step);
        }

        @Override
        public void electricity(int value) {
            if (null != getView()) {
                getView().electricity(value);
            }
        }

        @Override
        public void getElectricity() {
            if (null != mAttributesComponent) {
                mAttributesComponent.getElectricity();
            }
        }

        @Override
        public void onResume() {
            if (null != mDataComponent) {
                mDataComponent.requestSumStep();
            }
            if (null!= mOtaComponent){
                mOtaComponent.addOTAListener (this);
            }
        }

        @Override
        public void onPause() {
            if (null != mDataComponent) {
                mDataComponent.pauseSumStep();
            }
            if (null!= mOtaComponent){
                mOtaComponent.removeOTAListener (this);
            }
        }

        @Override
        public void setTargetValue(int target) {
            if (null != mDataComponent) {
                mDataComponent.setSportTarget(target);
            }
        }

        @Override
        public void getSecondTime() {
            if (null != mTimeComponent) {
                mTimeComponent.getSecondCity();
            }
        }

        @Override
        public void uploadAllData() {
            if (null != mDataComponent) {
                mDataComponent.requestContent(DataStyle.STEP);
            }
        }

        @Override
        public void startRequest() {

        }

        @Override
        public void requestProgress(int max, int progress) {

        }

        @Override
        public void requestComplete(List<WatchBean> watchBeen) {
            for (int i = 0; i < watchBeen.size(); i++) {
                Log.w("DataComponent", "requestComplete::" + watchBeen.size() + watchBeen.get(i).toString());
            }
            Log.e("UPDATELISTSTEPS", "  获取手表数据 成功");
            if (watchBeen == null || watchBeen.size() == 0) {
                return;
            }
            deviceUUID = DeviceMessage.getInstance ().getDeviceUUID ();
            dbWatchBeens = DbWatchBeanCompat.watchBeenToDbWatchBean(watchBeen, cuntomerId, deviceUUID);
            Log.e("UPDATELISTSTEPS", "  开始上传步数 ");
            HttpUtils.getInstance().autoLogin();
            updateListSteps(dbWatchBeens);
            //updateSportData(10000 + "", cuntomerId, deviceUUID, 1483416000000l + "");
        }

        @Override
        public void requestError(int errorCode, String errorMsg) {

        }

        private void updateListSteps (List<DbWatchBean> watchBeens) {
            List<Map<String, String>> parmas = new ArrayList<> ();
            Log.e ("UPDATELISTSTEPS", "  watchBeens.size () = " + watchBeens.size ());
            for (int i = 0; i < watchBeens.size (); i++) {
                Map<String, String> element = new HashMap<> ();
                DbWatchBean bean = watchBeens.get (i);
                element.put ("customerId", bean.getUserId ());
                element.put ("timestamp", bean.getTime () + "");
                element.put ("step", bean.getValue () + "");
                element.put ("deviceId", deviceUUID);
                Log.d ("bigstep", "  时间  ：： " + bean.getTime () + "  步数 ：： " + bean.getValue ());
                parmas.add (element);
            }

            HttpUtils.getInstance()
                    .requestCookieJsonArrayPost(ConstantURL.UPDATE_LIST_STEPS, 121212, parmas,
                            new SimpleResponseListener<JSONObject>() {
                                @Override
                                public void onSucceed(int what, Response<JSONObject> response) {
                                    super.onSucceed(what, response);
                                    NetSportStepBean bean = gson.fromJson(response.get().toString(), NetSportStepBean.class);
                                    if (RequestCode.SUCCESS.equals(bean.getCode())) {
                                        Log.e("UPDATELISTSTEPS", "  上传步数集合 成功");
                                    } else if (RequestCode.NOT_LOGIN_STATE.equals(bean.getCode())) {
                                        Log.e("UPDATELISTSTEPS", "  未登录状态 ");
                                    }
                                }

                                @Override
                                public void onFailed(int what, Response<JSONObject> response) {
                                    super.onFailed(what, response);
                                    Log.e("UPDATELISTSTEPS", "  上传步数集合 失败 " + what);
                                    if (null != dbWatchBeens) {
                                        DbManager.insertDbWatchBean(dbWatchBeens);
                                    }
                                }
                            });
        }

        private void updateSportData(String step, String customerID, String deviceID, String time) {

            map.put("customerId", customerID);
            map.put("timestamp", time);
            map.put("step", step);
            map.put("deviceId", deviceID);
            HttpUtils.getInstance().requestJsonObjectPost(ConstantURL.UPDATE_STEPS, UPLOAD_WHAT, map, mListener);
        }

        public void getStepsByDay () {
            deviceUUID = DeviceMessage.getInstance ().getDeviceUUID ();
            Map<String, String> map = new HashMap<> ();
            Date date = new Date ();
            map.put ("year", date.getYear () + 1900 + "");
            map.put ("month", date.getMonth () + 1 + "");
            map.put ("day", date.getDate () + "");
            map.put ("count", 30 + "");
            map.put ("queryTimeOffSet", TimeZoneUtil.getCurrentTimeZone (Calendar.getInstance ().getTimeZone ()));
            map.put ("deviceId", deviceUUID);
            map.put ("customerId", cuntomerId);
            HttpUtils.getInstance ()
                .requestCookieJsonObjectPost (ConstantURL.GET_STEP_BY_DAY, GET_DAY_STEP, map, mSimpleResponseListener);
        }

        public void getStepsByWeek() {
            Map<String, String> map = new HashMap<>();
            Date date = new Date();
            int weekYear = Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
            Log.e("NIXONLOGIN", "...weekYear.." + weekYear);
            map.put("year", date.getYear() + 1900 + "");
            map.put("week", weekYear + "");
            map.put("count", 16 + "");
            map.put("queryTimeOffSet", TimeZoneUtil.getCurrentTimeZone(Calendar.getInstance().getTimeZone()));
            map.put("deviceId", deviceUUID);
            map.put("customerId", cuntomerId);
            HttpUtils.getInstance()
                    .requestCookieJsonObjectPost(ConstantURL.GET_STEP_BY_WEEK, GET_WEEK_STEP, map,
                            mSimpleResponseListener);
        }

        public void getStepsByMonth () {
            Map<String, String> map = new HashMap<> ();
            Date date = new Date ();
            map.put ("year", date.getYear () + 1900 + "");
            map.put ("month", date.getMonth () + 1 + "");
            map.put ("count", 28 + "");
            map.put ("queryTimeOffSet", TimeZoneUtil.getCurrentTimeZone (Calendar.getInstance ().getTimeZone ()));
            map.put ("deviceId", deviceUUID);
            map.put ("customerId", cuntomerId);
            HttpUtils.getInstance ()
                .requestCookieJsonObjectPost (ConstantURL.GET_STEP_BY_MONTHS, GET_MONTH_STEP, map,
                    mSimpleResponseListener);
        }

        SimpleResponseListener mSimpleResponseListener = new SimpleResponseListener<JSONObject> () {
            @Override public void onSucceed (int what, Response<JSONObject> response) {
                super.onSucceed (what, response);
                NetSportStepBean bean = gson.fromJson (response.get ().toString (), NetSportStepBean.class);
                //Log.e ("NIXONLOGIN", "    " + bean.getInfo () + " value = " + bean.getObject ().toString ());
                switch (what) {
                    case GET_DAY_STEP:
                        switch (bean.getCode()) {
                            case RequestCode.SUCCESS:
                                if (getView() != null) {
                                    getView().stepsDay(bean);
                                }
                                getStepsByWeek();
                                break;
                            default:
                                break;
                        }
                        break;
                    case GET_WEEK_STEP:
                        switch (bean.getCode()) {
                            case RequestCode.SUCCESS:
                                if (getView() != null) {
                                    getView().stepsWeek(bean);
                                }
                                getStepsByMonth();
                                break;
                            default:
                                break;
                        }
                        break;
                    case GET_MONTH_STEP:
                        switch (bean.getCode()) {
                            case RequestCode.SUCCESS:
                                if (getView() != null) {
                                    getView().stepsMonth(bean);
                                }
                                break;
                            default:
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                super.onFailed(what, response);
                Log.e("NIXONLOGIN", " 获取步数 失败 ");
                if (response != null){
                    Log.e ("NIXONLOGIN"," response  =  " + response.getException ());
                }
            }
        };

        OnResponseListener mListener = new OnResponseListener<JSONObject>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<JSONObject> response) {
                Log.e("NIXONLOGIN", " 成功" + response.get().toString());
                UploadStepsBean bean = gson.fromJson(response.get().toString(), UploadStepsBean.class);
                switch (bean.getCode()) {
                    case RequestCode.SUCCESS:
                        Log.e("NIXONLOGIN", "运动数据上传成功");
                        break;
                    case RequestCode.NOT_LOGIN_STATE:
                        HttpUtils.getInstance().autoLogin(ConstantURL.UPDATE_STEPS, UPLOAD_WHAT, map, mListener);
                        break;
                    case RequestCode.LACK_USERINFO:
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response<JSONObject> response) {
                Log.e("NIXONLOGIN", " 运动数据上传失败 " + response.get().toString());
                if (null != dbWatchBeens) {
                    DbManager.insertDbWatchBean(dbWatchBeens);
                }
            }

            @Override
            public void onFinish(int what) {
            }
        };

        public void downLoad(String url) {
            Log.w("DownLoaderTask", "run--------->1" + url);
            if (TextUtils.isEmpty(url))
                return;
            Log.w("DownLoaderTask", "run--------->2" + url);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OTA/");
            FileUtil.DeleteFile(file);
            mDownLoaderTask = new DownLoaderTask(url, PATH);
            mDownLoaderTask.setLoaderResultListener(this);
            mDownLoaderTask.execute();
        }

        public void checkoutVersion(String version) {
            if (null != mVersionControl) {
                Log.e("DownLoaderTask", " 检测版本");
                mVersionControl.checkVersion(version, "P7003");
            }
        }

        @Override
        public void onSuccessful(String url, String message) {
            Log.w("DownLoaderTask", "开始下载新版本 ");
            downLoad(url);
        }

        @Override
        public void onNoVersion() {

        }

        @Override
        public void onVersionProgress(long max, long progress) {

        }

        @Override
        public void onVersionResult(boolean complete, String path) {
            Log.w("DownLoaderTask", "下载成功");
            File file = new File(path);
            if (null != file)
                if (file.getName().endsWith("zip")) {
                    try {
                        ZIP.UnZipFolder(file.getAbsolutePath(), file.getParent(), new ZIP.ZipResult() {
                            @Override
                            public void complete(String path) {
                                startOTA(path);
                                Log.w("DownLoaderTask", "complete::" + path);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.w("DownLoaderTask", "Exception" + e.toString());
                    }
                }
        }

        public void confirmUpdateOta() {
            mOtaComponent.delayToSendMcu();
        }

        @Override
        public void startOTA(String path) {
            if (!TextUtils.isEmpty(path))
                mOtaComponent.startPath(path);
        }

        @Override
        public void dealFile() {
            Log.w(TAG, "dealFile::");
        }

        @Override
        public void otaStart() {
            Log.w(TAG, "otaStart::");
            //if (null != getView())
            //    getView().onBegin();
        }

        @Override
        public void onProgress(int max, int progress) {
            Log.w("OTAComponent", "onProgress::" + max + "   or   " + progress);
            //if (null != getView())
            //    getView().onProgress(max, progress);
        }


        @Override
        public void onComplete(int complete) {
            Log.w(TAG, "onComplete::");
            if (0 == complete) {
                ToastManager.show(NixonApplication.getContext(), "升级成功", Toast.LENGTH_SHORT);
            } else if (5 == complete) {
                ToastManager.show(NixonApplication.getContext(), "升级版本与当前版本一致请更新", Toast.LENGTH_SHORT);
            } else {
                ToastManager.show(NixonApplication.getContext(), "升级中断", Toast.LENGTH_SHORT);
            }
            if (null != getView())
                getView().onComplete(0 == complete);
        }

        @Override
        public void onFail() {
            Log.w(TAG, "onFail::");
        }

        @Override
        public void secondTime(Date date, int high, int low) {
            if (null != getView()) {
                getView().secondDate(date, high);
            }
        }

        @Override
        public void version(int main, int minor, int test) {
            Log.e("DownLoaderTask", " xinbanben  ");
            if (null != getView()) {
                getView().showVersion(main, minor, test);
            }
        }

        @Override
        public void target(int target) {
            SPUtils.putString(NixonApplication.getContext(), Constant.GOAL_KEY, String.valueOf(target));
            if (null != getView()) {
                getView().target(target);
            }
        }

        @Override
        public void authorization(boolean author) {
            if (author) {
                uploadAllData();
            }
        }

        @Override
        public void AuthorizationTimeOut() {

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
        }
        public void sendCurrentZoneTime() {
            boolean autoTime = SPUtils.getBoolean(NixonApplication.getContext(), Constant.IS_AUTO_TIME_KEY);
            if (autoTime) {
                if(null != mTimeComponent){
                    mTimeComponent.setMcuTime(1, new Date());
                }
            }
        }
    }
}
