package com.example.bo.nixon.ui.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.BleNearListViewAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.presenter.connect.ConnectContract;
import com.example.bo.nixon.ui.activity.connect.SortRssi;
import com.example.bo.nixon.utils.SmartPopWindow;
import com.smart.smartble.smartBle.BleDevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public class BleNearByFragment extends BaseMvpFragment<ConnectContract.ConnectPresenter>
        implements ConnectContract.ConnectNixonView, AdapterView.OnItemClickListener {

    @BindView(R.id.conn_frag_title)
    TextView mConnFragTitle;
    @BindView(R.id.img1)
    ImageView mImg1;
    @BindView(R.id.img2)
    ImageView mImg2;
    @BindView(R.id.img3)
    ImageView mImg3;
    @BindView(R.id.img4)
    ImageView mImg4;
    @BindView(R.id.img5)
    ImageView mImg5;
    @BindView(R.id.img6)
    ImageView mImg6;
    @BindView(R.id.img7)
    ImageView mImg7;
    @BindView(R.id.conn_connecting)
    TextView mConnConnecting;
    @BindView(R.id.anim_img_rel)
    RelativeLayout mAnimImgRel;
    @BindView(R.id.connect_connecting_show_layout)
    FrameLayout mConnectConnectingShowLayout;
    private List<BleDevice> mList = new ArrayList<>();
    @BindView(R.id.ble_near_by_list_view)
    ListView mBleNearByListView;
    @BindView(R.id.ble_near_by_ll)
    LinearLayout mBleNearByLl;
    @BindView(R.id.ble_near_by_frag)
    FrameLayout mBleNearByFrag;
    //@BindView (R.id.ble_near_by_refresh) SwipeRefreshLayout mBleNearByRefresh;
    @BindView(R.id.ble_near_sel_refresh)
    ImageView mBleNearSelRefresh;
    private BleNearListViewAdapter mAdapter;
    private static final int OFFSET = 1000;  //每个动画的播放时间间隔
    private static final int MSG_WAVE2_ANIMATION = 2;
    private static final int MSG_WAVE3_ANIMATION = 3;
    private static final int MSG_WAVE4_ANIMATION = 4;
    private static final int MSG_WAVE5_ANIMATION = 5;
    private static final int MSG_WAVE6_ANIMATION = 6;
    private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3, mAnimationSet4, mAnimationSet5, mAnimationSet6;
    private SmartPopWindow mSmartPopWindow;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WAVE2_ANIMATION:
                    mImg2.setVisibility(View.VISIBLE);
                    mImg2.startAnimation(mAnimationSet2);
                    break;
                case MSG_WAVE3_ANIMATION:
                    mImg3.setVisibility(View.VISIBLE);
                    mImg3.startAnimation(mAnimationSet3);
                    break;
                case MSG_WAVE4_ANIMATION:
                    mImg4.setVisibility(View.VISIBLE);
                    mImg4.startAnimation(mAnimationSet4);
                    break;
                case MSG_WAVE5_ANIMATION:
                    mImg5.setVisibility(View.VISIBLE);
                    mImg5.startAnimation(mAnimationSet5);
                    break;
                case MSG_WAVE6_ANIMATION:
                    mImg6.setVisibility(View.VISIBLE);
                    mImg6.startAnimation(mAnimationSet6);
                    break;
            }
        }
    };
    private BleDevice mBleNearBean;

    @Override
    protected ConnectContract.ConnectPresenter createPresenter() {
        return new ConnectContract.ConnectPresenter();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_ble_near_by;
    }

    @Override
    protected void initView() {
        super.initView();
        mSmartPopWindow = new SmartPopWindow(NixonApplication.getContext(), R.layout.pop_layout_white);
        mAnimationSet1 = initAnimationSet(5);
        mAnimationSet2 = initAnimationSet(4);
        mAnimationSet3 = initAnimationSet(3);
        mAnimationSet4 = initAnimationSet(2);
        mAnimationSet5 = initAnimationSet(1);
        mAnimationSet6 = initAnimationSet(6);
        initRefresh();
        initAdapter();
    }

    private void initRefresh() {
        //mBleNearByRefresh.setColorSchemeResources (R.color.colorPrimary, R.color.dialog_btn_text, R.color.sup_color,
        //    R.color.black);
        //mBleNearByRefresh.setRefreshing (false);
        //mBleNearByRefresh.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
        //    @Override public void onRefresh () {
        //        presenter.refreshSearch ();
        //        if (null != mAdapter) {
        //            mAdapter.notifyDataSetChanged ();
        //        }
        //        mHandler.postDelayed (new Runnable () {
        //            @Override public void run () {
        //                mBleNearByRefresh.setRefreshing (false);
        //            }
        //        }, 5000);
        //    }
        //});
    }

    private AnimationSet initAnimationSet(int i) {
        AnimationSet as = new AnimationSet(true);
        ScaleAnimation sa = new ScaleAnimation(1f, i * 25f, 1f, i * 25f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(OFFSET * 2);
        sa.setRepeatCount(Animation.INFINITE);// 设置循环
        AlphaAnimation aa = new AlphaAnimation(0.2f, 0.01f);
        aa.setDuration(OFFSET * 2);
        aa.setRepeatCount(Animation.INFINITE);//设置循环
        as.addAnimation(sa);
        as.addAnimation(aa);
        return as;
    }

    private void initAdapter() {
        mAdapter = new BleNearListViewAdapter(mList);
        mBleNearByListView.setAdapter(mAdapter);
        mBleNearByListView.setOnItemClickListener(this);
        mBleNearByListView.setOnTouchListener(new View.OnTouchListener() {
            float y = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        y = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d("BLENEARREHRESH", "Math.abs (y - event.getY ())  ==  " + Math.abs(y - event.getY()));
                        if (event.getY() - y >= 200 && mBleNearByListView.getFirstVisiblePosition() == 0) {
                            refreshing();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private long lastRefreshTime;

    private void refreshing() {
        Log.d("BLENEARREHRESH", "开始刷新  ......." + mBleNearByListView.getFirstVisiblePosition());
        presenter.refreshSearch();
        mAdapter.clearData();
        mBleNearSelRefresh.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(NixonApplication.getContext(), R.anim.rotate);
        mBleNearSelRefresh.startAnimation(animation);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mBleNearSelRefresh != null) {
                    mBleNearSelRefresh.setVisibility(View.GONE);
                    mBleNearSelRefresh.clearAnimation();
                }
            }
        }, 5000);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mListener != null) {
            mBleNearBean = mList.get(i);
            presenter.connectDevice(mBleNearBean);
            //mListener.toConnectBle (bleNearBean);
        }
    }

    private void showWaveAnimation() {
        if (null == mImg7)
            return;
        mImg7.startAnimation(getAlphaAnim());
        mImg1.startAnimation(mAnimationSet1);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE4_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE5_ANIMATION, OFFSET);
        mHandler.sendEmptyMessageDelayed(MSG_WAVE6_ANIMATION, OFFSET);
    }

    private void clearWaveAnimation() {
        if (null == mImg1)
            return;
        mImg1.clearAnimation();
        mImg2.clearAnimation();
        mImg3.clearAnimation();
        mImg4.clearAnimation();
        mImg5.clearAnimation();
        mImg6.clearAnimation();
        mImg7.clearAnimation();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            clearWaveAnimation();
        }
    }

    private AlphaAnimation getAlphaAnim() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.01f);
        return alphaAnimation;
    }

    private void connecting() {
        mBleNearByFrag.setVisibility(View.GONE);
        mConnectConnectingShowLayout.setVisibility(View.VISIBLE);
        showWaveAnimation();
        mImg7.setImageResource(R.drawable.img_point);
        mConnFragTitle.setVisibility(View.VISIBLE);
        mConnConnecting.setText(R.string.connect_connecting);
    }

    private void connectFailed() {
        clearWaveAnimation();
        mImg7.setImageResource(R.drawable.img_bg_finish);
        mConnFragTitle.setVisibility(View.GONE);
        mConnConnecting.setText(R.string.failed);
    }

    private void connectSuccessed() {
        clearWaveAnimation();
        mImg7.setImageResource(R.drawable.img_bg_finish);
        mConnFragTitle.setVisibility(View.GONE);
        mConnConnecting.setText(R.string.paired);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != mListener) {
                    mListener.authorBleSuccessfully();
                    if (null != getActivity()) getActivity().finish();
                }
            }
        }, 2000);
    }

    private BleItemClickListener mListener;

    public void setBleItemClickListener(BleItemClickListener listener) {
        mListener = listener;
    }

    @Override
    public void addNewDevice(BleDevice bleDevice) {
        if (mList.contains(bleDevice)) {
            return;
        }
        mList.add(bleDevice);
        sortTheList(mList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void connectingDevice() {
        connecting();
    }

    @Override
    public void connectDevice() {
        //mBleNearByRefresh.setRefreshing (false);
        if (!mSmartPopWindow.isShowing()) {
            mSmartPopWindow.showAtLocation(mBleNearByFrag, Gravity.CENTER, 0, 0);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mSmartPopWindow && mSmartPopWindow.isShowing()) {
            mSmartPopWindow.dismiss();
        }
        if (null != presenter) {
            presenter.disConnectDevice(mBleNearBean);
            mHandler.removeCallbacks(null);
            presenter.removeAllListener();
        }
    }

    @Override
    public void authorSuccessfully() {
        if (mSmartPopWindow.isShowing()) {
            mSmartPopWindow.dismiss();
        }
        connectSuccessed();
    }

    @Override
    public void authorTimeOut() {
        clearWaveAnimation();
        mConnConnecting.setText(R.string.connect_paired_fail);
        mSmartPopWindow.dismiss();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != mListener) {
                    mListener.authorTimeOut();
                }
            }
        }, 2 * 1000);
    }

    @Override
    public void connectFail() {
        //mBleNearByRefresh.setRefreshing (false);
        clearWaveAnimation();
        ToastManager.show(NixonApplication.getContext(), "connect fail", Toast.LENGTH_SHORT);
    }

    @Override
    public void leSanEnd() {
        //mBleNearByRefresh.setRefreshing (false);
    }

    @Override
    public void leSanStart() {
        //mBleNearByRefresh.setRefreshing (true);
    }

    private void sortTheList(List<BleDevice> list) {
        Collections.sort(list, new SortRssi());
    }

    public interface BleItemClickListener {

        void toConnectBle(BleDevice bleNearBean);

        void connectBleDevice();

        void authorBleSuccessfully();

        void authorTimeOut();
    }
}
