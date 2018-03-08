package com.example.bo.nixon.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.ChooseCityBean;
import com.example.bo.nixon.base.NixonApplication;

import java.util.List;
import java.util.Locale;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public class ChooseCityAdapter extends BaseAdapter implements SectionIndexer {
    private List<ChooseCityBean.ObjectBean> mList;
    private WindowManager mManager;
    private boolean mIsBlack;

    public ChooseCityAdapter (List<ChooseCityBean.ObjectBean> list, WindowManager manager,
                              boolean isBlack) {
        mList = list;
        mManager = manager;
        mIsBlack = isBlack;
    }

    @Override public int getCount () {
        return mList == null ? 0 : mList.size ();
    }

    @Override public Object getItem (int i) {
        return mList.get (i);
    }

    @Override public long getItemId (int i) {
        return i;
    }

    @Override public View getView (int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder ();
            if (mIsBlack){
                view = View.inflate (NixonApplication.getContext (), R.layout.item_choose_city, null);
            }else{
                view = View.inflate (NixonApplication.getContext (), R.layout.item_choose_city_white, null);
            }
            holder.utc = (TextView) view.findViewById (R.id.item_choose_zone);
            holder.abbre = (TextView) view.findViewById (R.id.item_choose_city_abbre);
            holder.cityRegion = (TextView) view.findViewById (R.id.item_choose_city_reg);
            holder.country = (TextView) view.findViewById (R.id.item_choose_city_country);
            view.setTag (holder);
        } else {
            holder = (ViewHolder) view.getTag ();
        }
        ChooseCityBean.ObjectBean bean = mList.get (i);
        //holder.cityRegion.init (mManager);
        //holder.country.init (mManager);
        holder.abbre.setText (bean.getShortName ());
//        if (!LanguageUtil.isChinese ()) {
//            holder.cityRegion.setText (bean.getCityCN ());
//            holder.country.setText (bean.getCountryCN ());
//        } else {
            holder.cityRegion.setText (bean.getCity ());
            holder.country.setText (bean.getCountry ());
//        }
        //holder.country.startScroll ();
        //holder.cityRegion.startScroll ();
        String[] split = bean.getTimezone ().split (":");
        String s = split[0].substring (1, 2);
        String utc;
        if (Integer.parseInt (s) == 0) {

            utc = split[0].substring (0, 1) + split[0].substring (2);
        } else {
            utc = split[0];
        }
        //if ("+".equals (split[0].substring (0, 1))) {
        //    utc = utc.substring (1);
        //}
        holder.utc.setText (utc);
        return view;
    }

    @Override public Object[] getSections () {
        return new Object[0];
    }

    @Override public int getPositionForSection (int section) {
        if (section != 42) {
            for (int i = 0; i < getCount (); i++) {
                String sortStr = mList.get (i).getCity ().substring (0);
                char firstChar = sortStr.toUpperCase (Locale.CHINESE).charAt (0);
                if (firstChar == section) {
                    return i;
                }
            }
        } else {
            return 0;
        }

        return -1;
    }

    @Override public int getSectionForPosition (int i) {
        return mList.get (i).getCity ().charAt (0);
    }

    public void updateListView (List<ChooseCityBean.ObjectBean> list) {
        if (list != null) {
            mList = list;
        }
        notifyDataSetChanged ();
    }

    static class ViewHolder {
        TextView abbre, utc;
        TextView cityRegion, country;
    }
}
