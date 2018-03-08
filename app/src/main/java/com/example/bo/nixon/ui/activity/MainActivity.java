package com.example.bo.nixon.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.DbWatchBean;
import com.example.bo.nixon.bean.NetSportStepBean;
import com.example.bo.nixon.db.DbManager;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.MainContract;
import com.example.bo.nixon.service.ReBindNotifyService;
import com.example.bo.nixon.ui.fragment.UnitFragment;
import com.example.bo.nixon.ui.view.SmartToolbar;
import com.example.bo.nixon.ui.view.SmartZoneView;
import com.example.bo.nixon.ui.view.smartGraph.SmartBaseView;
import com.example.bo.nixon.ui.view.smartGraph.SmartGraphView;
import com.example.bo.nixon.ui.view.smartGraph.SmartPoint;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.MonthUtil;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.NotifyPermission;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.SimpleDateUtils;
import com.example.bo.nixon.utils.StepUtil;
import com.example.bo.nixon.utils.StringUtils;
import com.example.bo.nixon.utils.TimeZoneAbbreUtils;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.smart.smartble.DeviceMessage;
import com.smart.smartble.PermissionsUtils;
import com.smart.smartble.SmartManager;
import com.smart.smartble.utils.TimeZoneUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseMvpActivity<MainContract.MainPresenter> implements
        MainContract.MainNixonView, View.OnClickListener, SmartBaseView.IEditSelect,
        SmartGraphView.OnEventActionListener, SmartGraphView.OnSmartXChangeListener {

    private static final String TAG = "MainActivity";
    @BindView (R.id.act_main_current_zone)
    TextView mActMainCurrentZone;
    @BindView (R.id.act_main_second_zone)
    TextView mActMainSecondZone;
    @BindView (R.id.main_middle_step_unit)
    TextView mMainMiddleStepUnit;
    @BindView (R.id.main_middle_distance_unit)
    TextView mMainMiddleDistanceUnit;
    @BindView (R.id.main_middle_cal_unit)
    TextView mMainMiddleCalUnit;

    private long mExitTime;
    private static final int REQUEST_CODE = 0x0001;
    private final String STSRT_CODE = "STSRT_CODE";

    @BindView (R.id.toolbar)
    SmartToolbar mMainToolbar;
    @BindView (R.id.main_top_last_zone_view)
    SmartZoneView mLastSmartZoneView;
    @BindView (R.id.main_top_current_zone_view)
    SmartZoneView mCurrentSmartZoneView;
    @BindView (R.id.tab_bottom_left)
    ImageView mLeftImg;
    @BindView (R.id.tab_bottom_center)
    ImageView mCenterImg;
    @BindView (R.id.tab_bottom_right)
    ImageView mRightImg;
    @BindView (R.id.main_top_layout)
    View mTopLayout;
    @BindView (R.id.main_top_zone_layout)
    View mZoneLayout;
    @BindView (R.id.main_top_last_tv)
    TextView mLastTimeTv;
    @BindView (R.id.main_top_current_tv)
    TextView mCurrentTimeTv;
    @BindView (R.id.main_middle_step_tv)
    TextView mStepTv;
    @BindView (R.id.main_middle_distance_tv)
    TextView mDistanceTv;
    @BindView (R.id.main_middle_cal_tv)
    TextView mCalTv;
    @BindView (R.id.main_tag_message_tv)
    TextView mMessageTv;
    @BindView (R.id.main_tag_call_tv)
    TextView mCallTv;
    @BindView (R.id.main_tag_battery_img)
    ImageView mElectricityImg;
    @BindView (R.id.main_tag_battery_show_tv)
    TextView mElectricityTv;
    @BindView (R.id.main_bottom_smart_graphView)
    SmartGraphView mSmartGraphView;
    @BindView (R.id.main_tag_goal_tv)
    TextView mMainTagGoalTv;
    private String mSecondZone;
    private MainReceiver mRecevier;
    private ImageView mMImg;
    private boolean mIsKm;

    private List<SmartPoint> mDaySmartPoint = new ArrayList<> ();
    private List<SmartPoint> mWeekSmartPoint = new ArrayList<> ();
    private List<SmartPoint> mMonthSmartPoint = new ArrayList<> ();
    private String mVersion;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        ButterKnife.bind (this);
        initTab ();
        //AppManager.getAppManager ().addActivity (this);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_main));
        initListeners ();
        initView ();
        initToolbar ();
    }

    private void initView () {
        mCurrentSmartZoneView.setImageRescource (R.drawable.icon_clock_black);
        mLastSmartZoneView.setImageRescource (R.drawable.icon_clock_white);
        mSecondZone = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_KEY, "");
        String zoneName = TimeZoneAbbreUtils.getInstance ().getTineZoneName (mSecondZone);
        String sy = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_NAME_KEY, "---");
        String secondFullName = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_FUll_NAME_KEY,zoneName);
        mLastSmartZoneView.setText (secondFullName);
        mActMainSecondZone.setText (sy);
        if ("".equals (mSecondZone)) {
            mLastSmartZoneView.setText ("---");
            mLastTimeTv.setText ("---");
        } else {
            Date date = TimeZoneUtil.getTimeFromThereZone (mSecondZone);
            date = adaptationDst (date);
            initTimeZoneChange (mLastSmartZoneView, date, mLastTimeTv);
        }
        initTimeZoneChange (mCurrentSmartZoneView, new Date (), mCurrentTimeTv);

        String target = SPUtils.getString (NixonApplication.getContext (), Constant.GOAL_KEY);
        if (target != null) {
            mSmartGraphView.setTargetValue (Integer.valueOf (target) - 500); //之前设置的数据 -500
        }
    }

    /**
     * 适配夏令时时间
     *
     * @param date
     * @return
     */
    private Date adaptationDst (Date date) {
        String dst = SPUtils.getString (this, Constant.DST_KEY, "0.0");
        if (dst.length () != 0) {
            String[] split = dst.split ("\\.");
            int dstHour = date.getHours () + Integer.parseInt (split[0]);
            int dstMin = date.getMinutes () + (Integer.parseInt (split[1]) == 5 ? 30 : 0);
            date.setHours (dstHour);
            date.setMinutes (dstMin);
        }
        return date;
    }

    private void initTimeZoneChange (SmartZoneView view, Date date, TextView tv) {
        String timeString = TimeZoneUtil.getZoneTimeString (date);
        Log.e ("initTimeZoneChange", "   timeString  = " + timeString);
        boolean aBoolean = SPUtils.getBoolean (this, Constant.UNIT_TIME);
        if (timeString.endsWith ("am")) {
            view.setImageRescource (R.drawable.icon_clock_white);
        } else if (timeString.endsWith ("pm")) {
            view.setImageRescource (R.drawable.icon_clock_black);
        }
        view.setTime (date.getHours (), date.getMinutes ());
        if (aBoolean) { // 24小时制
            if (timeString.endsWith ("am")) {
                String time = timeString.replace ("am", "");
                String[] split = time.split (":");
                int t = Integer.parseInt (split[0]);
                time = t + ":" + (split[1].length () == 1 ? "0" + split[1] : split[1]);
                tv.setText (time);
            } else if (timeString.endsWith ("pm")) {
                String time = timeString.replace ("pm", "");
                String[] split = time.split (":");
                int t = Integer.parseInt (split[0]);
                if (t == 12) {
                    t = 12;
                } else {
                    t = t + 12;
                }
                time = t + ":" + (split[1].length () == 1 ? "0" + split[1] : split[1]);
                tv.setText (time);
            }
        } else { //十二小时制
            String[] split = timeString.split (":");
            String hour = "";
            if ("0".equals (split[0])) {
                hour = "12";
            } else {
                hour = split[0];
            }
            StringBuffer buffer = new StringBuffer (hour).append (":");
            if (split[1].length () == 3) {
                buffer.append ("0").append (split[1]);
            } else {
                buffer.append (split[1]);
            }
            tv.setText (buffer.toString ());
        }
    }

    private void initTab () {
        mLeftImg.setImageResource (R.drawable.selector_more);
        mCenterImg.setImageResource (R.drawable.selector_smart_alarm);
        mRightImg.setImageResource (R.drawable.icon_camera_tab);
        showTagTvStatus (mMessageTv, Constant.MESSAGE_SWITCH_KEY);
        showTagTvStatus (mCallTv, Constant.CALL);
        showTagTvStatus (mMainTagGoalTv, Constant.GOAL_REMIND_KEY);
    }

    private void initListeners () {
        mLeftImg.setOnClickListener (this);
        mCenterImg.setOnClickListener (this);
        mRightImg.setOnClickListener (this);
        mSmartGraphView.setIEditSelect (this);
        mSmartGraphView.setOnEventActionListener (this);
        mSmartGraphView.setOnSmartXChangeListener (this);
    }

    private void initToolbar () {
        View view = LayoutInflater.from (this).inflate (R.layout.right_img, null);
        mMainToolbar.addRightView (view);
        mMImg = (ImageView) view.findViewById (R.id.right_img);
        view.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (SPUtils.getBoolean (MainActivity.this, Constant.ALL_REMIND_KEY)) {
                    SPUtils.putBoolean (MainActivity.this, Constant.ALL_REMIND_KEY, false);
                    mMImg.setImageResource (R.drawable.icon_smart_alert_close);
                } else {
                    SPUtils.putBoolean (MainActivity.this, Constant.ALL_REMIND_KEY, true);
                    mMImg.setImageResource (R.drawable.icon_smart_alert_open);
                }
                allRemind ();
            }
        });
    }

    private void reStartTitle () {
        String name = "Austen";
        String watchName = SPUtils.getString (NixonApplication.getContext (), Constant
                .WATCH_NAME_KEY);
        if (!TextUtils.isEmpty (watchName)) {
            name = watchName;
        }
        mMainToolbar.setTittle (name + "\'s " + SPUtils.getString (this, Constant.BLE_NAME_KEY,
                "Ambassador"));
        if (!new SmartManager ().isDiscovery ()) {
            mMainToolbar.setTittleColor (getResources ().getColor (R.color.sup_color));
            mMainToolbar.setTip (true);
        } else {
            mMainToolbar.setTittleColor (getResources ().getColor (R.color.main_text_color));
            mMainToolbar.setTip (false);
        }
    }

    private void reStartImg () {
        boolean allRemind = SPUtils.getBoolean (this, Constant.ALL_REMIND_KEY);
        if (allRemind) {
            mMImg.setImageResource (R.drawable.icon_smart_alert_open);
        } else {
            mMImg.setImageResource (R.drawable.icon_smart_alert_close);
        }
    }

    private void allRemind () {
        boolean all = SPUtils.getBoolean (this, Constant.ALL_REMIND_KEY);
        if (all) {
            SPUtils.putBoolean (this, Constant.MESSAGE_SWITCH_KEY, true);
            showTagTvStatus (mMessageTv, Constant.MESSAGE_SWITCH_KEY);
            if (!NotifyPermission.isEnabled (this)) {
                show2NewDialog (R.string.request_notify_permission, this);
            }
            SPUtils.putBoolean (this, Constant.CALL, true);
            requestPermission ();
            showTagTvStatus (mCallTv, Constant.CALL);
            SPUtils.putBoolean (this, Constant.GOAL_REMIND_KEY, true);
            showTagTvStatus (mMainTagGoalTv, Constant.GOAL_REMIND_KEY);
        } else {
            SPUtils.putBoolean (this, Constant.MESSAGE_SWITCH_KEY, false);
            showTagTvStatus (mMessageTv, Constant.MESSAGE_SWITCH_KEY);
            SPUtils.putBoolean (this, Constant.CALL, false);
            showTagTvStatus (mCallTv, Constant.CALL);
            SPUtils.putBoolean (this, Constant.GOAL_REMIND_KEY, false);
            showTagTvStatus (mMainTagGoalTv, Constant.GOAL_REMIND_KEY);
        }
    }

    @Override
    protected void onStart () {
        super.onStart ();
    }

    @Override
    protected void onResume () {
        super.onResume ();
        reStartTitle ();
        reStartImg ();
        IntentFilter intentFilter = new IntentFilter ();
        intentFilter.addAction (Intent.ACTION_TIME_TICK);
        intentFilter.addAction (Constant.FINISH_MAIN_KEY);
        mRecevier = new MainReceiver ();
        registerReceiver (mRecevier, intentFilter);
        showTagTvStatus (mMessageTv, Constant.MESSAGE_SWITCH_KEY);
        showTagTvStatus (mMainTagGoalTv, Constant.GOAL_REMIND_KEY);
        showTagTvStatus (mCallTv, Constant.CALL);
        mSecondZone = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_KEY, "");
        if ("".equals (mSecondZone)) {
            mLastSmartZoneView.setText ("---");
            mLastTimeTv.setText ("---");
        } else {
            Date date = TimeZoneUtil.getTimeFromThereZone (mSecondZone);
            date = adaptationDst (date);
            initTimeZoneChange (mLastSmartZoneView, date, mLastTimeTv);
        }
        syncSecondName ();
        syncCurrentName ();
        setUnit ();
        initTimeZoneChange (mCurrentSmartZoneView, new Date (), mCurrentTimeTv);
        syncElectricity (SPUtils.getInt (NixonApplication.getContext (), Constant
                .ELECTRICITY_KEY));  //电量
        presenter.getStepValue ();
        updateVersion ();//检测APP版本升级
        presenter.getStepsByDay ();
        Intent intent = new Intent ();
        intent.setAction (Constant.FINISH_LOGIN_KEY);
        Intent intent1 = new Intent ();
        intent1.setAction (Constant.FINISH_WATCH_HOME_KEY);
        sendBroadcast (intent);
        sendBroadcast (intent1);
        mSmartGraphView.setTargetValue (Integer.parseInt (SPUtils.getString (this, Constant
                .GOAL_KEY, "10000")) - 500);
        if (!TextUtils.isEmpty (mVersion)) {
            presenter.checkoutVersion (mVersion);
        }
        UnitFragment.setUnitChangeListener (new UnitFragment.UnitChangeListener () {
            @Override
            public void unitChanged () {
                setUnit ();
            }
        });

        boolean call = SPUtils.getBoolean (this, Constant.CALL);
        boolean msg = SPUtils.getBoolean (this, Constant.MESSAGE_SWITCH_KEY);
        if (call && msg) {
            mMImg.setImageResource (R.drawable.icon_smart_alert_open);
        } else {
            mMImg.setImageResource (R.drawable.icon_smart_alert_close);
        }
    }

    private void syncCurrentName () {
        String currentTimeZone = TimeZoneUtil.getCurrentTimeZone (Calendar.getInstance ()
                .getTimeZone ());
        TimeZoneAbbreUtils instance = TimeZoneAbbreUtils.getInstance ();
        String abbreName = instance.getAbbreName (currentTimeZone);
        String zoneName = instance.getTineZoneName (currentTimeZone);
        String customAbbreName = SPUtils.getString (this, Constant.HOME_LOCATION_ABBRE, abbreName);
        String customFullName = SPUtils.getString (this, Constant.HOME_LOCATION_KEY, zoneName);
        Log.e ("mCurrentSmartZoneView","  customFullName == " + customFullName + "  == " + customFullName.length ());
        mCurrentSmartZoneView.setText (customFullName);
        mActMainCurrentZone.setText (customAbbreName);
    }

    private void setUnit () {
        mIsKm = SPUtils.getBoolean (this, Constant.UNIT_DISTANCE);
        if (mIsKm) {
            mMainMiddleDistanceUnit.setText ("KM");
        } else {
            mMainMiddleDistanceUnit.setText ("MILES");
        }
        if ("".equals (mSecondZone)) {
            mLastSmartZoneView.setText ("---");
            mLastTimeTv.setText ("---");
        } else {
            Date date = TimeZoneUtil.getTimeFromThereZone (mSecondZone);
            date = adaptationDst (date);
            initTimeZoneChange (mLastSmartZoneView, date, mLastTimeTv);
        }
        initTimeZoneChange (mCurrentSmartZoneView, new Date (), mCurrentTimeTv);
    }

    @Override
    protected MainContract.MainPresenter createPresenter () {
        return new MainContract.MainPresenter ();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis () - mExitTime) > 3000) {
                Toast.makeText (this, getResources ().getString (R.string.exit_app), Toast
                        .LENGTH_SHORT).show ();
                mExitTime = System.currentTimeMillis ();
                return false;
            }
            //AppManager.getAppManager ().appExit (this);
        }
        return super.onKeyDown (keyCode, event);
    }

    @Override
    protected void onStop () {
        super.onStop ();
        UnitFragment.releaseChangeListener ();
    }

    @Override
    protected void onPause () {
        super.onPause ();
        presenter.onPause ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (mRecevier != null) {
            unregisterReceiver (mRecevier);
        }
    }

    @OnClick ({R.id.toolbar_center_tv, R.id.main_top_zone_layout, R.id.main_top_layout, R.id
            .tab_bottom_left, R.id.tab_bottom_center, R.id.tab_bottom_right, R.id.home_edit_tv, R
            .id.main_tag_message_tv, R.id.main_tag_call_tv, R.id.main_tag_goal_tv})
    @Override
    public void onClick (View view) {
        Intent intent;
        switch (view.getId ()) {
            case R.id.main_top_layout:
            case R.id.main_top_zone_layout:
                changeViewStatus ();
                Log.w ("NotifyService", "run-------------->" + ReBindNotifyService
                        .isNotificationListenerEnable (this));
                break;
            case R.id.toolbar_center_tv:
                if (!new SmartManager ().isDiscovery ()) {
                    intent = new Intent (this, SettingsActivity.class);
                    intent.putExtra (STSRT_CODE, 3);
                    startActivity (intent);
                    overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                }
                //toActivity (ConnectActivity.class);
                break;
            case R.id.tab_bottom_left:
                intent = new Intent (this, SettingsActivity.class);
                intent.putExtra (STSRT_CODE, 0);
                //intent = new Intent (this, FrequentContactsActivity.class);
                startActivity (intent);
                overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.tab_bottom_center:
                intent = new Intent (this, SettingsActivity.class);
                intent.putExtra (STSRT_CODE, 1);
                startActivity (intent);
                overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.tab_bottom_right:
                intent = new Intent (this, SettingsActivity.class);
                intent.putExtra (STSRT_CODE, 2);
                startActivity (intent);
                overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                break;
            case R.id.home_edit_tv:
                //                intent = new Intent(this, ChooseCity.class);
                //                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.dialog_cancel_tv:
                hideDialog ();
                mMessageTv.setTextColor (getResources ().getColor (R.color.sup_text_color));
                boolean call = SPUtils.getBoolean (this, Constant.CALL);
                boolean msg = SPUtils.getBoolean (this, Constant.MESSAGE_SWITCH_KEY);
                if (call && msg) {
                    mMImg.setImageResource (R.drawable.icon_smart_alert_open);
                } else {
                    mMImg.setImageResource (R.drawable.icon_smart_alert_close);
                }
                break;
            case R.id.dialog_confirm_tv:
                NotifyPermission.openNotificationAccess (this);
                hideDialog ();
                break;
            case R.id.main_tag_message_tv:
                messageRemind ();
                break;
            case R.id.main_tag_call_tv:
                PermissionsUtils.requestPermissions (new String[]{Manifest.permission.CALL_PHONE,
                        Manifest.permission.READ_PHONE_STATE}, 0, this);
                callRemind ();
                break;
            case R.id.main_tag_goal_tv:
                //presenter.uploadAllData ();
                //uploadDbSportData ();
                goalRemind ();
                break;
        }
    }

    private void uploadDbSportData () {
        final String customerId = SPUtils.getString (NixonApplication.getContext (), Constant
                .CUSTOMER_ID);
        final String deviceUUID = DeviceMessage.getInstance ().getDeviceUUID ();
        List<DbWatchBean> dbWatchBean = DbManager.getDbWatchBean (customerId, deviceUUID);
        if (dbWatchBean != null && dbWatchBean.size () > 0) {
            int stemp = 0;
            for (DbWatchBean bean : dbWatchBean) {
                stemp += bean.getValue ();
            }
            Log.e ("NIXONLOGIN", " huoqudao 数据库信息 stemp = " + stemp);
        }
        Handler handler = NixonApplication.getmainThreadHandler ();
        handler.postDelayed (new Runnable () {
            @Override
            public void run () {
                DbManager.delete (customerId, deviceUUID);
                Log.e ("NIXONLOGIN", "删除成功");
            }
        }, 2000);
    }

    private void goalRemind () {
        boolean b = SPUtils.getBoolean (this, Constant.GOAL_REMIND_KEY);
        SPUtils.putBoolean (this, Constant.GOAL_REMIND_KEY, !b);
        showTagTvStatus (mMainTagGoalTv, Constant.GOAL_REMIND_KEY);

        boolean goal = SPUtils.getBoolean (this, Constant.GOAL_REMIND_KEY);
        boolean call = SPUtils.getBoolean (this, Constant.CALL);
        boolean msg = SPUtils.getBoolean (this, Constant.MESSAGE_SWITCH_KEY);
        if (call && goal && msg) {
            mMImg.setImageResource (R.drawable.icon_smart_alert_open);
        } else {
            mMImg.setImageResource (R.drawable.icon_smart_alert_close);
        }
    }

    private void callRemind () {
        requestPermission ();
        boolean call = SPUtils.getBoolean (this, Constant.CALL);
        SPUtils.putBoolean (this, Constant.CALL, !call);
        showTagTvStatus (mCallTv, Constant.CALL);
        boolean msg = SPUtils.getBoolean (this, Constant.MESSAGE_SWITCH_KEY);
        boolean goal = SPUtils.getBoolean (this, Constant.GOAL_REMIND_KEY);
        if (!call && msg && goal) {
            mMImg.setImageResource (R.drawable.icon_smart_alert_open);
        } else {
            mMImg.setImageResource (R.drawable.icon_smart_alert_close);
        }
        //if (!NotifyPermission.isEnabled (this) && !call) {
        //    show2NewDialog (R.string.request_notify_permission, this);
        //}
    }

    private void messageRemind () {
        boolean msg = SPUtils.getBoolean (this, Constant.MESSAGE_SWITCH_KEY);
        SPUtils.putBoolean (this, Constant.MESSAGE_SWITCH_KEY, !msg);
        showTagTvStatus (mMessageTv, Constant.MESSAGE_SWITCH_KEY);

        boolean call = SPUtils.getBoolean (this, Constant.CALL);
        boolean goal = SPUtils.getBoolean (this, Constant.GOAL_REMIND_KEY);
        if (call && !msg && goal) {
            mMImg.setImageResource (R.drawable.icon_smart_alert_open);
        } else {
            mMImg.setImageResource (R.drawable.icon_smart_alert_close);
        }

        if (!NotifyPermission.isEnabled (this) && !msg) {
            show2NewDialog (R.string.request_notify_permission, this);
        }
    }

    private void requestPermission () {
        PermissionsUtils.requestPermissions (new String[]{Manifest.permission.CALL_PHONE,
                Manifest.permission.READ_PHONE_STATE}, 0x001, this);
    }

    private void showTagTvStatus (TextView tv, String key) {
        boolean b = SPUtils.getBoolean (this, key);
        if (key.equals (Constant.GOAL_REMIND_KEY)) {
            Log.e ("GOAL123", "  Constant.GOAL_REMIND_KEY = " + b);
        }
        if (!b) {
            tv.setTextColor (getResources ().getColor (R.color.sup_text_color));
        } else {
            tv.setTextColor (getResources ().getColor (R.color.black));
        }
    }

    private void changeViewStatus () {
        if (mZoneLayout.getVisibility () != View.VISIBLE) {
            mZoneLayout.setVisibility (View.VISIBLE);
            mZoneLayout.setVisibility (View.VISIBLE);
            mTopLayout.setVisibility (View.INVISIBLE);
            mLastSmartZoneView.startAnimation ();
            mCurrentSmartZoneView.startAnimation ();
        } else {
            mZoneLayout.setVisibility (View.INVISIBLE);
            mTopLayout.setVisibility (View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            ChooseCityBean.ObjectBean objectBean = (ChooseCityBean.ObjectBean) data
                    .getSerializableExtra ("data");
            Date date = TimeZoneUtil.getTimeFromThereZone (objectBean.getTimezone ());
            initTimeZoneChange (mCurrentSmartZoneView, new Date (), mCurrentTimeTv);
            Date dstDate = adaptationDst (date);
            initTimeZoneChange (mLastSmartZoneView, dstDate, mLastTimeTv);
            int min = TimeZoneUtil.getMinFromThereZone (objectBean.getTimezone ());
            float value = (float) (min % 60) / 60;
            BigDecimal bigDecimal = new BigDecimal (value);
            value = bigDecimal.setScale (2, BigDecimal.ROUND_HALF_UP).floatValue ();
            presenter.sendSecondTime (date, min / 60, (int) (value * 100));
            SPUtils.putString (NixonApplication.getContext (), Constant.SECOND_ZONE_NAME_KEY,
                    objectBean.getShortName ());
            SPUtils.putString (NixonApplication.getContext (), Constant.SECOND_ZONE_KEY,
                    objectBean.getTimezone ());
            mLastSmartZoneView.setText (objectBean.getShortName ());
            mActMainSecondZone.setText (objectBean.getShortName ());
        }
    }

    @Override
    public void updateComplete (int step) {
        showStep (step);
    }

    @Override
    public void electricity (int electricity) {
        Log.w (TAG, "electricity::" + electricity);
        syncElectricity (electricity);
    }

    private void syncElectricity (int electricity) {
        mElectricityTv.setText (electricity + " %");
        SPUtils.putInt (this, Constant.ELECTRICITY_KEY, electricity);
        if (electricity <= 15) {
            mElectricityImg.setImageResource (R.drawable.icon_battery_0);
        } else if (electricity <= 50) {
            mElectricityImg.setImageResource (R.drawable.icon_battery_50);
        } else {
            mElectricityImg.setImageResource (R.drawable.icon_battery_100);
        }
    }

    @Override
    public void stepsDay (NetSportStepBean bean) {
        if (bean.getObject ().size () > 0) {
            mDaySmartPoint.clear ();
            List<NetSportStepBean.ObjectBean> objects = bean.getObject ();
            for (NetSportStepBean.ObjectBean obj : objects) {
                int value = (int) Double.parseDouble (obj.getSteps ());
                Log.e ("MainActivity:", value + "");
                String date = SimpleDateUtils.changeFormatString (obj.getDate ());
                SmartPoint point = new SmartPoint.Builder ().value (value > 40000 ? 40000 :
                        value).date (date).build ();
                point.setmObjectBean (obj);
                mDaySmartPoint.add (point);
            }
            onEvent (mSmartGraphView.getmEventAction ());
        }
    }

    @Override
    public void stepsWeek (NetSportStepBean bean) {
        if (bean.getObject ().size () > 0) {
            mWeekSmartPoint.clear ();
            List<NetSportStepBean.ObjectBean> objects = bean.getObject ();
            for (NetSportStepBean.ObjectBean obj : objects) {
                int value = (int) Double.parseDouble (obj.getSteps ());
                String[] weekNumber = obj.getDate ().split ("-");
                Log.e ("MainActivity:", weekNumber[1]);
                SmartPoint point = new SmartPoint.Builder ().value (value > 40000 ? 40000 :
                        value).date (weekNumber[1] + "th").build ();
                point.setmObjectBean (obj);
                mWeekSmartPoint.add (point);
            }
            onEvent (mSmartGraphView.getmEventAction ());
        }
    }

    @Override
    public void stepsMonth (NetSportStepBean bean) {
        if (bean.getObject ().size () > 0) {
            mMonthSmartPoint.clear ();
            List<NetSportStepBean.ObjectBean> objects = bean.getObject ();
            for (NetSportStepBean.ObjectBean obj : objects) {
                int value = (int) Double.parseDouble (obj.getSteps ());
                String[] monthNumber = obj.getDate ().split ("-");
                Log.e ("MainActivity:", obj.getDate ());
                SmartPoint point = new SmartPoint.Builder ().value (value > 40000 ? 40000 :
                        value).date (MonthUtil.getMon (Integer.valueOf (monthNumber[1]))).build ();
                point.setmObjectBean (obj);
                mMonthSmartPoint.add (point);
            }
            onEvent (mSmartGraphView.getmEventAction ());
        }
    }

    @Override
    public void onComplete (boolean isComplete) {
        if (!isShowing () && isComplete) {
            show2NewDialog (R.string.ota_new_version_tip, new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    switch (view.getId ()) {
                        case R.id.dialog_confirm_tv:
                            hideDialog ();
                            break;
                        case R.id.dialog_cancel_tv:
                            presenter.confirmUpdateOta ();
                            hideDialog ();
                            break;
                    }
                }
            }, R.string.ota_update_title, R.string.ota_update_confirm, R.string.ota_update_cancel);
        }
    }

    @Override
    public void secondDate (Date date, int zone) {
        mSecondZone = zone + ":0";
        syncSecondName ();
    }

    private void syncSecondName () {
        Date nowDate = TimeZoneUtil.getTimeFromThereZone (mSecondZone);
        Log.w ("secondDate", "run------>" + mSecondZone);
        SPUtils.putString (this, Constant.SECOND_ZONE_KEY, mSecondZone);
        Date dstDate = adaptationDst (nowDate);
        initTimeZoneChange (mLastSmartZoneView, dstDate, mLastTimeTv);

        String abbreName = SPUtils.getString (this, Constant.SECOND_ZONE_NAME_KEY, "---");
        Log.d ("secondLocaAbbre", "  取出自定义第二市区缩写  " + abbreName);
        TimeZoneAbbreUtils instance = TimeZoneAbbreUtils.getInstance ();
        if (mSecondZone != null && abbreName.equals ("---")) {
            abbreName = instance.getAbbreName (mSecondZone);
            Log.d ("secondLocaAbbre", "  转化的第二市区缩写  " + abbreName);
        }
        String zoneName = instance.getTineZoneName (mSecondZone);
        String secondFullName = SPUtils.getString (this, Constant.SECOND_ZONE_FUll_NAME_KEY,zoneName);
        Log.e ("mCurrentSmartZoneView","  secondFullName == " + secondFullName + "   " + secondFullName.length ());
        mLastSmartZoneView.setText (secondFullName);
        mActMainSecondZone.setText (abbreName);
        SPUtils.putString (NixonApplication.getContext (), Constant.SECOND_ZONE_NAME_KEY,
                abbreName);
    }

    @Override
    public void showVersion (int main, int minor, int test) {
        StringBuffer buffer = new StringBuffer ();
        buffer.append ("V").append (main + ".").append (minor + ".").append (test);
        mVersion = buffer.toString ();
        Log.e ("DownLoaderTask", "发现新版本 " + mVersion);
        presenter.checkoutVersion (mVersion);
    }

    @Override
    public void target (int target) {
        Log.w ("target::", target + "");
        mSmartGraphView.setTargetValue (target - 500);
    }

    private void showStep (int step) {
        if (step == 0) {
            mStepTv.setText ("---");
        } else {
            mStepTv.setText (step + "");
        }
        String sw = SPUtils.getString (NixonApplication.getContext (), Constant.WEIGHT_KEY);
        sw = StringUtils.lbs2kg (sw);
        double dw = 50d;
        if (!TextUtils.isEmpty (sw)) {
            dw = Double.valueOf (sw);
        }
        double v = StepUtil.getCalorieByWeightAndStep (dw, step, 2);
        if (v == 0) {
            mCalTv.setText ("---");
        } else {
            mCalTv.setText (v + "");
        }

        String sh = SPUtils.getString (NixonApplication.getContext (), Constant.TALL_KEY);
        sh = StringUtils.inch2cm (sh);
        //String tall = SPUtils.getString(this, Constant.TALL_KEY);
        double dh = 0;/*= Double.parseDouble(tall);*/
        if (!TextUtils.isEmpty (sh)) {
            dh = Double.valueOf (sh);
        }
        double s = StepUtil.getDistanceByHeightAndStep (dh, step, 2);
        setMiddleDistance (s);
    }

    /**
     * @param s
     *         运动距离，单位 m
     */
    private void setMiddleDistance (double s) {
        Log.e ("DISTANCE", " juli = " + s);
        if (s == 0) {
            mDistanceTv.setText ("---");
        } else {
            s = s / 1000; //转成km
            if (mIsKm) {
                DecimalFormat df = new DecimalFormat ("#0.00");
                mDistanceTv.setText (df.format (s) + "");
                Log.e ("DISTANCE", " juli = " + df.format (s) + "km");
            } else {
                String s1 = StringUtils.kmToMiles (s); //转成英里
                mDistanceTv.setText (s1);
            }
        }
    }

    private void updateVersion () {
        if (NetUtil.getNetworkState (this) != NetUtil.NETWORN_NONE) {
            PgyUpdateManager.register (this, "com.example.bo.nixon.fileprovider", new
                    UpdateManagerListener () {
                @Override
                public void onNoUpdateAvailable () {

                }

                @Override
                public void onUpdateAvailable (String s) {
                    final AppBean appBean = getAppBeanFromString (s);
                    show2NewDialog (R.string.new_version_tip, new View.OnClickListener () {
                        @Override
                        public void onClick (View view) {
                            switch (view.getId ()) {
                                case R.id.dialog_confirm_tv:  //do it later
                                    hideDialog ();
                                    break;
                                case R.id.dialog_cancel_tv:  //update now
                                    UpdateManagerListener.startDownloadTask (MainActivity.this,
                                            appBean.getDownloadURL ());
                                    break;
                            }
                        }
                    }, R.string.ota_update_title, R.string.ota_update_confirm, R.string
                            .ota_update_cancel);
                }
            });
        }
    }

    @Override
    public void onEditSelected (boolean select, int target) {
        if (!select) {
            presenter.setTargetValue (target);
            SPUtils.putString (NixonApplication.getContext (), Constant.GOAL_KEY, target
                    + "");
        }
    }

    @Override
    public void onEvent (SmartGraphView.EventAction eventAction) {
        switch (eventAction.getPosition ()) {
            case 0:
                mSmartGraphView.setSmartPoints (mDaySmartPoint);
                break;
            case 1:
                mSmartGraphView.setSmartPoints (mWeekSmartPoint);
                break;
            case 2:
                mSmartGraphView.setSmartPoints (mMonthSmartPoint);
                break;
        }
    }

    @Override
    public void onSmart (SmartPoint smartPoint) {
        NetSportStepBean.ObjectBean bean = smartPoint.getmObjectBean ();
        if (bean == null) {
            return;
        }
        double step = Double.parseDouble (bean.getSteps ());
        String text;
        if (step == 0) {
            text = "---";
        } else {
            if (step >= 100000) {
                DecimalFormat df = new DecimalFormat ("#0.0");
                text = df.format (step / 1000) + "k";
            } else {
                text = (int) step + "";
            }
        }
        mStepTv.setText (text);
        Log.e ("VIEWSTEPS", "  控件回调过来的数据  " + bean.getSteps ());
        setMiddleDistance (Double.parseDouble (bean.getDistance ()) / 100);
        double v = Double.parseDouble (bean.getCalori ());
        if (v == 0) {
            mCalTv.setText ("---");
        } else {
            v = Math.abs (v);
            mCalTv.setText (v + "");
        }
    }

    class MainReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {

            if (intent.getAction ().equals (Intent.ACTION_TIME_TICK)) {  // 时间变化，每一分钟调用一次
                if ("".equals (mSecondZone)) {
                    mLastSmartZoneView.setText ("---");
                    mLastTimeTv.setText ("---");
                } else {
                    Date date = TimeZoneUtil.getTimeFromThereZone (mSecondZone);
                    date = adaptationDst (date);
                    initTimeZoneChange (mLastSmartZoneView, date, mLastTimeTv);
                }
                initTimeZoneChange (mCurrentSmartZoneView, new Date (), mCurrentTimeTv);
            } else if (intent.getAction ().equals (Constant.FINISH_MAIN_KEY)) {
                finish ();
            } else if (Intent.ACTION_TIMEZONE_CHANGED.equals (intent.getAction ())) {  //时区变化
                showTimeZoneChangedTip ();
            }

        }
    }

    View.OnClickListener timeZoneListener = new View.OnClickListener () {
        @Override
        public void onClick (View v) {
            switch (v.getId ()) {
                case R.id.dialog_confirm_tv: // do it later
                    hideDialog ();
                    break;
                case R.id.dialog_cancel_tv: // update now
                    presenter.sendCurrentZoneTime ();
                    break;
            }
        }
    };

    private void showTimeZoneChangedTip () {
        show2NewDialog (R.string.time_zone_changed, timeZoneListener, R.string.blank, R.string
                .ota_update_confirm, R.string.ota_update_cancel);
    }
}
