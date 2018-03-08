package com.example.bo.nixon.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseNoTitleActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.activity.connect.ConnectActivity;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.SPUtils;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class ConnectWatchHomeActivity extends BaseNoTitleActivity {

    @BindView (R.id.edit_userinfo_skip) TextView mEditUserInfoSkip;
    @BindView (R.id.conn_pair) TextView mConnPair;
    @BindView (R.id.conn_about) TextView mConnAbout;
    private FinishWatchHomeReceiver mHomeReceiver;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_connect_watch_home);
        ButterKnife.bind (this);
        FontManager.changeFonts ((ViewGroup) findViewById (R.id.activity_connect_watch_home));
    }

    @Override protected void onResume () {
        super.onResume ();
        mHomeReceiver = new FinishWatchHomeReceiver ();
        IntentFilter filter = new IntentFilter ();
        filter.addAction (Constant.FINISH_WATCH_HOME_KEY);
        registerReceiver (mHomeReceiver,filter);
    }

    @OnClick ({ R.id.edit_userinfo_skip, R.id.conn_pair, R.id.conn_about }) public void onViewClicked (View view) {
        Intent intent;
        switch (view.getId ()) {
            case R.id.edit_userinfo_skip:
                intent = new Intent (this, MainActivity.class);
                startActivity (intent);
                finish ();
                break;
            case R.id.conn_pair:
                intent = new Intent (this, ConnectActivity.class);
                startActivity (intent);
                //finish ();
                //bindDevice();
                //getStepsByDay ();
                break;
            case R.id.conn_about:
                intent = new Intent (this,CalibrationActivity.class);
                startActivity (intent);
                //updateSteps ();
                break;
        }
    }

    class FinishWatchHomeReceiver extends BroadcastReceiver {

        @Override public void onReceive (Context context, Intent intent) {
            if (intent.getAction ().equals (Constant.FINISH_WATCH_HOME_KEY)) {
                finish ();
            }
        }
    }

    @Override protected void onDestroy () {
        super.onDestroy ();
        unregisterReceiver (mHomeReceiver);
    }

    private void bindDevice () {
        Map<String, String> map = new HashMap<> ();
        map.put ("deviceId", 555 + "");
        map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
        HttpUtils.getInstance ()
            .requestJsonObjectPost (ConstantURL.BIND_DEVICE, 454, map, new OnResponseListener<JSONObject> () {
                @Override public void onStart (int what) {

                }

                @Override public void onSucceed (int what, Response<JSONObject> response) {
                    Log.e ("NIXONLOGIN", " 绑定 ==cg" + response.get ().toString ());
                }

                @Override public void onFailed (int what, Response<JSONObject> response) {
                    Log.e ("NIXONLOGIN", " 绑定 --shibai ");
                }

                @Override public void onFinish (int what) {

                }
            });
    }

}
