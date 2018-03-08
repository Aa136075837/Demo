package com.example.bo.nixon.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.bo.nixon.base.NixonApplication;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Request;
import com.yanzhenjie.nohttp.rest.RequestQueue;
import com.yanzhenjie.nohttp.rest.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc 同一页面的不同请求可以使用同一个Listener，会将请求时传过来的what回调给Listener,用以区分不同请求
 */

public class HttpUtils {
    private static HttpUtils mNewHttpUtils;

    private final RequestQueue mRequestQueue;

    private HttpUtils () {
        mRequestQueue = NoHttp.newRequestQueue ();
    }

    public static HttpUtils getInstance () {
        if (mNewHttpUtils == null) {
            synchronized (HttpUtils.class) {
                if (mNewHttpUtils == null) {
                    mNewHttpUtils = new HttpUtils ();
                }
            }
        }
        return mNewHttpUtils;
    }

    /**
     * post请求，参数是json字符串，返回的也是json字符串
     * @param url url
     * @param what 请求标志
     * @param map 参数
     * @param mListener 回调
     */
    public void requestJsonObjectPost (String url, int what, Map<String, String> map,
        OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.POST);
        JSONObject jsonObject = new JSONObject (map);
        objRequest.setDefineRequestBodyForJson (jsonObject);
        Log.e ("NIXONLOGIN", "请求链接 ----》" + url);
        mRequestQueue.add (what, objRequest, mListener);
    }

    /**
     * post请求，请求会携带cookie
     * @param url url
     * @param what 请求标志
     * @param map 参数
     * @param mListener 回调
     */
    public void requestCookieJsonObjectPost (String url, int what, Map<String, String> map,
        OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.POST);
        String cookie = getCookie (url, what, map, mListener);
        if (cookie == null) {
            return;
        }
        objRequest.setHeader ("Cookie", cookie);
        Log.e ("NIXONLOGIN", "cookie = " + cookie);
        JSONObject jsonObject = new JSONObject (map);
        Log.e("NIXONLOGIN","run------>" + jsonObject.toString());
        objRequest.setDefineRequestBodyForJson (jsonObject);
        Log.e ("NIXONLOGIN", "请求链接 ----》" + url);
        mRequestQueue.add (what, objRequest, mListener);
    }

    /**
     *请求会携带cookie, 参数是jsonArray ，请求结果是jsonObject
     */
    public void requestCookieJsonArrayPost (String url, int what, List<Map<String, String>> mapList,
        OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> objectRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.POST);
        String cookie = getCookie (url, what, mapList, mListener);
        if (cookie == null) {
            return;
        }
        objectRequest.setHeader ("Cookie", cookie);
        Log.e ("UPDATELISTSTEPS", "cookie = " + cookie);
        JSONArray jsonArray = new JSONArray (mapList);
        String s = jsonArray.toString ();
        objectRequest.setDefineRequestBodyForJson (s);
        Log.e ("UPDATELISTSTEPS", "请求链接 ----》" + url + "  " + s);
        mRequestQueue.add (what, objectRequest, mListener);
    }
    /**
     *请求不携带cookie, 传入jsonArray,结果是jsonObject
     */
    public void requestJsonArrayPost (String url, int what, List<Map<String, String>> mapList,
        OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> onjectRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.POST);
        JSONArray jsonArray = new JSONArray (mapList);
        String s = jsonArray.toString ();
        onjectRequest.setDefineRequestBodyForJson (s);
        Log.e ("UPDATELISTSTEPS", "请求链接 ----》" + url + "  " + s);
        mRequestQueue.add (what, onjectRequest, mListener);
    }

    /**
     * get请求
     */
    public void requestGet (String url, int what, OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.GET);
        mRequestQueue.add (what, objRequest, mListener);
    }

    /**
     * 携带cookie的get请求
     */
    public void requestCookieGet (String url, int what, OnResponseListener<JSONObject> mListener) {
        Request<JSONObject> objRequest = NoHttp.createJsonObjectRequest (url, RequestMethod.GET);
        String cookie = SPUtils.getString (NixonApplication.getContext (), Constant.COOKIE_KEY, "");
        objRequest.addHeader ("Cookie",cookie);
        mRequestQueue.add (what, objRequest, mListener);
    }

    /**
     * GET请求拼接完整的url
     *
     * @return
     */
    @NonNull public String getCompleteUrl (String url, String[] keys, String[] values) {
        StringBuilder sb = new StringBuilder (url);
        if (null != keys && null != values && keys.length != 0 && keys.length == values.length) {
            sb.append ("?");
            for (int i = 0; i < values.length; i++) {
                sb.append (keys[i]).append ("=").append (values[i]).append ("&");
            }
            sb.deleteCharAt (sb.length () - 1);
        }
        return sb.toString ();
    }

    /**
     * GET请求拼接完整的url 拼接国家代码
     *
     * @return
     */
    @NonNull public String getCompleteUrlPhone (Context context, String url, String[] keys, String[] values) {
        StringBuilder sb = new StringBuilder (url);
        if (null != keys && null != values && keys.length != 0 && keys.length == values.length) {
            sb.append ("?");
            for (int i = 0; i < values.length; i++) {
                sb.append (keys[i]).append ("=%2B").append (values[i]).append ("&");
            }
            sb.deleteCharAt (sb.length () - 1);
        }
        String languageAndCountry = LanguageUtil.getLanguageAndCountry (context);
        return sb.toString () + "&lang_code=" + languageAndCountry;
    }

    public void requestCancel () {
        mRequestQueue.cancelAll ();
    }

    /**
     * 获取cookie,如果cookie为空，就去自动登录，登录成功后继续上一次的请求。
     * @param url 需要继续的url
     * @param what 需要继续的请求的标志
     * @param map 需要继续的请求的参数，参数是jsonObject
     * @param mListener
     * @return
     */
    public String getCookie (String url, int what, Map<String, String> map, OnResponseListener mListener) {
        String cookie = SPUtils.getString (NixonApplication.getContext (), Constant.COOKIE_KEY, "");
        if (!TextUtils.isEmpty (cookie)) {
            return cookie;
        } else { //cookie 为空  去自动登录
            Log.e ("NIXONLOGIN_COOKIE", " COOKIE 为null ");
            autoLogin (url, what, map, mListener);
            return null;
        }
    }
    /**
     * 获取cookie,如果cookie为空，就去自动登录，登录成功后继续上一次的请求。
     * @param url 需要继续的url
     * @param what 需要继续的请求的标志
     * @param map 需要继续的请求的参数，参数是jsonArray
     * @param mListener
     * @return
     */
    public String getCookie (String url, int what, final List<Map<String, String>> map,
        OnResponseListener<JSONObject> mListener) {
        String cookie = SPUtils.getString (NixonApplication.getContext (), Constant.COOKIE_KEY, "");
        if (!TextUtils.isEmpty (cookie)) {
            return cookie;
        } else { //cookie 为空  去自动登录
            Log.e ("NIXONLOGIN_COOKIE", " COOKIE 为null ");
            autoLogin (url, what, map, mListener);
            return null;
        }
    }

    private final int AUTO_LOGIN = 3;

    /**
     * 自动登录
     */
    public void autoLogin () {
        String mUserName = SPUtils.getString (NixonApplication.getContext (), Constant.USERNAME_KEY);
        String mPassWord = SPUtils.getString (NixonApplication.getContext (), Constant.PASSWORD_KEY);
        if (TextUtils.isEmpty (mUserName) || TextUtils.isEmpty (mPassWord)) {
            return;
        }
        Map<String, String> params = new HashMap<> ();
        params.put ("subject", mUserName);
        params.put ("password", mPassWord);
        requestJsonObjectPost (ConstantURL.LOGIN_URL, AUTO_LOGIN, params, new OnResponseListener<JSONObject> () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response<JSONObject> response) {
                List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                if (cookies != null && cookies.size () > 0) {
                    SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY, cookies.toString ());
                }
            }

            @Override public void onFailed (int what, Response<JSONObject> response) {

            }

            @Override public void onFinish (int what) {

            }
        });
    }

    /**
     * @param url
     *     自动登录成功后，将要请求的url
     * @param firstWhat
     *     自动登录成功后，将要请求的标志
     * @param map
     *     自动登录成功后，将要请求的参数（jsonObject）
     * @param mListener
     *     自动登录成功后，将要请求的回调
     */
    public void autoLogin (final String url, final int firstWhat, final Map<String, String> map,
        final OnResponseListener<JSONObject> mListener) {
        String mUserName = SPUtils.getString (NixonApplication.getContext (), Constant.USERNAME_KEY);
        String mPassWord = SPUtils.getString (NixonApplication.getContext (), Constant.PASSWORD_KEY);
        if (TextUtils.isEmpty (mUserName) || TextUtils.isEmpty (mPassWord)) {
            return;
        }
        Map<String, String> params = new HashMap<> ();
        params.put ("subject", mUserName);
        params.put ("password", mPassWord);
        requestJsonObjectPost (ConstantURL.LOGIN_URL, AUTO_LOGIN, params, new OnResponseListener<JSONObject> () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response<JSONObject> response) {
                List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                if (cookies != null && cookies.size () > 0) {
                    SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY, cookies.toString ());
                }
                requestCookieJsonObjectPost (url, firstWhat, map, mListener);
            }

            @Override public void onFailed (int what, Response<JSONObject> response) {

            }

            @Override public void onFinish (int what) {

            }
        });
    }
    /**
     * @param url
     *     自动登录成功后，将要请求的url
     * @param firstWhat
     *     自动登录成功后，将要请求的标志
     * @param map
     *     自动登录成功后，将要请求的参数（jsonArray）
     * @param mListener
     *     自动登录成功后，将要请求的回调
     */
    public void autoLogin (final String url, final int firstWhat, final List<Map<String, String>> map,
        final OnResponseListener<JSONObject> mListener) {
        String mUserName = SPUtils.getString (NixonApplication.getContext (), Constant.USERNAME_KEY);
        String mPassWord = SPUtils.getString (NixonApplication.getContext (), Constant.PASSWORD_KEY);
        if (TextUtils.isEmpty (mUserName) || TextUtils.isEmpty (mPassWord)) {
            return;
        }
        Map<String, String> params = new HashMap<> ();
        params.put ("subject", mUserName);
        params.put ("password", mPassWord);
        requestJsonObjectPost (ConstantURL.LOGIN_URL, AUTO_LOGIN, params, new OnResponseListener<JSONObject> () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response<JSONObject> response) {
                List<HttpCookie> cookies = NixonApplication.getmDbCookieStore ().getCookies ();
                if (cookies != null && cookies.size () > 0) {
                    SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY, cookies.toString ());
                }
                requestCookieJsonArrayPost (url, firstWhat, map, mListener);
            }

            @Override public void onFailed (int what, Response<JSONObject> response) {

            }

            @Override public void onFinish (int what) {

            }
        });
    }
}
