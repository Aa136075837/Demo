package com.example.bo.nixon.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.bo.nixon.R;

public class DialogUtil {

    private static final String TAG = "DialogUtil";

    private AlertDialog mAlertDialog;
    private final Typeface mTf;

    public DialogUtil (Context context) {
        mTf = Typeface.createFromAsset (context.getAssets (), "fonts/Montserrat-Regular.ttf");
        mAlertDialog = new AlertDialog.Builder (context, R.style.Theme_dialog).create ();
    }

    public void show (@LayoutRes int resid) {
        if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
            show ();
            mAlertDialog.setContentView (resid);
        }
    }

    public void show (@LayoutRes int resid, String title, @IdRes int viewId) {
        if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
            show ();
            mAlertDialog.setContentView (resid);
            TextView tv = (TextView) mAlertDialog.findViewById (viewId);
            if (null != tv) {
                tv.setText (title);
                tv.setTypeface (mTf);
            }
        }
    }

    public void show (@LayoutRes int resid, @StringRes int strRes, @IdRes int viewId) {
        if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
            show ();
            mAlertDialog.setContentView (resid);
            TextView tv = (TextView) mAlertDialog.findViewById (viewId);
            if (null != tv) {
                tv.setText (strRes);
                tv.setTypeface (mTf);
            }
        }
    }

    public void show (@LayoutRes int resid, @StringRes int msg, @StringRes int title, @IdRes int viewId,
        @IdRes int titleId) {
        if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
            show ();
            mAlertDialog.setContentView (resid);
            TextView tv = (TextView) mAlertDialog.findViewById (viewId);
            TextView titleTv = (TextView) mAlertDialog.findViewById (titleId);
            if (null != tv) {
                tv.setText (msg);
                //tv.setTextSize (DisplayUtil.px2dip (NixonApplication.getContext (), 18));
                titleTv.setText (title);
                tv.setTypeface (mTf);
            }
        }
    }

    public void show (@LayoutRes int resid, @StringRes int msg, @StringRes int title, @IdRes int viewId,
        @IdRes int titleId, @StringRes int confirm, @IdRes int confirmId, @StringRes int cancel, @IdRes int cancelId) {
        if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
            show ();
            mAlertDialog.setContentView (resid);
            TextView tv = (TextView) mAlertDialog.findViewById (titleId);
            TextView content = (TextView) mAlertDialog.findViewById (viewId);
            TextView confirmTv = (TextView) mAlertDialog.findViewById (confirmId);
            TextView cancelTv = (TextView) mAlertDialog.findViewById (cancelId);
            if (null != tv && null != content && null != confirmTv && null != cancelTv) {
                tv.setText (title);
                //tv.setTextSize (DisplayUtil.px2dip (NixonApplication.getContext (), 18));
                content.setText (msg);
                confirmTv.setText (confirm);
                cancelTv.setText (cancel);
                tv.setTypeface (mTf);
                confirmTv.setTypeface (mTf);
                cancelTv.setTypeface (mTf);
            }
        }
    }
    //
    //public void show (@LayoutRes int res, @DrawableRes int draw, @IdRes int imgID, @StringRes int msg,
    //    @IdRes int tvID) {
    //    if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
    //        show ();
    //        mAlertDialog.setContentView (res);
    //        ImageView imageView = (ImageView) mAlertDialog.findViewById (imgID);
    //        if (imageView != null) {
    //            imageView.setImageResource (draw);
    //        }
    //        TextView tv = (TextView) mAlertDialog.findViewById (tvID);
    //        if (tv != null) {
    //            tv.setText (msg);
    //            tv.setTypeface (mTf);
    //        }
    //    }
    //}

    private void show () {
        try {
            if (null != mAlertDialog && !mAlertDialog.isShowing ()) {
                mAlertDialog.show ();
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void setLayoutParams (Context context) {
        Window window = mAlertDialog.getWindow ();
        WindowManager.LayoutParams lp = window.getAttributes ();
        lp.alpha = 0.9f;
        window.setAttributes (lp);
    }

    public void setLayoutParams () {
        WindowManager.LayoutParams lp = mAlertDialog.getWindow ().getAttributes ();
        lp.alpha = 1.0f;
        lp.dimAmount = 0.0f;
        mAlertDialog.getWindow ().setAttributes (lp);
        //mAlertDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    /**
     * 先处理异常
     */
    public void dismiss () {
        if (null != mAlertDialog && mAlertDialog.isShowing ()) {
            try {
                mAlertDialog.dismiss ();
            } catch (Exception e) {
                try {
                    mAlertDialog.dismiss ();
                } catch (Exception e1) {
                }
            }
        }
    }

    public View getAnimationView (@IdRes int resId) {
        if (null != mAlertDialog) {
            return mAlertDialog.findViewById (resId);
        }
        return null;
    }

    public void setCancelable (boolean b) {
        if (null != mAlertDialog) mAlertDialog.setCancelable (b);
    }

    public void setCanceledOnTouchOutside (boolean b) {
        if (null != mAlertDialog) {
            mAlertDialog.setCanceledOnTouchOutside (b);
        }
    }

    public void setOnClickListener (OnClickListener l) {
        View confirmView = mAlertDialog.getWindow ().findViewById (R.id.dialog_confirm_tv);
        View cancelView = mAlertDialog.getWindow ().findViewById (R.id.dialog_cancel_tv);
        if (null != confirmView) confirmView.setOnClickListener (l);
        if (null != cancelView) cancelView.setOnClickListener (l);
    }

    public void setOnClickListener (OnClickListener l, @IdRes int id1, @IdRes int id2) {
        View view1 = mAlertDialog.getWindow ().findViewById (id1);
        View view2 = mAlertDialog.getWindow ().findViewById (id2);
        if (null != view1) view1.setOnClickListener (l);
        if (null != view2) view2.setOnClickListener (l);
    }

    public void setOnClickListener (OnClickListener l, @IdRes int id1) {
        View view1 = mAlertDialog.getWindow ().findViewById (id1);
        if (null != view1) {
            view1.setOnClickListener(l);
        } else {
            Log.w(TAG,"setOnClickListener::" + "is null " );
        }
    }

    public void setOnCancelListener (DialogInterface.OnCancelListener listener) {
        if (listener != null) {
            mAlertDialog.setOnCancelListener (listener);
        }
    }

    public View findViewById (@IdRes int id) {
        return mAlertDialog.findViewById (id);
    }

    public boolean isShowing () {
        if (null != mAlertDialog) return mAlertDialog.isShowing ();
        return false;
    }

    public void setText (String text, @IdRes int id) {
        TextView tv = (TextView) findViewById (id);
        if (null != tv) {
            tv.setText (text);
            tv.setTypeface (mTf);
        }
    }

}
