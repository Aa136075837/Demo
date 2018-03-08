package com.example.bo.nixon.presenter;

import android.net.Uri;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.bean.UserInfoBean;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;
import com.facebook.AccessToken;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.Scope;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */
public interface LoginContract {
    interface LoginNixonView extends BaseNixonView {
        void newUserLoginSuccess ();

        void oldUserLoginSuccess ();

        void loginFailed ();

        void noActivition ();//未激活

        void registerSuccess ();

        void registerFailed ();

        void registerUserExist();
    }

    class LoginPresenter extends BasePresenter<LoginNixonView> {
        private final int LOGIN = 0;
        private final int REGISTER = 1;
        private final int SENDLINK = 2;
        private final int LOGIN_GOOGLE = 3;
        private final int GET_USER_INFO_WHAT = 4;
        private final int LOGIN_FACEBOOK = 5;
        Gson mGson = new Gson ();
        OnResponseListener mListener = new OnResponseListener<JSONObject> () {
            @Override public void onStart (int what) {
            }

            @Override public void onSucceed (int what, Response<JSONObject> response) {
                LoginResponseBean responseBean = mGson.fromJson (response.get ().toString (), LoginResponseBean.class);
                Log.e ("NIXONLOGIN", "  ------code = " + responseBean.getCode () + " INfo" + responseBean.getInfo ());
                switch (what) {
                    case LOGIN:
                        switch (responseBean.getCode ()) {
                            case RequestCode.SUCCESS:
                                int customerId = responseBean.getObject ().getCustomerId ();
                                SPUtils.putString (NixonApplication.getContext (), Constant.CUSTOMER_ID,
                                    customerId + "");
                                List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                                SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY,
                                    cookies.toString ());
                                getUserInfoRequest (customerId + "");
                                break;
                            case RequestCode.PARAMETER_WRONG:
                            case RequestCode.USER_NOT_EXIST:
                            case RequestCode.USER_LOCKED:
                            case RequestCode.USERNAME_PASS_WRONG:
                                getView ().loginFailed ();
                                break;
                            case RequestCode.USER_NO_ACTIVATED:
                                getView ().noActivition ();
                                break;
                        }
                        break;
                    case REGISTER:
                        switch (responseBean.getCode ()) {
                            case RequestCode.SUCCESS:
                                getView ().registerSuccess ();
                                break;
                            case RequestCode.PARAMETER_WRONG:
                                break;
                            case RequestCode.USERNAME_ALREADY_EXIST:
                                getView ().registerUserExist ();
                                break;
                            case RequestCode.PASSWORD_NO_SAME:
                                break;
                            case RequestCode.ERROR_EMAIL:
                                break;
                            case RequestCode.PASSWORD_NOT_RULE:
                                getView ().registerFailed ();
                                break;
                            case RequestCode.SEND_EMAIL_FAILED:
                                break;
                        }
                        break;
                    case LOGIN_GOOGLE:
                        if (responseBean.getCode ().equals (RequestCode.SUCCESS)) {
                            List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                            int customerId = responseBean.getObject ().getCustomerId ();
                            Log.e ("NIXONLOGIN", " customerId = "
                                + responseBean.getObject ().getCustomerId ()
                                + " COOKIE = "
                                + cookies.toString ());
                            SPUtils.putString (NixonApplication.getContext (), Constant.CUSTOMER_ID, customerId + "");
                            SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY,
                                cookies.toString ());
                            getUserInfoRequest (customerId + "");
                        }
                        break;
                    case LOGIN_FACEBOOK:
                        if (responseBean.getCode ().equals (RequestCode.SUCCESS)){
                            List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                            int customerId = responseBean.getObject ().getCustomerId ();
                            Log.e ("NIXONLOGIN", " customerId = "
                                    + responseBean.getObject ().getCustomerId ()
                                    + " COOKIE = "
                                    + cookies.toString ());
                            SPUtils.putString (NixonApplication.getContext (), Constant.CUSTOMER_ID, customerId + "");
                            SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY,
                                    cookies.toString ());
                            getUserInfoRequest (customerId + "");
                        }
                        break;
                }
            }

            @Override public void onFailed (int what, Response<JSONObject> response) {
                Log.e ("NIXONLOGIN", "  ------ 》连接失败 ." + response.get ());
            }

            @Override public void onFinish (int what) {
            }
        };

        private void syncUserInfo (UserInfoBean infoBean) {
            UserInfoBean.ObjectBean infoBeanObject = infoBean.getObject ();
            SPUtils.putString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY, infoBeanObject.getNickname ());
            SPUtils.putString (NixonApplication.getContext (), Constant.WEIGHT_KEY,
                StringUtils.g2lbs (infoBeanObject.getWeight ()));
            SPUtils.putString (NixonApplication.getContext (), Constant.TALL_KEY,
                StringUtils.cm2in (infoBeanObject.getHeight ()));
            SPUtils.putString (NixonApplication.getContext (), Constant.GOAL_KEY, infoBeanObject.getActivityGoal ());
        }

        public void loginRequest (Map<String, String> map) {
            if (NetUtil.getNetworkState (NixonApplication.getContext ()) == 0) {
                return;
            }
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.LOGIN_URL, LOGIN, map, mListener);
        }

        public void registerRequest (Map<String, String> map) {
            if (NetUtil.getNetworkState (NixonApplication.getContext ()) == 0) {
                return;
            }
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.REGISTE_URL, REGISTER, map, mListener);
        }

        public void sendActivateLink (String s) {
            Map<String, String> map = new HashMap<> ();
            //String s = SPUtils.getString (NixonApplication.getContext (), Constant.USERNAME_KEY);
            map.put ("email", s);
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.SEND_ACTIVATE_LINK, SENDLINK, map, mListener);
        }

        public void googleLoginRequest (GoogleSignInResult result) {
            GoogleSignInAccount signInAccount = result.getSignInAccount ();
            Log.e ("NIXONLOGIN", " google dneglu  准备上传信息  ");
            if (null == signInAccount) {
                return;
            }
            String email = signInAccount.getEmail ();
            String displayName = signInAccount.getDisplayName ();
            String id = signInAccount.getId ();
            String idToken = signInAccount.getIdToken ();
            String serverAuthCode = signInAccount.getServerAuthCode ();
            String familyName = signInAccount.getFamilyName ();
            String givenName = signInAccount.getGivenName ();
            Set<Scope> grantedScopes = signInAccount.getGrantedScopes ();
            Uri photoUrl = signInAccount.getPhotoUrl ();
            Map<String, String> map = new HashMap<> ();
            map.put ("googleAccount", email);
            map.put ("displayName", displayName);
            if (null != photoUrl){
                map.put ("photoUrl", photoUrl.toString ());
            }else{
                map.put ("photoUrl", " ");
            }
            map.put ("googleId", id);
            map.put ("platform", "GOOGLE");
            Log.e ("NIXONLOGIN", " google dneglu  开始上传信息  ");
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.LOGIN_GOOGLE, LOGIN_GOOGLE, map, mListener);
        }

        public void facebookLoginRequest(AccessToken currentAccessToken){
            if (currentAccessToken == null){
                return;
            }
            String token = currentAccessToken.getToken ();
            String applicationId = currentAccessToken.getApplicationId ();
            String userId = currentAccessToken.getUserId ();
            Map<String,String> map = new HashMap<> ();
            map.put ("googleAccount",userId);
            map.put ("displayName",token);
            map.put ("googleId",applicationId);
            map.put ("photoUrl", " ");
            map.put ("platform","FACEBOOK");
            HttpUtils.getInstance ().requestJsonObjectPost (ConstantURL.LOGIN_GOOGLE, LOGIN_FACEBOOK, map, mListener);
        }

        private void getUserInfoRequest (String id) {
            Map<String, String> map = new HashMap<> ();
            map.put ("customerId", id);
            HttpUtils.getInstance ()
                .requestCookieJsonObjectPost (ConstantURL.GET_USER_INFO, GET_USER_INFO_WHAT, map,
                    new SimpleResponseListener<JSONObject> () {
                        @Override public void onSucceed (int what, Response<JSONObject> response) {
                            super.onSucceed (what, response);
                            UserInfoBean infoBean = mGson.fromJson (response.get ().toString (), UserInfoBean.class);
                            Log.e ("UserInfoBean", "  . ." + response.get ().toString ());
                            switch (what) {
                                case GET_USER_INFO_WHAT:
                                    switch (infoBean.getCode ()) {
                                        case RequestCode.SUCCESS:
                                            syncUserInfo (infoBean);
                                            if (null == infoBean.getObject ().getActivityGoal ()) {  //新用户第一次登录
                                                if (getView () != null) getView ().newUserLoginSuccess ();
                                            } else { //老用户登录
                                                if (getView () != null) getView ().oldUserLoginSuccess ();
                                            }
                                            break;
                                        case RequestCode.USER_NOT_EXIST:
                                            if (getView () != null) getView ().newUserLoginSuccess ();
                                            break;
                                    }
                                    break;
                            }
                        }

                        @Override public void onFailed (int what, Response<JSONObject> response) {
                            super.onFailed (what, response);
                        }
                    });
        }
    }
}
