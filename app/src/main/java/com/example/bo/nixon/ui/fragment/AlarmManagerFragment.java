package com.example.bo.nixon.ui.fragment;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.ui.fragment.alarm.AlarmCacheHelper;
import com.example.bo.nixon.ui.fragment.alarm.AlarmListProxy;
import com.example.bo.nixon.ui.fragment.alarm.AlarmRepeatHelper;
import com.example.bo.nixon.ui.fragment.alarm.AlarmRepeatProxy;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.smartcustomview.adapter.SmartNoScrollPagerAdapter;
import com.example.smartcustomview.views.SmartNoScrollViewPager;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/9.
 * @desc
 */

public class AlarmManagerFragment extends BaseFragment implements AlarmListProxy.AlarmListClickListener, ViewPager.OnPageChangeListener {

    private static String TAG = "AlarmManagerFragment";
    public static final int ADD_ALARM = 0x0001;
    public static final int SET_ALARM = 0X0002;

    @BindView(R.id.alarm_manager_arrow)
    ImageView mAlarmManagerArrow;
    @BindView(R.id.alarm_manager_cancel)
    TextView mAlarmManagerCancel;
    @BindView(R.id.alarm_manager_confirm)
    TextView mAlarmManagerDelete;
    @BindView(R.id.alarm_manager_ll)
    LinearLayout mAlarmManagerLl;
    @BindView(R.id.alarm_manager_pager)
    SmartNoScrollViewPager mAlarmManagerPager;

    private EditText mEditText;

    private List<View> mDate = new ArrayList<>();
    private AlarmListProxy mAlarmListProxy;
    private AlarmRepeatProxy mAlarmRepeatProxy;
    private AlarmEventBean mAlarmEventBean;
    private int mMode = ADD_ALARM;
    private String mEvent;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_alarm_manager;
    }

    @Override
    protected void initView() {
        super.initView();
        initPager();
        initAlarm(mAlarmEventBean);
        bindView();
    }

    @Override public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (!hidden){
            initAlarm(mAlarmEventBean);
        }
    }

    private void bindView() {
        if (null != mAlarmListProxy) {
            mEvent = mEditText.getText ().toString ();
            if (TextUtils.isEmpty (mEvent)){
                //mEvent = SPUtils.getString (NixonApplication.getContext (),Constant.ALARM_EVENT,"alarm");
                mEvent = mAlarmEventBean.getmAction ();
            }
            mAlarmListProxy.setAction(mEvent);
            String repeat = AlarmRepeatHelper.getRepeatString (getActivity (), mAlarmRepeatProxy.getRepeat ());
            mAlarmListProxy.setRepeat(TextUtils.isEmpty (repeat) ? getResources ().getString (R.string.alarm_item_tip) : repeat);
            Log.e ("ALARMEVENT",AlarmRepeatHelper.getRepeatString(getActivity(), mAlarmRepeatProxy.getRepeat()));
        }
    }

    private void initPager() {
        mAlarmListProxy = new AlarmListProxy();
        mAlarmListProxy.setAlarmListClickListener(this);
        mDate.add(mAlarmListProxy.getView());
        mDate.add(View.inflate(NixonApplication.getContext(), R.layout.fragment_alarm_event, null));
        mAlarmRepeatProxy = new AlarmRepeatProxy();
        mDate.add(mAlarmRepeatProxy.getView());
        mEditText = (EditText) mDate.get(1).findViewById(R.id.alarm_action_edit);
        SmartNoScrollPagerAdapter adapter = new SmartNoScrollPagerAdapter(mDate);
        mAlarmManagerPager.setAdapter(adapter);
        mAlarmManagerPager.addOnPageChangeListener(this);
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    switch (mAlarmManagerPager.getCurrentItem()) {
                        case 0://回调给SettingActivity 切换Fragment
                            if (mListener != null) {
                                mListener.back2AlarmFragment();
                            }
                            break;
                        case 1:
                        case 2:
                            mAlarmManagerPager.setCurrentItem(0, false);
                            break;
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.alarm_manager_arrow, R.id.alarm_manager_cancel, R.id.alarm_manager_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.alarm_manager_arrow:
                switch (mAlarmManagerPager.getCurrentItem()) {
                    case 0://回调给SettingActivity 切换Fragment
                        if (mListener != null) {
                            mListener.back2AlarmFragment();
                        }
                        break;
                    case 1:
                    case 2:
                        mAlarmManagerPager.setCurrentItem(0, false);
                        break;
                }
                break;
            case R.id.alarm_manager_cancel:
                if (0 != mAlarmManagerPager.getCurrentItem()) {
                    mAlarmManagerPager.setCurrentItem(0);
                } else {
                    if (null != mListener)
                        mListener.back2AlarmFragment();
                }
                break;
            case R.id.alarm_manager_confirm:
                SPUtils.putString (NixonApplication.getContext (), Constant.ALARM_EVENT,mEvent);
                if (0 != mAlarmManagerPager.getCurrentItem()) {
                    mAlarmManagerPager.setCurrentItem(0);
                } else {
                    dealAlarm();
                    if (null != mListener)
                        mListener.back2AlarmFragment();
                }
                break;
        }
    }

    private void dealAlarm() {
        if (ADD_ALARM == mMode) {
            addAlarm();
        } else {
            setAlarm();
        }
    }

    private void setAlarm() {
        int time = mAlarmListProxy.getSelectTime();
        boolean open = mAlarmEventBean.isOpenType();
        int repeatTime = mAlarmRepeatProxy.getRepeat();
        int repeat = (repeatTime & 128) >> 7;
        String action = mAlarmListProxy.getAction();
        mAlarmEventBean.setAlarmTime(time);
        mAlarmEventBean.setOpenType(open);
        mAlarmEventBean.setRepeat(repeat == 1);
        mAlarmEventBean.setRepeatTime(repeatTime);
        mAlarmEventBean.setmAction(action);
        Log.w(TAG, "run------------->" + mAlarmEventBean.getIndex());
        AlarmCacheHelper.setAlarm(getActivity(), mAlarmEventBean.getIndex(), mAlarmEventBean);
        //AlarmCacheHelper.updateAlarm2Server (mAlarmEventBean);
    }

    private void addAlarm() {
        int time = mAlarmListProxy.getSelectTime();
        int open = 1;
        int repeatTime = mAlarmRepeatProxy.getRepeat();
        int repeat = (repeatTime & 128) >> 7;
        String action = mAlarmListProxy.getAction();
        AlarmEventBean eventBean = new AlarmEventBean(time, open == 1, repeat == 1, repeatTime, action);
        AlarmCacheHelper.addAlarm(getActivity(), eventBean);
    }

    @Override
    public void repeatClick() {
        mAlarmManagerPager.setCurrentItem(2, false);
    }

    @Override
    public void eventClick() {
        mAlarmManagerPager.setCurrentItem(1, false);
    }

    private AlarmMangBackClickListener mListener;

    public void setAlarmMangBackClickListener(AlarmMangBackClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (0 == position) {
            bindView();
            mAlarmListProxy.doScrollView(1,20);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface AlarmMangBackClickListener {
        void back2AlarmFragment();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void setAlarmEventBean(AlarmEventBean alarmEventBean, int mode) {
        mAlarmEventBean = alarmEventBean;
        mMode = mode;
        if (null != mAlarmListProxy) {
            initAlarm(alarmEventBean);
            bindView();
        }
    }

    private void initAlarm(@NonNull AlarmEventBean alarmEventBean) {
        mAlarmListProxy.setAction(alarmEventBean.getmAction());
        mAlarmListProxy.setTime(alarmEventBean.getAlarmTime());
        mAlarmRepeatProxy.setRepeat(alarmEventBean.getRepeatTime());
        if (null != mEditText) {
            mEditText.setHint(alarmEventBean.getmAction());
        }
    }
}
