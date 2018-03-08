package com.example.bo.nixon.ui.fragment.alarm;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AddALarmNetBean;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.bean.AlarmNetBean;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.Response;
import com.yanzhenjie.nohttp.rest.SimpleResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ARZE
 * @version 创建时间：2017/6/15 14:24
 * @说明
 */
public class AlarmCacheHelper {

    private static final String TAG = "AlarmCacheHelper";

    private static final String ALARM_KEY = "ALARM_KEY";

    private static final String OPEN = "OPEN";
    private static final String REPEAT = "REPEAT";
    private static final String ACTION = "ACTION";
    private static final String TIME = "TIME";
    private static final String ID = "ID";
    private static final int GET_ALARM_WHAT = 0x1234;
    private static final int ADD_ALARM_WHAT = 0x1235;
    private static final int UPDATE_ALARM_WHAT = 0x1236;
    private static final int DEL_ALARM_WHAT = 0x1237;
    private static final int Add_ALARMS_WHAT = 0x1238;
    private static Gson gson = new Gson ();

    /**
     * 获取闹钟，本地有用本地，没有用服务器
     *
     * @return 闹钟列表
     */
    public static List<AlarmEventBean> getAlarmList (Context context) {
        List<AlarmEventBean> list = new ArrayList<> ();
        String json = SPUtils.getString (context, ALARM_KEY);
        Log.w (TAG, "run---------->" + json);
        try {
            if (!"[]".equals (json)) {
                JSONArray jsonArray = new JSONArray (json);
                for (int i = 0; i < jsonArray.length (); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (i);
                    AlarmEventBean alarmEventBean = createAlarmEventBean (jsonObject);
                    list.add (alarmEventBean);
                }
            } else { //从服务器拿
                //    getAlarmFromServer ();
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return list;
    }

    public static List<AlarmEventBean> getAlarmListFromLocal (Context context) {
        List<AlarmEventBean> list = new ArrayList<> ();
        String json = SPUtils.getString (context, ALARM_KEY);
        Log.w (TAG, "run---------->" + json);
        try {
            if (!"[]".equals (json)) {
                JSONArray jsonArray = new JSONArray (json);
                for (int i = 0; i < jsonArray.length (); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (i);
                    AlarmEventBean alarmEventBean = createAlarmEventBean (jsonObject);
                    list.add (alarmEventBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return list;
    }

    /**
     * 从服务器获取数据
     */
    public static void getAlarmFromServer (@NonNull final GetAlarmCallBack callBack) {
        final List<AlarmEventBean> list = new ArrayList<> ();
        Map<String, String> map = new HashMap<> ();
        map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
        HttpUtils.getInstance ()
            .requestCookieJsonObjectPost (ConstantURL.GET_ALARM, GET_ALARM_WHAT, map,
                new SimpleResponseListener<JSONObject> () {
                    @Override public void onSucceed (int what, Response<JSONObject> response) {
                        super.onSucceed (what, response);
                        AlarmNetBean bean = gson.fromJson (response.get ().toString (), AlarmNetBean.class);
                        Log.e ("ALARM", " bean 从服务器获取的闹钟 .. = " + bean.getInfo ());
                        if (RequestCode.SUCCESS.equals (bean.getCode ())) {
                            List<AlarmNetBean.ObjectBean> objectBeanList = bean.getObject ();
                            for (AlarmNetBean.ObjectBean b : objectBeanList) {
                                AlarmEventBean alarmEventBean = netBean2AlarmEventBean (b);
                                alarmEventBean.setId (b.getId ());
                                list.add (alarmEventBean);
                                String json = SPUtils.getString (NixonApplication.getContext (), ALARM_KEY);
                                try {
                                    saveAlarm2Local (NixonApplication.getContext (), alarmEventBean, json);
                                    callBack.call (true);
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            }
                            if (null != mListener) {
                                mListener.getAlarmSuccess (list);
                            }
                        }
                    }

                    @Override public void onFailed (int what, Response<JSONObject> response) {
                        super.onFailed (what, response);
                        Log.e ("ALARM", " fail  response = " + response.getException ());
                    }
                });
    }

    private synchronized static void getAlarmFromServer () {
        final List<AlarmEventBean> list = new ArrayList<> ();
        Map<String, String> map = new HashMap<> ();
        map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
        HttpUtils.getInstance ()
            .requestCookieJsonObjectPost (ConstantURL.GET_ALARM, GET_ALARM_WHAT, map,
                new SimpleResponseListener<JSONObject> () {
                    @Override public void onSucceed (int what, Response<JSONObject> response) {
                        super.onSucceed (what, response);
                        AlarmNetBean bean = gson.fromJson (response.get ().toString (), AlarmNetBean.class);
                        Log.e ("ALARM", " bean = " + bean.getInfo ());
                        if (RequestCode.SUCCESS.equals (bean.getCode ())) {
                            List<AlarmNetBean.ObjectBean> objectBeanList = bean.getObject ();
                            for (AlarmNetBean.ObjectBean b : objectBeanList) {
                                AlarmEventBean alarmEventBean = netBean2AlarmEventBean (b);
                                alarmEventBean.setId (b.getId ());
                                list.add (alarmEventBean);
                                String json = SPUtils.getString (NixonApplication.getContext (), ALARM_KEY);
                                try {
                                    saveAlarm2Local (NixonApplication.getContext (), alarmEventBean, json);
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            }
                            if (null != mListener) {
                                mListener.getAlarmSuccess (list);
                            }
                        }
                    }

                    @Override public void onFailed (int what, Response<JSONObject> response) {
                        super.onFailed (what, response);
                        Log.e ("ALARM", " fail  response = " + response.getException ());
                    }
                });
    }

    /**
     * bean 适配
     *
     * @return
     */
    @NonNull private static AlarmEventBean netBean2AlarmEventBean (AlarmNetBean.ObjectBean b) {
        int alarmTime = b.getHour () * 60 + b.getMin ();
        boolean openType = b.getIsOpen () == 1;
        boolean repeat = Integer.parseInt (b.getCycle ()) > 0;
        int repeatTime = getRepeatTime (b.getCycle ());
        String action = b.getEventReminder ();
        int id = b.getId ();
        return new AlarmEventBean (alarmTime, openType, repeat, repeatTime, action, id);
    }

    @NonNull private static AlarmEventBean netBean2AlarmEventBean (AddALarmNetBean.ObjectBean b) {
        int alarmTime = b.getHour () * 60 + b.getMin ();
        boolean openType = b.getIsOpen () == 1;
        boolean repeat = Integer.parseInt (b.getCycle ()) > 0;
        int repeatTime = getRepeatTime (b.getCycle ());
        String action = b.getEventReminder ();
        int id = b.getId ();
        return new AlarmEventBean (alarmTime, openType, repeat, repeatTime, action, id);
    }

    private static int getRepeatTime (String repeat) {
        if (repeat.length () != 7) {
            return 0;
        } else {
            int a = (int) repeat.charAt (0) * 2 ^ 7;
            int b = (int) repeat.charAt (1) * 2 ^ 6;
            int c = (int) repeat.charAt (2) * 2 ^ 5;
            int d = (int) repeat.charAt (3) * 2 ^ 4;
            int e = (int) repeat.charAt (4) * 2 ^ 3;
            int f = (int) repeat.charAt (5) * 2 ^ 2;
            int g = (int) repeat.charAt (6) * 2 ^ 1;
            return a + b + c + d + e + f + g;
        }
    }

    /**
     * json转为bean
     *
     * @return
     *
     * @throws JSONException
     */
    public static AlarmEventBean createAlarmEventBean (JSONObject jsonObject) throws JSONException {
        Log.w (TAG, "createAlarmEventBean::" + jsonObject.toString ());
        int open = jsonObject.getInt (OPEN);
        int repeat = jsonObject.getInt (REPEAT);
        int time = jsonObject.getInt (TIME);
        String action = jsonObject.getString (ACTION);
        int id = jsonObject.getInt (ID);
        return new AlarmEventBean (time, 1 == open, ((repeat & 128) >> 7) == 1, repeat, action, id);
    }

    /**
     * bean 转json
     *
     * @return
     *
     * @throws JSONException
     */
    private static JSONObject createEventBeanJson (AlarmEventBean bean) throws JSONException {
        JSONObject jsonObject = new JSONObject ();
        int open = bean.isOpenType () ? 1 : 0;
        int repeatTime = bean.getRepeatTime ();
        String action = bean.getmAction ();
        int time = bean.getAlarmTime ();
        int id = bean.getId ();
        jsonObject.put (TIME, time);
        jsonObject.put (OPEN, open);
        jsonObject.put (REPEAT, repeatTime);
        jsonObject.put (ACTION, action);
        jsonObject.put (ID, id);
        return jsonObject;
    }

    /**
     * 设置闹钟
     *
     * @return
     */
    public static boolean setAlarm (Context context, int index, AlarmEventBean alarmEventBean) {
        String json = SPUtils.getString (context, ALARM_KEY);
        boolean result = false;
        try {
            if (!TextUtils.isEmpty (json)) {
                JSONArray jsonArray = new JSONArray (json);
                JSONArray array = new JSONArray ();
                for (int i = 0; i < jsonArray.length (); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject (i);
                    if (index != i) {
                        array.put (jsonObject);
                    } else {
                        JSONObject object = createEventBeanJson (alarmEventBean);
                        array.put (object);
                        result = true;
                    }
                }
                if (result) {
                    SPUtils.putString (context, ALARM_KEY, array.toString ());
                    //updateAlarm2Server (alarmEventBean);
                } else {
                    addAlarm (context, alarmEventBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return result;
    }

    /**
     * 将闹钟变更情况上传至服务器
     */
    public static void updateAlarm2Server (AlarmEventBean alarmEventBean) {
        Map<String, String> paramsMap = getParamsMap (alarmEventBean);
        paramsMap.put ("id", alarmEventBean.getIndex () + "");
        HttpUtils.getInstance ()
            .requestCookieJsonObjectPost (ConstantURL.UPDATE_ALARM, UPDATE_ALARM_WHAT, paramsMap,
                new SimpleResponseListener<JSONObject> () {
                    @Override public void onSucceed (int what, Response<JSONObject> response) {
                        super.onSucceed (what, response);
                        AlarmNetBean netBean = gson.fromJson (response.get ().toString (), AlarmNetBean.class);
                        if (RequestCode.SUCCESS.equals (netBean.getCode ())) {
                            ToastManager.show (NixonApplication.getContext (), "服务器修改成功", Toast.LENGTH_SHORT);
                        }
                    }

                    @Override public void onFailed (int what, Response<JSONObject> response) {
                        super.onFailed (what, response);
                        Log.e ("ALARM", "  上传失败  ++ ");
                    }
                });
    }

    /**
     * 添加闹钟
     *
     * @return
     */
    public static boolean addAlarm (Context context, AlarmEventBean alarmEventBean) {
        String json = SPUtils.getString (context, ALARM_KEY);

        boolean result = false;
        if (!TextUtils.isEmpty (json)) {
            try {
                saveAlarm2Local (context, alarmEventBean, json);
            } catch (JSONException e) {
                e.printStackTrace ();
            }

            result = true;
            if (mListener != null) {
                mListener.uploadAlarmsSuccess (); //将现在列表中的bean换为 有 服务器 ID 的bean
            }
        }
        return result;
    }

    /**
     * 保存闹钟到本地
     *
     * @throws JSONException
     */
    private synchronized static void saveAlarm2Local (Context context, AlarmEventBean alarmEventBean, String json)
        throws JSONException {
        String js = SPUtils.getString (context, ALARM_KEY);
        JSONArray jsonArray = new JSONArray (js);
        jsonArray.put (createEventBeanJson (alarmEventBean));
        SPUtils.putString (context, ALARM_KEY, jsonArray.toString ());
    }

    /**
     * 将添加的闹钟上传至服务器
     */
    private static void upload2Server (final AlarmEventBean b) {
        Map<String, String> params = getParamsMap (b);
        HttpUtils.getInstance ()
            .requestCookieJsonObjectPost (ConstantURL.ADD_ALARM, ADD_ALARM_WHAT, params,
                new SimpleResponseListener<JSONObject> () {
                    @Override public void onSucceed (int what, Response<JSONObject> response) {
                        super.onSucceed (what, response);
                        AddALarmNetBean bean = gson.fromJson (response.get ().toString (), AddALarmNetBean.class);
                        Log.e ("ALARM", "info = " + bean.getInfo ());
                        if (RequestCode.SUCCESS.equals (bean.getCode ())) {
                            try {
                                //String string = SPUtils.getString (NixonApplication.getContext (), ALARM_KEY);
                                String string = SPUtils.getString (NixonApplication.getContext (), ALARM_KEY);
                                JSONArray jsonArray = new JSONArray (string);
                                AlarmEventBean alarmEventBean = netBean2AlarmEventBean (bean.getObject ());
                                alarmEventBean.setId (bean.getObject ().getId ()); //保存服务器返回的数据库ID，用于更改和删除
                                JSONObject jsonObject = createEventBeanJson (alarmEventBean);
                                jsonArray.put (jsonObject);
                                SPUtils.putString (NixonApplication.getContext (), ALARM_KEY,
                                    jsonArray.toString ());  //用带有服务器ID的闹钟数据，替换原来的数据
                                if (mListener != null) {
                                    mListener.uploadAlarmsSuccess (); //将现在列表中的bean换为 有 服务器 ID 的bean
                                }
                            } catch (JSONException e) {
                                e.printStackTrace ();
                            }
                        }
                    }

                    @Override public void onFailed (int what, Response<JSONObject> response) {
                        super.onFailed (what, response);
                        Log.e ("ALARM", "fail info = " + response.getException ());
                        if (mListener != null) {
                            mListener.uploadAlarmsSuccess (); //将现在列表中的bean换为 有 服务器 ID 的bean
                        }
                    }
                });
    }

    /**
     * 将bean转为请求参数
     *
     * @return
     */
    @NonNull private static Map<String, String> getParamsMap (AlarmEventBean b) {
        Map<String, String> params = new HashMap<> ();
        params.put ("hour", b.getAlarmTime () / 60 + "");
        params.put ("min", b.getAlarmTime () % 60 + "");
        params.put ("isOpen", (b.isOpenType () ? 1 : 0) + "");
        params.put ("cycle", Integer.toBinaryString (b.getRepeatTime ()));
        params.put ("eventReminder", b.getmAction ());
        params.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
        return params;
    }

    /**
     * 设置默认闹钟 3个 ，每个用户默认设置一次， 然后从服务器获取
     */
    public static void setDefaultEventAlarm (Context context) {
        //if (SPUtils.getInt (NixonApplication.getContext (), Constant.FIRST_ALARM) == 1) { //
        //    return;
        //}
        String json = SPUtils.getString (context, ALARM_KEY);
        if (TextUtils.isEmpty (json)) {
            try {
                JSONArray jsonArray = new JSONArray ();
                for (int i = 0; i < 3; i++) {
                    AlarmEventBean alarmEventBean = new AlarmEventBean (480, false, true, 190, "alarm");
                    JSONObject jsonObject = createEventBeanJson (alarmEventBean);
                    jsonArray.put (jsonObject);
                }
                SPUtils.putString (context, ALARM_KEY, jsonArray.toString ());
                //uploadDefaultToServer (jsonArray); // 上传初始闹钟到服务器
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }
    }

    /**
     * 上传初始闹钟到服务器
     */
    private static void uploadDefaultToServer (JSONArray jsonArray) {
        List<Map<String, String>> parmasList = new ArrayList<> ();
        try {
            for (int i = 0; i < jsonArray.length (); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject (i);
                AlarmEventBean alarmEventBean = createAlarmEventBean (jsonObject);
                Map<String, String> paramsMap = getParamsMap (alarmEventBean);
                parmasList.add (paramsMap);
            }
            HttpUtils.getInstance ()
                .requestCookieJsonArrayPost (ConstantURL.ADD_ALARMS, Add_ALARMS_WHAT, parmasList,
                    new SimpleResponseListener<JSONObject> () {
                        @Override public void onSucceed (int what, Response<JSONObject> response) {
                            super.onSucceed (what, response);
                            AlarmNetBean netBean = gson.fromJson (response.get ().toString (), AlarmNetBean.class);
                            if (RequestCode.SUCCESS.equals (netBean.getCode ())) {
                                try {
                                    JSONArray jsonArray = new JSONArray ();
                                    for (int i = 0; i < netBean.getObject ().size (); i++) {
                                        AlarmNetBean.ObjectBean objectBean = netBean.getObject ().get (i);
                                        AlarmEventBean alarmEventBean = netBean2AlarmEventBean (objectBean);
                                        alarmEventBean.setId (objectBean.getId ()); //保存服务器返回的数据库ID，用于更改和删除
                                        JSONObject jsonObject = createEventBeanJson (alarmEventBean);
                                        jsonArray.put (jsonObject);
                                    }
                                    SPUtils.putString (NixonApplication.getContext (), ALARM_KEY,
                                        jsonArray.toString ());  //用带有服务器ID的闹钟数据，替换原来的数据
                                    if (mListener != null) {
                                        mListener.uploadAlarmsSuccess (); //将现在列表中的bean换为 有 服务器 ID 的bean
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace ();
                                }
                            }
                        }

                        @Override public void onFailed (int what, Response<JSONObject> response) {
                            super.onFailed (what, response);
                            Log.e ("ALARM", " .... 批量上传失败  连接失败 ");
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

    /**
     * 删除闹钟（本地删除）
     *
     * @return
     */
    @TargetApi (Build.VERSION_CODES.KITKAT) public static boolean deleteEventAlarm (Context context,
        AlarmEventBean bean, int position) {
        boolean result = false;
        String json = SPUtils.getString (context, ALARM_KEY);
        try {
            JSONArray jsonArray = new JSONArray (json);
            if (jsonArray.length () > bean.getIndex ()) {
                jsonArray.remove (position);
                SPUtils.putString (context, ALARM_KEY, jsonArray.toString ());
                Log.e ("ALARMPOSITION", " position = + " + bean.getIndex ());
                result = true;
            }
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        return result;
    }

    /**
     * 删除闹钟（服务器删除）
     */
    public static void deleteServerAlarm (List<Map<String, String>> mapList) {
        HttpUtils.getInstance ()
            .requestCookieJsonArrayPost (ConstantURL.DEL_ALARM, DEL_ALARM_WHAT, mapList,
                new SimpleResponseListener<JSONObject> () {
                    @Override public void onSucceed (int what, Response<JSONObject> response) {
                        super.onSucceed (what, response);
                        AlarmNetBean netBean = gson.fromJson (response.get ().toString (), AlarmNetBean.class);
                        if (RequestCode.SUCCESS.equals (netBean.getCode ())) {
                            Log.e ("ALARM", " 闹钟 删除成功 ");
                        }
                    }

                    @Override public void onFailed (int what, Response<JSONObject> response) {
                        super.onFailed (what, response);
                        Log.e ("ALARM", "  删除失败 " + response.getException ());
                    }
                });
    }

    public static AlarmEventBean getADefaultEventBean () {
        return new AlarmEventBean (480, false, false, 0, "Alarm");
    }

    public static AlarmEventBean getEmptyEventBean () {
        return new AlarmEventBean (0, false, false, 0, "Alarm");
    }

    public static AlarmEventBean getAlarmEventBean (Context context, int index) {
        String json = SPUtils.getString (context, ALARM_KEY);
        if (TextUtils.isEmpty (json)) {
            return getADefaultEventBean ();
        } else {
            try {
                JSONArray jsonArray = new JSONArray (json);
                if (index < jsonArray.length ()) {
                    return createAlarmEventBean (jsonArray.getJSONObject (index));
                } else {
                    return getADefaultEventBean ();
                }
            } catch (JSONException e) {
                e.printStackTrace ();
            }
        }
        return getADefaultEventBean ();
    }

    private static GetServerAlarmListener mListener;

    public void setGetServerAlarmListener (GetServerAlarmListener listener) {
        mListener = listener;
    }

    public void freedGetServerAlarmListener (GetServerAlarmListener listener) {
        mListener = null;
    }

    public interface GetServerAlarmListener {
        void getAlarmSuccess (List<AlarmEventBean> list);

        void uploadAlarmsSuccess ();
    }

    public interface GetAlarmCallBack {
        void call (boolean complete);
    }
}
