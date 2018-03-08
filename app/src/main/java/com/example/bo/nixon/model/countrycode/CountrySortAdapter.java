package com.example.bo.nixon.model.countrycode;

/**
 * Created by admin on 2016/7/22.
 */

import android.content.Context;
import android.support.annotation.ColorRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.bean.ContactBean;
import com.example.bo.nixon.manager.FontManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 国家码选择
 * <p>
 * <p>
 * 类详细描述
 * </p>
 *
 * @author duanbokan
 */

public class CountrySortAdapter extends BaseAdapter implements SectionIndexer {

    private List<ContactBean.ObjectBean> mList;
    private List<ContactBean.ObjectBean> checkedBeanList = new ArrayList<> ();
    @ColorRes
    private int itemBgColor;

    public List<ContactBean.ObjectBean> getCheckedBeanList () {
        return checkedBeanList;
    }

    public void clearCheckList () {
        checkedBeanList.clear ();
    }

    private Context mContext;

    public boolean isShowRadio () {
        return isShowRadio;
    }

    public void setShowRadio (boolean showRadio) {
        isShowRadio = showRadio;
        notifyDataSetChanged ();
    }

    private boolean isShowRadio;

    LayoutInflater mInflater;

    public void setShowSelected (int showSelected) {
        isShowSelected = showSelected;
        notifyDataSetChanged ();
    }

    private int isShowSelected;

    private boolean mIsPopup;
    private List<ContactBean.ObjectBean> mFaviriteList;

    /***
     * 初始化
     *
     * @param mContext
     * @param list
     */
    public CountrySortAdapter (Context mContext, List<ContactBean.ObjectBean> list, boolean
            isShowRadio, @ColorRes int itemBgColor, boolean isPopup, List<ContactBean.ObjectBean>
            favoriteList) {
        this.mContext = mContext;
        this.isShowRadio = isShowRadio;
        this.itemBgColor = itemBgColor;
        if (list == null) {
            this.mList = new ArrayList<ContactBean.ObjectBean> ();
        } else {
            this.mList = list;
        }
        mIsPopup = isPopup;
        if (favoriteList != null) {
            mFaviriteList = favoriteList;
        }
    }

    public void setData (List<ContactBean.ObjectBean> list) {
        if (null == list)
            return;
        mList.clear ();
        mList.addAll (list);
        notifyDataSetChanged ();
    }

    @Override
    public int getCount () {
        return this.mList.size ();
    }

    @Override
    public Object getItem (int position) {
        return mList.get (position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View view, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final ContactBean.ObjectBean mContent = mList.get (position);

        if (view == null) {
            viewHolder = new ViewHolder ();
            view = LayoutInflater.from (mContext).inflate (R.layout.item_contact_rec, null);
            viewHolder.viewLL = (LinearLayout) view.findViewById (R.id.item_ll);
            viewHolder.contactName = (TextView) view.findViewById (R.id.item_contact_name);
            viewHolder.isChecked = (CheckBox) view.findViewById (R.id.item_contact_check);
            view.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag ();
        }
        viewHolder.viewLL.setBackgroundColor (mContext.getResources ().getColor (itemBgColor));
        if (isShowRadio) {
            viewHolder.isChecked.setVisibility (View.VISIBLE);
            viewHolder.contactName.setTextColor (mContext.getResources ().getColor (R.color
                    .sup_text_color));
        } else {
            viewHolder.isChecked.setVisibility (View.GONE);
            viewHolder.contactName.setTextColor (mContext.getResources ().getColor (R.color
                    .main_text_color));
        }
        mList.get (position).setChecked (false);
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.isChecked.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                if (finalViewHolder.isChecked.isChecked ()) {
                    finalViewHolder.contactName.setTextColor (mContext.getResources ().getColor
                            (R.color.main_text_color));
                    mList.get (position).setChecked (true);
                    checkedBeanList.add (mList.get (position));
                    if (mListener != null) {
                        mListener.checkClick (true);
                    }
                } else {
                    finalViewHolder.contactName.setTextColor (mContext.getResources ().getColor
                            (R.color.sup_text_color));
                    mList.get (position).setChecked (false);
                    if (mListener != null) {
                        mListener.checkClick (false);
                    }
                }
            }
        });
        viewHolder.isChecked.setChecked (mList.get (position).isChecked ());
        // 根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition (position);

        viewHolder.contactName.setText (mContent.getContactNameX ());
        viewHolder.contactName.setTypeface (FontManager.mTf);

        if (mIsPopup && mFaviriteList != null) {
            for (ContactBean.ObjectBean ob : mFaviriteList) {
                if (mList.get (position).getPhoneNumber ().equals (ob.getPhoneNumber ())) {
                    viewHolder.isChecked.setEnabled (false);
                }else{
                    viewHolder.isChecked.setEnabled (true);
                }
            }
        }

        return view;
    }

    @Override
    public int getPositionForSection (int section) {
        if (section != 42) {
            for (int i = 0; i < getCount (); i++) {
                String sortStr = mList.get (i).getContactNameX ();
                if (!TextUtils.isEmpty (sortStr)) {
                    char firstChar = sortStr.toUpperCase (Locale.ENGLISH).charAt (0);
                    if (firstChar == section) {
                        return i;
                    }
                } else {
                    return i;
                }
            }
        } else {
            return 0;
        }
        return -1;
    }

    @Override
    public int getSectionForPosition (int position) {
        if (TextUtils.isEmpty (mList.get (position).getContactNameX ())) {
            return 'z';
        }
        return mList.get (position).getContactNameX ().charAt (0);
    }

    @Override
    public Object[] getSections () {
        return null;
    }

    /**
     * 增加listView条目
     */
    public void updateListView (List<ContactBean.ObjectBean> list) {
        if (list != null) {
            mList.addAll (list);
            Log.e ("CONTACT", "  新增 item");
        }
        notifyDataSetChanged ();
    }

    /**
     * 删除LIstview条目
     */
    public void deleteItem (List<ContactBean.ObjectBean> list) {
        if (null == list) {
            return;
        }
        for (ContactBean.ObjectBean b : list) {
            mList.remove (b);
        }
        notifyDataSetChanged ();
    }

    private FavoriteCheckClickListener mListener;

    public void setFavoriteCheckClickListener (FavoriteCheckClickListener listener) {
        mListener = listener;
    }

    public interface FavoriteCheckClickListener {
        void checkClick (boolean b);
    }

    public static class ViewHolder {

        public TextView contactName;

        public CheckBox isChecked;

        LinearLayout viewLL;
    }
}
