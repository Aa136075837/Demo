package com.example.bo.nixon.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.adapter.EditPersonalPagerAdapter;
import com.example.bo.nixon.base.BaseNoTitleActivity;
import com.example.smartcustomview.views.SmartNoScrollViewPager;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends BaseNoTitleActivity {

    @BindView (R.id.help_view_pager) SmartNoScrollViewPager mHelpViewPager;
    @BindView (R.id.help_next) TextView mHelpNext;
    @BindView (R.id.help_back) ImageView mHelpBack;
    @BindView (R.id.help_title) TextView mHelpTitle;

    @Override protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_help);
        ButterKnife.bind (this);
        initPager ();
    }

    private void initPager () {
        List<View> mDate = new ArrayList<> ();
        View pager1 = View.inflate (this, R.layout.help_pager1, null);
        View pager2 = View.inflate (this, R.layout.help_pager2, null);
        View pager3 = View.inflate (this, R.layout.help_pager3, null);
        View pager4 = View.inflate (this, R.layout.help_pager4, null);
        View pager5 = View.inflate (this, R.layout.help_pager5, null);
        View pager6 = View.inflate (this, R.layout.help_pager6, null);
        View pager7 = View.inflate (this, R.layout.help_pager7, null);
        View pager8 = View.inflate (this, R.layout.help_pager8, null);
        View pager9 = View.inflate (this, R.layout.help_pager9, null);
        View pager10 = View.inflate (this, R.layout.help_pager10, null);
        View pager11 = View.inflate (this, R.layout.help_pager11, null);
        View pager12 = View.inflate (this, R.layout.help_pager12, null);
        View pager13 = View.inflate (this, R.layout.help_pager13, null);
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
    }

    private void goToNextPager () {
        int currentItem = mHelpViewPager.getCurrentItem ();
        if (currentItem < 12) {
            mHelpViewPager.setCurrentItem (currentItem + 1, false);
        } else {
            Intent intent = new Intent (this, ConnectWatchHomeActivity.class);
            startActivity (intent);
            finish ();
        }
    }

    private void goToPrePager () {
        int currentItem = mHelpViewPager.getCurrentItem ();
        if (currentItem > 0) {
            mHelpViewPager.setCurrentItem (currentItem - 1, false);
        }else{
            Intent intent = new Intent (this, EditUserInfoActivity.class);
            startActivity (intent);
            finish ();
        }
    }

    @OnClick ({ R.id.help_back, R.id.help_next }) public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.help_back:
                goToPrePager();
                break;
            case R.id.help_next:
                goToNextPager ();
                break;
        }
    }
}
