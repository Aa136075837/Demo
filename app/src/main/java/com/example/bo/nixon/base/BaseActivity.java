package com.example.bo.nixon.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import com.example.bo.nixon.R;
import com.example.bo.nixon.utils.DialogUtil;
import com.example.bo.nixon.utils.SystemBarTintManager;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public class BaseActivity extends AppCompatActivity {

    private DialogUtil mDialogUtil;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setToolbarColor (getResources ().getColor (R.color.bg_color));
        }
    }

    protected void setToolbarColor (int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow ().addFlags (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            SystemBarTintManager tintManager = new SystemBarTintManager (this);
            tintManager.setStatusBarTintEnabled (true);
            tintManager.setStatusBarTintColor (color);
        }
    }

    protected void toActivity (@NonNull Class cl) {
        startActivity (new Intent (this, cl));
    }

    /**
     * 让APP字体大小不受系统字体改变的影响
     *
     * @return
     */
    @Override public Resources getResources () {
        Resources res = super.getResources ();
        Configuration config = new Configuration ();
        config.setToDefaults ();
        res.updateConfiguration (config, res.getDisplayMetrics ());
        return res;
    }

    /**
     * 判断点击位置
     *
     * @return
     */
    protected boolean inputIsShow (View v, MotionEvent ev) {
        if (null != v && v instanceof EditText) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow (leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight ();
            int right = left + v.getWidth ();
            if (ev.getX () > left && ev.getX () < right && ev.getY () < bottom && ev.getY () > top) {
                return false;//点击的是Edittext内部，不隐藏
            } else {
                return true;
            }
        }
        return false;
    }

    protected void show2NewDialog (@StringRes int msg, View.OnClickListener listener) {
        showTwoBtnDialog (R.layout.two_btn_white_dialog_new, msg, R.id.dialog_message_tv, listener,
            R.id.dialog_confirm_tv, R.id.dialog_cancel_tv);
    }

    protected void show1NewDialog (@StringRes int msg) {
        showOneBtnDialog (R.layout.one_btn_white_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
            }
        }, R.id.dialog_confirm_tv);
    }

    protected void show1NewLongDialog (@StringRes int msg) {
        showOneBtnDialog (R.layout.one_btn_long_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
            }
        }, R.id.dialog_confirm_tv);
    }

    protected void show1NewMidDialog (@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog (R.layout.one_btn_mid_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
            }
        }, R.id.dialog_confirm_tv);
    }


    protected boolean isShowing() {
        if (null == mDialogUtil)
            return false;
        return mDialogUtil.isShowing();
    }

    protected void showOneBtnDialog (@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
        View.OnClickListener listener, @IdRes int btnID) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss ();
        }
        mDialogUtil = new DialogUtil (this);
        mDialogUtil.show (res, msg, tvID);
        mDialogUtil.setOnClickListener (listener, btnID);
        mDialogUtil.setLayoutParams ();
    }

    protected void showTwoBtnDialog (@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
        View.OnClickListener listener, @IdRes int idL, @IdRes int idR) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss ();
        }
        mDialogUtil = new DialogUtil (this);
        mDialogUtil.show (res, msg, tvID);
        mDialogUtil.setOnClickListener (listener, idL, idR);
        mDialogUtil.setLayoutParams ();
    }

    protected void show2NewDialog (@StringRes int msg, View.OnClickListener listener, @StringRes int title,
        @StringRes int confirm, @StringRes int cancel) {
        showTwoBtnDialog (R.layout.two_btn_white_dialog_new1, msg, R.id.dialog_message_tv, listener,
            R.id.dialog_confirm_tv, R.id.dialog_cancel_tv, title, R.id.dialog_title, confirm, cancel);
    }

    protected void showTwoBtnDialog (@LayoutRes int res, @StringRes int msg, @IdRes int tvID,
        View.OnClickListener listener, @IdRes int iDL, @IdRes int iDR, @StringRes int title, @IdRes int titleId,
        @StringRes int confirm, @StringRes int cancel) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss ();
        }
        mDialogUtil = new DialogUtil (this);
        mDialogUtil.show (res, msg, title, tvID, titleId, confirm, iDL, cancel, iDR);
        mDialogUtil.setOnClickListener (listener, iDL, iDR);
        mDialogUtil.setLayoutParams ();
    }

    protected void show2BlackRightDialog (@StringRes int msg, View.OnClickListener listener) {
        showTwoBtnDialog (R.layout.two_btn_black_dialog, msg, R.id.dialog_message_tv, listener, R.id.dialog_confirm_tv,
            R.id.dialog_cancel_tv);
    }

    protected void hideDialog () {
        if (mDialogUtil != null) {
            mDialogUtil.dismiss ();
        }
    }
}
