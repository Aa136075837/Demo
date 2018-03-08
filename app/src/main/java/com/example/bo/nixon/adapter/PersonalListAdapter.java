package com.example.bo.nixon.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.view.SmartSwitchView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/2.
 * @desc
 */

public class PersonalListAdapter extends BaseAdapter {
    private List<PersonalInfoBean> mList;

    public void setData (List<PersonalInfoBean> list) {
        mList = list;
        notifyDataSetChanged ();
    }

    public PersonalListAdapter (List<PersonalInfoBean> list) {
        mList = list;
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

    @Override public View getView (final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (null == view) {
            holder = new ViewHolder ();
            view = View.inflate (NixonApplication.getContext (), R.layout.item_fragment_personal, null);
            holder.personalKey = (TextView) view.findViewById (R.id.personal_key);
            holder.personalValue = (TextView) view.findViewById (R.id.personal_value);
            holder.personalEdit = (TextView) view.findViewById (R.id.personal_edit);
            holder.personalLL = (LinearLayout) view.findViewById (R.id.personal_ll);
            holder.mSwitch = (SmartSwitchView) view.findViewById (R.id.personal_switch);
            view.setTag (holder);
        } else {
            holder = (ViewHolder) view.getTag ();
        }
        holder.personalKey.setTypeface (FontManager.mTf);
        holder.personalValue.setTypeface (FontManager.mTf);
        holder.personalEdit.setTypeface (FontManager.mTf);
        if (mList.get (i).isShowSwitch ()) {
            holder.personalKey.setText (mList.get (i).getTextLeft ());
            holder.mSwitch.setVisibility (View.VISIBLE);
            holder.mSwitch.setCheck (
                SPUtils.getBoolean (NixonApplication.getContext (), Constant.IS_AUTO_TIME_KEY, false));
            holder.personalLL.setVisibility (View.GONE);
        } else {
            holder.personalKey.setText (mList.get (i).getTextLeft ());
            holder.personalValue.setText (mList.get (i).getTextRight ());
        }
        holder.personalEdit.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View view) {
                if (mListener != null) {
                    mListener.editClick (i);
                }
            }
        });

        holder.mSwitch.setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View view) {
                //boolean selected = holder.mSwitch.isSelected ();
                //holder.mSwitch.setCheck (!selected);
                if (mListener != null) {
                    mListener.editClick (i);
                }
            }
        });
        return view;
    }

    static class ViewHolder {
        TextView personalKey, personalValue, personalEdit;
        SmartSwitchView mSwitch;
        LinearLayout personalLL;
    }

    private EditClickListener mListener;

    public void setEditClickListener (EditClickListener listener) {
        mListener = listener;
    }

    public interface EditClickListener {
        void editClick (int position);
    }
}
