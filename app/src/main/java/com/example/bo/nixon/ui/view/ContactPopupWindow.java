package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.bo.nixon.R;
import com.example.bo.nixon.manager.FontManager;
import com.example.bo.nixon.model.countrycode.CountrySortAdapter;
import com.example.smartcustomview.utils.DisplayUtil;

public class ContactPopupWindow extends PopupWindow {
    public ViewGroup vg;
    private final TextView mCancel;
    private final TextView mConfirm;

    public ContactPopupWindow (Context context, View.OnClickListener listener, final CountrySortAdapter adapter) {
        WindowManager manager = (WindowManager) context.getSystemService (Context.WINDOW_SERVICE);
        int width = manager.getDefaultDisplay ().getWidth ();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        vg = (ViewGroup) inflater.inflate (R.layout.activity_contact, null);
        final ListView listView = (ListView) vg.findViewById (R.id.contact_listview);
        SideBar sideBar = (SideBar) vg.findViewById (R.id.sidebar);
        mCancel = (TextView) vg.findViewById (R.id.contact_popu_cancel);
        mConfirm = (TextView) vg.findViewById (R.id.contact_popu_ok);
        mCancel.setTypeface (FontManager.mTf);
        mConfirm.setTypeface (FontManager.mTf);
        listView.setAdapter (adapter);
        setListener (listener);
        this.setContentView (vg);
        this.setHeight (DisplayUtil.dip2px (context, 500));
        this.setWidth (width);
        this.setFocusable (true);
        this.setAnimationStyle (R.style.popWindow_animation);
        this.setOutsideTouchable (true);
        this.update ();
        //ColorDrawable dw = new ColorDrawable(0000000000);
        //this.setBackgroundDrawable(dw);
        // 右侧sideBar监听
        sideBar.setOnTouchingLetterChangedListener (new SideBar.OnTouchingLetterChangedListener () {

            @Override public void onTouchingLetterChanged (String s) {
                // 该字母首次出现的位置
                int position = adapter.getPositionForSection (s.charAt (0));
                if (position != -1) {
                    listView.setSelection (position);
                }
            }
        });
    }

    public void setListener (View.OnClickListener listener) {
        mCancel.setOnClickListener (listener);
        mConfirm.setOnClickListener (listener);
    }
}