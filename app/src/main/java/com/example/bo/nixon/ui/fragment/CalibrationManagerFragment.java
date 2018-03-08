package com.example.bo.nixon.ui.fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.calibration.CalibrationContract;
import com.example.smartcustomview.adapter.SmartNoScrollPagerAdapter;
import com.example.smartcustomview.views.SmartNoScrollViewPager;
import com.example.smartcustomview.views.TimeWheelView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public class CalibrationManagerFragment extends BaseMvpFragment<CalibrationContract.CalibrationPresenter>
        implements CalibrationContract.CalibrationNixonView {
    @BindView(R.id.calibration_view_pager)
    SmartNoScrollViewPager mCalibrationViewPager;
    @BindView(R.id.calibration_next)
    TextView mCalibrationNext;
    @BindView(R.id.calibration_back)
    ImageView mCalibrationBack;
    private List<View> mData;
    private CalibrationBackClickListener mListener;
    private TimeWheelView mTimeWheelView;
    private int mTimeValue = 0;

    @Override
    protected CalibrationContract.CalibrationPresenter createPresenter() {
        return new CalibrationContract.CalibrationPresenter();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_calibration_manager;
    }

    @Override
    protected void initView() {
        super.initView();
        mData = new ArrayList<>();
        mData.add(View.inflate(NixonApplication.getContext(), R.layout.calibration_manager_pager1, null));
        mData.add(View.inflate(NixonApplication.getContext(), R.layout.calibration_manager_pager2, null));
        mData.add(View.inflate(NixonApplication.getContext(), R.layout.calibration_manager_pager3, null));
        mData.add(View.inflate(NixonApplication.getContext(), R.layout.calibration_manager_pager4, null));
        SmartNoScrollPagerAdapter adapter = new SmartNoScrollPagerAdapter(mData);
        mTimeWheelView = (TimeWheelView) mData.get(0).findViewById(R.id.time_select_date_view);
        initFont();
        mCalibrationViewPager.setAdapter(adapter);
    }

    private void initFont () {
        TextView tip1 = (TextView) mData.get (0).findViewById (R.id.calibration_1_tip);
        tip1.setTypeface (FontManager.mTf);
        TextView tip2 = (TextView) mData.get (1).findViewById (R.id.calibration_2_tip);
        tip2.setTypeface (FontManager.mTf);
        TextView tip3 = (TextView) mData.get (2).findViewById (R.id.calibration_3_tip);
        tip3.setTypeface (FontManager.mTf);
        TextView tip4 = (TextView) mData.get (3).findViewById (R.id.calibration_4_tip);
        tip4.setTypeface (FontManager.mTf);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        /*if (hidden && mCalibrationViewPager.getCurrentItem() != 3) {
            mCalibrationNext.setText(getResources().getString(R.string.next));
        }*/
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                        switch (mCalibrationViewPager.getCurrentItem()) {
                            case 0:
                                if (mListener != null) {
                                    mListener.calibration2Settings();
                                }
                                break;
                            case 1:
                                mCalibrationViewPager.setCurrentItem(0, false);
                                break;
                            case 2:
                                mCalibrationViewPager.setCurrentItem(1, false);
                                presenter.intoSmall(16,4);
                                break;
                            case 3:
                                mCalibrationViewPager.setCurrentItem(2, false);
                                presenter.intoSmall(17,4);
                                mCalibrationNext.setText(getResources().getString(R.string.next));
                                break;
                            default:
                                break;
                        }
                    return true;
                }
                return false;
            }
        });
    }

    @OnClick({R.id.calibration_back, R.id.calibration_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.calibration_back:
                switch (mCalibrationViewPager.getCurrentItem()) {
                    case 0:
                        if (mListener != null) {
                            mListener.calibration2Settings();
                        }
                        break;
                    case 1:
                        mCalibrationViewPager.setCurrentItem(0, false);
                        break;
                    case 2:
                        mCalibrationViewPager.setCurrentItem(1, false);
                        presenter.intoSmall(16,4);
                        break;
                    case 3:
                        mCalibrationViewPager.setCurrentItem(2, false);
                        presenter.intoSmall(17,4);
                        mCalibrationNext.setText(getResources().getString(R.string.next));
                        break;
                    default:
                        break;
                }
                break;
            case R.id.calibration_next:
                switch (mCalibrationViewPager.getCurrentItem()) {
                    case 0:
                        mCalibrationViewPager.setCurrentItem(1, false);
                        mTimeValue = mTimeWheelView.getSecTime();
                        presenter.intoSmall(16,4);
                        break;
                    case 1:
                        mCalibrationViewPager.setCurrentItem(2, false);
                        presenter.intoSmall(17,4);
                        break;
                    case 2:
                        mCalibrationViewPager.setCurrentItem(3, false);
                        mCalibrationNext.setText("Done");
                        presenter.intoSmall(15,4);
                        break;
                    case 3:
                        presenter.sendTime(1,mTimeValue, new Date());
                        presenter.outCalibration();
                        if (mListener != null) {
                            mListener.calibration2Settings();
                        }
                        break;
                    default:
                        break;
                }
                break;
        }
    }

    public void setCalibrationBackClickListener(CalibrationBackClickListener listener) {
        mListener = listener;
    }

    public interface CalibrationBackClickListener {
        void calibration2Settings();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("ARZE991","run0----->onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("ARZE991","run0----->onPause");
    }
}
