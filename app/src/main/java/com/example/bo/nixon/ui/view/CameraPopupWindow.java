package com.example.bo.nixon.ui.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.bo.nixon.R;
import com.example.smartcustomview.utils.DisplayUtil;

/**
 * Created by ninedau_zheng on 2017/3/9.
 *
 */

public class CameraPopupWindow extends PopupWindow {
    public ViewGroup vg;

    public CameraPopupWindow (Context context, View.OnClickListener listener) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        vg = (ViewGroup) inflater.inflate (R.layout.popup_camera, null);
        setListener (listener);
        this.setContentView (vg);
        this.setHeight (DisplayUtil.dip2px (context, 75));
        this.setWidth (DisplayUtil.getWindowWidth ((Activity) context));
        this.setFocusable (true);
        this.setOutsideTouchable (true);
        this.update ();
        this.setAnimationStyle (R.style.camera_popWindow_animation);
        ColorDrawable dw = new ColorDrawable (0000000000);
        this.setBackgroundDrawable (dw);
    }

    public void setListener (View.OnClickListener listener) {
        for (int i = 1; i < vg.getChildCount (); i ++) {
            vg.getChildAt (i).setTag (i);
            vg.getChildAt (i).setOnClickListener (listener);
        }
    }

    public void showPopupWindow (View parent) {
        if (!this.isShowing ()) {
            int[] location = new int[2];
            parent.getLocationOnScreen(location);
            //this.showAsDropDown(parent, parent.getLayoutParams().width / 2 - this.getWidth() / 2, DisplayUtil.dip2px(parent.getContext(), 30));
            //this.showAtLocation (parent, Gravity.RIGHT, 0/*DisplayUtil.getWindowWidth ((Activity) parent.getContext ())*/, DisplayUtil.getWindowHeight ((Activity)parent.getContext ()));
            this.showAtLocation(parent, Gravity.NO_GRAVITY, location[0] - this.getWidth(), location[1]);
        } else {
            this.dismiss ();
        }
    }
}
