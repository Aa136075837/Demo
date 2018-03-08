package com.example.bo.nixon.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import com.example.bo.nixon.manager.FontManager;

/**
 * @author bo.
 * @Date 2017/7/8.
 * @desc
 */

public class SmartTextView extends AppCompatTextView {
    public SmartTextView (Context context) {
        super (context);
        init();
    }

    public SmartTextView (Context context, @Nullable AttributeSet attrs) {
        super (context, attrs);
        init();
    }

    public SmartTextView (Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super (context, attrs, defStyleAttr);
        init();
    }

    private void init () {
        setTypeface (FontManager.mTf);
    }
}
