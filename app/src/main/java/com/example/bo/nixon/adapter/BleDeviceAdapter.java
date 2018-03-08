package com.example.bo.nixon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.ui.view.WaftView;
import com.smart.smartble.smartBle.BleDevice;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/7 16:50
 * @说明
 */
public class BleDeviceAdapter extends RecyclerView.Adapter implements View.OnClickListener {

    private Context mContext;
    private List<BleDevice> mDevices = new ArrayList<> ();
    private OnItemClickListener mListener;

    public BleDeviceAdapter (Context mContext, List<BleDevice> mDevices) {
        this.mContext = mContext;
        this.mDevices = mDevices;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (mContext).inflate (R.layout.device_item, parent, false);
        return new BleViewHolder (view);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        BleViewHolder viewHolder = (BleViewHolder) holder;
        BleDevice device = mDevices.get (position);
        String macAddress = device.getDevice ().getAddress ();
        StringBuffer buffer = new StringBuffer ();
        String[] split = macAddress.split (":");
        for (String s : split) {
            StringBuffer sb = new StringBuffer (s);
            sb.reverse ();
            buffer.append (sb).append (":");
        }
        viewHolder.mMac.setText ("MAC:" + buffer.reverse ().substring (1));
        if (null != device) {
            String name = device.getDevice ().getName ();
            viewHolder.tv.setText (TextUtils.isEmpty (name) ? "Ambassador" : name);
            int rssi = device.getRssi ();
            if (rssi > -60) {
                viewHolder.img.setWaftStrength (5);
            } else if (rssi > -80) {
                viewHolder.img.setWaftStrength (4);
            } else if (rssi > -100) {
                viewHolder.img.setWaftStrength (3);
            } else if (rssi > -120) {
                viewHolder.img.setWaftStrength (2);
            } else if (rssi > -140) {
                viewHolder.img.setWaftStrength (1);
            }
            viewHolder.parent.setTag (position);
            viewHolder.parent.setOnClickListener (this);
        }
    }

    public void clearData () {
        mDevices.clear ();
    }

    @Override
    public int getItemCount () {
        return mDevices.size ();
    }

    @Override
    public void onClick (View view) {
        if (null != mListener) {
            int position = (int) view.getTag ();
            mListener.onItem (position);
        }
    }

    private class BleViewHolder extends RecyclerView.ViewHolder {

        private View rootView;
        private View parent;
        private TextView tv;
        private WaftView img;
        private TextView mMac;

        public BleViewHolder (View itemView) {
            super (itemView);
            rootView = itemView;
            parent = rootView.findViewById (R.id.item_message_layout);
            tv = (TextView) rootView.findViewById (R.id.item_message_tv);
            img = (WaftView) rootView.findViewById (R.id.item_message_img);
            mMac = (TextView) rootView.findViewById (R.id.item_message_mac);
        }
    }

    public interface OnItemClickListener {
        void onItem (int position);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mListener = listener;
    }
}
