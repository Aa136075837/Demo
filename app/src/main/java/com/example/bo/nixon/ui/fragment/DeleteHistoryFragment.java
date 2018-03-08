package com.example.bo.nixon.ui.fragment;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.presenter.DeleteHistoryContract;
import com.example.bo.nixon.ui.view.SmartToast;

/**
 * @author bo.
 * @Date 2017/6/20.
 * @desc
 */

public class DeleteHistoryFragment extends BaseMvpFragment<DeleteHistoryContract.DeleteHistoryPresenter>
    implements DeleteHistoryContract.DeleteNixonView, CompoundButton.OnCheckedChangeListener {
    @BindView (R.id.device_detail_arrow) ImageView mDeviceDetailArrow;
    @BindView (R.id.delete_data_type) TextView mDeleteDataType;
    @BindView (R.id.delete_data_check) CheckBox mDeleteDataCheck;
    @BindView (R.id.delete_data_clear) TextView mDeleteDataClear;

    @Override protected DeleteHistoryContract.DeleteHistoryPresenter createPresenter () {
        return new DeleteHistoryContract.DeleteHistoryPresenter ();
    }

    @Override public int getLayoutResId () {
        return R.layout.fragment_delete_history;
    }

    @Override protected void initView () {
        super.initView ();
        mDeleteDataCheck.setOnCheckedChangeListener (this);
    }

    @OnClick ({ R.id.device_detail_arrow, R.id.delete_data_check, R.id.delete_data_clear })
    public void onClick (View view) {
        switch (view.getId ()) {
            case R.id.device_detail_arrow:
                if (mListener != null) {
                    mListener.backTo ();
                }
                break;
            case R.id.delete_data_check:
                break;
            case R.id.delete_data_clear:
                showDialog ();
                break;
        }
    }

    @Override public void onCheckedChanged (CompoundButton compoundButton, boolean b) {
        if (b) {
            mDeleteDataClear.setTextColor (getResources ().getColor (R.color.sup_color));
            mDeleteDataClear.setEnabled (true);
        }else{
            mDeleteDataClear.setTextColor (getResources ().getColor (R.color.sup_text_color));
            mDeleteDataClear.setEnabled (false);
        }
    }

    public void showDialog () {
        show2NewDialog (R.string.delete_history_tip, new View.OnClickListener () {
            @Override public void onClick (View view) {
                switch (view.getId ()) {
                    case R.id.dialog_confirm_tv:
                        presenter.deleteFile ();
                        hideDialog ();
                        break;
                    case R.id.dialog_cancel_tv:
                        hideDialog ();
                        break;
                }
            }
        });
    }

    private DeleteBackClickListener mListener;

    public void setDeleteBackClickListener (DeleteBackClickListener l) {
        mListener = l;
    }

    @Override public void showToast () {
        SmartToast smartToast =
            SmartToast.getInstanse (getActivity (), getResources ().getString (R.string.delete_toast));
        smartToast.show ();
    }

    public interface DeleteBackClickListener {
        void backTo ();
    }
}
