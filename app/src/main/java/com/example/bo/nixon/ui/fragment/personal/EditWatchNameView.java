package com.example.bo.nixon.ui.fragment.personal;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.NixonApplication;

/**
 * @author bo.
 * @Date 2017/6/15.
 * @desc
 */

public class EditWatchNameView {

    private final View mView;
    private EditText mWatchNameEt;

    public EditWatchNameView () {
        mView = View.inflate (NixonApplication.getContext (), R.layout.edit_watch_name, null);
        initView ();
    }

    private void initView () {
        mWatchNameEt = (EditText) mView.findViewById (R.id.frag_edit_name);
    }

    public String getContent () {
        if (mWatchNameEt == null) {
            return "NAME";
        }
        String s = mWatchNameEt.getText ().toString ().trim ();
        return TextUtils.isEmpty (s) ? "NAME" : s;
    }

    public void setContent(String hint) {
        if (null == mWatchNameEt)
            return;
        mWatchNameEt.setText(hint);
    }

    public View getView () {
        return mView;
    }
}
