package com.example.bo.nixon.presenter;

import android.content.ComponentName;
import android.util.Log;

import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.google.gson.Gson;
import com.smart.connectComponent.ConnectComponent;
import com.smart.smartble.SmartManager;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public interface PersonalFragmentContract {

    interface PersonalView extends BaseNixonView {
        void signOutSuccess();

        void singOutFailed();
    }

    class PersonalPresenter extends BaseBlePresenter<PersonalView> {
        Gson mGson = new Gson();
        ConnectComponent mConnectComponent;
        OnResponseListener mlistener = new OnResponseListener() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response response) {
                LoginResponseBean bean = mGson.fromJson(response.get().toString(), LoginResponseBean.class);
                Log.e("NIXONLOGIN", "  --- " + bean.getInfo());
                switch (bean.getCode()) {
                    case RequestCode.SUCCESS:
                        getView().signOutSuccess();
                        if (null != mConnectComponent) {
                            mConnectComponent.disConnectDevice();
                        }
                        break;
                    case RequestCode.SIGN_OUT_FAILED:
                        getView ().signOutSuccess ();
                        if (null != mConnectComponent) {
                            mConnectComponent.disConnectDevice();
                        }
                        break;
                }
            }

            @Override
            public void onFailed(int what, Response response) {
                if (getView ()!= null){
                    getView ().signOutSuccess ();
                }
                if (null != mConnectComponent) {
                    mConnectComponent.disConnectDevice();
                }
                Log.e("NIXONLOGIN", "  --- 连接失败");
            }

            @Override
            public void onFinish(int what) {

            }
        };
        private final int SIGN_OUT = 12121;

        public void requestSignOut() {
            HttpUtils.getInstance ().autoLogin ();
            HttpUtils.getInstance().requestCookieGet(ConstantURL.LOG_OUT, SIGN_OUT, mlistener);
        }

        @Override
        protected void serviceConnected(SmartManager smartManager) {
            mConnectComponent = ConnectComponent.getInstance(smartManager);
        }

        @Override
        protected void serviceDisconnected(ComponentName name) {

        }
    }
}
