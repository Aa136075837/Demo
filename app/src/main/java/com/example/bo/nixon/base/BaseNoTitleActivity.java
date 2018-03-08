package com.example.bo.nixon.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import com.example.bo.nixon.R;
import com.example.bo.nixon.utils.DialogUtil;

/**
 * @author bo.
 * @Date 2017/6/7.
 * @desc
 */

public class BaseNoTitleActivity extends AppCompatActivity {

    private DialogUtil mDialogUtil;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

    protected void errorDialogDissmiss (View view){

    }

    protected void show1NewDialog(@StringRes int msg){
        showOneBtnDialog (R.layout.one_btn_white_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
                errorDialogDissmiss(view);
            }
        },R.id.dialog_confirm_tv);
    }
    protected void show1NewDialog(@StringRes int msg,View.OnClickListener listener){
        showOneBtnDialog (R.layout.one_btn_white_dialog_new, msg, R.id.dialog_message_tv, listener ,R.id.dialog_confirm_tv);
    }
    protected void show1NewMidDialog(@StringRes int msg){
        showOneBtnDialog (R.layout.one_btn_mid_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
                errorDialogDissmiss(view);
            }
        },R.id.dialog_confirm_tv);
    }

    protected void show1NewMidDialog(@StringRes int msg,View.OnClickListener listener){
        showOneBtnDialog (R.layout.one_btn_mid_dialog_new, msg, R.id.dialog_message_tv, listener ,R.id.dialog_confirm_tv);
    }

    protected void show1NewMidDialog (@StringRes int msg, @IdRes int titleId, @StringRes int title) {
        showOneBtnDialog (R.layout.one_btn_mid_dialog_new, msg, R.id.dialog_message_tv, new View.OnClickListener () {
            @Override public void onClick (View view) {
                hideDialog ();
            }
        }, R.id.dialog_confirm_tv);
    }

    protected void showOneBtnDialog (@LayoutRes int res, @StringRes int msg,
        @IdRes int tvID, View.OnClickListener listener, @IdRes int btnID) {
        if (null != mDialogUtil) {
            mDialogUtil.dismiss ();
        }
        mDialogUtil = new DialogUtil (this);
        mDialogUtil.show (res, msg, tvID);
        mDialogUtil.setOnClickListener (listener, btnID);
        mDialogUtil.setLayoutParams ();
    }

    //protected void showOneBtnDialog (@LayoutRes int res, @DrawableRes int draw, @IdRes int imgID, @StringRes int msg,
    //    @IdRes int tvID, View.OnClickListener listener, @IdRes int btnID) {
    //    mDialogUtil = new DialogUtil (this);
    //    mDialogUtil.show (res, draw, imgID, msg, tvID);
    //    mDialogUtil.setOnClickListener (listener, btnID);
    //    mDialogUtil.setLayoutParams ();
    //}

    protected void hideDialog(){
        if (mDialogUtil != null){
            mDialogUtil.dismiss ();
        }
    }
}
