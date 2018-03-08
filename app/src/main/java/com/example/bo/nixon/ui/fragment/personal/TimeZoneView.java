package com.example.bo.nixon.ui.fragment.personal;

import android.view.View;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.ui.view.SmartSwitchView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public class TimeZoneView implements View.OnClickListener {

    private final View mView;
    private SmartSwitchView mTimeZone;
    private boolean isAutoTime;

    public boolean isAutoTime () {
        return isAutoTime;
    }

    public void setAutoTime(boolean auto) {
        isAutoTime = auto;
        mTimeZone.setSelected(auto);
    }

    public View getView () {
        return mView;
    }

    public TimeZoneView () {
        mView = View.inflate (NixonApplication.getContext (), R.layout.edit_personal_auto, null);
        initView ();
    }

    private void initView () {
        mTimeZone = (SmartSwitchView) mView.findViewById (R.id.frag_goal_switch);
        mTimeZone.setCheck (SPUtils.getBoolean (NixonApplication.getContext (), Constant.IS_AUTO_TIME_KEY,false));
        mTimeZone.setOnClickListener (this);
    }

    @Override public void onClick (View view) {
        mTimeZone.setCheck (!isAutoTime);
        isAutoTime = !isAutoTime;
    }
}
