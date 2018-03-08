package com.example.bo.nixon.ui.activity.connect;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.BleDeviceAdapter;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.presenter.connect.ConnectContract;
import com.example.bo.nixon.ui.activity.CalibrationActivity;
import com.example.bo.nixon.ui.view.SmartToolbar;
import com.example.bo.nixon.utils.AnimationUtil;
import com.example.bo.nixon.utils.SmartPopWindow;
import com.smart.smartble.PermissionsUtils;
import com.smart.smartble.smartBle.BleDevice;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 15:14
 * @说明
 */
public class ConnectActivity extends BaseMvpActivity<ConnectContract.ConnectPresenter>
    implements ConnectContract.ConnectNixonView, BleDeviceAdapter.OnItemClickListener, View.OnClickListener,
    View.OnTouchListener {

    private static final String START_CODE = "START_CODE";
    private BleDeviceAdapter mBleDeviceAdapter;
    private List<BleDevice> mList = new ArrayList<> ();
    private SmartPopWindow mSmartPopWindow;
    private static final int REQUEST_PERMISSION_CODE = 0x0012;

    @BindView (R.id.connecting_img1) ImageView mConnectingImg1;
    @BindView (R.id.connecting_img2) ImageView mConnectingImg2;
    @BindView (R.id.connecting_img3) ImageView mConnectingImg3;
    @BindView (R.id.connecting_img4) ImageView mConnectingImg4;
    @BindView (R.id.connecting_img5) ImageView mConnectingImg5;
    @BindView (R.id.connecting_img6) ImageView mConnectingImg6;
    @BindView (R.id.connecting_img7) ImageView mConnectingImg7;
    @BindView (R.id.connecting_anim_img_rel) RelativeLayout mConnectingAnimImgRel;
    @BindView (R.id.connect_close_watch_tip_layout) ViewGroup mCloseTipLayout;
    @BindView (R.id.connect_connecting_show_layout) ViewGroup mConnectingLayout;
    @BindView (R.id.connect_device_show_recyclerView) RecyclerView mRecyclerView;
    @BindView (R.id.toolbar) SmartToolbar mSmartToolbar;
    @BindView (R.id.connect_status_show_tv) TextView mStatusTv;
    @BindView (R.id.conn_refresh) ImageView mConnRefresh;
    //@BindView (R.id.connect_refresh) SwipeRefreshLayout mConnectRefresh;

    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;
    private static final int MSG_WAVE4_ANIMATION = 4;
    private static final int MSG_WAVE5_ANIMATION = 5;
    private static final int MSG_WAVE6_ANIMATION = 6;
    private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3, mAnimationSet4, mAnimationSet5;

    Handler mHandler = new Handler () {

        @Override public void handleMessage (Message msg) {
            super.handleMessage (msg);
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mConnectingImg2.setVisibility (View.VISIBLE);
                    mConnectingImg2.startAnimation (mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mConnectingImg3.setVisibility (View.VISIBLE);
                    mConnectingImg3.startAnimation (mAnimationSet3);
                    break;
                case MSG_WAVE4_ANIMATION:
                    mConnectingImg4.setVisibility (View.VISIBLE);
                    mConnectingImg4.startAnimation (mAnimationSet4);
                    break;
                case MSG_WAVE5_ANIMATION:
                    mConnectingImg5.setVisibility (View.VISIBLE);
                    mConnectingImg5.startAnimation (mAnimationSet5);
                    break;
                case MSG_WAVE6_ANIMATION:
                    mConnectingImg6.setVisibility (View.VISIBLE);
                    mConnectingImg6.startAnimation (mAnimationSet5);
                    break;
            }
        }
    };
    private int mAnInt;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_connect);
        ButterKnife.bind (this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setToolbarColor (getResources ().getColor (R.color.main_text_color));
        }
        Bundle extras = getIntent ().getExtras ();
        if (null != extras) {
            mAnInt = extras.getInt (START_CODE);
        }
        initToolbar ();
        settingTip ();
        initRefresh ();
        init ();
        initListeners ();
        initAnimation ();
    }

    private void initRefresh () {
        //mConnectRefresh.setColorSchemeResources (R.color.colorPrimary, R.color.dialog_btn_text, R.color.sup_color,
        //    R.color.black);
        //mConnectRefresh.setRefreshing (false);
        //mConnectRefresh.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
        //    @Override public void onRefresh () {
        //        presenter.refreshSearch ();
        //        if (null != mBleDeviceAdapter) {
        //            mBleDeviceAdapter.notifyDataSetChanged ();
        //        }
        //        mHandler.postDelayed (new Runnable () {
        //            @Override public void run () {
        //                mConnectRefresh.setRefreshing (false);
        //            }
        //        }, 5000);
        //    }
        //});
    }

    private void initToolbar () {
        mSmartToolbar.setTittleColor (getResources ().getColor (R.color.white));
        mSmartToolbar.addBackView (LayoutInflater.from (this).inflate (R.layout.back_layout, null));
    }

    private void init () {
        mBleDeviceAdapter = new BleDeviceAdapter (this, mList);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setAdapter (mBleDeviceAdapter);
        mRecyclerView.setOnTouchListener (this);
        mBleDeviceAdapter.setOnItemClickListener (this);
        mSmartPopWindow = new SmartPopWindow (this, R.layout.pop_layout);
    }

    private void initListeners () {
    }

    private void settingTip () {
        if (mAnInt == 1) {
            mCloseTipLayout.setVisibility (View.GONE);
            mSmartToolbar.setTittle (getString (R.string.connect_near_devices));
            mRecyclerView.setVisibility (View.VISIBLE);
            //mConnectRefresh.setVisibility (View.VISIBLE);

        } else {
            mHandler.postDelayed (new Runnable () {
                @Override public void run () {
                    mCloseTipLayout.setVisibility (View.GONE);
                    mSmartToolbar.setTittle (getString (R.string.connect_near_devices));
                    mRecyclerView.setVisibility (View.VISIBLE);
                    //mConnectRefresh.setVisibility (View.VISIBLE);
                }
            }, 4 * 1000);
        }
    }

    @Override protected ConnectContract.ConnectPresenter createPresenter () {
        return new ConnectContract.ConnectPresenter ();
    }

    @Override protected void onDestroy () {
        super.onDestroy ();
        if (mSmartPopWindow.isShowing ()) {
            mSmartPopWindow.dismiss ();
        }
    }

    @Override public void addNewDevice (BleDevice bleDevice) {
        if (mList.contains (bleDevice)) {
            return;
        }
        mList.add (bleDevice);
        sortTheList (mList);
        mBleDeviceAdapter.notifyDataSetChanged ();
    }

    @Override public void connectingDevice () {
        mRecyclerView.setVisibility (View.GONE);
        //mConnectRefresh.setVisibility (View.GONE);
        mConnectingLayout.setVisibility (View.VISIBLE);
        mSmartToolbar.setTittle (getString (R.string.connect_add_watch));
        showConnectingAnim ();
    }

    @Override public void connectDevice () {
        if (!mSmartPopWindow.isShowing ()) mSmartPopWindow.showAtLocation (mCloseTipLayout, Gravity.CENTER, 0, 0);
    }

    @Override public void authorSuccessfully () {
        mSmartPopWindow.dismiss ();
        clearConnectionAnim ();
        ToastManager.show (this, getString (R.string.authorization_sucessful), Toast.LENGTH_SHORT);
        mStatusTv.setText (getString (R.string.connect_paired));
        mConnectingImg7.setImageResource (R.drawable.img_bg_finish);
        //bindDevice();
        mHandler.postDelayed (new Runnable () {
            @Override public void run () {
                Intent intent = new Intent (ConnectActivity.this, CalibrationActivity.class);
                startActivity (intent);
                finish ();
            }
        }, 2 * 1000);
    }

    @Override public void authorTimeOut () {
        mSmartPopWindow.dismiss ();
        clearConnectionAnim ();
        mStatusTv.setText (getString (R.string.connect_paired_fail));
        mHandler.postDelayed (new Runnable () {
            @Override public void run () {
                Intent intent = new Intent (ConnectActivity.this, ConnectActivity.class);
                intent.putExtra (START_CODE, 1);
                startActivity (intent);
                finish ();
            }
        }, 2 * 1000);
    }

    @Override public void connectFail () {
        clearConnectionAnim ();
        ToastManager.show (NixonApplication.getContext (), "connect fail", Toast.LENGTH_SHORT);
    }

    @Override public void leSanEnd () {
        //mConnectRefresh.setRefreshing (false);
    }

    @Override public void leSanStart () {
        //mConnectRefresh.setRefreshing (true);
    }

    private void bindDevice () {

    }

    private void sortTheList (List<BleDevice> list) {
        Collections.sort (list, new SortRssi ());
    }

    @Override public void onItem (int position) {
        BleDevice bleDevice = mList.get (position);
        presenter.connectDevice (bleDevice);
    }

    @Override public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.toolbar_center_tv:
                toActivity (ConnectActivity.class);
                break;
        }
    }

    @Override protected void onResume () {
        super.onResume ();
        PermissionsUtils.requestPermissions (new String[] {
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        }, REQUEST_PERMISSION_CODE, this);
    }

    @Override public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (REQUEST_PERMISSION_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate ();
            } else {
                finish ();
            }
        }
    }

    private void initAnimation () {
        mAnimationSet1 = AnimationUtil.initAnimationSet (5, OFFSET);
        mAnimationSet2 = AnimationUtil.initAnimationSet (4, OFFSET);
        mAnimationSet3 = AnimationUtil.initAnimationSet (3, OFFSET);
        mAnimationSet4 = AnimationUtil.initAnimationSet (2, OFFSET);
        mAnimationSet5 = AnimationUtil.initAnimationSet (1, OFFSET);
    }

    private void showConnectingAnim () {
        mConnectingImg7.startAnimation (new AlphaAnimation (1, 0.0f));
        mConnectingImg1.startAnimation (mAnimationSet1);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE2_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE3_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE4_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE5_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed (MSG_WAVE6_ANIMATION, OFFSET);
    }

    private void clearConnectionAnim () {
        mConnectingImg1.clearAnimation ();
        mConnectingImg2.clearAnimation ();
        mConnectingImg3.clearAnimation ();
        mConnectingImg4.clearAnimation ();
        mConnectingImg5.clearAnimation ();
        mConnectingImg6.clearAnimation ();
        mConnectingImg7.clearAnimation ();
    }

    float y = 0;

    @Override public boolean onTouch (View v, MotionEvent event) {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager ();
        switch (event.getAction ()) {
            case MotionEvent.ACTION_DOWN:
                y = event.getY ();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                Log.d ("BLENEARREHRESH", "开始刷新  ......." + (event.getY () - y));
                if (event.getY () - y > 200 && linearLayoutManager.findFirstVisibleItemPosition () == 0) {
                    refreshing ();
                }
                break;
        }
        return false;
    }

    private void refreshing () {
        Log.d ("BLENEARREHRESH", "开始刷新  .......");
        presenter.refreshSearch ();
        mBleDeviceAdapter.clearData ();
        mConnRefresh.setVisibility (View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation (NixonApplication.getContext (), R.anim.rotate);
        mConnRefresh.startAnimation (animation);
        mHandler.postDelayed (new Runnable () {
            @Override public void run () {
                mConnRefresh.setVisibility (View.GONE);
                mConnRefresh.clearAnimation ();
            }
        }, 5000);
    }
}
