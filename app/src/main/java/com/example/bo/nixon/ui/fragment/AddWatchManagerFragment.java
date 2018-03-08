package com.example.bo.nixon.ui.fragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.smart.smartble.smartBle.BleDevice;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public class AddWatchManagerFragment extends BaseFragment
    implements BleNearByFragment.BleItemClickListener, ConnectFragment.ConnectResultListener {
    @BindView (R.id.add_watch_back_arrow) ImageView mAddWatchBackArrow;
    @BindView (R.id.add_watch_textView) TextView mAddWatchTextView;
    @BindView (R.id.add_watch_back_tv) TextView mAddWatchBackTv;
    private BleNearByFragment mBleNearByFragment;
    private Fragment mCurrentFragment;
    private ConnectFragment mConnectFragment;
    private AddWatchBackOnClickListener mListener;
    private boolean isConnect;
    private Handler mHandler;

    @Override public int getLayoutResId () {
        return R.layout.fragment_add_watch_manager;
    }

    @Override protected void initView () {
        super.initView ();
        initFragment ();
    }

    @Override public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (!hidden){
//            initFragment();
        }else{
            mHandler.removeCallbacksAndMessages (null);
        }
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mListener != null) {
                        mListener.AddWatch2Settings ();
                        //mBleNearByFragment = null;
                        destroyAlarmFragment ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void destroyAlarmFragment () {
        if (null != mBleNearByFragment) {
            FragmentTransaction transaction = mFragmentManager.beginTransaction ();
            transaction.remove (mBleNearByFragment);
            transaction.commitAllowingStateLoss ();
            mBleNearByFragment = null;
        }
    }


    private void initFragment () {
        mHandler = NixonApplication.getmainThreadHandler ();
        final FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        mHandler.postDelayed (new Runnable () {
            @Override public void run () {
                if (mAddWatchTextView != null){
                    mAddWatchTextView.setVisibility (View.GONE);
                }

                if (null == mBleNearByFragment) {
                    mBleNearByFragment = new BleNearByFragment ();
                    transaction.add (R.id.add_watch_fraglayout, mBleNearByFragment);
                }
                mBleNearByFragment.setBleItemClickListener (AddWatchManagerFragment.this);
                transaction.show (mBleNearByFragment);
                transaction.commit ();
                mCurrentFragment = mBleNearByFragment;
            }
        }, 2000);
    }

    public void setAddWatchBackOnClickListener (AddWatchBackOnClickListener listener) {
        mListener = listener;
    }

    @OnClick ({ R.id.add_watch_back_arrow, R.id.add_watch_right }) public void onViewClicked () {
        if (isConnect) {
            back2BleBear1 ();
            mAddWatchBackTv.setVisibility (View.GONE);
            isConnect = false;
        } else {
            if (mListener != null) {
                mListener.AddWatch2Settings ();
                destroyAlarmFragment();
            }
        }
    }

    private void back2BleBear1 () {
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mBleNearByFragment) {
            mBleNearByFragment = new BleNearByFragment ();
            transaction.add (R.id.add_watch_fraglayout, mBleNearByFragment);
        }
        mBleNearByFragment.setBleItemClickListener (AddWatchManagerFragment.this);
        transaction.hide (mCurrentFragment);
        transaction.show (mBleNearByFragment);
        transaction.commit ();
        mCurrentFragment = mBleNearByFragment;
    }

    @Override public void toConnectBle (BleDevice bleNearBean) {

    }

    @Override public void connectBleDevice () {

    }

    @Override public void authorBleSuccessfully () {

    }

    @Override public void authorTimeOut () {
        back2BleBear ();
    }

    private void go2ConnectFragment () {
        isConnect = true;
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mConnectFragment) {
            mConnectFragment = new ConnectFragment ();
            transaction.add (R.id.add_watch_fraglayout, mConnectFragment);
        }
        mConnectFragment.setConnectResultListener (this);
        transaction.hide (mCurrentFragment);
        transaction.show (mConnectFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mConnectFragment;
    }

    private synchronized void back2BleBear () {
        mBleNearByFragment = null;
        if (mCurrentFragment.isHidden ()){
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction ();
        if (null == mBleNearByFragment) {
            mBleNearByFragment = new BleNearByFragment ();
            transaction.add (R.id.add_watch_fraglayout, mBleNearByFragment);
        }
        mBleNearByFragment.setBleItemClickListener (AddWatchManagerFragment.this);
        transaction.hide (mCurrentFragment);
        transaction.show (mBleNearByFragment);
        transaction.commitAllowingStateLoss ();
        mCurrentFragment = mBleNearByFragment;
    }

    @Override public void onDestroyView () {
        super.onDestroyView ();
        mHandler.removeCallbacksAndMessages (null);
    }

    @Override public void connectSucc () {
        isConnect = false;
    }

    @Override public void connectFail () {
        isConnect = true;
    }

    public interface AddWatchBackOnClickListener {
        void AddWatch2Settings ();
    }
}
