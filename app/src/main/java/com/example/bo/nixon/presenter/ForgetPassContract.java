package com.example.bo.nixon.presenter;

import android.util.Log;
import com.example.bo.nixon.bean.LoginResponseBean;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/6/7.
 * @desc
 */

public interface ForgetPassContract {
    interface ForgetPassNixonView extends BaseNixonView {
        void sendSuccess ();

        void sendFailed ();

        void checkSuccess ();

        void checkFailed ();

        void resetSuccess ();

        void resetFialed ();
    }

    class ForgetPassPresenter extends BasePresenter<ForgetPassNixonView> {
        Gson mGson = new Gson ();
        OnResponseListener mListener = new OnResponseListener () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response response) {
                LoginResponseBean bean = mGson.fromJson (response.get ().toString (), LoginResponseBean.class);
                Log.e ("NIXONLOGIN", "  FORGET" + bean.getCode () + "   " + bean.getInfo ());
                switch (what) {
                    case SEND_CODE:
                        switch (bean.getCode ()) {
                            case RequestCode.SUCCESS:
                                getView ().sendSuccess ();
                                break;
                            case RequestCode.PASSWORD_NOT_RULE:
                                break;
                            case RequestCode.USER_NOT_EXIST:
                                break;
                            case RequestCode.CHECK_CODE_ALREADY_SEND:
                                break;
                            case RequestCode.CHECK_CODE_SEND_FAILED:
                                getView ().sendFailed ();
                                break;
                        }
                        break;
                    case RESET_PASSWORD:
                        switch (bean.getCode ()) {
                            case RequestCode.SUCCESS:
                                getView ().resetSuccess ();
                                break;
                            case RequestCode.PASSWORD_NOT_RULE:
                                break;
                            case RequestCode.USER_NOT_EXIST:
                                break;
                            case RequestCode.CHECK_CODE_ALREADY_SEND:
                                break;
                            case RequestCode.CHECK_CODE_SEND_FAILED:
                                getView ().resetFialed ();
                                break;
                        }
                        break;
                    case CHECK_CODE:
                        switch (bean.getCode ()) {
                            case RequestCode.PASSWORD_NOT_RULE:
                                break;
                            case RequestCode.SUCCESS:
                                getView ().checkSuccess ();
                                break;
                            case RequestCode.CODE_CHECK_WRONG:
                                getView ().checkFailed ();
                                break;
                        }
                        break;
                }
            }

            @Override public void onFailed (int what, Response response) {
                Log.e ("NIXONLOGIN", "shibai  ---");
            }

            @Override public void onFinish (int what) {

            }
        };
        private final int SEND_CODE = 0;
        private final int RESET_PASSWORD = 1;
        private final int CHECK_CODE = 2;

        public void sendCodeRequest (String userEmail) {
            String completeUrl = HttpUtils.getInstance ()
                .getCompleteUrl (ConstantURL.SEND_RESET_PSD_CODE, new String[] { "subject" },
                    new String[] { userEmail });
            HttpUtils.getInstance ().requestGet (completeUrl, SEND_CODE, mListener);
        }

        public void resetPassword (Map<String, String> parmas) {
            HttpUtils.getInstance ()
                .requestJsonObjectPost (ConstantURL.RESET_PASSWORD, RESET_PASSWORD, parmas, mListener);
        }

        public void checkCode (String email, String code) {
            String completeUrl = HttpUtils.getInstance ()
                .getCompleteUrl (ConstantURL.RESET_CHECK_CODE, new String[] { "subject", "code" },
                    new String[] { email, code });
            HttpUtils.getInstance ().requestGet (completeUrl, CHECK_CODE, mListener);
        }
    }
}
