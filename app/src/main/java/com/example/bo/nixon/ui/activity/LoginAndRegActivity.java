package com.example.bo.nixon.ui.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseActivity;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.utils.Constant;
import com.smart.smartble.PermissionsUtils;

public class LoginAndRegActivity extends BaseActivity {

    @BindView (R.id.login_reg_login) TextView mLoginRegLogin;
    @BindView (R.id.login_reg_register) TextView mLoginRegRegister;
    private final int NIXON_PERMISSION = 0x0001;
    private FinishSelfReceiver mReceiver;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_login_and_reg);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_login_and_reg));
        //AppManager.getAppManager ().addActivity (this);
        PermissionsUtils.requestPermissions (new String[] {
            Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION
        }, NIXON_PERMISSION, this);
        ButterKnife.bind (this);
    }

    @Override protected void onResume () {
        super.onResume ();
        mReceiver = new FinishSelfReceiver ();
        IntentFilter filter = new IntentFilter ();
        filter.addAction (Constant.FINISH_LOGIN_KEY);
        registerReceiver (mReceiver, filter);
    }

    @Override public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (NIXON_PERMISSION == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recreate ();
            } else {
                finish ();
            }
        }
    }

    @OnClick ({ R.id.login_reg_login, R.id.login_reg_register }) public void onViewClicked (View view) {
        Intent intent;
        switch (view.getId ()) {
            case R.id.login_reg_login:
                intent = new Intent (this, LoginActivity.class);
                startActivity (intent);
                break;
            case R.id.login_reg_register:
                intent = new Intent (this, LoginActivity.class);
                intent.putExtra (Constant.START_CODE_KEY, Constant.STARTCODE);
                startActivity (intent);
                break;
        }
    }

    @Override protected void onDestroy () {
        super.onDestroy ();
        if (mReceiver != null) {
            unregisterReceiver (mReceiver);
            mReceiver = null;
        }
    }

    class FinishSelfReceiver extends BroadcastReceiver {

        @Override public void onReceive (Context context, Intent intent) {
            if (intent.getAction ().equals (Constant.FINISH_LOGIN_KEY)) {
                finish ();
            }
        }
    }
}
