package com.example.bo.nixon.ui.view;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.example.bo.nixon.R;

/**
 * @author ARZE
 * @version 创建时间：2016年9月13日 下午4:54:24
 * @说明
 */

public class SmartToolbar extends FrameLayout implements OnClickListener {

    private ViewGroup mLeftLayout;
    public ViewGroup mRightLayout;
    private ViewGroup mCenterLayout;
    private TextView mCenterTv;

    private static final int LEFT_TAG = 0x0001;
    private static final int RIGHT_TAG = 0x0002;
    private TextView mMCenterTip;

    public SmartToolbar (Context context) {
        super(context);
        init(context, null, 0);
    }

    public SmartToolbar (Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SmartToolbar (Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(context).inflate(R.layout.toolbar_layout, this, true);
        mLeftLayout = (ViewGroup) findViewById(R.id.toolbar_left_layout);
        mRightLayout = (ViewGroup) findViewById(R.id.toolbar_right_layout);
        mCenterLayout = (ViewGroup) findViewById(R.id.toolbar_center_layout);
        mCenterTv = (TextView) findViewById(R.id.toolbar_center_tv);
        mMCenterTip = (TextView) findViewById (R.id.toolbar_center_tip);
    }

    public void addBackView(View view) {
        mLeftLayout.addView(view);
        view.setTag(LEFT_TAG);
        view.setOnClickListener(this);
    }

    public void addRightView(View view) {
        mRightLayout.addView(view);
    }

    public void addLeftView(View view) {
        mLeftLayout.addView(view);
    }

    public void setTittle(String tittle) {
        mCenterTv.setText(tittle);
    }

    public void setTittleColor(@ColorInt int color) {
        mCenterTv.setTextColor (color);
    }

    @Override
    public void onClick(View v) {
        int tag = (Integer) v.getTag();
        if (LEFT_TAG == tag) {
            if (v.getContext() instanceof Activity)
                ((Activity) v.getContext()).finish();
        }
    }

    public void removeLeftView(){
        if (null != mLeftLayout)
            mLeftLayout.removeAllViews();
    }

    public void setTip(boolean isShowTip){
        if (isShowTip){
            mMCenterTip.setVisibility (View.VISIBLE);
        }else{
            mMCenterTip.setVisibility (View.GONE);
        }
    }

}
