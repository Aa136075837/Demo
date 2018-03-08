package com.example.bo.nixon.ui.fragment.alarm;

import android.view.View;
import android.widget.CompoundButton;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;

/**
 * @author bo.
 * @Date 2017/6/9.
 * @desc
 */

public class AlarmRepeatProxy implements CompoundButton.OnCheckedChangeListener {

    private View mView;
    private CompoundButton mSunButton;
    private CompoundButton mMonButton;
    private CompoundButton mTueButton;
    private CompoundButton mWedButton;
    private CompoundButton mThuButton;
    private CompoundButton mFriButton;
    private CompoundButton mSatButton;
    private int mValue = 0;

    public AlarmRepeatProxy() {
        mView = View.inflate(NixonApplication.getContext(), R.layout.alarm_repeat_fragment, null);
        initView();
        initListener();
    }

    private void initView() {
        mSunButton = (CompoundButton)mView.findViewById(R.id.repeat_check_sun);
        mMonButton = (CompoundButton)mView.findViewById(R.id.repeat_check_mon);
        mTueButton = (CompoundButton)mView.findViewById(R.id.repeat_check_tue);
        mWedButton = (CompoundButton)mView.findViewById(R.id.repeat_check_wed);
        mThuButton = (CompoundButton)mView.findViewById(R.id.repeat_check_thu);
        mFriButton = (CompoundButton)mView.findViewById(R.id.repeat_check_fri);
        mSatButton = (CompoundButton)mView.findViewById(R.id.repeat_check_sat);
        mSunButton.setTag(1);
        mMonButton.setTag(2);
        mTueButton.setTag(4);
        mWedButton.setTag(8);
        mThuButton.setTag(16);
        mFriButton.setTag(32);
        mSatButton.setTag(64);
    }

    private void initListener() {
        mSunButton.setOnCheckedChangeListener(this);
        mMonButton.setOnCheckedChangeListener(this);
        mTueButton.setOnCheckedChangeListener(this);
        mWedButton.setOnCheckedChangeListener(this);
        mThuButton.setOnCheckedChangeListener(this);
        mFriButton.setOnCheckedChangeListener(this);
        mSatButton.setOnCheckedChangeListener(this);
    }

    public View getView() {
        return mView;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int tag = (int) compoundButton.getTag();
        if (b) {
            mValue = mValue | tag;
        } else {
            mValue = mValue - tag;
        }
        initValue(mValue);
    }

    private void initValue(int value) {
        if (1 == (value & 1) ) {
            mSunButton.setChecked(true);
        } else{
            mSunButton.setChecked(false);
        }
        if (2 == (value & 2) ) {
            mMonButton.setChecked(true);
        } else {
            mMonButton.setChecked(false);
        }
        if(4 == (value & 4) ) {
            mTueButton.setChecked(true);
        } else{
            mTueButton.setChecked(false);
        }
        if (8 == (value & 8) ) {
            mWedButton.setChecked(true);
        } else {
            mWedButton.setChecked(false);
        }
        if (16 == (value & 16) ) {
            mThuButton.setChecked(true);
        } else {
            mThuButton.setChecked(false);
        }
        if (32 == (value & 32) ) {
            mFriButton.setChecked(true);
        } else {
            mFriButton.setChecked(false);
        }
        if (64 == (value & 64) ) {
            mSatButton.setChecked(true);
        } else {
            mSatButton.setChecked(false);
        }

    }

    public void setRepeat(int value) {
        mValue = value;
        initValue(value);
    }

    public int getRepeat() {
        return mValue;
    }
}
