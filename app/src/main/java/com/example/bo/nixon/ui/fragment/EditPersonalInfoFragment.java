package com.example.bo.nixon.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.EditPersonalPagerAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.presenter.EditUserInfoContract;
import com.example.bo.nixon.ui.fragment.personal.EditStepGoalView;
import com.example.bo.nixon.ui.fragment.personal.EditUserTallView;
import com.example.bo.nixon.ui.fragment.personal.EditUserWeightView;
import com.example.bo.nixon.ui.fragment.personal.EditWatchNameView;
import com.example.bo.nixon.ui.fragment.personal.TimeZoneView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.example.bo.nixon.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.example.bo.nixon.utils.Constant.TALL_KEY;

/**
 * @author bo.
 * @Date 2017/6/3.
 * @desc
 */

public class EditPersonalInfoFragment extends BaseMvpFragment<EditUserInfoContract.EditUserInfoPresenter>
    implements EditUserInfoContract.EditUserInfoView, ViewPager.OnPageChangeListener {
    private static final String PAGER_CURRENT = "PAGER_CURRENT";
    private static final String PAGER_TEMP_VALUE = "PAGER_TEMP_VALUE";
    @BindView (R.id.edit_personal_info_pager) ViewPager mEditPersonalInfoPager;
    @BindView (R.id.personal_dot1) ImageView mPersonalDot1;
    @BindView (R.id.personal_dot2) ImageView mPersonalDot2;
    @BindView (R.id.personal_dot3) ImageView mPersonalDot3;
    @BindView (R.id.personal_dot4) ImageView mPersonalDot4;
    @BindView (R.id.personal_dot5) ImageView mPersonalDot5;
    @BindView (R.id.personal_dot_ll) LinearLayout mPersonalDotLl;
    @BindView (R.id.frag_back_arrow) ImageView mFragBackArrow;
    EditFragmentBackListener mEditFragmentBackListener;
    private View mNamePager;
    private View mWeightPager;
    private View mTallager;
    private View mGoalPager;
    private View mZonePager;
    private EditWatchNameView mNameView;
    private EditUserWeightView mWeightView;
    private EditUserTallView mTallView;
    private EditStepGoalView mGoalView;
    private TimeZoneView mZoneView;
    private Handler mHandler = NixonApplication.getmainThreadHandler ();

    @Override public int getLayoutResId () {
        return R.layout.fragment_edit_personal_info;
    }

    @Override protected EditUserInfoContract.EditUserInfoPresenter createPresenter () {
        return new EditUserInfoContract.EditUserInfoPresenter ();
    }

    @Override protected void initView () {
        Bundle bundle = getArguments ();
        int tag = (int) bundle.getSerializable (PAGER_CURRENT);
        initToolbar ();
        initPagerView ();
        List<View> list = new ArrayList<> ();
        list.add (mNamePager);
        list.add (mWeightPager);
        list.add (mTallager);
        list.add (mGoalPager);
        list.add (mZonePager);
        EditPersonalPagerAdapter adapter = new EditPersonalPagerAdapter (list);
        mEditPersonalInfoPager.setAdapter (adapter);
        mEditPersonalInfoPager.setCurrentItem (tag);
        setSelectedDot (tag);
        mEditPersonalInfoPager.addOnPageChangeListener (this);
    }

    private void initPagerView () {
        mEditPersonalInfoPager.setOffscreenPageLimit (5);
        mNameView = new EditWatchNameView ();
        mNamePager = mNameView.getView ();
        mNameView.setContent (SPUtils.getString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY, "NAME"));

        mWeightView = new EditUserWeightView ();
        mWeightPager = mWeightView.getView ();
        String weight = SPUtils.getString (NixonApplication.getContext (), Constant.WEIGHT_KEY, 150 + "");
        String[] split = weight.split ("\\.");
        weight = split[0];
        int intWeight = Double.valueOf (weight) - 10 > 0 ? Integer.valueOf (weight) - 10 : 0;
        mWeightView.setProgress (intWeight);

        mTallView = new EditUserTallView ();
        mTallager = mTallView.getView ();
        String height = SPUtils.getString (NixonApplication.getContext (), TALL_KEY, 70 + "");
        String[] array = height.split ("\\.");
        int intHeight = Integer.valueOf (array[0]) > 0 ? Integer.valueOf (array[0]) : 0;
        Log.e ("EditUserTallView"," Tall ====== 2  >>" +(intHeight - 12) );
        mTallView.setProgress (intHeight - 12);

        mGoalView = new EditStepGoalView (getActivity ());
        mGoalPager = mGoalView.getView ();
        String target = SPUtils.getString (NixonApplication.getContext (), Constant.GOAL_KEY, 10000 + "");
        mGoalView.setProgress (Integer.valueOf (target) - 500);

        mZoneView = new TimeZoneView ();
        mZonePager = mZoneView.getView ();
        mZoneView.setAutoTime (SPUtils.getBoolean (NixonApplication.getContext (), Constant.IS_AUTO_TIME_KEY, false));
    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    mHandler.post (new Runnable () {
                        @Override public void run () {
                            presenter.upLoadUserInfo (initParms ());
                        }
                    });
                    saveInfo ();
                    back2PersonalFrag ();
                    return true;
                }
                return false;
            }
        });
    }


    private void initToolbar () {
        mFragBackArrow.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View view) {
                mHandler.post (new Runnable () {
                    @Override public void run () {
                        presenter.upLoadUserInfo (initParms ());
                    }
                });
                saveInfo ();
                back2PersonalFrag ();
            }
        });
    }

    private void saveInfo () {
        SPUtils.putString (NixonApplication.getContext (), Constant.WATCH_NAME_KEY, mNameView.getContent ());
        SPUtils.putString (NixonApplication.getContext (), Constant.WEIGHT_KEY, mWeightView.getProgress () + 10 + "");
        SPUtils.putString (NixonApplication.getContext (), Constant.FALSE_WEIGHT_KEY, mWeightView.getFalseMetricWeight ());
        SPUtils.putString (NixonApplication.getContext (), Constant.TALL_KEY, mTallView.getProgress () + 12 + "");
        SPUtils.putString (NixonApplication.getContext (), Constant.FALSE_TALL_KEY, mTallView.getFalseMetricTall ());
        SPUtils.putString (NixonApplication.getContext (), Constant.GOAL_KEY, mGoalView.getContent ());
        SPUtils.putBoolean (NixonApplication.getContext (), Constant.IS_AUTO_TIME_KEY, mZoneView.isAutoTime ());

        presenter.setTarget2Watch (Integer.parseInt (mGoalView.getContent ()));

        Log.d ("onProgressChanged","mGoalView.getContent ()  = " + mGoalView.getContent ());
    }

    private Map<String, String> initParms () {
        Map<String, String> parmas = new HashMap<> ();
        parmas.put ("customerId", SPUtils.getString (NixonApplication.getContext (), Constant.CUSTOMER_ID));
        parmas.put ("nickname", mNameView.getContent ());
        parmas.put ("weight", StringUtils.lbs2g (mWeightView.getProgress () + 10));
        parmas.put ("height", StringUtils.inch2cm (mTallView.getProgress () + 12));
        parmas.put ("activityGoal", mGoalView.getContent ());
        parmas.put ("unit", 0 + "");
        return parmas;
    }

    private void back2PersonalFrag () {
        mEditFragmentBackListener.editFragmentBack ();
    }

    @Override public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override public void onPageSelected (int position) {
        changeSelectedDot (position);
    }

    @Override public void onPageScrollStateChanged (int state) {

    }

    private void setSelectedDot (int position) {
        for (int i = 0; i < mPersonalDotLl.getChildCount (); i++) {
            ImageView dot = (ImageView) mPersonalDotLl.getChildAt (i);
            dot.setImageResource (R.drawable.peraonal_dot_nor);
            if (i == position) {
                dot.setImageResource (R.drawable.peraonal_dot_sel);
            }
        }
    }

    private void changeSelectedDot (int position) {
        ImageView dot = (ImageView) mPersonalDotLl.getChildAt (position);
        if (position > 0) {
            ImageView dot1 = (ImageView) mPersonalDotLl.getChildAt (position - 1);
            dot1.setImageResource (R.drawable.peraonal_dot_nor);
        }
        if (position < mPersonalDotLl.getChildCount () - 1) {
            ImageView dot2 = (ImageView) mPersonalDotLl.getChildAt (position + 1);
            dot2.setImageResource (R.drawable.peraonal_dot_nor);
        }
        dot.setImageResource (R.drawable.peraonal_dot_sel);
    }

    public static EditPersonalInfoFragment getInstance (PersonalInfoBean personalInfoBean) {
        Bundle bundle = new Bundle ();
        bundle.putSerializable (PAGER_CURRENT, personalInfoBean.getTag ());
        EditPersonalInfoFragment f = new EditPersonalInfoFragment ();
        f.setArguments (bundle);
        return f;
    }

    @Override public void onDestroy () {
        super.onDestroy ();
    }

    public void setEditFragmentBackListener (EditFragmentBackListener listener) {
        mEditFragmentBackListener = listener;
    }

    @Override public void upLoadSuccess () {
        //getActivity ().onBackPressed ();
        if (null != mHandler) {
            mHandler = null;
        }
    }

    @Override public void upLoadFail () {
        mHandler.postDelayed (new Runnable () {
            @Override public void run () {
                presenter.upLoadUserInfo (initParms ());
            }
        }, 3000);
    }

    public interface EditFragmentBackListener {
        void editFragmentBack ();
    }
}
