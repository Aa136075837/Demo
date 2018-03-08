package com.example.bo.nixon.ui.fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.AlarmListViewAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.presenter.AlarmFragmentContract;
import com.example.bo.nixon.ui.fragment.alarm.AlarmCacheHelper;
import com.example.bo.nixon.ui.view.AlarmEventItemView;
import com.example.bo.nixon.ui.view.SmartToast;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.SmartManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class AlarmFragment extends BaseMvpFragment<AlarmFragmentContract.AlarmPresenter>
    implements AlarmFragmentContract.AlarmView, AlarmListViewAdapter.OnItemClickListener,
    AlarmCacheHelper.GetServerAlarmListener {

    private static final String TAG = "AlarmFragment";
    private static final String ALARM_KEY = "ALARM_KEY";

    @BindView (R.id.alarm_list_view) ListView mAlarmListView;
    @BindView (R.id.alarm_frame_add) TextView mAlarmFrameAdd;
    @BindView (R.id.alarm_frame_edit) TextView mAlarmFrameEdit;
    @BindView (R.id.alarm_frame_cancel) TextView mAlarmFrameCancel;
    @BindView (R.id.alarm_frame_delete) TextView mAlarmFrameDelete;
    @BindView (R.id.edit_delete_ll) LinearLayout mEditDeleteLl;

    private AlarmListViewAdapter mAdapter;
    private AlarmCacheHelper mAlarmCacheHelper;

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity ().finish ();
                    getActivity ().overridePendingTransition (R.anim.activity_out_bit, R.anim.activity_out_bit);
                    return true;
                }
                return false;
            }
        });
    }

    @Override protected AlarmFragmentContract.AlarmPresenter createPresenter () {
        return new AlarmFragmentContract.AlarmPresenter ();
    }

    @Override public int getLayoutResId () {
        return R.layout.fragment_alarm;
    }

    @Override protected void initView () {
        super.initView ();
        mAlarmListView.setAdapter (mAdapter);
        initAdapter ();
        initListener ();
    }

    private void initListener () {
        //  mAlarmListView.setOnItemClickListener(this);
    }

    private void initFragment () {

        mAlarmListView.setVisibility (View.GONE);
    }

    private List<AlarmEventBean> mDatas = new ArrayList<> ();

    private void initAdapter () {
        mAlarmCacheHelper = new AlarmCacheHelper ();
        mAlarmCacheHelper.setGetServerAlarmListener (this);
        AlarmCacheHelper.setDefaultEventAlarm (getActivity ());
        mDatas.clear ();
        mDatas.addAll (AlarmCacheHelper.getAlarmList (getActivity ()));
        Log.w (TAG, "initAdapter::" + mDatas.size ());
        mAdapter = new AlarmListViewAdapter (mDatas, mCallBack);
        mAdapter.setOnItemClickListener (this);
        mAdapter.setEditState (false);
        mAdapter.setClickType (false);
        mAdapter.setSlide (true);
        mAlarmListView.setAdapter (mAdapter);
    }

    private void notifySetChange () {
        mDatas.clear ();
        mDatas.addAll (AlarmCacheHelper.getAlarmList (getActivity ()));
        Log.e (TAG, "   notifySetChange  " + mDatas.size ());
        mAdapter.notifyDataSetChanged ();
    }

    @Override public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (!hidden) {
            notifySetChange ();
        }
    }

    AlarmEventItemView.AlarmEventCallBack mCallBack = new AlarmEventItemView.AlarmEventCallBack () {
        @Override public void onDataChange (AlarmEventBean alarmBean) {
            AlarmCacheHelper.setAlarm (getActivity (), alarmBean.getIndex (), alarmBean);
            notifySetChange ();
        }

        @Override public void onOpenTypeClick (AlarmEventBean alarmBean) {
            Log.w (TAG, "onOpenTypeClick:::");
            notifySetChange ();
        }
    };

    @OnClick ({ R.id.alarm_frame_add, R.id.alarm_frame_edit, R.id.alarm_frame_cancel, R.id.alarm_frame_delete })
    public void onViewClicked (View view) {
        switch (view.getId ()) {
            case R.id.alarm_frame_add:
                if (mDatas.size () >= 5) {
                    SmartToast.getInstanse (getActivity (), getResources ().getString (R.string.alarm_toast)).show (1500);
                    return;
                }
                if (!SmartManager.isDiscovery()) {
                    SmartToast.getInstanse (getActivity (), getResources ().getString (R.string.please_connect_watch)).show ();
                    return;
                }
                mListener.go2AlarmListFrag (AlarmCacheHelper.getADefaultEventBean (), AlarmManagerFragment.ADD_ALARM);
                break;
            case R.id.alarm_frame_edit:
                mAlarmListView.setEnabled (true);
                mAdapter.setEditState (true);
                mAdapter.setClickType (true);
                mAdapter.setSlide (true);
                mAlarmFrameEdit.setVisibility (View.GONE);
                mEditDeleteLl.setVisibility (View.VISIBLE);
                break;
            case R.id.alarm_frame_cancel:
                mAlarmListView.setEnabled (false);
                mAdapter.setEditState (false);
                mAdapter.setClickType (false);
                mAdapter.setSlide (true);
                mAlarmFrameEdit.setVisibility (View.VISIBLE);
                mEditDeleteLl.setVisibility (View.GONE);
                break;
            case R.id.alarm_frame_delete:
                List<Map<String, String>> mapList = new ArrayList<> ();
                for (int i = 0; i < mDatas.size (); i++) {
                    AlarmEventBean bean = mDatas.get (i);
                    Map<String, String> map = new HashMap<> ();
                    Log.e ("ALARM", " Onclick  id = " + bean.getId ());
                    map.put ("id", bean.getId () + "");
                    map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
                    mapList.add (map);
                    if (bean.isSelect ()) {
                        AlarmCacheHelper.deleteEventAlarm (getActivity (), bean,i);
                        mDatas.remove (bean);
                    }
                }
                mAdapter.notifyDataSetChanged ();
                //AlarmCacheHelper.deleteServerAlarm (mapList);
                break;
        }
    }

    private AlarmFragListviewListener mListener;

    public void setAlarmFragListviewListener (AlarmFragListviewListener listener) {
        mListener = listener;
    }

    @Override public void onItem (AlarmEventBean alarmEventBean, int position) {
        for (AlarmEventBean eventBean : mDatas) {
            Log.w (TAG, "run------------》AlarmEventBean:: " + eventBean.getIndex ());
        }
        if (null != mListener) {
            Log.w (TAG, "run----------->" + alarmEventBean.getIndex () + "   or  " + position);
            alarmEventBean.setIndex (position);
            mListener.go2AlarmListFrag (alarmEventBean, AlarmManagerFragment.SET_ALARM);
        }
    }

    @Override public void slideDelete (int position) {
        String string = SPUtils.getString (NixonApplication.getContext (), ALARM_KEY);
        try {
            JSONArray jsonArray = new JSONArray (string);
            JSONObject jsonObject = jsonArray.getJSONObject (position);
            AlarmEventBean alarmEventBean = AlarmCacheHelper.createAlarmEventBean (jsonObject);
            AlarmCacheHelper.deleteEventAlarm (getActivity (), alarmEventBean,position);
            List<Map<String, String>> mapList = new ArrayList<> ();
            Map<String, String> map = new HashMap<> ();
            map.put ("id", alarmEventBean.getId () + "");
            map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
            mapList.add (map);
            //AlarmCacheHelper.deleteServerAlarm (mapList);
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

    @Override public void checkBoxClick (boolean b) {
        if (!b) {
            mAlarmFrameDelete.setTextColor (getResources ().getColor (R.color.sup_text_color));
        } else {
            mAlarmFrameDelete.setTextColor (getResources ().getColor (R.color.sup_color));
        }
    }

    @Override public void upLoadAlarm () {

        notifySetChange();
    }

    @Override public void getAlarmSuccess (List<AlarmEventBean> list) {
        mDatas.clear ();
        mDatas.addAll (list);
        mAdapter.notifyDataSetChanged ();
    }

    @Override public void uploadAlarmsSuccess () {
        notifySetChange ();
    }

    public interface AlarmFragListviewListener {
        void go2AlarmListFrag (AlarmEventBean alarmEventBean, int mode);
    }

    @Override public void onDestroyView () {
        super.onDestroyView ();
        SPUtils.putInt (NixonApplication.getContext (), Constant.FIRST_ALARM, 1);
        mAlarmCacheHelper.freedGetServerAlarmListener (this);
        mAlarmCacheHelper = null;
    }

}
