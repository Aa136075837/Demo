package com.example.bo.nixon.ui.fragment.personal;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.ui.view.SmartSeekBar;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * @author bo.
 * @Date 2017/6/15.
 * @desc
 */

public class EditUserTallView implements SeekBar.OnSeekBarChangeListener, View.OnClickListener,
        SmartSeekBar.ProgressChangedListener {

    private final View mView;
    private TextView mTallTv;
    private String userTall;
    private int progress;
    private SmartSeekBar mSeekBar;
    private TextView mReduce;
    private TextView mAdd;
    private String mFalseMetricTall;

    public int getProgress () {
        return progress;
    }

    public String getFalseMetricTall () {
        mFalseMetricTall = mTallTv.getText ().toString ().trim ();
        return TextUtils.isEmpty (mFalseMetricTall) ? "" : mFalseMetricTall;
    }

    public void setProgress (int progress) {
        Log.w ("EditUserTallView", progress + "");
        this.progress = progress;
        mSeekBar.setProgress (progress);
    }

    public String getUserTall () {
        userTall = mTallTv.getText ().toString ().trim ();
        return TextUtils.isEmpty (userTall) ? 70 + "" : userTall;
    }

    public View getView () {
        return mView;
    }

    public EditUserTallView () {
        mView = View.inflate (NixonApplication.getContext (), R.layout.edit_personal_tall, null);
        initView ();
    }

    private void initView () {
        mSeekBar = (SmartSeekBar) mView.findViewById (R.id.frag_tall_smart_seekbar);
        mTallTv = (TextView) mView.findViewById (R.id.frag_tall_seek_text);
        mReduce = (TextView) mView.findViewById (R.id.frag_tall_seek_text_reduce);
        mAdd = (TextView) mView.findViewById (R.id.frag_tall_seek_text_add);
        String tall = SPUtils.getString (NixonApplication.getContext (), Constant.TALL_KEY, 70 +
                "");
        String[] array = tall.split ("\\.");
        Log.e ("EditUserTallView", " Tall ====== 1  >>" + array[0]);
        mSeekBar.setOnSeekBarChangeListener (this);

        mSeekBar.setProgress (Integer.parseInt (array[0]));
        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT, false)) {
            String string = SPUtils.getString (NixonApplication.getContext (), Constant
                    .FALSE_TALL_KEY);
            if (string.endsWith ("in")) {
                String[] split = string.split (" ");
                String ft = split[0].substring (0, split[0].length () - 2);
                String in = split[1].substring (0, split[1].length () - 2);
                int ftInt = Integer.parseInt (ft);
                int inInt = Integer.parseInt (in);
                string = StringUtils.inch2m (((ftInt * 12) + inInt) + "") + " m";
            }
            mTallTv.setText (string);
            Log.e ("mFalseMetricTall", "   ====  " + SPUtils.getString (NixonApplication
                    .getContext (), Constant.FALSE_TALL_KEY));
        }else{
            initTallTvContent (tall);
        }
        progress = mSeekBar.getProgress ();
        mReduce.setOnClickListener (this);
        mSeekBar.setProgressChangedListener (this);
        mAdd.setOnClickListener (this);
    }

    private void initTallTvContent (String tall) {
        if (tall.contains (".")) {
            tall = tall.split ("\\.")[0];
        }
        int s = Integer.parseInt (tall);
        if (s == 120) {
            tall = s + "";
        } else {
            tall = s + 12 + "";
        }
        StringBuilder content = new StringBuilder ();
        content.append ((Integer.parseInt (StringUtils.getFt (tall))) - 1 + "").append ("ft ")
                .append (StringUtils.getIn (tall)).append ("in");
        mTallTv.setText (content.toString ());
    }

    /**
     * 根据英制公制单位，设置textview内容
     */
    private void initTallTvContent (String tall, boolean isFromUser) {
        if (tall.contains (".")) {
            tall = tall.split ("\\.")[0];
        }
        int s = Integer.parseInt (tall);
        if (s == 120) {
            tall = s + "";
        } else {
            tall = s + 12 + "";
        }
        StringBuilder content = new StringBuilder ();
        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT, false)) {
            if (isFromUser) {
                content.append (StringUtils.inch2m (tall)).append (" m");
            } else {
                String string = SPUtils.getString (NixonApplication.getContext (), Constant
                        .FALSE_TALL_KEY, "");
                if (string.endsWith ("in")) {
                    String[] split = string.split (" ");
                    String ft = split[0].substring (0, split[0].length () - 2);
                    String in = split[1].substring (0, split[1].length () - 2);
                    int ftInt = Integer.parseInt (ft);
                    int inInt = Integer.parseInt (in);
                    string = StringUtils.inch2m (((ftInt * 12) + inInt) + "") + " m";
                }
                content.append (string);
            }
        } else {
            content.append (StringUtils.getFt (tall)).append ("ft ").append (StringUtils.getIn
                    (tall)).append ("in");
        }
        Log.e ("initTallTvContent", "  content.toString ()  = " + content.toString ());
        mTallTv.setText (content.toString ());
    }


    @Override
    public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
        Log.w ("EditUserTallView", "onProgressChanged::" + b);
                seekBarProgressChanged (seekBar, i, b);
    }

    @Override
    public void onStartTrackingTouch (SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch (SeekBar seekBar) {

    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.frag_tall_seek_text_reduce:
                if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT,
                        false)) {
                    reduceTall ();
                } else {
                    progress--;
                    if (progress <= 12) {
                        return;
                    }
                    mSeekBar.setProgress (progress);
                }
                break;
            case R.id.frag_tall_seek_text_add:
                if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT,
                        false)) {
                    addTall ();
                } else {
                    progress++;
                    if (progress >= 108) {
                        return;
                    }
                    mSeekBar.setProgress (progress);
                }
                break;
        }
    }

    private void addTall () {
        String content = mTallTv.getText ().toString ().trim ();
        int progressTemp = 0;
        if (content.contains (".")) { //m
            String substring = content.substring (0, content.length () - 2);
            double d = Double.parseDouble (substring);
            if (d > 3.048) {
                return;
            }
            d = d + 0.01;
            DecimalFormat df = new DecimalFormat ("#0.00");
            content = df.format (d) + " m";
            progressTemp = (int) Double.parseDouble (StringUtils.cm2in (d * 100));
        }
        mTallTv.setText (content);
        mFalseMetricTall = content;
//        mSeekBar.setProgress (progressTemp - 12);
    }


    private void reduceTall () {
        String content = mTallTv.getText ().toString ().trim ();
        int progressTemp = 0;
        if (content.contains (".")) { //m
            String substring = content.substring (0, content.length () - 2);
            double d = Double.parseDouble (substring);
            if (d < 0.3048) {
                return;
            }
            d = d - 0.01;
            DecimalFormat df = new DecimalFormat ("#0.00");
            progressTemp = (int) (Double.parseDouble (StringUtils.cm2in (d * 100)) + 0.75f);
            content = df.format (d) + " m";
        }
        mTallTv.setText (content);
        mFalseMetricTall = content;
//        mSeekBar.setProgress (progressTemp - 12);
    }

    @Override
    public void seekBarProgressChanged (SeekBar seekBar, int iprogress, boolean fromeUser) {
        if (fromeUser) {
            progress = seekBar.getProgress ();
        }
        initTallTvContent ((seekBar.getProgress ()) + "", fromeUser);
    }
}
