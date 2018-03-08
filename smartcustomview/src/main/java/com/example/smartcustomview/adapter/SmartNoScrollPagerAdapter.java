package com.example.smartcustomview.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public class SmartNoScrollPagerAdapter extends PagerAdapter {
    private List<View> mList;

    public SmartNoScrollPagerAdapter (List<View> list) {
        mList = list;
    }

    @Override public int getCount () {
        return mList == null ? 0 : mList.size ();
    }

    @Override public boolean isViewFromObject (View view, Object object) {
        return view == object;
    }

    @Override public void destroyItem (ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView (mList.get (position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mList.get(position), 0);
        return mList.get(position);
    }
}
