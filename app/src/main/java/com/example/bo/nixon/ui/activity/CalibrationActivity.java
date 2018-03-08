package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.calibration.CalibrationContract;
import com.example.smartcustomview.adapter.SmartNoScrollPagerAdapter;
import com.example.smartcustomview.views.SmartNoScrollViewPager;
import com.example.smartcustomview.views.TimeWheelView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CalibrationActivity extends
        BaseMvpActivity<CalibrationContract.CalibrationPresenter> implements ViewPager.OnPageChangeListener,
        CalibrationContract.CalibrationNixonView {

    @BindView(R.id.act_calibration_view_pager)
    SmartNoScrollViewPager mActCalibrationViewPager;
    @BindView(R.id.act_calibration_next)
    TextView mActCalibrationNext;
    @BindView(R.id.act_calibration_back)
    ImageView mActCalibrationBack;
    @BindView(R.id.act_calibration_skip)
    TextView mActCalibrationSkip;

    private int mTimeValue = 0;
    private TimeWheelView mTimeWheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        ButterKnife.bind(this);
        FontManager.changeFonts((ViewGroup) findViewById(R.id.activity_calibration));
        initPager();
    }

    @Override
    protected CalibrationContract.CalibrationPresenter createPresenter() {
        return new CalibrationContract.CalibrationPresenter();
    }

    private void initPager() {
        List<View> date = new ArrayList<>();
        date.add(View.inflate(this, R.layout.calibration_manager_pager1, null));
        date.add(View.inflate(this, R.layout.calibration_manager_pager2, null));
        date.add(View.inflate(this, R.layout.calibration_manager_pager3, null));
        date.add(View.inflate(this, R.layout.calibration_manager_pager4, null));
        mTimeWheelView = (TimeWheelView) date.get(0).findViewById(R.id.time_select_date_view);
        SmartNoScrollPagerAdapter adapter = new SmartNoScrollPagerAdapter(date);
        mActCalibrationViewPager.setAdapter(adapter);
        mActCalibrationViewPager.addOnPageChangeListener(this);
    }

    @OnClick({R.id.act_calibration_back, R.id.act_calibration_skip, R.id.act_calibration_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.act_calibration_back:
                switch (mActCalibrationViewPager.getCurrentItem()) {
                    case 0:
                        break;
                    case 1:
                        mActCalibrationViewPager.setCurrentItem(0, false);
                        mActCalibrationBack.setVisibility(View.GONE);
                        mActCalibrationSkip.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mActCalibrationViewPager.setCurrentItem(1, false);
                        presenter.intoSmall(16, 4);
                        break;
                    case 3:
                        mActCalibrationViewPager.setCurrentItem(2, false);
                        presenter.intoSmall(17, 4);
                        break;
                }
                break;
            case R.id.act_calibration_skip:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                presenter.outCalibration();
                finish ();
                break;
            case R.id.act_calibration_next:
                switch (mActCalibrationViewPager.getCurrentItem()) {
                    case 0:
                        mActCalibrationViewPager.setCurrentItem(1, false);
                        mActCalibrationBack.setVisibility(View.VISIBLE);
                        mActCalibrationSkip.setVisibility(View.GONE);
                        mTimeValue = mTimeWheelView.getSecTime();
                        presenter.intoSmall(16, 4);
                        break;
                    case 1:
                        mActCalibrationViewPager.setCurrentItem(2, false);
                        presenter.intoSmall(17, 4);
                        break;
                    case 2:
                        mActCalibrationViewPager.setCurrentItem(3, false);
                        mActCalibrationNext.setText(getResources().getString(R.string.done));
                        presenter.intoSmall(15, 4);
                        break;
                    case 3:

                        presenter.sendTime(1, mTimeValue, new Date());
                        presenter.outCalibration();
                        toActivity (MainActivity.class);
                        finish();
                        break;
                }
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
