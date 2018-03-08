package com.example.bo.nixon.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/8/26.
 * @desc
 */

public interface SplashContract {
    interface SplashNixonView extends BaseNixonView {

        void autoLoginFailed ();

        void noActivation ();

        void autoLoginSuccess ();

        void badNet ();

    }

    class SplashPresenter extends BasePresenter<SplashNixonView> {
        private final int LOGIN = 0x123;

        private final int SENDLINK = 0x135;
        Gson mGson = new Gson ();
        private OnResponseListener<JSONObject> mListener = new SimpleResponseListener<JSONObject>
                () {

            @Override
            public void onSucceed (int what, Response<JSONObject> response) {
                super.onSucceed (what, response);
                LoginResponseBean responseBean = mGson.fromJson (response.get ().toString (),
                        LoginResponseBean.class);
                Log.e ("NIXONLOGIN", "  ------code = " + responseBean.getCode () + " INfo" +
                        responseBean.getInfo ());
                switch (what) {
                    case LOGIN:
                        switch (responseBean.getCode ()) {
                            case RequestCode.SUCCESS:
                                int customerId = responseBean.getObject ().getCustomerId ();
                                SPUtils.putString (NixonApplication.getContext (), Constant
                                        .CUSTOMER_ID, customerId + "");
                                List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ()
                                        .getCookies ();
                                SPUtils.putString (NixonApplication.getContext (), Constant
                                        .COOKIE_KEY, cookies.toString ());
                                if (getView () != null) {
                                    Log.e ("NIXONAUTO", " 成功");
                                    getView ().autoLoginSuccess ();
                                }
                                break;
                            case RequestCode.PARAMETER_WRONG:
                            case RequestCode.USER_NOT_EXIST:
                            case RequestCode.USER_LOCKED:
                            case RequestCode.USERNAME_PASS_WRONG:
                                Log.e ("NIXONAUTO", " 失败");
                                break;
                            case RequestCode.USER_NO_ACTIVATED:
                                if (getView () != null) {
                                    Log.e ("NIXONAUTO", " 未激活 ");
                                    getView ().noActivation ();
                                    sendActivateLink (SPUtils.getString (NixonApplication
                                            .getContext (), Constant.USERNAME_KEY));
                                }
                                break;
                        }
                        break;
                }
            }

            @Override
            public void onFailed (int what, Response<JSONObject> response) {
                super.onFailed (what, response);
                Log.e ("NIXONAUTO", " 连接 失败，");
                if (getView () != null) {
                    getView ().autoLoginFailed ();
                }
            }
        };

        public void sendActivateLink (String s) {
            Map<String, String> map = new HashMap<> ();
            map.put ("email", s);
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.SEND_ACTIVATE_LINK,
                    SENDLINK, map, mListener);
        }

        public void autoLogin () {
            String mUserName = SPUtils.getString (NixonApplication.getContext (), Constant
                    .USERNAME_KEY);
            String mPassWord = SPUtils.getString (NixonApplication.getContext (), Constant
                    .PASSWORD_KEY);
            if (TextUtils.isEmpty (mUserName) || TextUtils.isEmpty (mPassWord)) {
                Log.e ("NIXONAUTO", " 保存的用户名或密码为空");
                if (getView () != null) {
                    getView ().autoLoginFailed ();
                }
                return;
            }
            if (NetUtil.getNetworkState (NixonApplication.getContext ()) == NetUtil.NETWORN_NONE) {
                if (getView () != null) {
                    getView ().badNet ();
                }
            }
            if (NetUtil.getNetworkState (NixonApplication.getContext ()) == 0) {
                return;
            }

            Map<String, String> params = new HashMap<> ();
            params.put ("subject", mUserName);
            params.put ("password", mPassWord);
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.LOGIN_URL, LOGIN, params, mListener);
        }
    }
}
