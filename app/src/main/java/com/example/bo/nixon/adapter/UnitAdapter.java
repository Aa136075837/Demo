package com.example.bo.nixon.adapter;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.manager.FontManager;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/16.
 * @desc
 */

public class UnitAdapter extends BaseAdapter {
    @LayoutRes
    private int layoutId;
    private boolean mIsBlack;

    public UnitAdapter (List<PersonalInfoBean> list, boolean isBlack) {
        mList = list;
        mIsBlack = isBlack;
        if (isBlack) {
            layoutId = R.layout.item_fragment_unit_black;
        } else {
            layoutId = R.layout.item_fragment_unit;
        }
    }

    private List<PersonalInfoBean> mList;

    @Override
    public int getCount () {
        return mList == null ? 0 : mList.size ();
    }

    @Override
    public Object getItem (int i) {
        return mList.get (i);
    }

    @Override
    public long getItemId (int i) {
        return i;
    }

    @Override
    public View getView (final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (null == view) {
            viewHolder = new ViewHolder ();
            view = View.inflate (NixonApplication.getContext (), layoutId, null);
            viewHolder.unitName = (TextView) view.findViewById (R.id.unit_frag_name);
            viewHolder.leftText = (TextView) view.findViewById (R.id.unit_bigswitch_left);
            viewHolder.rightText = (TextView) view.findViewById (R.id.unit_bigswitch_right);
            viewHolder.mLinearLayout = (LinearLayout) view.findViewById (R.id.unit_bigswitch_ll);
            view.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag ();
        }
        final PersonalInfoBean bean = mList.get (i);
        viewHolder.unitName.setText (bean.getUnitName ());
        viewHolder.leftText.setText (bean.getTextLeft ());
        viewHolder.rightText.setText (bean.getTextRight ());
        viewHolder.unitName.setTypeface (FontManager.mTf);
        viewHolder.leftText.setTypeface (FontManager.mTf);
        viewHolder.rightText.setTypeface (FontManager.mTf);
        changeChecked (viewHolder, bean.isShowSwitch ());
        viewHolder.mLinearLayout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                changeChecked (viewHolder, !bean.isShowSwitch ());
                bean.setShowSwitch (!bean.isShowSwitch ());
                if (mListener != null) {
                    mListener.switchChanged (!bean.isShowSwitch (), i);
                }
                notifyDataSetChanged ();
            }
        });

        return view;
    }

    private void changeChecked (ViewHolder viewHolder, boolean isMetric) {
        if (isMetric) {//左边被选择  公制 24时
            if (mIsBlack) {
                viewHolder.leftText.setBackgroundResource (R.drawable.switch_btn_sel);
            } else {
                viewHolder.leftText.setBackgroundResource (R.drawable.switch_btn_nor);
            }
            viewHolder.leftText.setTextColor (NixonApplication.getContext ().getResources ()
                    .getColor (R.color.white));
            viewHolder.rightText.setBackgroundResource (R.drawable.wheel_bg);
            viewHolder.rightText.setTextColor (NixonApplication.getContext ().getResources ()
                    .getColor (R.color.sup_text_color));
        } else {
            if (mIsBlack) {
                viewHolder.rightText.setBackgroundResource (R.drawable.switch_btn_sel);
            } else {
                viewHolder.rightText.setBackgroundResource (R.drawable.switch_btn_nor);
            }
            viewHolder.rightText.setTextColor (NixonApplication.getContext ().getResources ()
                    .getColor (R.color.white));
            viewHolder.leftText.setBackgroundResource (R.drawable.wheel_bg);
            viewHolder.leftText.setTextColor (NixonApplication.getContext ().getResources ()
                    .getColor (R.color.sup_text_color));
        }
    }

    private SwitchChangedListener mListener;

    public void setSwitchChangedListener (SwitchChangedListener listener) {
        mListener = listener;
    }

    public interface SwitchChangedListener {
        void switchChanged (boolean b, int position);
    }

    static class ViewHolder {
        TextView unitName, leftText, rightText;
        LinearLayout mLinearLayout;
    }
}
