package com.example.bo.nixon.adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.ui.view.WaftView;
import com.smart.smartble.smartBle.BleDevice;

import java.util.List;

/**
 * @author bo.
 * @Date 2017/6/10.
 * @desc
 */

public class BleNearListViewAdapter extends BaseAdapter {
    private List<BleDevice> mList;

    public BleNearListViewAdapter (List<BleDevice> list) {
        mList = list;
    }

    public void setData (List<BleDevice> list) {
        if (null != list) {
            mList = list;
        }
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
            view = View.inflate (NixonApplication.getContext (), R.layout.item_ble_near, null);
            holder.bleName = (TextView) view.findViewById (R.id.item_ble_name);
            holder.waft = (WaftView) view.findViewById (R.id.item_ble_waft);
            holder.mac = (TextView) view.findViewById (R.id.item_ble_mac);
            view.setTag (holder);
        } else {
            holder = (ViewHolder) view.getTag ();
        }
        if (mList.size () - 1 >= i) {
            BleDevice bean = mList.get (i);
            String name = bean.getDevice ().getName ();
            holder.bleName.setText (TextUtils.isEmpty (name) ? "Ambassador" : name);
            holder.bleName.setTypeface (FontManager.mTf);
            holder.mac.setTypeface (FontManager.mTf);
            String macAddress = bean.getDevice ().getAddress ();
            StringBuffer buffer = new StringBuffer ();
            String[] split = macAddress.split (":");
            for (String s : split) {
                StringBuffer sb = new StringBuffer (s);
                sb.reverse ();
                buffer.append (sb).append (":");
            }
            holder.mac.setText ("MAC:" + buffer.reverse ().substring (1));
            int rssi = bean.getRssi ();
            if (rssi > -60) {
                holder.waft.setWaftStrength (5);
            } else if (rssi > -80) {
                holder.waft.setWaftStrength (4);
            } else if (rssi > -100) {
                holder.waft.setWaftStrength (3);
            } else if (rssi > -120) {
                holder.waft.setWaftStrength (2);
            } else if (rssi > -140) {
                holder.waft.setWaftStrength (1);
            }
        }
        return view;
    }

    public void clearData () {
        mList.clear ();
        notifyDataSetChanged ();
    }

    static class ViewHolder {
        TextView bleName;
        WaftView waft;
        TextView mac;
    }
}
