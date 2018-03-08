package com.example.bo.nixon.adapter;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.ui.fragment.SettingsFragment;
import com.example.bo.nixon.ui.view.SmartSwitchView;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.SmartManager;
import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/5.
 * @desc
 */

public class SettingsListViewAdapter extends BaseAdapter implements View.OnClickListener {

    List<PersonalInfoBean> mList;
    private boolean mSelected;
    Typeface tf =
        Typeface.createFromAsset (NixonApplication.getContext ().getAssets (), "fonts/Montserrat-Regular.ttf");

    public SettingsListViewAdapter (List<PersonalInfoBean> list) {
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

    @Override public View getView (int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {

            holder = new ViewHolder ();
            view = View.inflate (NixonApplication.getContext (), R.layout.item_fragment_settings, null);
            holder.settingsName = (TextView) view.findViewById (R.id.settings_frag_name);
            holder.mSwitch = (SmartSwitchView) view.findViewById (R.id.settings_frag_sw);

            view.setTag (holder);
        } else {
            holder = (ViewHolder) view.getTag ();
        }
        PersonalInfoBean bean = mList.get (i);
        holder.settingsName.setText (bean.getTextLeft ());
        if (!new SmartManager ().isDiscovery ()) {
            switch (bean.getTag ()) {
                case SettingsFragment.CALIBRATE_HANDS:
                case SettingsFragment.REMOTE_SHUTTER:
                case SettingsFragment.FAVORITE_CONTACTS:
                case SettingsFragment.DISCONNECT_ALERT:
//                case SettingsFragment.MY_PROFILE:
                    holder.settingsName.setTextColor (
                        NixonApplication.getContext ().getResources ().getColor (R.color.sup_text_color));
                    break;
            }
        } else {
            holder.settingsName.setTextColor (
                NixonApplication.getContext ().getResources ().getColor (R.color.main_text_color));
        }
        holder.settingsName.setTypeface (tf);
        mSelected = holder.mSwitch.isSelected ();
        if (bean.isShowSwitch ()) {
            holder.mSwitch.setVisibility (View.VISIBLE);
            holder.mSwitch.setOnClickListener (this);
            if (new SmartManager ().isDiscovery ()){
                boolean b = SPUtils.getBoolean (NixonApplication.getContext (), Constant.DISCONNECT_ALERT_KEY);
                holder.mSwitch.setCheck (b);
                if (null != mListener) {
                    mListener.isCheck (b);
                }
            }else{
                holder.mSwitch.setCheck (false);
                if (null != mListener) {
                    mListener.isCheck (false);
                }
            }
        }
        return view;
    }

    private boolean isChecked;

    @Override public void onClick (View view) {
        if (new SmartManager ().isDiscovery ()) {
            SmartSwitchView smartSwitchView = (SmartSwitchView) view;
            boolean b = SPUtils.getBoolean (NixonApplication.getContext (), Constant.DISCONNECT_ALERT_KEY);
            isChecked = !b;
            smartSwitchView.setCheck (isChecked);
            if (null != mListener) {
                mListener.isCheck (isChecked);
            }
            isChecked = !isChecked;
            SPUtils.putBoolean (NixonApplication.getContext (), Constant.DISCONNECT_ALERT_KEY, !isChecked);
        }
    }

    private SmartSwitchClickListener mListener;

    public void setSmartSwitchClickListener (SmartSwitchClickListener listener) {
        mListener = listener;
    }

    public interface SmartSwitchClickListener {
        void isCheck (boolean isChecked);
    }

    static class ViewHolder {
        TextView settingsName;
        SmartSwitchView mSwitch;
    }
}
