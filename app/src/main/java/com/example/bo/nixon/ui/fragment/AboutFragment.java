package com.example.bo.nixon.ui.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpFragment;
import com.example.bo.nixon.presenter.ota.OtaContract;
import com.example.bo.nixon.ui.view.SmartTextView;
import com.example.bo.nixon.utils.NetUtil;
import com.example.bo.nixon.utils.StringUtils;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.smart.smartble.PermissionsUtils;
import com.smart.smartble.SmartManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 20:02
 * @说明
 */
public class AboutFragment extends BaseMvpFragment<OtaContract.OtaPresenter> implements View
        .OnClickListener, OtaContract.OtaNixonView {

    private static final String TAG = "AboutFragment";
    private static final int REQUEST_PERMISSION_CODE = 0x001;
    @BindView (R.id.about_app_version_name_tv)
    TextView mAboutAppVersionNameTv;
    @BindView (R.id.app_new_version_red)
    View mAppNewVersionRed;
    @BindView (R.id.fw_new_version_red)
    View mFwNewVersionRed;
    private ProgressBar mProgressBar;
    @BindView (R.id.about_firmware_version_name_tv)
    TextView mFwVersionTv;
    @BindView (R.id.about_firmware_layout)
    View mFwVersionView;
    String mUrl;
    private SmartTextView mProgressTv;

    private String mVersion;

    @Override
    public void onResume () {
        super.onResume ();
        getView ().setFocusableInTouchMode (true);
        getView ().requestFocus ();
        getView ().setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                if (event.getAction () == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (null != mListener) {
                        mListener.aboutBack2Setting ();
                    }
                    return true;
                }
                return false;
            }
        });
        mAboutAppVersionNameTv.setText (StringUtils.getVersion ());
        if (!new SmartManager ().isDiscovery ()) {
            mFwVersionView.setVisibility (View.GONE);
        } else {
            mFwVersionView.setVisibility (View.VISIBLE);
        }
        PermissionsUtils.requestPermissions (new String[]{Manifest.permission
                .READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE, getActivity ());
        updateVersion ();
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[]
            grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (REQUEST_PERMISSION_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {

            }
        }
    }

    @Override
    public int getLayoutResId () {
        return R.layout.fragment_about;
    }

    @Override
    protected OtaContract.OtaPresenter createPresenter () {
        return new OtaContract.OtaPresenter ();
    }

    @OnClick ({R.id.about_app_layout, R.id.about_firmware_layout, R.id.about_frag_back_arrow})
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.about_app_layout:
                updateVersion ();
                break;
            case R.id.about_firmware_layout:
                if (!TextUtils.isEmpty (mVersion))
                    presenter.checkoutVersion (mVersion);
                break;
            case R.id.dialog_confirm_tv:
                hideDialog ();
                break;
            case R.id.dialog_cancel_tv:
                hideDialog ();
                show1NewLongDialog (R.layout.one_btn_long_dialog_new_got, R.string.ota_tip, R.id
                        .dialog_title, R.string.give_us_minute);
                break;
            case R.id.about_frag_back_arrow:
                if (null != mListener) {
                    mListener.aboutBack2Setting ();
                }
                break;
            case R.id.version_cancel:
                hideDialog ();
                break;
            case R.id.version_confirm:
                hideDialog ();
                break;
        }
    }

    private void updateVersion () {
        if (NetUtil.getNetworkState (getActivity ()) != NetUtil.NETWORN_NONE) {
            PgyUpdateManager.register (getActivity (), "com.example.bo.nixon.fileprovider", new
                    UpdateManagerListener () {
                @Override
                public void onNoUpdateAvailable () {
                    if (mAppNewVersionRed != null){
                        mAppNewVersionRed.setVisibility (View.GONE);
                    }
                }

                @Override
                public void onUpdateAvailable (String s) {
                    if (mAppNewVersionRed != null){
                        mAppNewVersionRed.setVisibility (View.VISIBLE);
                    }
                    final AppBean appBean = getAppBeanFromString (s);
                    show2NewDialog (R.string.new_version_tip, new View.OnClickListener () {
                        @Override
                        public void onClick (View view) {
                            switch (view.getId ()) {
                                case R.id.dialog_confirm_tv:  //do it later
                                    hideDialog ();
                                    break;
                                case R.id.dialog_cancel_tv:  //update now
                                    UpdateManagerListener.startDownloadTask (getActivity (),
                                            appBean.getDownloadURL ());
                                    mAppNewVersionRed.setVisibility (View.GONE);
                                    break;
                            }
                        }
                    }, R.string.ota_update_title, R.string.ota_update_confirm, R.string
                            .ota_update_cancel);
                }
            });
        }
    }

    @Override
    protected void afterHideErrorDialog (View view) {
        super.afterHideErrorDialog (view);
        Log.w ("OtaPresenter", "afterHideErrorDialog::");
        presenter.downLoad (mUrl);
    }

    @Override
    public void onBegin () {
        showProgress ();
    }

    private void showProgress () {
        showNoneBtnDialog (R.layout.dialog_ota_progress, R.string.ota_progress_title, R.id
                .ota_title_tv);
        mProgressBar = (ProgressBar) findViewByDialog (R.id.ota_progress);
        mProgressTv = (SmartTextView) findViewByDialog (R.id.ota_progress_tv);
    }

    @Override
    public void onProgress (int max, int progress) {
//        if (!isShowing ()) {
//            showProgress ();
//        }
        if (null != mProgressBar) {
            mProgressBar.setProgress (max);
            mProgressBar.setProgress (progress);
            mProgressTv.setText (progress + "%");
        }
    }

    @Override
    public void onComplete (boolean complete) {
        hideDialog ();
        mFwNewVersionRed.setVisibility (View.GONE);
    }

    @Override
    public void onFail () {

    }

    @Override
    public void findFwVersion (String url, String message) {
        Log.w (TAG, "findFwVersion:::" + url);
        mUrl = url;
        mFwNewVersionRed.setVisibility (View.VISIBLE);
        if (!isShowing ()) {
            show2NewDialog (R.string.ota_new_version_tip, this, R.string.ota_update_title, R
                    .string.ota_update_confirm, R.string.ota_update_cancel);
        }
    }

    @Override
    public void noVersion () {

    }

    @Override
    public void showFwVersion (int main, int minor, int test) {
        StringBuffer buffer = new StringBuffer ();
        buffer.append ("V").append (main + ".").append (minor + ".").append (test);
        //presenter.checkoutVersion(buffer.toString());
        mVersion = buffer.toString ();
        mFwVersionTv.setText (buffer.toString ());
    }

    private AboutBackClickListener mListener;

    public void setAboutBackClickListener (AboutBackClickListener listener) {
        mListener = listener;
    }

    public interface AboutBackClickListener {
        void aboutBack2Setting ();
    }

}
