package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.presenter.ForgetPassContract;
import com.example.bo.nixon.ui.view.SmartToolbar;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;
import com.rengwuxian.materialedittext.MaterialEditText;
import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends BaseMvpActivity<ForgetPassContract.ForgetPassPresenter>
    implements ForgetPassContract.ForgetPassNixonView {

    @BindView (R.id.forget_toolbar) SmartToolbar mForgetToolbar;
    @BindView (R.id.forget_email) MaterialEditText mForgetEmail;
    @BindView (R.id.forget_code) MaterialEditText mForgetCode;
    @BindView (R.id.forget_send) TextView mForgetSend;
    @BindView (R.id.forget_password) MaterialEditText mForgetPassword;
    @BindView (R.id.forget_done) TextView mForgetDone;
    private String mForgetUserName;
    private String mForgetPsd;
    private String mCheckCode;
    private ForgetTimer mTimer;
    private InputMethodManager mImm;
    private boolean isRequesting;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_forget_password);
        ButterKnife.bind (this);
        mTimer = new ForgetTimer (61980, 1033);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_forget_password));
        mImm = (InputMethodManager) getSystemService (NixonApplication.INPUT_METHOD_SERVICE);
        initToolbar ();
        initView ();
    }

    private void initView () {
        mForgetCode.setHint (StringUtils.getSpannableString (R.string.enter_code, 14));
        mForgetPassword.setHint (StringUtils.getSpannableString (R.string.forget_pass_hint, 16));
    }

    private void initToolbar () {
        View backView = View.inflate (this, R.layout.back_layout, null);
        backView.setBackgroundColor (getResources ().getColor (R.color.bg_color));
        ImageView backImg = (ImageView) backView.findViewById (R.id.back_img);
        backImg.setImageResource (R.drawable.icon_back);
        mForgetToolbar.addBackView (backView);
        mForgetToolbar.setTittle (getResources ().getString (R.string.login_forget_pass));
    }

    @Override protected ForgetPassContract.ForgetPassPresenter createPresenter () {
        return new ForgetPassContract.ForgetPassPresenter ();
    }

    @OnClick ({ R.id.forget_send, R.id.forget_done }) public void onClick (View view) {
        mForgetUserName = mForgetEmail.getText ().toString ().trim ();
        mForgetPsd = mForgetPassword.getText ().toString ().trim ();
        mCheckCode = mForgetCode.getText ().toString ().trim ();
        if (StringUtils.isEmail (mForgetUserName)) {
            switch (view.getId ()) {
                case R.id.forget_send:
                    if (isRequesting){
                        return;
                    }
                    presenter.sendCodeRequest (mForgetUserName);
                    isRequesting = true;
                    break;
                case R.id.forget_done:
                    if (!TextUtils.isEmpty (mForgetPsd) && !TextUtils.isEmpty (mCheckCode)) {
                        if (mCheckCode.length () != 4){
                            show1NewDialog (R.string.wrong_code);
                        }
                        checkCode ();
                    }
                    break;
            }
        } else {
            show1NewDialog (R.string.wrong_email);
        }
    }

    private void checkCode () {
        presenter.checkCode (mForgetUserName, mCheckCode);
    }

    private void resetPassword () {
        Map<String, String> parma = new HashMap<> ();
        parma.put ("subject", mForgetUserName);
        parma.put ("pwd", mForgetPsd);
        parma.put ("code", mCheckCode);
        presenter.resetPassword (parma);
    }

    @Override public void sendSuccess () {
        mTimer.start ();
        show1NewLongDialog (R.string.check_sent);
        isRequesting = false;
    }

    @Override public void sendFailed () {

    }

    @Override public void checkSuccess () {
        resetPassword ();
    }

    @Override public void checkFailed () {
        show1NewDialog (R.string.wrong_code);
    }

    @Override public void resetSuccess () {
        SPUtils.putString (this, Constant.USERNAME_KEY, mForgetUserName);
        SPUtils.putString (this, Constant.PASSWORD_KEY, mForgetPsd);
        Intent intent = new Intent (this, LoginActivity.class);
        startActivity (intent);
    }

    @Override public void resetFialed () {

    }

    /**
     * 根据点击的位置判断是否隐藏键盘
     *
     * @return
     */
    @Override public boolean dispatchTouchEvent (MotionEvent ev) {
        if (ev.getAction () == MotionEvent.ACTION_UP) {
            View view = getCurrentFocus ();
            if (inputIsShow (view, ev)) {
                if (null != mImm) {
                    mImm.hideSoftInputFromWindow (view.getWindowToken (), 0);
                }
            }
        }
        return super.dispatchTouchEvent (ev);
    }

    @Override protected void onDestroy () {
        super.onDestroy ();
        if (null != mTimer) {
            mTimer.cancel ();
            mTimer = null;
        }
    }

    class ForgetTimer extends CountDownTimer {

        public ForgetTimer (long millisInFuture, long countDownInterval) {
            super (millisInFuture, countDownInterval);
        }

        @Override public void onTick (long millisUntilFinished) {
            mForgetSend.setClickable (false);
            mForgetSend.setText (millisUntilFinished / 1033 + "s");
        }

        @Override public void onFinish () {
            mForgetSend.setClickable (true);
            mForgetSend.setText (getResources ().getString (R.string.forget_resend));
        }
    }
}
