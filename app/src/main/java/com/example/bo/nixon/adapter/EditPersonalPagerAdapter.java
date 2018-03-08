package com.example.bo.nixon.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/1.
 * @desc
 */

public class EditPersonalPagerAdapter extends PagerAdapter {
    private List<View> mList;

    public EditPersonalPagerAdapter (List<View> list) {
        mList = list;
    }

    @Override public int getCount () {

        return mList == null ? 0 : mList.size ();
    }

    @Override public boolean isViewFromObject (View view, Object object) {

        return view == object;
    }

    @Override public void destroyItem (ViewGroup container, int position, Object object) {
        Log.w("EditUserTallView","destroyItem::" + position);
        ((ViewPager) container).removeView (mList.get (position));
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mList.get(position), 0);
        return mList.get(position);
    }
}
