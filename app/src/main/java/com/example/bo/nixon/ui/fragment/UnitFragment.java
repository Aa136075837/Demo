package com.example.bo.nixon.ui.fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.UnitAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.presenter.UnitContract;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/14.
 * @desc
 */

public class UnitFragment extends BaseMvpFragment<UnitContract.UnitPresenter> {
    @BindView (R.id.unit_frag_arrow) ImageView mUnitFragArrow;
    @BindView (R.id.unit_frag_toolbar) RelativeLayout mUnitFragToolbar;
    @BindView (R.id.unit_frag_list) ListView mUnitFragList;
    private final int TIME = 0;
    private final int DISTANCE = 0;
    private final int HEIGHT = 0;
    private final int WEIIGHT = 0;
    private PersonalInfoBean mTimeFormat;
    private PersonalInfoBean mDistance;
    private PersonalInfoBean mHeight;
    private PersonalInfoBean mWeight;

    @Override protected UnitContract.UnitPresenter createPresenter () {
        return new UnitContract.UnitPresenter ();
    }

    @Override public int getLayoutResId () {
        return R.layout.fragment_unit;
    }

    @Override protected void initView () {
        super.initView ();
        List<PersonalInfoBean> mData = new ArrayList<> ();
        mTimeFormat = new PersonalInfoBean (getResources ().getString (R.string.time_fom), "24", "12",
            SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_TIME), TIME);
        mDistance = new PersonalInfoBean (getResources ().getString (R.string.distance), "km", "mile",
            SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_DISTANCE), DISTANCE);
        mHeight = new PersonalInfoBean (getResources ().getString (R.string.height), "m", "ft",
            SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT), HEIGHT);
        mWeight = new PersonalInfoBean (getResources ().getString (R.string.weight), "kg", "lb",
            SPUtils.getBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT), WEIIGHT);
        mData.add (mTimeFormat);
        mData.add (mDistance);
        mData.add (mHeight);
        mData.add (mWeight);
        UnitAdapter adapter = new UnitAdapter (mData, false);
        mUnitFragList.setAdapter (adapter);
    }

    @OnClick ({ R.id.unit_frag_arrow, R.id.unit_right }) public void onClick () {
        if (null != mListener) {
            mListener.backSettings ();
        }
    }

    @Override public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (hidden){
                   /*保存单位制式  true ：24时，公制；false ： 12时，英制*/
            Log.e ("NIXONUNIT", "保存单位制式 ++++++ ");
            SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_TIME, mTimeFormat.isShowSwitch ());
            SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_DISTANCE, mDistance.isShowSwitch ());
            SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_HEIGHT, mHeight.isShowSwitch ());
            SPUtils.putBoolean (NixonApplication.getContext (), Constant.UNIT_WEIGHT, mWeight.isShowSwitch ());
            if (mChangeListener != null) {
                mChangeListener.unitChanged ();
            }
        }
    }

    @Override public void onDestroyView () {
        super.onDestroyView ();
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != mListener) {
                        mListener.backSettings ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private static UnitChangeListener mChangeListener;

    public static void setUnitChangeListener (UnitChangeListener listener) {
        mChangeListener = listener;
    }

    public static void releaseChangeListener () {
        if (mChangeListener != null) {
            mChangeListener = null;
        }
    }

    public interface UnitChangeListener {
        void unitChanged ();
    }

    private UnitFragBackListener mListener;

    public void setUnitFragBackListener (UnitFragBackListener listener) {
        mListener = listener;
    }

    public interface UnitFragBackListener {
        void backSettings ();
    }
}
