package com.example.bo.nixon.ui.fragment.timeZone;

import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.EditPersonalPagerAdapter;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.presenter.TimeZoneContract;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.utils.TimeZoneUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static java.lang.Integer.parseInt;

/**
 * @author bo.
 * @Date 2017/8/14.
 * @desc
 */

public class TimeZoneFragment extends BaseMvpFragment<TimeZoneContract.TimeZonePresenter>
        implements CustomLocationView.NextPagerListener, SelectSecondView.DstChangedListener {
    @BindView (R.id.time_zone_frag_arrow)
    ImageView mTimeZoneFragArrow;
    @BindView (R.id.time_zone_right)
    TextView mTimeZoneRight;
    @BindView (R.id.time_zone_viewpager)
    ViewPager mTimeZoneViewpager;
    private SelectSecondView mSelectSecondView;
    private CustomLocationView mCustomLocationView;

    @Override
    protected TimeZoneContract.TimeZonePresenter createPresenter () {
        return new TimeZoneContract.TimeZonePresenter ();
    }

    @Override
    public int getLayoutResId () {
        return R.layout.fragment_time_zone;
    }

    @Override
    protected void initView () {
        super.initView ();
        initPagerAdapter ();
    }

    private void initPagerAdapter () {
        mCustomLocationView = new CustomLocationView (NixonApplication.getContext ());
        mSelectSecondView = new SelectSecondView (getActivity ());
        mCustomLocationView.setNextPagerListener (this);
        mSelectSecondView.setDstChangedListener (this);

        List<View> data = new ArrayList<> ();
        data.add (mCustomLocationView.getView ());
        data.add (mSelectSecondView.getView ());
        EditPersonalPagerAdapter adapter = new EditPersonalPagerAdapter (data);
        mTimeZoneViewpager.setAdapter (adapter);
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged (hidden);
        if (!hidden) {
            mTimeZoneViewpager.setCurrentItem (0);
        } else {
            dstChanged (true);
            mCustomLocationView.saveInformation ();
            mSelectSecondView.saveDstSettings ();
        }
    }

    @OnClick ({R.id.time_zone_frag_arrow, R.id.time_zone_right})
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.time_zone_frag_arrow:
            case R.id.time_zone_right:
                if (mListener != null) {
                    mListener.TimeZoneBack2Settings ();
                }
                break;
        }
    }

    /**
     * 适配夏令时时间
     *
     * @param date
     * @return
     */
    private Date adaptationDst (Date date) {
        String dst = SPUtils.getString (NixonApplication.getContext (), Constant.DST_KEY, "0.0");
        if (dst.length () != 0) {
            String[] split = dst.split ("\\.");
            int dstHour = date.getHours () + parseInt (split[0]);
            int dstMin = date.getMinutes () + (parseInt (split[1]) == 5 ? 30 : 0);
            date.setHours (dstHour);
            date.setMinutes (dstMin);
        }
        return date;
    }

    public void setChooseCityContent (String content, ChooseCityBean.ObjectBean objectBean) {
        if (!TextUtils.isEmpty (content) && mSelectSecondView != null) {
            mSelectSecondView.setResultContent (content);
            Date date = TimeZoneUtil.getTimeFromThereZone (objectBean.getTimezone ());
            int min = TimeZoneUtil.getMinFromThereZone (objectBean.getTimezone ());
            float value = (float) (min % 60) / 60;
            BigDecimal bigDecimal = new BigDecimal (value);
            value = bigDecimal.setScale (2, BigDecimal.ROUND_HALF_UP).floatValue ();
            Date dstDate = adaptationDst (date);
            presenter.sendSecondTime (dstDate, min / 60, (int) (value * 100));
        }
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
                        mListener.TimeZoneBack2Settings ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private TimeZoneBackListener mListener;

    public void setTimeZoneBackListener (TimeZoneBackListener l) {
        mListener = l;
    }

    @Override
    public void setCurrentPager () {
        mTimeZoneViewpager.setCurrentItem (1);
    }

    @Override
    public void dstChanged (boolean isChecked) {
        String secondTimeZone = SPUtils.getString (NixonApplication.getContext (), Constant
                .SECOND_ZONE_KEY);
        Date date = TimeZoneUtil.getTimeFromThereZone (secondTimeZone);
        int min = TimeZoneUtil.getMinFromThereZone (secondTimeZone);
        min = adaptationDstMin (min);
        float value = (float) (min % 60) / 60;
        BigDecimal bigDecimal = new BigDecimal (value);
        value = bigDecimal.setScale (2, BigDecimal.ROUND_HALF_UP).floatValue ();
        Date dstDate = adaptationDst (date);
        presenter.sendSecondTime (dstDate, min / 60, (int) (value * 100));
    }

    private int adaptationDstMin (int min) {
        String dst = SPUtils.getString (NixonApplication.getContext (), Constant.DST_KEY, "0.0");
        int dstTime = min;
        if (dst.length () != 0) {
            String[] split = dst.split ("\\.");
            double dstHour = Double.parseDouble (split[0]) + (Double.parseDouble (split[1]) * 0.1);
            dstTime = (int) (min + (dstHour * 60));
        }
        return dstTime;
    }

    public interface TimeZoneBackListener {
        void TimeZoneBack2Settings ();
    }
}
