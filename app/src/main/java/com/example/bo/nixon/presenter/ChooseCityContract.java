package com.example.bo.nixon.presenter;

import android.util.Log;

import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;

import java.util.HashMap;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public interface ChooseCityContract {
    interface ChooseCityNixonView extends BaseNixonView {
        void requestSuss (List<ChooseCityBean.ObjectBean> list);

        void requestFail ();
    }

    class ChooseCityPresenter extends BasePresenter<ChooseCityNixonView> {

        private int GETLIST = 356;
        Gson mGson = new Gson ();

        OnResponseListener mListener = new OnResponseListener () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response response) {
                ChooseCityBean bean = mGson.fromJson (response.get ().toString (), ChooseCityBean.class);
                Log.e ("CHOOSE", " 0 =  " + response.get ());
                if (null != getView ()) getView ().requestSuss (bean.getObject ());
            }

            @Override public void onFailed (int what, Response response) {
                Log.e ("CHOOSE", " 请求失败 ");
                if (null != getView ()) getView ().requestFail ();
            }

            @Override public void onFinish (int what) {

            }
        };

        public void requestCityList () {
            HttpUtils.getInstance ()
                .requestJsonObjectPost (ConstantURL.GET_CITY_LIST, GETLIST, new HashMap<String, String> (), mListener);
        }
    }
}
