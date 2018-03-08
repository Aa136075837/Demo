package com.example.bo.nixon.ui.fragment.personal;

import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.PersonalListAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.presenter.PersonalFragmentContract;
import com.example.bo.nixon.ui.activity.LoginAndRegActivity;
import com.example.bo.nixon.ui.fragment.UnitFragment;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/5/26.
 * @desc
 */

public class PersonalFragment extends BaseMvpFragment<PersonalFragmentContract.PersonalPresenter>
        implements PersonalFragmentContract.PersonalView, AdapterView.OnItemClickListener,
        PersonalListAdapter.EditClickListener, UnitFragment.UnitChangeListener {

    private static final int NAME = 0;
    private static final int WEIGHT = 1;
    private static final int HEIGHT = 2;
    private static final int GOAL = 3;
    private static final int TIMEZONE = 4;
    @BindView (R.id.personal_sign_out)
    TextView mPersonalSignOut;
    @BindView (R.id.personal_frag_back_arrow)
    ImageView mPersonalFragBackArrow;
    @BindView (R.id.personal_frag_toolbar)
    LinearLayout mPersonalFragToolbar;
    private ArrayList<PersonalInfoBean> mData = new ArrayList<> ();
    private PersonalListViewClickListener mPersonalListViewClickLIstener;

    @BindView (R.id.personal_ll)
    LinearLayout mPersonalLl;
    @BindView (R.id.personal_list_view)
    ListView mPersonalListView;
    @BindView (R.id.personal_frag)
    FrameLayout mPersonalFrag;
    private PersonalListAdapter mAdapter;

    @Override
    protected PersonalFragmentContract.PersonalPresenter createPresenter () {
        return new PersonalFragmentContract.PersonalPresenter ();
    }

    @Override
    public int getLayoutResId () {
        return R.layout.fragment_personal;
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
                    if (null != mPersonalListViewClickLIstener) {
                        Log.d ("OnBackPressedListener", "  PersonalFragment");
                        mPersonalListViewClickLIstener.personalBack2Setting ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void initView () {
        super.initView ();
        UnitFragment.setUnitChangeListener (this);
        initData ();
        initAdapter ();
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (!hidden && mAdapter != null) {
            mData.clear ();
            initData ();
            mAdapter.setData (mData);
        }
    }

    private void initData () {
        Log.e ("UNIT", " height = " + SPUtils.getBoolean (NixonApplication.getContext (),
                Constant.UNIT_HEIGHT));
        mData.add (new PersonalInfoBean ("Name", SPUtils.getString (NixonApplication.getContext
                (), Constant.WATCH_NAME_KEY), NAME));
        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT)) {
            //            mData.add (new PersonalInfoBean ("Weight",
            //                StringUtils.lbs2kg (SPUtils.getString (NixonApplication.getContext
            // (), Constant.WEIGHT_KEY)) + "kg",
            //                WEIGHT));
            String string = SPUtils.getString (NixonApplication.getContext (), Constant
                    .FALSE_WEIGHT_KEY,"");
            if (string.endsWith ("lbs")){
                String[] split = string.split (" ");
                string = StringUtils.lbs2kg (split[0])+" kg";
            }
            mData.add (new PersonalInfoBean ("Weight", string, WEIGHT));


        } else {
            mData.add (new PersonalInfoBean ("Weight", SPUtils.getString (NixonApplication
                    .getContext (), Constant.WEIGHT_KEY) + " lbs", WEIGHT));
        }

        if (SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT)) {
            //            mData.add (new PersonalInfoBean ("Height",
            //                StringUtils.inch2m (SPUtils.getString (NixonApplication.getContext
            // (), Constant.TALL_KEY)) + " m",
            //                HEIGHT));
            String string = SPUtils.getString (NixonApplication.getContext (), Constant
                    .FALSE_TALL_KEY,"");
            if (string.endsWith ("in")) {
                String[] split = string.split (" ");
                String ft = split[0].substring (0, split[0].length () - 2);
                String in = split[1].substring (0, split[1].length () - 2);
                int ftInt = Integer.parseInt (ft);
                int inInt = Integer.parseInt (in);
                string = StringUtils.inch2m (((ftInt * 12) + inInt) + "") + " m";
            }
            mData.add (new PersonalInfoBean ("Height", string, HEIGHT));
        } else {
            mData.add (new PersonalInfoBean ("Height", StringUtils.getFt (SPUtils.getString
                    (NixonApplication.getContext (), Constant.TALL_KEY)) + "’" + StringUtils
                    .getIn (SPUtils.getString (NixonApplication.getContext (), Constant.TALL_KEY)
                    ) + "”", HEIGHT));
        }
        mData.add (new PersonalInfoBean ("Activity Goal", SPUtils.getString (NixonApplication
                .getContext (), Constant.GOAL_KEY) + " steps", GOAL));
        mData.add (new PersonalInfoBean ("Automatic\n timezone", true, TIMEZONE));
    }

    private void initAdapter () {
        mAdapter = new PersonalListAdapter (mData);
        mAdapter.setEditClickListener (this);
        mPersonalListView.setAdapter (mAdapter);
        mPersonalListView.setOnItemClickListener (this);
    }

    @Override
    public void onItemClick (AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void setONPersonalListViewClickLIstener (PersonalListViewClickListener listener) {
        mPersonalListViewClickLIstener = listener;
    }

    @Override
    public void signOutSuccess () {
        SPUtils.putString (NixonApplication.getContext (), Constant.COOKIE_KEY, "");
        Intent intent1 = new Intent ();
        intent1.setAction (Constant.FINISH_MAIN_KEY);
        getActivity ().sendBroadcast (intent1);
        Intent intent = new Intent (getActivity (), LoginAndRegActivity.class);
        startActivity (intent);
        getActivity ().finish ();
    }

    @Override
    public void singOutFailed () {

    }

    @Override
    public void editClick (int position) {
        if (null != mPersonalListViewClickLIstener) {
            mPersonalListViewClickLIstener.go2EditInfoFragment (mData.get (position));
        }
    }

    @OnClick ({R.id.personal_frag_back_arrow, R.id.personal_sign_out})
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.personal_frag_back_arrow:
                if (null != mPersonalListViewClickLIstener) {
                    mPersonalListViewClickLIstener.personalBack2Setting ();
                }
                break;
            case R.id.personal_sign_out:
                presenter.requestSignOut ();

                break;
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        UnitFragment.releaseChangeListener ();
    }

    @Override
    public void unitChanged () {
        Log.d ("unitChanged1", "  回调  ");
        initData ();
    }

    public interface PersonalListViewClickListener {
        void go2EditInfoFragment (PersonalInfoBean personalInfoBean);

        void personalBack2Setting ();
    }
}
