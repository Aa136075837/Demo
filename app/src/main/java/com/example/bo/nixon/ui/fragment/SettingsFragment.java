package com.example.bo.nixon.ui.fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.SettingsListViewAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.presenter.SettingsFragmentContract;
import com.example.bo.nixon.ui.activity.NixonUrlActivity;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.PermissionsUtils;
import com.smart.smartble.SmartManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class SettingsFragment extends BaseMvpFragment<SettingsFragmentContract.SettingsPresenter>
        implements SettingsFragmentContract.SettingsView, AdapterView.OnItemClickListener,
        SettingsListViewAdapter.SmartSwitchClickListener {

    @BindView (R.id.frag_setting_title)
    TextView mFragSettingTitle;
    private SettingFragListViewItemClickListener mListener;
    @BindView (R.id.settings_list_view)
    ListView mSettingsListView;
    @BindView (R.id.personal_ll)
    LinearLayout mPersonalLl;
    @BindView (R.id.setting_frame_layout)
    FrameLayout mSettingFrameLayout;
    private static final int PERMISSION_CONTACT = 11111;
    private List<PersonalInfoBean> mData;
    private Fragment mCurrentFragment;

    public static final int ADD_WATCH = 10001;
    public static final int CALIBRATE_HANDS = 10002;
    public static final int REMOTE_SHUTTER = 10003;
    public static final int FAVORITE_CONTACTS = 10004;
    public static final int DISCONNECT_ALERT = 10005;
    public static final int HELP = 10006;
    public static final int NIXON_URL = 10007;
    public static final int UNIT = 10008;
    public static final int ABOUT = 10009;
    public static final int CONNECTED_DEVICE = 10010;
    public static final int MY_PROFILE = 10011;
    public static final int TIME_ZONE = 10012;
    private SettingsListViewAdapter mAdapter;
    private String mWatchName;
    private String mBleName;

    @Override
    protected SettingsFragmentContract.SettingsPresenter createPresenter () {
        return new SettingsFragmentContract.SettingsPresenter ();
    }

    @Override
    public int getLayoutResId () {
        return R.layout.fragment_settings;
    }

    @Override
    protected void initView () {
        super.initView ();
        PermissionsUtils.requestPermissions (new String[]{Manifest.permission.READ_CONTACTS},
                PERMISSION_CONTACT, getActivity ());
        mData = new ArrayList<> ();
        mWatchName = SPUtils.getString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY);
        mBleName = SPUtils.getString (NixonApplication.getContext (), Constant.BLE_NAME_KEY);
        mFragSettingTitle.setText ((TextUtils.isEmpty (mWatchName) ? "Austen's " : (mWatchName +
                "'s ")) + (TextUtils.isEmpty (mBleName) ? "Ambassador" : mBleName) + " Settings");
        initListView ();
        mAdapter = new SettingsListViewAdapter (mData);
        mSettingsListView.setAdapter (mAdapter);
        mSettingsListView.setOnItemClickListener (this);
        mAdapter.setSmartSwitchClickListener (this);
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (hidden) {

        } else {
            mData.clear ();
            initListView ();
            mAdapter.notifyDataSetChanged ();
        }
    }

    private void initListView () {
        SmartManager smartManager = new SmartManager ();
        Log.e ("CONNECT111", " 连接 == " + smartManager.isDiscovery ());
        if (smartManager.isDiscovery ()) {
            mData.add (new PersonalInfoBean (getResources ().getString (R.string
                    .settings_fragment_connected), CONNECTED_DEVICE));
        } else {
            mData.add (new PersonalInfoBean (getResources ().getString (R.string
                    .settings_fragment_add), ADD_WATCH));
        }
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_profile), MY_PROFILE));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_hands), CALIBRATE_HANDS));
        //mData.add (new PersonalInfoBean (getResources ().getString (R.string
        // .settings_fragment_shutter), REMOTE_SHUTTER));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_contacts), FAVORITE_CONTACTS));
        PersonalInfoBean disconnectAlert = new PersonalInfoBean (getResources ().getString (R
                .string.settings_fragment_alert), true, DISCONNECT_ALERT);
        disconnectAlert.setSelect (SPUtils.getBoolean (NixonApplication.getContext (), Constant
                .DISCONNECT_ALERT_KEY, SPUtils.getBoolean (NixonApplication.getContext (),
                Constant.DISCONNECT_ALERT_KEY, false)));
        mData.add (disconnectAlert);
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_unit), UNIT));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_zone), TIME_ZONE));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_help), HELP));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_about), ABOUT));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string
                .settings_fragment_url), NIXON_URL));
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
                                            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CONTACT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

    @Override
    public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity ().finish ();
                    getActivity ().overridePendingTransition (R.anim.activity_out_bit, R.anim
                            .activity_out_bit);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
        if (null == mListener) {
            return;
        }
        if (!new SmartManager ().isDiscovery ()) {
            switch (mData.get (i).getTag ()) {
                case SettingsFragment.CALIBRATE_HANDS:
                case SettingsFragment.FAVORITE_CONTACTS:
//                case SettingsFragment.MY_PROFILE:
                case SettingsFragment.DISCONNECT_ALERT:
                    return;
            }
        }
        switch (mData.get (i).getTag ()) {
            case ADD_WATCH:
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && getActivity ()
                        .checkSelfPermission (Manifest.permission.BLUETOOTH) != PackageManager
                        .PERMISSION_GRANTED) {
                    show1NewDialog (R.string.open_blue_tooth);
                }
                mListener.cutoverSettingFragment (ADD_WATCH);
                break;
            case CALIBRATE_HANDS:
                mListener.cutoverSettingFragment (CALIBRATE_HANDS);
                break;
            case REMOTE_SHUTTER:
                mListener.cutoverSettingFragment (REMOTE_SHUTTER);
                break;
            case FAVORITE_CONTACTS:
                mListener.cutoverSettingFragment (FAVORITE_CONTACTS);
                break;
            case DISCONNECT_ALERT:
                /**
                 * 此Item点击事件移至，SettingsListViewAdapter.SmartSwitchClickListener接口的回调中
                 */
                //Toast.makeText (NixonApplication.getContext (), "点击断开提醒", Toast.LENGTH_SHORT)
                // .show ();
                ////PersonalInfoBean infoBean = mData.get (i);
                //boolean b = SPUtils.getBoolean (NixonApplication.getContext (), Constant
                // .DISCONNECT_ALERT_KEY);
                ////infoBean.setSelect (b);
                //mAdapter.notifyDataSetChanged ();
                //presenter.setDisconnectNotify (b);
                //SPUtils.putBoolean (NixonApplication.getContext (), Constant
                // .DISCONNECT_ALERT_KEY, !b);
                break;
            case HELP:
                mListener.cutoverSettingFragment (HELP);
                break;
            case NIXON_URL:
                Intent intent = new Intent (getActivity (), NixonUrlActivity.class);
                startActivity (intent);
                break;
            case UNIT:
                mListener.cutoverSettingFragment (UNIT);
                break;
            case ABOUT:
                mListener.cutoverSettingFragment (ABOUT);
                break;
            case CONNECTED_DEVICE:
                mListener.cutoverSettingFragment (CONNECTED_DEVICE);
                break;
            case MY_PROFILE:
                mListener.cutoverSettingFragment (MY_PROFILE);
                break;
            case TIME_ZONE:
                mListener.cutoverSettingFragment (TIME_ZONE);
                break;
        }
    }

    private PersonalInfoBean getPersonalBean (int tag) {
        for (PersonalInfoBean infoBean : mData) {
            if (infoBean.getTag () == tag) {
                return infoBean;
            }
        }
        return new PersonalInfoBean ("1");
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        mListener = null;
    }

    public void setSettingFragListViewItemClickListener (SettingFragListViewItemClickListener
                                                                 listener) {
        mListener = listener;
    }

    @Override
    public void disconnectNotifySetting (boolean notify) {
        //if (null != mData) {
        //    PersonalInfoBean personalBean = getPersonalBean (DISCONNECT_ALERT);
        //    personalBean.setShowSwitch (notify);
        //    mAdapter.notifyDataSetChanged ();
        //    SPUtils.putBoolean (NixonApplication.getContext (), Constant.DISCONNECT_ALERT_KEY,
        //        personalBean.isSelect ());
        //}
    }

    @Override
    public void isCheck (boolean isChecked) {
        presenter.setDisconnectNotify (isChecked);
    }

    public interface SettingFragListViewItemClickListener {
        void cutoverSettingFragment (int tag);
    }
}
