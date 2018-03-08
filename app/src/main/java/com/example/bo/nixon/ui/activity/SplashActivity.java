package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpNotitleActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.presenter.SplashContract;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

public class SplashActivity extends BaseMvpNotitleActivity<SplashContract.SplashPresenter>
        implements SplashContract.SplashNixonView {

    private Handler mHandler = new Handler ();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_splash);
        mHandler.postDelayed (new Runnable () {
            @Override
            public void run () {
                Intent intent;
                String cookie = SPUtils.getString (NixonApplication.getContext (), Constant
                        .COOKIE_KEY, "");
                if (!"".equals (cookie)) {
                    //设置过个人信息
//                    autoLogin ();
                    toMainactivity();
                } else {
                    intent = new Intent (SplashActivity.this, LoginAndRegActivity.class);
                    startActivity (intent);
                    finish ();
                }
            }
        }, 2 * 1000);
    }

    private void autoLogin () {
        if(presenter != null){
            presenter.autoLogin ();
        }
    }

    @Override
    protected SplashContract.SplashPresenter createPresenter () {
        return new SplashContract.SplashPresenter ();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
    }

    @Override
    public void autoLoginFailed () {
        toLoginActivity ();
    }

    private void toLoginActivity () {
        Intent intent = new Intent (this,LoginActivity.class);
        startActivity (intent);
    }

    @Override
    public void noActivation () {
        show1NewMidDialog (R.string.no_activited);

    }

    @Override
    protected void errorDialogDissmiss (View view){
        toLoginActivity ();
        finish ();
    }

    @Override
    public void autoLoginSuccess () {
        toMainactivity();
    }

    @Override
    public void badNet () {
//        show1NewDialog (R.string.bad_net);
        toMainactivity();
    }

    private void toMainactivity(){
        Intent intent = new Intent (SplashActivity.this, MainActivity.class);
        startActivity (intent);
        finish ();
    }
}
