package com.example.bo.nixon.adapter;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.bean.AlarmEventBean;
import com.example.bo.nixon.ui.view.AlarmEventItemView;
import java.util.List;

public class AlarmListViewAdapter extends BaseAdapter
    implements AlarmEventItemView.OnDeleteListener, View.OnClickListener {
    private List<AlarmEventBean> list;
    private AlarmEventItemView.AlarmEventCallBack mCallBack;
    private OnItemClickListener mOnItemClickListener;
    private boolean mClickType;

    public AlarmListViewAdapter (List<AlarmEventBean> lis, AlarmEventItemView.AlarmEventCallBack callBack) {
        this.list = lis;
        mCallBack = callBack;
    }

    @Override public int getCount () {
        return list == null ? 0 : list.size ();
    }

    @Override public Object getItem (int i) {
        return list.get (i);
    }

    @Override public long getItemId (int position) {
        return position;
    }

    /**
     * 闹钟Item 获取view
     */
    @Override public View getView (final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (null == convertView) {
            //View view = LayoutInflater.from (NixonApplication.getContext ()).inflate (R.layout.item_alarm, null);
            convertView = new AlarmEventItemView (NixonApplication.getContext ());//闹钟 item ==AlarmEventItemView
            viewHolder = new ViewHolder ();
            viewHolder.alarmView = (AlarmEventItemView) convertView;
            convertView.setTag (viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag ();
        }
        viewHolder.alarmView.setCheckboxState (false);
        viewHolder.alarmView.setOnDeleteListener (this);
        viewHolder.alarmView.setOnClickListener (this);
        viewHolder.position = position;
        AlarmEventBean alarmBean = list.get (position);
        alarmBean.setIndex (position);
        viewHolder.alarmView.setAlarmBean (alarmBean);
        viewHolder.alarmView.setCallBack (mCallBack);
        viewHolder.alarmView.setEditState (mIsShowCheck);
        viewHolder.alarmView.setSlide (mIsSlide);
        viewHolder.alarmView.setClickType (mClickType);
        if (alarmBean.isOpenType () || viewHolder.alarmView.getCheckState ()) {
            viewHolder.alarmView.setTextColor (R.color.main_text_color);
        } else {
            viewHolder.alarmView.setTextColor (R.color.sup_text_color);
        }
        viewHolder.alarmView.getDeleteBtn ().setOnClickListener (new View.OnClickListener () {
            @Override public void onClick (View view) {
                if (null != mOnItemClickListener) {
                    mOnItemClickListener.slideDelete (position);
                }
                list.remove (list.get (position));
                Log.e ("DELETE", "DELETE dianji");
                viewHolder.alarmView.turnNoamal ();
                AlarmListViewAdapter.this.notifyDataSetChanged ();
            }
        });
        return convertView;
    }

    public void setNotifly (List<AlarmEventBean> lis) {
        this.list = lis;
        this.notifyDataSetChanged ();
    }

    private boolean mIsShowCheck;

    public void setEditState (boolean isShowCheck) {
        mIsShowCheck = isShowCheck;
        notifyDataSetChanged ();
    }
    public void turnToNormal(){

    }

    private boolean mIsSlide;
    public void setSlide(boolean isSlide){
        mIsSlide = isSlide;
    }

    public void setClickType(boolean clickType){
        mClickType = clickType;
    }

    @Override public void isDelete (int index, boolean b) {
        if (index < list.size ()) {
            list.get (index).setSelect (b);
            if(mOnItemClickListener != null){
                mOnItemClickListener.checkBoxClick (b);
            }
        }
    }

    @Override public void deleteItem () {
    }

    @Override public void click (AlarmEventItemView view) {
        ViewHolder holder = (ViewHolder) view.getTag ();
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItem (list.get (holder.position), holder.position);
        }
    }

    @Override public void onClick (View view) {
        ViewHolder holder = (ViewHolder) view.getTag ();
        if (null != mOnItemClickListener) {
            mOnItemClickListener.onItem (list.get (holder.position), holder.position);
        }
    }

    public interface OnItemClickListener {
        void onItem (AlarmEventBean alarmEventBean, int position);

        void slideDelete (int position);

        void checkBoxClick(boolean b);
    }

    public void setOnItemClickListener (OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    private static class ViewHolder {
        AlarmEventItemView alarmView;
        int position = 0;
    }
}
