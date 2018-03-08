package com.example.bo.nixon.ui.fragment.personal;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.smartcustomview.views.seekbar.BubbleSeekBar;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public class EditStepGoalView
    implements BubbleSeekBar.OnProgressChangedListener, View.OnTouchListener {

    private final View mView;
    private TextView mTextView;
    private String content;
    private int mProgress;
    //private SeekBar mGoalBar;
    private BubbleSeekBar mGoalBar;

    public int getProgress () {
        return mProgress;
    }

    public void setProgress (int progress) {
        this.mProgress = progress;
        mGoalBar.setBubProgress (progress);
    }

    public String getContent () {
        mProgress = mGoalBar.getProgress ();
        content = mProgress + 500 + "";
        return TextUtils.isEmpty (content) ? 10000 + "" : content;
    }

    public View getView () {
        return mView;
    }

    public EditStepGoalView (Activity activity) {
        mView = View.inflate (activity, R.layout.edit_personal_goal, null);
        initView ();
    }

    private void initView () {
        String goal = SPUtils.getString (NixonApplication.getContext (), Constant.GOAL_KEY, 10000 + "");
        mTextView = (TextView) mView.findViewById (R.id.frag_ambitious_seek_text);

        //mGoalBar = (SeekBar) mView.findViewById (R.id.frag_ambitious_seekbar);
        mGoalBar = (BubbleSeekBar) mView.findViewById (R.id.frag_ambitious_seekbar);

        //mGoalBar.setOnSeekBarChangeListener (this);
        mGoalBar.setOnProgressChangedListener (this);
        mGoalBar.setOnTouchListener (this);
        mGoalBar.getConfigBuilder ()
            .min (0)
            .max (39500)
            .sectionCount (158)
            .trackColor (ContextCompat.getColor (NixonApplication.getContext (), R.color.transparent))
            .secondTrackColor (ContextCompat.getColor (NixonApplication.getContext (), R.color.transparent))
            .trackSize (1)
            .secondTrackSize (1)
            .seekBySection ()
            .thumbColor (ContextCompat.getColor (NixonApplication.getContext (),R.color.sup_color))
            .thumbRadius (18)
            .bubbleColor (ContextCompat.getColor (NixonApplication.getContext (),R.color.transparent))
            .bubbleTextColor (ContextCompat.getColor (NixonApplication.getContext (),R.color.transparent))
            .build ();
        mTextView.setText (goal + " steps");
        mGoalBar.setBubProgress (Integer.parseInt (goal));
    }

    @Override public void onProgressChanged (int progress, float progressFloat) {
        Log.d ("onProgressChanged", "  progress =  " + progress + "    progressFloat  = " + progressFloat);
        mTextView.setText (progress + 500 + " steps");
    }

    @Override public void getProgressOnActionUp (int progress, float progressFloat) {

    }

    @Override public void getProgressOnFinally (int progress, float progressFloat) {

    }

    @Override public boolean onTouch (View v, MotionEvent event) {
        if (v instanceof BubbleSeekBar) {
            v.getParent ().requestDisallowInterceptTouchEvent (true);
        }
        return false;
    }
}
