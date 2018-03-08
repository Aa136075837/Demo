package com.example.bo.nixon.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.PersonalInfoBean;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.fragment.DeviceInfoFragment;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.DeviceMessage;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/20.
 * @desc
 */

public class DeviceInfoAdapter extends BaseAdapter {
    private List<PersonalInfoBean> mList;

    public DeviceInfoAdapter (List<PersonalInfoBean> list) {
        mList = list;
    }

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
    public View getView (int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder ();
            view = View.inflate (NixonApplication.getContext (), R.layout
                    .item_fragment_device_info, null);
            holder.left = (TextView) view.findViewById (R.id.device_text_left);
            holder.right = (TextView) view.findViewById (R.id.device_text_right);
            holder.img = (ImageView) view.findViewById (R.id.device_frag_img);
            view.setTag (holder);
        } else {
            holder = (ViewHolder) view.getTag ();
        }
        PersonalInfoBean bean = mList.get (i);
        holder.left.setText (bean.getTextLeft ());
        holder.right.setText (bean.getTextRight ());
        holder.left.setTypeface (FontManager.mTf);
        holder.right.setTypeface (FontManager.mTf);
        int battery = SPUtils.getInt (NixonApplication.getContext (), Constant.ELECTRICITY_KEY);
        if (bean.getTag () == DeviceInfoFragment.BATTERY) {
            if (battery != 0) {
                holder.right.setText (battery + "%");
            }
            if (battery <= 15) {
                holder.img.setImageResource (R.drawable.icon_battery_0);
            } else if (battery <= 50) {
                holder.img.setImageResource (R.drawable.icon_battery_50);
            } else {
                holder.img.setImageResource (R.drawable.icon_battery_100);
            }
        }
        if (bean.getTag () == DeviceInfoFragment.DEVICE_DETAIL) {
            String string = SPUtils.getString (NixonApplication.getContext (), Constant
                    .BLE_NAME_KEY);
            holder.right.setText (string);
            holder.img.setVisibility (View.GONE);
        }
        if (bean.getTag () == DeviceInfoFragment.SN_CODE) {
            holder.img.setVisibility (View.GONE);
            String deviceSn = DeviceMessage.getInstance ().getDeviceSn ();
            if (deviceSn.length () >= 6){
                String snCode = deviceSn.substring (deviceSn.length () - 6);
                holder.right.setText (snCode);
            }else{
                holder.right.setText (deviceSn);
            }
        }
        return view;
    }

    static class ViewHolder {
        TextView left, right;
        ImageView img;
    }
}
