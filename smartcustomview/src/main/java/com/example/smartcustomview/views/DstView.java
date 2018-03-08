package com.example.smartcustomview.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartcustomview.R;

/**
 * @author bo.
 * @Date 2017/8/14.
 * @desc
 */

public class DstView extends LinearLayout implements View.OnClickListener {

    private TextView mDstText;
    private String[] mDst = {"0.5H", "1.0H", "1.5H", "2.0H", "2.5H", "3.0H"};
    private int mCurrentIndex = 1;
    private LinearLayout mLinearLayout;
    private boolean mIsBlack;

    public DstView (Context context) {
        this (context, null);
    }

    public DstView (Context context, @Nullable AttributeSet attrs) {
        this (context, attrs, 0);
    }

    public DstView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes (attrs, R.styleable.DstView);
        mIsBlack = a.getBoolean (R.styleable.DstView_dst_is_black_background, false);
        init (context, attrs, defStyleAttr);
        a.recycle ();
    }

    private void init (Context context, AttributeSet attrs, int defStyleAttr) {
        View view;
        Log.d ("disView", "   mIsBlack  = " + mIsBlack);
        if (mIsBlack) {
            view = LayoutInflater.from (context).inflate (R.layout.dst_black, this);
        } else {
            view = LayoutInflater.from (context).inflate (R.layout.dst_white, this);
        }
        view.findViewById (R.id.dst_add).setOnClickListener (this);
        view.findViewById (R.id.dst_reduce).setOnClickListener (this);
        mDstText = (TextView) view.findViewById (R.id.dst_text);
        mLinearLayout = (LinearLayout) view.findViewById (R.id.dst_ll);
        mDstText.setText (mDst[1]);
    }

    public void changeUI (boolean isShow) {
        if (isShow) {
            mLinearLayout.setVisibility (VISIBLE);
        } else {
            mLinearLayout.setVisibility (INVISIBLE);
        }
    }

    public void changeUI (boolean isShow, String content) {
        if (isShow) {
            mLinearLayout.setVisibility (VISIBLE);
            mDstText.setText (content);
            for (int i = 0; i < mDst.length; i++) {
                if (mDst[i].equals (content)) {
                    mCurrentIndex = i;
                    break;
                }
            }
            Log.e ("mCurrentIndex","    mCurrentIndex  = = " + mCurrentIndex  + "  content  = = " + content);
        } else {
            mLinearLayout.setVisibility (INVISIBLE);
        }
    }

    /**
     * @return
     */
    public String getContent () {
        return mDst[mCurrentIndex];
    }

    @Override
    public void onClick (View v) {
        int i = v.getId ();
        if (i == R.id.dst_reduce) {
            if (mCurrentIndex > 0) {
                mCurrentIndex--;
            }
        } else if (i == R.id.dst_add) {
            if (mCurrentIndex < mDst.length - 1) {
                mCurrentIndex++;
            }
        }
        mDstText.setText (mDst[mCurrentIndex]);
    }
}
