package com.example.bo.nixon.ui.fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.presenter.DeviceDetailContract;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.SPUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author bo.
 * @Date 2017/6/20.
 * @desc
 */

public class DeviceDetailFragment extends BaseMvpFragment<DeviceDetailContract
        .DeviceDetailPresenter> {
    @BindView (R.id.device_detail_arrow)
    ImageView mDeviceDetailArrow;
    @BindView (R.id.device_detail_edit)
    EditText mDeviceDetailEdit;
    @BindView (R.id.device_detail_confirm)
    TextView mDeviceDetailConfirm;
    private boolean mIsShow;

    @Override
    protected DeviceDetailContract.DeviceDetailPresenter createPresenter () {
        return new DeviceDetailContract.DeviceDetailPresenter ();
    }

    @Override
    public int getLayoutResId () {
        return R.layout.fragment_device_details;
    }

    @Override
    protected void initView () {
        mDeviceDetailEdit.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (s.length () > 0) {
                    mDeviceDetailConfirm.setTextColor (getResources ().getColor (R.color
                            .sup_color));
                    mDeviceDetailConfirm.setEnabled (true);
                } else {
                    mDeviceDetailConfirm.setTextColor (getResources ().getColor (R.color
                            .sup_text_color));
                    mDeviceDetailConfirm.setEnabled (false);
                }
            }

            @Override
            public void afterTextChanged (Editable s) {

            }
        });
    }

    @Override
    public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (mListener != null) {
                        String name = mDeviceDetailEdit.getText ().toString ().trim ();
                        SPUtils.putString (NixonApplication.getContext (), Constant.BLE_NAME_KEY,
                                name);
                        mListener.deviceDetailBack (name);
                        presenter.changeBleName (name);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private DeviceDetailBackListener mListener;

    public void setDeviceDetailBackListener (DeviceDetailBackListener l) {
        mListener = l;
    }

    @OnClick ({R.id.device_detail_arrow, R.id.device_detail_confirm})
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.device_detail_arrow:
                if (mListener != null) {
                    mListener.deviceDetailBack (SPUtils.getString (NixonApplication.getContext ()
                            , Constant.BLE_NAME_KEY, "Ambassador"));
                }
                break;
            case R.id.device_detail_confirm:
                show1NewDialog (R.string.custom_ble_name_tip);
                break;
        }
    }

    @Override
    protected void afterHideErrorDialog (View view) {
        if (mListener != null) {
            String name = mDeviceDetailEdit.getText ().toString ().trim ();
            SPUtils.putString (NixonApplication.getContext (), Constant.BLE_NAME_KEY, name);
            mListener.deviceDetailBack (name);
            presenter.changeBleName (name);
        }
    }

    public interface DeviceDetailBackListener {
        void deviceDetailBack (String name);
    }


}
