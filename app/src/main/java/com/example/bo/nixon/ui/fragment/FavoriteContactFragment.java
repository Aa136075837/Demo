package com.example.bo.nixon.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.ContactBean;
import com.example.bo.nixon.model.countrycode.CountrySortAdapter;
import com.example.bo.nixon.presenter.FavoriteContract;
import com.example.bo.nixon.ui.view.ContactPopupWindow;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public class FavoriteContactFragment extends BaseMvpFragment<FavoriteContract.FavoritePresenter>
        implements FavoriteContract.FavoriteNixonView, CountrySortAdapter
        .FavoriteCheckClickListener {

    private List<ContactBean.ObjectBean> mPopuDatas = new ArrayList<> ();
    private List<ContactBean.ObjectBean> mDatas = new ArrayList<> ();

    @BindView (R.id.favorite_content_ll)
    LinearLayout mFavoriteContentLl;
    @BindView (R.id.favorite_tip)
    TextView mFavoriteTip;
    @BindView (R.id.frequent_listview)
    ListView mFrequentListview;
    @BindView (R.id.frag_frequent_back_arrow)
    ImageView mFragFrequentBackArrow;
    @BindView (R.id.frequent_add)
    TextView mFrequentAdd;
    @BindView (R.id.contact_btn_edit)
    TextView mContactBtnEdit;
    @BindView (R.id.contact_btn_cancel)
    TextView mContactBtnCancel;
    @BindView (R.id.contact_btn_delete)
    TextView mContactBtnDelete;
    @BindView (R.id.contact_btn_ll)
    LinearLayout mContactBtnLl;

    /**
     * popuwindow 里ListView的adapter
     */
    private CountrySortAdapter mPopuAdapter;
    /**
     * 当前页面的 adapter
     */
    private CountrySortAdapter mAdapter;
    private ContactPopupWindow mContactPopupWindow;
    private FavoriteBackOnClickListener mListener;

    @Override
    protected FavoriteContract.FavoritePresenter createPresenter () {
        return new FavoriteContract.FavoritePresenter ();
    }

    @Override
    public int getLayoutResId () {
        return R.layout.activity_frequent_contacts;
    }

    @Override
    public void onStart () {
        super.onStart ();
    }

    @Override
    protected void initView () {
        super.initView ();

        mPopuAdapter = new CountrySortAdapter (NixonApplication.getContext (), mPopuDatas, true,
                R.color.white, true, mDatas);
        mAdapter = new CountrySortAdapter (NixonApplication.getContext (), mDatas, false, R.color
                .bg_color, false, null);
        mAdapter.setFavoriteCheckClickListener (this);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || getActivity ().checkSelfPermission
                (Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            presenter.initContractData ();
        } else {
            mFavoriteTip.setVisibility (View.VISIBLE);
            mFavoriteContentLl.setVisibility (View.GONE);
        }
        mFrequentListview.setAdapter (mAdapter);
        if (null == NixonApplication.appList) {
            NixonApplication.appList = new ArrayList<> ();
        }
        getContactFromServer ();
        int i = SPUtils.getInt (NixonApplication.getContext (), Constant.FIRST_CONTACT, 0);
        if (i != 1) {
            show1NewLongDialog (R.string.contact_tip, R.id.dialog_title, R.string.dont_forget);
        }
    }

    private void getContactFromServer () {
        Map<String, String> map = new HashMap<> ();
        map.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant
                .CUSTOMER_ID));
        presenter.getServerContact (map);
    }

    @Override
    public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mListener != null) {
                        Log.e ("BOBACK", " 返回settings ");
                        mListener.favorite2Settings ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    View.OnClickListener popuListener = new View.OnClickListener () {
        @Override
        public void onClick (View view) {
            switch (view.getId ()) {
                case R.id.contact_popu_cancel:
                    mContactPopupWindow.dismiss ();
                    break;
                case R.id.contact_popu_ok:
                    List<String> numberList = getPhoneNumberList ();
                    //ToastManager.show(NixonApplication.getContext(), "保存选中的联系人", Toast
                    // .LENGTH_SHORT);
                    //DaoSession daoSession = NixonApplication.getDaoSession ();
                    List<ContactBean.ObjectBean> checkedBeanList = mPopuAdapter
                            .getCheckedBeanList ();
                    Iterator<ContactBean.ObjectBean> iterator = checkedBeanList.iterator ();
                    while (iterator.hasNext ()) {
                        ContactBean.ObjectBean b = iterator.next ();
                        b.setChecked (false);
                        Log.d ("FRVORITECONTACT", " 0 " + mDatas.toString () + " 0          " + b
                                .toString ());
                        if (numberList.contains (b.getContactNameX ())) {
                            Log.e ("FRVORITECONTACT", " 1 " + mDatas.toString ());
                            iterator.remove (); //不添加重复联系人
                        }
                    }
                    mAdapter.updateListView (checkedBeanList);
                    addContact2Server (checkedBeanList);
                    mAdapter.notifyDataSetChanged ();
                    checkedBeanList.clear ();//清除缓存
                    mContactPopupWindow.dismiss ();
                    break;
                default:
                    break;
            }
        }
    };

    private List<String> getPhoneNumberList () {
        List<String> phoneList = new ArrayList<> ();
        for (ContactBean.ObjectBean bean : mDatas) {
            phoneList.add (bean.getContactNameX ());
        }
        return phoneList;
    }

    private void addContact2Server (List<ContactBean.ObjectBean> checkedBeanList) {
        List<Map<String, String>> parmas = getAddParams (checkedBeanList);
        presenter.addContactToServer (parmas);
    }

    @NonNull
    private List<Map<String, String>> getAddParams (List<ContactBean.ObjectBean> checkedBeanList) {
        List<Map<String, String>> parmas = new ArrayList<> ();
        Map<String, String> element;
        for (ContactBean.ObjectBean b : checkedBeanList) {
            element = new HashMap<> ();
            element.put ("contactName", b.getContactNameX ());
            element.put ("phoneNumber", b.getPhoneNumber ());
            element.put ("customerId", SPUtils.getString (NixonApplication.getContext (),
                    Constant.CUSTOMER_ID));

            Log.e ("NIXONLOGINaa", "element = " + element.toString ());
            parmas.add (element);
        }
        return parmas;
    }

    @NonNull
    private List<Map<String, String>> getDelParams (List<ContactBean.ObjectBean> checkedBeanList) {
        List<Map<String, String>> parmas = new ArrayList<> ();
        Map<String, String> element;
        for (ContactBean.ObjectBean b : checkedBeanList) {
            element = new HashMap<> ();
            element.put ("id", b.getId () + "");
            element.put ("customerId", SPUtils.getString (NixonApplication.getContext (),
                    Constant.CUSTOMER_ID));
            Log.e ("NIXONLOGINaa", "element = " + element.toString ());
            parmas.add (element);
        }
        return parmas;
    }

    @Override
    public void getContactData (List<ContactBean.ObjectBean> list) {
        Log.e ("BONIXON", "  初始化 adapter ");
        mPopuAdapter.setData (list);
    }

    @Override
    public void getServerContact (List<ContactBean.ObjectBean> list) {
        NixonApplication.appList.clear ();
        NixonApplication.appList.addAll (list);
        mAdapter.setData (list);
    }

    @OnClick ({R.id.frag_frequent_back_arrow, R.id.frequent_add, R.id.contact_btn_edit, R.id
            .contact_btn_delete, R.id.contact_btn_cancel})
    public void onViewClicked (View view) {
        switch (view.getId ()) {
            case R.id.frag_frequent_back_arrow:
                if (mListener != null) {
                    Log.e ("BOBACK", " 返回settings ");
                    mListener.favorite2Settings ();
                }
                break;
            case R.id.frequent_add:
                if (mContactPopupWindow == null) {
                    mContactPopupWindow = new ContactPopupWindow (NixonApplication.getContext (),
                            popuListener, mPopuAdapter);
                }
                mContactPopupWindow.showAtLocation (mFrequentAdd, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.contact_btn_edit:
                mAdapter.setShowRadio (true);
                mContactBtnEdit.setVisibility (View.GONE);
                mContactBtnLl.setVisibility (View.VISIBLE);
                break;
            case R.id.contact_btn_cancel:
                mAdapter.setShowRadio (false);
                mContactBtnEdit.setVisibility (View.VISIBLE);
                mContactBtnLl.setVisibility (View.GONE);
                break;
            case R.id.contact_btn_delete:
                deleteContact ();
                break;
        }
    }

    private void deleteContact () {
        List<ContactBean.ObjectBean> checkedBeanList = mAdapter.getCheckedBeanList ();
        mAdapter.deleteItem (checkedBeanList);
        List<Map<String, String>> params = getDelParams (checkedBeanList);
        presenter.deleteContactToServer (params);
        checkedBeanList.clear ();
        params.clear ();
        mContactPopupWindow = null;  // 清除pupupwindow 缓存，避免删除联系人后，还会有常用联系人标记
    }

    public void setFavoriteBackOnClickListener (FavoriteBackOnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void checkClick (boolean b) {
        if (b) {
            mContactBtnDelete.setTextColor (getResources ().getColor (R.color.sup_color));
        } else {
            mContactBtnDelete.setTextColor (getResources ().getColor (R.color.sup_text_color));
        }
    }

    public interface FavoriteBackOnClickListener {
        void favorite2Settings ();
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        SPUtils.putInt (NixonApplication.getContext (), Constant.FIRST_CONTACT, 1);
    }
}
