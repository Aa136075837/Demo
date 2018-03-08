package com.example.bo.nixon.ui.fragment.personal;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;

import java.text.DecimalFormat;

/**
 * @author bo.
 * @Date 2017/6/15.
 * @desc
 */

public class EditUserWeightView implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    private View mView;
    private TextView mWeightTv;
    private String weight;
    private int progress;
    private SeekBar mWeightSeek;
    private TextView mReduce;
    private TextView mAdd;
    private String mWeightContent;
    private String mFalseMetricWeight;

    public int getProgress () {
        return progress;
    }

    public void setProgress (int progress) {
        this.progress = progress;
        mWeightSeek.setProgress (progress);
    }

    public String getFalseMetricWeight () {
        mFalseMetricWeight = mWeightTv.getText ().toString ().trim ();
        return mFalseMetricWeight;
    }

    public View getView () {
        return mView;
    }

    public String getWeight () {
        weight = mWeightTv.getText ().toString ().trim ();
        return TextUtils.isEmpty (weight) ? 150 + "" : weight;
    }

    public EditUserWeightView () {
        mView = View.inflate (NixonApplication.getContext (), R.layout.edit_personal_weight, null);
        initView ();
    }

    private void initView () {
        mWeightTv = (TextView) mView.findViewById (R.id.frag_weight_seek_text);
        mWeightSeek = (SeekBar) mView.findViewById (R.id.frag_weight_seekbar);
        mReduce = (TextView) mView.findViewById (R.id.frag_weight_text_reduce);
        mAdd = (TextView) mView.findViewById (R.id.frag_weight_text_add);
        String stempWeight = SPUtils.getString (NixonApplication.getContext (), Constant
                .WEIGHT_KEY, 150 + "");
        mWeightSeek.setProgress ((int) Double.parseDouble (stempWeight));
        mReduce.setOnClickListener (this);
        mAdd.setOnClickListener (this);
        initWeightTvContent (stempWeight, false);
        mWeightSeek.setOnSeekBarChangeListener (this);
        initTextChangedListener ();
    }

    private void initTextChangedListener () {
        mWeightContent = mWeightTv.getText ().toString ().trim ();
        mWeightTv.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                mWeightContent = s.toString ().trim ();
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });
    }

    private void initWeightTvContent (String stempWeight, boolean isFromUser) {
        StringBuilder content = new StringBuilder ();
        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT, false)) {
            if (isFromUser) {
                content.append (StringUtils.lbs2kg (stempWeight)).append (" kg");
            } else {
                String string = SPUtils.getString (NixonApplication.getContext (), Constant
                        .FALSE_WEIGHT_KEY);
                if (string.endsWith ("lbs")){
                    String[] split = string.split (" ");
                    string = StringUtils.lbs2kg (split[0])+" kg";
                }
                content.append (string);
            }
        } else {
            content.append (stempWeight).append (" lbs");
        }
        mWeightTv.setText (content);
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int i, boolean b) {
        if (b) {
            progress = seekBar.getProgress ();
            initWeightTvContent (seekBar.getProgress () + 10 + "", b);
        }
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
            case R.id.frag_weight_text_add:
                if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT,
                        false)) {
                    addWeight ();
                } else {
                    progress++;
                    initWeightTvContent (String.valueOf (progress + 10), false);
                    mWeightSeek.setProgress (progress + 10);
                }
                break;
            case R.id.frag_weight_text_reduce:
                if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT,
                        false)) {
                    reduceWeight ();
                } else {
                    progress--;
                    initWeightTvContent (String.valueOf (progress + 10), false);
                    mWeightSeek.setProgress (progress);
                }
                break;
        }
    }

    private void reduceWeight () {
        int progressWeight = 0;
        if (mWeightContent.contains (".")) { //kg
            String substring = mWeightContent.substring (0, mWeightContent.length () - 3);
            double d = Double.parseDouble (substring);
            if (d < 4.5359) {
                return;
            }
            d = d - 0.1;
            DecimalFormat df = new DecimalFormat ("#0.0");
            mWeightContent = df.format (d) + " kg";
            progressWeight = (int) (Double.parseDouble (StringUtils.g2lbs (d * 1000)));
        }
        mWeightTv.setText (mWeightContent);
        mWeightSeek.setProgress (progressWeight - 10);
        mFalseMetricWeight = mWeightContent;
        progress = progressWeight - 10;
    }

    private void addWeight () {
        int progressWeight = 0;
        if (mWeightContent.contains (".")) { //kg
            String substring = mWeightContent.substring (0, mWeightContent.length () - 3);
            double d = Double.parseDouble (substring);
            if (d > 226.796) {
                return;
            }
            d = d + 0.1;
            DecimalFormat df = new DecimalFormat ("#0.0");
            mWeightContent = df.format (d) + " kg";
            progressWeight = (int) (Double.parseDouble (StringUtils.g2lbs (d * 1000)));
        }
        mWeightTv.setText (mWeightContent);
        mWeightSeek.setProgress (progressWeight);
        mFalseMetricWeight = mWeightContent;
        progress = progressWeight - 10;
    }
}
