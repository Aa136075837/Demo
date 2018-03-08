package com.example.bo.nixon.presenter;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.ContactBean;
import com.example.bo.nixon.manager.ToastManager;
import com.example.bo.nixon.model.countrycode.CountryComparator;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ConstantURL;
import com.example.bo.nixon.utils.HttpUtils;
import com.example.bo.nixon.utils.RequestCode;
import com.example.bo.nixon.utils.SPUtils;
import com.google.gson.Gson;
import com.yanzhenjie.nohttp.rest.OnResponseListener;
import com.yanzhenjie.nohttp.rest.Response;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public interface FavoriteContract {
    interface FavoriteNixonView extends BaseNixonView {
        void getContactData (List<ContactBean.ObjectBean> list);

        void getServerContact (List<ContactBean.ObjectBean> list);
    }

    class FavoritePresenter extends BasePresenter<FavoriteNixonView> {
        Gson mGson = new Gson ();
        private final int GET_SERVER_CONTACT = 1111;
        private final int ADD_CONTACT = 2222;
        private final int DEL_CONTACT = 3333;

        @Override public void attachView (FavoriteNixonView view) {
            super.attachView (view);
            getServerContact ();
        }

        private void getServerContact () {
            Map<String, String> map = new HashMap<> ();
            map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
            getServerContact (map);
        }

        /**
         * 获取手机通讯录中的联系人
         */
        public ArrayList<HashMap<String, String>> readContact () {
            // 首先,从raw_contacts中读取联系人的id("contact_id")
            // 其次, 根据contact_id从data表中查询出相应的电话号码和联系人名称
            // 然后,根据mimetype来区分哪个是联系人,哪个是电话号码

            Uri rawContactsUri = Uri.parse ("content://com.android.contacts/raw_contacts");
            Uri dataUri = Uri.parse ("content://com.android.contacts/data");

            ArrayList<HashMap<String, String>> list = new ArrayList<> ();

            // 从raw_contacts中读取所有联系人的id("contact_id")
            Cursor rawContactsCursor = NixonApplication.getContext ()
                .getContentResolver ()
                .query (rawContactsUri, new String[] { "contact_id" }, null, null, null);
            if (rawContactsCursor != null) {
                while (rawContactsCursor.moveToNext ()) {
                    String contactId = rawContactsCursor.getString (0);

                    // 根据contact_id从data表中查询出相应的电话号码和联系人名称, 实际上查询的是视图view_data
                    Cursor dataCursor = NixonApplication.getContext ()
                        .getContentResolver ()
                        .query (dataUri, new String[] { "data1", "mimetype" }, "contact_id=?",
                            new String[] { contactId +""}, null);

                    if (dataCursor != null) {
                        HashMap<String, String> map = new HashMap<> ();
                        while (dataCursor.moveToNext ()) {
                            String data1 = dataCursor.getString (0);
                            String mimetype = dataCursor.getString (1);

                            if ("vnd.android.cursor.item/phone_v2".equals (mimetype)) {//手机号码
                                map.put ("phone", data1);
                            } else if ("vnd.android.cursor.item/name".equals (mimetype)) {//联系人名字
                                map.put ("name", data1);
                            }
                        }
                        list.add (map);
                        dataCursor.close ();
                    }
                }
                rawContactsCursor.close ();
            }
            return list;
        }

        public void initContractData () {
            SortAsyncTask task = new SortAsyncTask ();
            task.execute ();
        }

        List<ContactBean.ObjectBean> mDatas = new ArrayList<> ();
        private CountryComparator pinyinComparator = new CountryComparator ();

        class SortAsyncTask extends
            AsyncTask<List<ContactBean.ObjectBean>, List<ContactBean.ObjectBean>, List<ContactBean.ObjectBean>> {

            @Override protected List<ContactBean.ObjectBean> doInBackground (List<ContactBean.ObjectBean>... mList) {
                ArrayList<HashMap<String, String>> hashMaps = readContact ();
                if (null != hashMaps && hashMaps.size () > 0) {
                    for (HashMap<String, String> map : hashMaps) {
                        String name = map.get ("name");
                        String phone = map.get ("phone");
                        if (TextUtils.isEmpty (name)) {
                            name = "contact";
                        }
                        if (TextUtils.isEmpty (phone)) {
                            phone = "12580";
                        }
                        ContactBean.ObjectBean bean = new ContactBean.ObjectBean (name, phone);
                        mDatas.add (bean);
                    }
                    Collections.sort (mDatas, pinyinComparator);
                }
                return mDatas;
            }

            @Override protected void onPostExecute (List<ContactBean.ObjectBean> list) {
                if (getView () == null) { // 页面已经销毁
                    return;
                }
                getView ().getContactData (list);
            }
        }

        OnResponseListener mListener = new OnResponseListener () {
            @Override public void onStart (int what) {

            }

            @Override public void onSucceed (int what, Response response) {
                ContactBean bean = mGson.fromJson (response.get ().toString (), ContactBean.class);
                Log.e ("NIXONLOGIN", "   成功  = " + response.get ().toString ());
                switch (what) {
                    case GET_SERVER_CONTACT:
                        switch (bean.getCode ()) {
                            case RequestCode.SUCCESS:
                                List<ContactBean.ObjectBean> beanList = bean.getObject ();
                                if (null != getView ()) {
                                    getView ().getServerContact (beanList);
                                }
                                break;
                            case RequestCode.NOT_LOGIN_STATE: //不在登录状态
                            case RequestCode.SECCION_FAILURE: // seccion过期
                                if (null != mParmas) {
                                    HttpUtils.getInstance ()
                                        .autoLogin (ConstantURL.GET_FAVORITE_CONTACT, GET_SERVER_CONTACT, mParmas,
                                            mListener);
                                }
                                break;
                        }
                        break;
                    case ADD_CONTACT:
                        switch (bean.getCode ()) {
                            case RequestCode.SUCCESS:
                                ToastManager.show (NixonApplication.getContext (), "上传成功", Toast.LENGTH_SHORT);
                                getServerContact();
                                //NixonApplication.appList.clear ();
                                break;
                        }
                        break;
                    case DEL_CONTACT:
                        switch (bean.getCode ()) {
                            case RequestCode.SUCCESS:
                                ToastManager.show (NixonApplication.getContext (), "删除成功", Toast.LENGTH_SHORT);
                                getServerContact();
                                //NixonApplication.appList.clear ();
                                break;
                        }
                        break;
                }
            }

            @Override public void onFailed (int what, Response response) {
                Log.e ("NIXONLOGIN", "   .  = 连接失败");
            }

            @Override public void onFinish (int what) {

            }
        };
        private Map<String, String> mParmas;

        public void getServerContact (Map<String, String> map) {
            mParmas = map;
            HttpUtils.getInstance ()
                .requestCookieJsonObjectPost (ConstantURL.GET_FAVORITE_CONTACT, GET_SERVER_CONTACT, map, mListener);
        }

        public void addContactToServer (List<Map<String, String>> mapList) {
            HttpUtils.getInstance ()
                .requestCookieJsonArrayPost (ConstantURL.ADD_FAVORITE_CONTACT, ADD_CONTACT, mapList, mListener);
        }

        public void deleteContactToServer (List<Map<String, String>> mapList) {
            HttpUtils.getInstance ()
                .requestCookieJsonArrayPost (ConstantURL.DEL_FAVORITE_CONTACT, DEL_CONTACT, mapList, mListener);
        }
    }
}
