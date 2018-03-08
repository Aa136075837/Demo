package com.example.bo.nixon.ui.fragment.timeZone;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.ui.activity.ChooseCity;
import com.example.bo.nixon.ui.view.SmartSwitchView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.smartcustomview.views.DstView;

/**
 * @author bo.
 * @Date 2017/8/14.
 * @desc
 */

public class SelectSecondView implements View.OnClickListener {

    private final View mView;
    private TextView mSelectSecondLocationAbbre;
    private DstView mSelectSecondLocationDstview;
    private SmartSwitchView mSelectSecondLocationSwitch;
    private boolean isChecked;
    private Activity mActivity;

    public SelectSecondView (Activity activity) {
        mView = LayoutInflater.from (activity).inflate (R.layout.white_select_second_location,
                null);
        mActivity = activity;
        initViews ();
    }

    private void initViews () {
        mSelectSecondLocationAbbre = (TextView) mView.findViewById (R.id
                .select_second_location_abbre);
        mSelectSecondLocationDstview = (DstView) mView.findViewById (R.id
                .select_second_location_dstview);
        mSelectSecondLocationSwitch = (SmartSwitchView) mView.findViewById (R.id
                .select_second_location_switch);

        mSelectSecondLocationAbbre.setOnClickListener (this);
        mSelectSecondLocationSwitch.setOnClickListener (this);
        mSelectSecondLocationAbbre.setText (SPUtils.getString (NixonApplication.getContext (),
                Constant.SECOND_ZONE_FUll_NAME_KEY, NixonApplication.getContext ().getResources
                        ().getString (R.string.pacific_standard_time)));
        String dst = SPUtils.getString (NixonApplication.getContext (), Constant.DST_KEY, "");
        if (!"0.0".equals (dst)) {
            mSelectSecondLocationSwitch.setCheck (true);
            mSelectSecondLocationDstview.changeUI (true, dst + "H");
            isChecked = true;
        }
    }

    public void saveDstSettings () {
        String content = mSelectSecondLocationDstview.getContent ();
        if (isChecked) {
            content = content.substring (0, content.length () - 1);
        } else {
            content = "0.0";
        }
        Log.e ("dstsettings", "content = " + content);
        SPUtils.putString (NixonApplication.getContext (), Constant.DST_KEY, content);
    }

    public View getView () {
        return mView;
    }

    public void setResultContent (String content) {
        if (!TextUtils.isEmpty (content)) {
            mSelectSecondLocationAbbre.setText (content);
            Log.d ("setResultContent", "  content = " + content);
        }
    }

    @Override
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.select_second_location_abbre:
                Intent intent = new Intent (NixonApplication.getContext (), ChooseCity.class);
                intent.putExtra (ChooseCity.START_CHOOSECITY_KEY, false);
                mActivity.startActivityForResult (intent, Constant.SELECTSECONDVIEW_REQUEST_CODE);
                break;
            case R.id.select_second_location_switch:
                mSelectSecondLocationSwitch.setCheck (!isChecked);
                mSelectSecondLocationDstview.changeUI (!isChecked);
                isChecked = !isChecked;

                break;
        }
    }

    private DstChangedListener mListener;

    public void setDstChangedListener (DstChangedListener listener) {
        mListener = listener;
    }

    public interface DstChangedListener {
        void dstChanged (boolean isChecked);
    }

}
