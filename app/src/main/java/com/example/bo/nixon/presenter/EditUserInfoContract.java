package com.example.bo.nixon.presenter;

import android.content.ComponentName;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.RequestCode;
import com.google.gson.Gson;
import com.smart.dataComponent.DataComponent;
import com.smart.smartble.SmartManager;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.Map;

/**
 * @author bo.
 * @Date 2017/6/8.
 * @desc
 */

public interface EditUserInfoContract {
    interface EditUserInfoView extends BaseNixonView {
        void upLoadSuccess ();

        void upLoadFail ();
    }

    class EditUserInfoPresenter extends BaseBlePresenter<EditUserInfoView> {
        private final int UPDATE_INFO = 564;
        Gson mGson = new Gson ();
        private DataComponent mDataComponent;

        @Override
        protected void serviceConnected (SmartManager smartManager) {
            mDataComponent = new DataComponent (smartManager);
            mDataComponent.registerComponent ();
        }

        @Override
        protected void serviceDisconnected (ComponentName name) {

        }

        @Override
        public void detachView () {
            super.detachView ();
            if (null != mDataComponent) {
                mDataComponent.unRegisterComponent ();
            }
        }

        public void setTarget2Watch (int target) {
            if (null != mDataComponent) {
                mDataComponent.setSportTarget (target);
            }
        }

        public void upLoadUserInfo (Map<String, String> map) {
            if (NetUtil.getNetworkState (NixonApplication.getContext ()) == 0) {
                return;
            }
            HttpUtils.getInstance ().requestCookieJsonObjectPost (ConstantURL.UPDATE_USER_INFO,
                    UPDATE_INFO, map, mListener);
        }

        OnResponseListener mListener = new OnResponseListener () {
            @Override
            public void onStart (int what) {

            }

            @Override
            public void onSucceed (int what, Response response) {
                LoginResponseBean bean = mGson.fromJson (response.get ().toString (),
                        LoginResponseBean.class);
                Log.e ("NIXONLOGIN", " userinfo = " + bean.getCode () + " ===" + bean.getInfo ());
                switch (bean.getCode ()) {
                    case RequestCode.SUCCESS:
                        if (null != getView ()) {
                            getView ().upLoadSuccess ();
                        }
                        break;
                    case RequestCode.UPDATE_NULL_INFO:
                        break;
                    case RequestCode.USER_NOT_EXIST:
                        break;
                    case RequestCode.OUT_OF_RANGE:
                        break;
                    case RequestCode.NOT_LOGIN_STATE:
                        break;
                }
            }

            @Override
            public void onFailed (int what, Response response) {
                Log.e ("NIXONLOGIN", " 网络或者 服务器异常");
            }

            @Override
            public void onFinish (int what) {

            }
        };
    }
}
