package com.example.bo.nixon.manager;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.bo.nixon.base.NixonApplication;

/**
 * @author bo.
 * @Date 2017/6/8.
 * @desc
 */

public class FontManager {
    public static Typeface mTf =
        Typeface.createFromAsset (NixonApplication.getContext ().getAssets (), "fonts/Montserrat-Regular.ttf");

    public static void changeFonts (ViewGroup root) {

        for (int i = 0; i < root.getChildCount (); i++) {
            View v = root.getChildAt (i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface (mTf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface (mTf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface (mTf);
            } else if (v instanceof ViewGroup) {
                changeFonts ((ViewGroup) v);
            }
        }
    }

}
