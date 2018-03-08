package com.example.bo.nixon.ui.fragment;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.EditPersonalPagerAdapter;
import com.example.bo.nixon.base.BaseFragment;
import com.example.bo.nixon.base.NixonApplication;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/14.
 * @desc
 */

public class HelpFragment extends BaseFragment implements ViewPager.OnPageChangeListener {
    @BindView (R.id.help_frag_arrow) ImageView mHelpFragArrow;
    @BindView (R.id.help_frag_toolbar) RelativeLayout mHelpFragToolbar;
    @BindView (R.id.help_view_pager) ViewPager mHelpViewPager;
    @BindView (R.id.help_dot_ll) LinearLayout mHelpDotLl;

    @Override public int getLayoutResId () {
        return R.layout.fragment_help;
    }

    @Override protected void initView () {
        super.initView ();
        List<View> mDate = new ArrayList<> ();
        View pager1 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager1, null);
        View pager2 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager2, null);
        View pager3 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager3, null);
        View pager4 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager4, null);
        View pager5 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager5, null);
        View pager6 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager6, null);
        View pager7 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager7, null);
        View pager8 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager8, null);
        View pager9 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager9, null);
        View pager10 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager10, null);
        View pager11 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager11, null);
        View pager12 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager12, null);
        View pager13 = View.inflate (NixonApplication.getContext (), R.layout.help_white_pager13, null);
        mDate.add (pager1);
        mDate.add (pager2);
        mDate.add (pager3);
        mDate.add (pager4);
        mDate.add (pager5);
        mDate.add (pager6);
        mDate.add (pager7);
        mDate.add (pager8);
        mDate.add (pager9);
        mDate.add (pager10);
        mDate.add (pager11);
        mDate.add (pager12);
        mDate.add (pager13);
        EditPersonalPagerAdapter adapter = new EditPersonalPagerAdapter (mDate);
        mHelpViewPager.setAdapter (adapter);
        mHelpViewPager.addOnPageChangeListener (this);
    }

    @OnClick ({R.id.help_frag_arrow,R.id.help_right}) public void onClick () {
        if (mListener != null) {
            mListener.helpBack2Setting ();
        }
    }

    private HelpFragBackClickListener mListener;

    public void setHelpFragBackClickListener (HelpFragBackClickListener listener) {
        mListener = listener;
    }

    @Override public void onPageScrolled (int i, float v, int i1) {

    }

    @Override public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mListener != null) {
                        mListener.helpBack2Setting ();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override public void onPageSelected (int i) {
        ImageView dot = (ImageView) mHelpDotLl.getChildAt (i);
        if (i > 0){
            ImageView dot1 = (ImageView) mHelpDotLl.getChildAt (i-1);
            dot1.setImageResource (R.drawable.peraonal_dot_nor);
        }
        if (i < mHelpDotLl.getChildCount () - 1){
            Log.e ("HELP"," i = " + i + " count = " +  mHelpDotLl.getChildCount ());
            ImageView dot2 = (ImageView) mHelpDotLl.getChildAt (i + 1);
            dot2.setImageResource (R.drawable.peraonal_dot_nor);
        }
        dot.setImageResource (R.drawable.peraonal_dot_sel);
    }

    @Override public void onPageScrollStateChanged (int i) {

    }

    public interface HelpFragBackClickListener {
        void helpBack2Setting ();
    }
}
