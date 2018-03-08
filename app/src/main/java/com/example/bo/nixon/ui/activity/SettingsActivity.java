package com.example.bo.nixon.ui.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseActivity;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.activity.camera.CameraActivity;
import com.example.bo.nixon.ui.fragment.AboutFragment;
import com.example.bo.nixon.ui.fragment.AddWatchManagerFragment;
import com.example.bo.nixon.ui.fragment.AlarmFragment;
import com.example.bo.nixon.ui.fragment.AlarmManagerFragment;
import com.example.bo.nixon.ui.fragment.CalibrationManagerFragment;
import com.example.bo.nixon.ui.fragment.DeviceInfoFragment;
import com.example.bo.nixon.ui.fragment.EditPersonalInfoFragment;
import com.example.bo.nixon.ui.fragment.FavoriteContactFragment;
import com.example.bo.nixon.ui.fragment.HelpFragment;
import com.example.bo.nixon.ui.fragment.SettingsFragment;
import com.example.bo.nixon.ui.fragment.UnitFragment;
import com.example.bo.nixon.ui.fragment.personal.PersonalFragment;
import com.example.bo.nixon.ui.fragment.timeZone.TimeZoneFragment;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends BaseActivity implements PersonalFragment
        .PersonalListViewClickListener, EditPersonalInfoFragment.EditFragmentBackListener,
        SettingsFragment.SettingFragListViewItemClickListener, FavoriteContactFragment
                .FavoriteBackOnClickListener, AddWatchManagerFragment
                .AddWatchBackOnClickListener, CalibrationManagerFragment
                .CalibrationBackClickListener, AlarmFragment.AlarmFragListviewListener,
        AlarmManagerFragment.AlarmMangBackClickListener, HelpFragment.HelpFragBackClickListener,
        UnitFragment.UnitFragBackListener, DeviceInfoFragment.DeviceBackListener, AboutFragment
                .AboutBackClickListener, TimeZoneFragment.TimeZoneBackListener {
    private FragmentManager mFragmentManager;
    private SettingsFragment mSettingsFragment;
    private AlarmFragment mAlarmFragment;
    private PersonalFragment mPersonalFragment;
    private Fragment mCurrentFragment;
    private EditPersonalInfoFragment mEditPersonalInfoFragment;
    private FavoriteContactFragment mFavoriteContactFragment;
    private AddWatchManagerFragment mAddWatchManagerFragment;
    private CalibrationManagerFragment mCalibrationManagerFragment;
    private AlarmManagerFragment mAlarmManagerFragment;
    private AboutFragment mAboutFragment;
    private HelpFragment mHelpFragment;
    private UnitFragment mUnitFragment;
    private DeviceInfoFragment mDeviceInfoFragment;
    int stsrtCode;
    int currentId;
    @BindView (R.id.tab_top_left)
    ImageView mTabLeft;
    @BindView (R.id.tab_top_center)
    ImageView mTabCenter;
    @BindView (R.id.tab_top_right)
    ImageView mTabRight;
    @BindView (R.id.main_layout_fragment)
    FrameLayout mMainLayoutFragment;
    private FragmentTransaction mTransaction;
    private InputMethodManager mImm;
    private TimeZoneFragment mTimeZoneFragment;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_settings);
        ButterKnife.bind (this);
        stsrtCode = getIntent ().getExtras ().getInt ("STSRT_CODE");
        currentId = stsrtCode;
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_setting));
        mFragmentManager = getFragmentManager ();
        mImm = (InputMethodManager) getSystemService (NixonApplication.INPUT_METHOD_SERVICE);
        if (savedInstanceState == null) {
            initFragment (currentId);
        }
    }

    @Override
    protected void onStart () {
        super.onStart ();
        if (stsrtCode == 2) {
            finish ();
            //overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
        }
    }

    private void initFragment (int stsrtCode) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        switch (stsrtCode) {
            case 0:
                if (null == mSettingsFragment) {
                    mSettingsFragment = new SettingsFragment ();
                    transaction.add (R.id.main_layout_fragment, mSettingsFragment);
                }
                mSettingsFragment.setSettingFragListViewItemClickListener (this);
                transaction.show (mSettingsFragment);
                transaction.commit ();
                mCurrentFragment = mSettingsFragment;
                mTabLeft.setSelected (true);

                break;
            case 1:
                if (null == mAlarmFragment) {
                    mAlarmFragment = new AlarmFragment ();
                    transaction.add (R.id.main_layout_fragment, mAlarmFragment);
                }
                mAlarmFragment.setAlarmFragListviewListener (this);
                transaction.show (mAlarmFragment);
                transaction.commit ();
                mCurrentFragment = mAlarmFragment;
                mTabCenter.setSelected (true);
                break;
            case 2:
                //if (null == mPersonalFragment) {
                //    mPersonalFragment = new PersonalFragment ();
                //    transaction.add (R.id.main_layout_fragment, mPersonalFragment);
                //}
                //mPersonalFragment.setONPersonalListViewClickLIstener (this);
                //Log.e ("BOLISTENER", " shezhi listener  1");
                //transaction.show (mPersonalFragment);
                //transaction.commit ();
                //mCurrentFragment = mPersonalFragment;
                //mTabRight.setSelected (true);
                startCameraActivity ();
                overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                break;
            case 3:
                if (null == mAddWatchManagerFragment) {
                    mAddWatchManagerFragment = new AddWatchManagerFragment ();
                    transaction.add (R.id.main_layout_fragment, mAddWatchManagerFragment);
                }
                mAddWatchManagerFragment.setAddWatchBackOnClickListener (this);
                //transaction.hide (mCurrentFragment);
                transaction.show (mAddWatchManagerFragment);
                transaction.commit ();
                mCurrentFragment = mAddWatchManagerFragment;
                mTabLeft.setSelected (true);
                break;
        }
    }

    private void destroyAlarmFragment () {
        if (null != mAlarmFragment) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction ();
            transaction.remove (mAlarmFragment);
            transaction.commitAllowingStateLoss ();
            mAlarmFragment = null;
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
    }

    @OnClick ({R.id.tab_top_left, R.id.tab_top_center, R.id.tab_top_right})
    public void onViewClicked (View view) {
        mTransaction = mFragmentManager.beginTransaction ();
        switch (view.getId ()) {
            case R.id.tab_top_left:
                currentId = 0;
                if (mTabLeft.isSelected ()) {
                    finish ();
                    overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
                }
                if (null == mSettingsFragment) {
                    mSettingsFragment = new SettingsFragment ();
                    mTransaction.add (R.id.main_layout_fragment, mSettingsFragment);
                }
                mSettingsFragment.setSettingFragListViewItemClickListener (this);
                mTransaction.hide (mCurrentFragment);
                mTransaction.show (mSettingsFragment);
                mTransaction.commitAllowingStateLoss ();
                mCurrentFragment = mSettingsFragment;
                mTabLeft.setSelected (true);
                mTabCenter.setSelected (false);
                mTabRight.setSelected (false);
                //destroyAlarmFragment ();
                break;
            case R.id.tab_top_center:
                currentId = 1;
                if (mTabCenter.isSelected ()) {
                    finish ();
                    overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
                }
                if (null == mAlarmFragment) {
                    mAlarmFragment = new AlarmFragment ();
                    mTransaction.add (R.id.main_layout_fragment, mAlarmFragment);
                }
                mAlarmFragment.setAlarmFragListviewListener (this);
                mTransaction.hide (mCurrentFragment);
                mTransaction.show (mAlarmFragment);
                mTransaction.commitAllowingStateLoss ();
                mCurrentFragment = mAlarmFragment;
                mTabCenter.setSelected (true);
                mTabLeft.setSelected (false);
                mTabRight.setSelected (false);
                break;
            case R.id.tab_top_right:
                startCameraActivity ();
                //if (mTabRight.isSelected ()) {
                //    finish ();
                //    overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
                //}
                //if (null == mPersonalFragment) {
                //    mPersonalFragment = new PersonalFragment ();
                //    mTransaction.add (R.id.main_layout_fragment, mPersonalFragment);
                //}
                //mPersonalFragment.setONPersonalListViewClickLIstener (this);
                //Log.e ("BOLISTENER", " shezhi listener  2");
                //mTransaction.hide (mCurrentFragment);
                //mTransaction.show (mPersonalFragment);
                //mTransaction.commitAllowingStateLoss ();
                //mCurrentFragment = mPersonalFragment;
                //mTabRight.setSelected (true);
                //mTabCenter.setSelected (false);
                //mTabLeft.setSelected (false);
                //destroyAlarmFragment ();
                overridePendingTransition (R.anim.activity_in, R.anim.activity_out);
                break;
        }
    }

    private void startCameraActivity () {
        Intent intent = new Intent (this, CameraActivity.class);
        startActivity (intent);
    }

    private void initSettingFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mSettingsFragment) {
            mSettingsFragment = new SettingsFragment ();
            transaction.add (R.id.main_layout_fragment, mSettingsFragment);
        }
        mSettingsFragment.setSettingFragListViewItemClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mSettingsFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mSettingsFragment;
        mTabLeft.setSelected (true);
        mTabCenter.setSelected (false);
        mTabRight.setSelected (false);
    }

    private void initAlarmFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mAlarmFragment) {
            mAlarmFragment = new AlarmFragment ();
            transaction.add (R.id.main_layout_fragment, mAlarmFragment);
        }
        mAlarmFragment.setAlarmFragListviewListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mAlarmFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mAlarmFragment;
        mTabCenter.setSelected (true);
        mTabLeft.setSelected (false);
        mTabRight.setSelected (false);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        //if (keyCode == KeyEvent.KEYCODE_BACK) {
        //    finish ();
        //    overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
        //}
        return super.onKeyDown (keyCode, event);
    }

    @Override
    public void go2EditInfoFragment (PersonalInfoBean personalInfoBean) {
        goToEditInfoFragment (personalInfoBean);
    }

    @Override
    public void personalBack2Setting () {
        mPersonalFragment = null;
        initSettingFragment ();
    }

    private void goToEditInfoFragment (PersonalInfoBean personalInfoBean) {
        if (null != mEditPersonalInfoFragment) {
            mEditPersonalInfoFragment = null;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mEditPersonalInfoFragment) {
            mEditPersonalInfoFragment = EditPersonalInfoFragment.getInstance (personalInfoBean);
            transaction.add (R.id.main_layout_fragment, mEditPersonalInfoFragment);
            mEditPersonalInfoFragment.setEditFragmentBackListener (this);
        }
        transaction.hide (mCurrentFragment);
        transaction.show (mEditPersonalInfoFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mEditPersonalInfoFragment;
    }

    @Override
    public void editFragmentBack () {
        back2PersonalFragment ();
    }

    private void back2PersonalFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mPersonalFragment) {
            mPersonalFragment = new PersonalFragment ();
            transaction.add (R.id.main_layout_fragment, mPersonalFragment);
        }
        mPersonalFragment.setONPersonalListViewClickLIstener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mPersonalFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mPersonalFragment;
        //mTabRight.setSelected (true);
        //mTabCenter.setSelected (false);
        //mTabLeft.setSelected (false);
    }

    @Override
    public void cutoverSettingFragment (int tag) {
        switch (tag) {
            case SettingsFragment.ADD_WATCH:
                initAddWatchFrag ();
                break;
            case SettingsFragment.CALIBRATE_HANDS:
                initCalibration ();
                break;
            case SettingsFragment.REMOTE_SHUTTER:
                toActivity (CameraActivity.class);
                break;
            case SettingsFragment.FAVORITE_CONTACTS:
                initContactFrag ();
                break;
            case SettingsFragment.DISCONNECT_ALERT:
                break;
            case SettingsFragment.HELP:
                initHelpFrag ();
                break;
            case SettingsFragment.NIXON_URL:
                break;
            case SettingsFragment.UNIT:
                initUnitFragment ();
                break;
            case SettingsFragment.ABOUT:
                initAboutFragment ();
                break;
            case SettingsFragment.CONNECTED_DEVICE:
                initDeviceFragment ();
                break;
            case SettingsFragment.MY_PROFILE:
                initPersonalFragment ();
                break;
            case SettingsFragment.TIME_ZONE:
                initTimeZoneFragment ();
                break;
        }
    }

    private void initTimeZoneFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mTimeZoneFragment) {
            mTimeZoneFragment = new TimeZoneFragment ();
            transaction.add (R.id.main_layout_fragment, mTimeZoneFragment);
        }
        mTimeZoneFragment.setTimeZoneBackListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mTimeZoneFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mTimeZoneFragment;
    }

    private void initPersonalFragment () {
        back2PersonalFragment ();
    }

    private void initAboutFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mAboutFragment) {
            mAboutFragment = new AboutFragment ();
            transaction.add (R.id.main_layout_fragment, mAboutFragment);
        }
        mAboutFragment.setAboutBackClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mAboutFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mAboutFragment;
    }

    private void initDeviceFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mDeviceInfoFragment) {
            mDeviceInfoFragment = new DeviceInfoFragment ();
            transaction.add (R.id.main_layout_fragment, mDeviceInfoFragment);
        }
        mDeviceInfoFragment.setDeviceBackListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mDeviceInfoFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mDeviceInfoFragment;
    }

    private void initUnitFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mUnitFragment) {
            mUnitFragment = new UnitFragment ();
            transaction.add (R.id.main_layout_fragment, mUnitFragment);
        }
        mUnitFragment.setUnitFragBackListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mUnitFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mUnitFragment;
    }

    private void initHelpFrag () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mHelpFragment) {
            mHelpFragment = new HelpFragment ();
            transaction.add (R.id.main_layout_fragment, mHelpFragment);
        }
        mHelpFragment.setHelpFragBackClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mHelpFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mHelpFragment;
    }

    private void initCalibration () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mCalibrationManagerFragment) {
            mCalibrationManagerFragment = new CalibrationManagerFragment ();
            transaction.add (R.id.main_layout_fragment, mCalibrationManagerFragment);
        }
        mCalibrationManagerFragment.setCalibrationBackClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mCalibrationManagerFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mCalibrationManagerFragment;
    }

    private void initAddWatchFrag () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mAddWatchManagerFragment) {
            mAddWatchManagerFragment = new AddWatchManagerFragment ();
            transaction.add (R.id.main_layout_fragment, mAddWatchManagerFragment);
        }
        mAddWatchManagerFragment.setAddWatchBackOnClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mAddWatchManagerFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mAddWatchManagerFragment;
    }

    private void initContactFrag () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mFavoriteContactFragment) {
            mFavoriteContactFragment = new FavoriteContactFragment ();
            transaction.add (R.id.main_layout_fragment, mFavoriteContactFragment);
        }
        mFavoriteContactFragment.setFavoriteBackOnClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mFavoriteContactFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mFavoriteContactFragment;
    }

    @Override
    public void favorite2Settings () {
        initSettingFragment ();
    }

    @Override
    public void AddWatch2Settings () {
        mAddWatchManagerFragment = null;
        initSettingFragment ();
    }

    @Override
    public void calibration2Settings () {
        destroyCalibration ();
        initSettingFragment ();
    }

    @Override
    public void backSettings () {
        initSettingFragment ();
    }

    @Override
    public void backTosetting () {
        initSettingFragment ();
    }

    private void destroyCalibration () {
        if (null != mCalibrationManagerFragment) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction ();
            transaction.remove (mCalibrationManagerFragment);
            transaction.commitAllowingStateLoss ();
            mCalibrationManagerFragment = null;
        }
    }

    @Override
    public void helpBack2Setting () {
        initSettingFragment ();
    }

    @Override
    public void go2AlarmListFrag (AlarmEventBean alarmEventBean, int mode) {
        //goToAlarmListFragment();
        goToAlarmManaFragment (alarmEventBean, mode);
    }

    private void goToAlarmManaFragment (AlarmEventBean alarmEventBean, int mode) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mAlarmManagerFragment) {
            mAlarmManagerFragment = new AlarmManagerFragment ();
            transaction.add (R.id.main_layout_fragment, mAlarmManagerFragment);
        }
        mAlarmManagerFragment.setAlarmMangBackClickListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mAlarmManagerFragment);
        transaction.commitAllowingStateLoss ();
        mAlarmManagerFragment.setAlarmEventBean (alarmEventBean, mode);
        mCurrentFragment = mAlarmManagerFragment;
    }

    @Override
    public void back2AlarmFragment () {
        initAlarmFragment ();
    }

    /**
     * 根据点击的位置判断是否隐藏键盘
     *
     * @return
     */
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if (ev.getAction () == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus ();
            if (inputIsShow (view, ev)) {
                if (null != mImm) {
                    mImm.hideSoftInputFromWindow (view.getWindowToken (), 0);
                }
            }
        }
        return super.dispatchTouchEvent (ev);
    }

    @Override
    public void aboutBack2Setting () {
        initSettingFragment ();
    }

    @Override
    public void TimeZoneBack2Settings () {
        initSettingFragment();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == Constant.SELECTSECONDVIEW_REQUEST_CODE){
                ChooseCityBean.ObjectBean objectBean = (ChooseCityBean.ObjectBean) data
                        .getSerializableExtra ("data");
                String city = objectBean.getCity ();
                String timezone = objectBean.getTimezone ();
                SPUtils.putString (this, Constant.SECOND_ZONE_KEY, timezone);
                SPUtils.putString (NixonApplication.getContext (), Constant.SECOND_ZONE_NAME_KEY,
                        objectBean.getShortName ());
                mTimeZoneFragment.setChooseCityContent(city,objectBean);
            }
        }
    }
}
