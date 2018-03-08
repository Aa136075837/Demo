package com.example.bo.nixon.ui.fragment.timeZone;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

/**
 * @author bo.
 * @Date 2017/8/14.
 * @desc
 */

public class CustomLocationView implements View.OnClickListener {


    private final View mView;
    private EditText mTimeZoneHomeLoca;
    private EditText mTimeZoneHomeAbb;
    private EditText mTimeZoneSecondL;
    private EditText mTimeZoneSecondA;
    private TextView mTimeZoneConfirm;

    public CustomLocationView (Context context) {
        mView = LayoutInflater.from (context).inflate (R.layout.time_zone_pager1, null);
        initViews ();
    }

    private void initViews () {
        mTimeZoneHomeLoca = (EditText) mView.findViewById (R.id.time_zone_home_loca);
        mTimeZoneHomeAbb = (EditText) mView.findViewById (R.id.time_zone_home_abbre);
        mTimeZoneSecondL = (EditText) mView.findViewById (R.id.time_zone_second_loca);
        mTimeZoneSecondA = (EditText) mView.findViewById (R.id.time_zone_second_abbre);
        mTimeZoneConfirm = (TextView) mView.findViewById (R.id.time_zone_confirm);
        mTimeZoneConfirm.setOnClickListener (this);
        String homeLoca = SPUtils.getString (NixonApplication.getContext (), Constant
                .HOME_LOCATION_KEY);
        String homeLocaAbbre = SPUtils.getString (NixonApplication.getContext (), Constant
                .HOME_LOCATION_ABBRE);
        String sencondLoca = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_FUll_NAME_KEY);
        String secondLocaAbbre = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_NAME_KEY);
        mTimeZoneHomeLoca.setText (homeLoca);
        mTimeZoneHomeAbb.setText (homeLocaAbbre);
        mTimeZoneSecondL.setText (sencondLoca);
        mTimeZoneSecondA.setText (secondLocaAbbre);
    }

    public void saveInformation () {
        String homeLocaName = mTimeZoneHomeLoca.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (homeLocaName)) {
            SPUtils.putString (NixonApplication.getContext (), Constant.HOME_LOCATION_KEY,
                    homeLocaName);
        }
        String homeLocaAbbre = mTimeZoneHomeAbb.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (homeLocaAbbre)) {
            SPUtils.putString (NixonApplication.getContext (), Constant.HOME_LOCATION_ABBRE,
                    homeLocaAbbre);
        }
        String secondLocaName = mTimeZoneSecondL.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (secondLocaName)) {
            SPUtils.putString (NixonApplication.getContext (), Constant
                    .SECOND_ZONE_FUll_NAME_KEY, secondLocaName);
        }
        String secondLocaAbbre = mTimeZoneSecondA.getText ().toString ().trim ();
        if (!TextUtils.isEmpty (secondLocaAbbre)) {
            Log.d ("secondLocaAbbre", "  保存自定义第二市区缩写  " + secondLocaAbbre);
            SPUtils.putString (NixonApplication.getContext (), Constant.SECOND_ZONE_NAME_KEY,
                    secondLocaAbbre);
        }
    }

    public View getView () {
        return mView;
    }

    private NextPagerListener mListener;

    public void setNextPagerListener (NextPagerListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick (View v) {
        if (mListener != null) {
            mListener.setCurrentPager ();
        }
    }

    public interface NextPagerListener {
        void setCurrentPager ();
    }
}
