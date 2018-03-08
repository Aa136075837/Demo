package com.example.bo.nixon.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.DeviceInfoAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.presenter.DeviceInfoContract;
import com.example.bo.nixon.ui.view.SmartToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/6/19.
 * @desc
 */

public class DeviceInfoFragment extends BaseMvpFragment<DeviceInfoContract.DeviceInfoPresenter>
    implements AdapterView.OnItemClickListener, DeviceDetailFragment.DeviceDetailBackListener,
    DeleteHistoryFragment.DeleteBackClickListener, DeviceInfoContract.DeviceInfoNixonView {
    public static final int DISCONNECTED = 1123;
    public static final int BATTERY = 1124;
    public static final int DEVICE_DETAIL = 1125;
    public static final int DELETE_HISTORY = 1126;
    public static final int SN_CODE = 1127;
    @BindView (R.id.device_frag_list) ListView mDeviceFragList;
    @BindView (R.id.device_info_fram) FrameLayout mDeviceInfoFram;
    @BindView (R.id.device_frag_arrow) ImageView mDeviceFragArrow;
    @BindView (R.id.device_frag_content) LinearLayout mDeviceFragContent;
    private List<PersonalInfoBean> mData;
    private DeviceDetailFragment mDeviceDetailFragment;
    private Fragment mCurrentFragment;
    private PersonalInfoBean mDetailBean;
    private DeviceInfoAdapter mAdapter;
    private DeleteHistoryFragment mDeleteHistoryFragment;

    @Override protected DeviceInfoContract.DeviceInfoPresenter createPresenter () {
        return new DeviceInfoContract.DeviceInfoPresenter ();
    }

    @Override public int getLayoutResId () {
        return R.layout.fragment_device_info;
    }

    @Override protected void initView () {
        super.initView ();
        initData ();
    }

    private void initData () {
        mData = new ArrayList<> ();
        mData.add (new PersonalInfoBean (getResources ().getString (R.string.disconnected), DISCONNECTED));
        mData.add (new PersonalInfoBean (getResources ().getString (R.string.battery), BATTERY));
        mDetailBean = new PersonalInfoBean (getResources ().getString (R.string.device_details), DEVICE_DETAIL);
        mData.add (mDetailBean);
        mData.add (new PersonalInfoBean (getResources ().getString (R.string.delete_history), DELETE_HISTORY));
//        mData.add (new PersonalInfoBean (getResources ().getString (R.string.sn_code),SN_CODE));
        mAdapter = new DeviceInfoAdapter (mData);
        mDeviceFragList.setAdapter (mAdapter);
        mDeviceFragList.setOnItemClickListener (this);
    }

    @Override public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {
        switch (mData.get (i).getTag ()) {
            case DISCONNECTED:
                show2NewDialog (R.string.disconnect_device, mOnClickListener);
                break;
            case BATTERY:
                break;
            case DEVICE_DETAIL:
//                goToDeviceDetailFragment ();
                break;
            case DELETE_HISTORY:
//                goToDeleteHistoryFragment ();
                showDialog ();
                break;
        }
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener () {
        @Override public void onClick (View view) {
            switch (view.getId ()) {
                case R.id.dialog_confirm_tv:
                    disconnectDevice ();
                    break;
                case R.id.dialog_cancel_tv:
                    hideDialog ();
                    break;
            }
        }
    };

    /**
     * 断开手表
     */
    private void disconnectDevice () {
        presenter.disConnectDevice ();
    }

    private void goToDeviceDetailFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mDeviceDetailFragment) {
            mDeviceDetailFragment = new DeviceDetailFragment ();
            transaction.add (R.id.device_info_fram, mDeviceDetailFragment);
        }
        if (mCurrentFragment != null) {
            transaction.hide (mCurrentFragment);
        }
        mDeviceDetailFragment.setDeviceDetailBackListener (this);
        transaction.show (mDeviceDetailFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mDeviceDetailFragment;
        mDeviceFragContent.setVisibility (View.GONE);
    }

    private void goToDeleteHistoryFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mDeleteHistoryFragment) {
            mDeleteHistoryFragment = new DeleteHistoryFragment ();
            transaction.add (R.id.device_info_fram, mDeleteHistoryFragment);
        }
        if (mCurrentFragment != null) {
            transaction.hide (mCurrentFragment);
        }
        mDeleteHistoryFragment.setDeleteBackClickListener (this);
        transaction.show (mDeleteHistoryFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mDeleteHistoryFragment;
        mDeviceFragContent.setVisibility (View.GONE);
    }

    private void backFragment () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        transaction.hide (mCurrentFragment);
        transaction.commitAllowingStateLoss ();
        mDeviceFragContent.setVisibility (View.VISIBLE);
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != mListener) {
                        mListener.backTosetting ();
                    }
                    return true;
                }
                return false;
            }
        });
    }


    @OnClick ({R.id.device_frag_arrow,R.id.device_info_right}) public void onClick () {
        if (null != mListener) {
            mListener.backTosetting ();
        }
    }

    @Override public void deviceDetailBack (String name) {

        backFragment ();
        if (!TextUtils.isEmpty (name)) {
            mDetailBean.setTextRight (name);
            mAdapter.notifyDataSetChanged ();
        }
    }

    public void showDialog () {
        show2NewDialog (R.string.delete_history_tip, new View.OnClickListener () {
            @Override public void onClick (View view) {
                switch (view.getId ()) {
                    case R.id.dialog_confirm_tv:
                        presenter.deleteFile ();
                        presenter.clearCache (getActivity ());
                        hideDialog ();
                        break;
                    case R.id.dialog_cancel_tv:
                        hideDialog ();
                        break;
                }
            }
        });
    }

    public void setDeviceBackListener (DeviceBackListener listener) {
        mListener = listener;
    }

    private DeviceBackListener mListener;

    @Override public void backTo () {
        backFragment ();
    }

    @Override public void disConnected () {
        hideDialog ();
        if (mListener != null) {
            mListener.backTosetting ();
        }
    }

    @Override public void showToast () {
        SmartToast smartToast =
            SmartToast.getInstanse (getActivity (), getResources ().getString (R.string.delete_toast));
        smartToast.show ();
    }

    public interface DeviceBackListener {
        void backTosetting ();
    }
}
