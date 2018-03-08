package com.example.bo.nixon.ui.activity.camera;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.SensorManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.example.bo.nixon.R;
import com.example.bo.nixon.base.BaseMvpNotitleActivity;
import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.presenter.camera.CameraContract;
import com.example.bo.nixon.ui.view.CameraPopupWindow;
import com.example.bo.nixon.utils.AnimationUtil;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ImageUntil;
import com.example.bo.nixon.utils.SPUtils;
import com.smart.smartble.PermissionsUtils;
import java.io.File;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 19:09
 * @说明
 */
public class CameraActivity extends BaseMvpNotitleActivity<CameraContract.ConnectPresenter>
        implements SurfaceHolder.Callback, View.OnClickListener, CameraContract.CameraNixonView,
        MediaScannerConnection.MediaScannerConnectionClient, SensorHelper.OnFocus {

    private SensorHelper mSensorHelper;
    private static final int CAMERA_PERMISSION = 0x001;
    private MediaScannerConnection mConnection;
    private static final String REVIEW_ACTION = "com.cooliris.media.action.REVIEW";
    private AnimatorSet mAnimatorSet;

    private CameraPopupWindow mTimeDelayPopupWin;

    @BindView(R.id.camera_surfaceView)
    SurfaceView mSurfaceView;
    @BindView(R.id.review_thumbnail)
    ImageView mThumbImg;
    @BindView(R.id.camera_img)
    ImageView mFocusImage;
    @BindView(R.id.tv_camera_delay)
    TextView mCameraDelayTv;
    @BindView(R.id.tv_camera_delay_time)
    TextView mCameraDelayTime;
    @BindView(R.id.tv_time)
    TextView mTimeTv;
    @BindView(R.id.review_voice)
    ImageView mVoiceImg;
    @BindView(R.id.tv_camera_delay_rel)
    RelativeLayout mCameraDelayTvRel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        PermissionsUtils.requestPermissions(new String[]{
                Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, CAMERA_PERMISSION, this);
        presenter.startCamera(this, mSurfaceView);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.destroyCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSensorHelper.unListener();
        System.gc ();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (CAMERA_PERMISSION == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                hideDialog();
                recreate();
            } else {
                Log.w("onRequestPermissions","run onRequestPermissionsResult::");
                 showPermissionDialog();
            }
        }
    }

    private void init() {
        SurfaceHolder holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        mAnimatorSet = CameraAnimFactory.createAnimatorSet(mFocusImage);
        String path = ImageUntil.getHistoryImage();
        if (!TextUtils.isEmpty(path)) {
            mThumbImg.setImageURI(Uri.parse(path));
        }
        int time = SPUtils.getInt(this, Constant.TAKE_PHOTO_TIME_KEY);
        updateRes(time);
        changeVoice();
    }

    @Override
    protected CameraContract.ConnectPresenter createPresenter() {
        return new CameraContract.ConnectPresenter();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == holder.getSurface()) {
            return;
        }
        presenter.setPreviewDisplay(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (null == holder.getSurface()) {
            return;
        }
        presenter.startPreview(this, mSurfaceView);
        initSensor();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    private void initSensor() {
        SensorManager manager = (SensorManager) NixonApplication.getContext()
                .getSystemService(NixonApplication.getContext().SENSOR_SERVICE);
        mSensorHelper = new SensorHelper(manager);
        mSensorHelper.setFocusListener(this);
    }

    @OnClick({
            R.id.review_thumbnail, R.id.iv_camera_to_back, R.id.tv_camera_delay, R.id.review_voice_layout,
            R.id.camera_surfaceView, R.id.tv_back
    })
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.review_thumbnail:
                goIntoSystemPhotos();
                break;
            case R.id.iv_camera_to_back:
                presenter.changeCamera();
                mFocusImage.setVisibility(View.INVISIBLE);
                presenter.setPreviewDisplay(mSurfaceView.getHolder());
                presenter.startPreview(this, mSurfaceView);
                break;
            case R.id.tv_camera_delay:
                if (mTimeDelayPopupWin == null) {
                    mTimeDelayPopupWin = new CameraPopupWindow(CameraActivity.this, delayListener);
                }
                //if (presenter.isFontCamera()) {
                //    return;
                //}
                //ObjectAnimator objectAnimator =
                //        AnimationUtil.transByOff(mCameraDelayTvRel, DisplayUtil.getWindowWidth(this) * 3 / 4, 500);
                //objectAnimator.start();
                mTimeDelayPopupWin.showPopupWindow(mCameraDelayTv);
                break;
            case R.id.review_voice_layout:
                boolean open = SPUtils.getBoolean(this, Constant.CAMERA_VOICE_KEY, true);
                SPUtils.putBoolean(this, Constant.CAMERA_VOICE_KEY, !open);
                changeVoice();
                break;
            case R.id.camera_surfaceView:
                startFocus();
                break;
            case R.id.tv_back:
                finish();
                overridePendingTransition(R.anim.activity_out_bit, R.anim.activity_out_bit);
                break;
        }
    }

    private void changeVoice() {
        boolean open = SPUtils.getBoolean(this, Constant.CAMERA_VOICE_KEY, true);
        if (open) {
            mVoiceImg.setImageResource(R.drawable.icon_sounds);
        } else {
            mVoiceImg.setImageResource(R.drawable.icon_silence);
        }
    }

    public View.OnClickListener delayListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = (int) v.getTag();
            if (mTimeDelayPopupWin.isShowing()) mTimeDelayPopupWin.dismiss();
            ObjectAnimator objectAnimator1 = AnimationUtil.transByOff(mCameraDelayTvRel, 0.0f, 400);
            objectAnimator1.start();
            int time = 0;
            if (index == 4) {
                time = 10;
            } else if (index == 3) {
                time = 5;
            } else if (index == 2) {
                time = 3;
            } else if (index == 1) {
                time = 0;
            }
            SPUtils.putInt(NixonApplication.getContext(), Constant.TAKE_PHOTO_TIME_KEY, time);
            updateRes(time);
        }
    };

    private void updateRes(int time) {
        if (time == 0) {
            mCameraDelayTime.setText("");
        } else {
            mCameraDelayTime.setText(time + "s");
        }
    }

    private void startFocus() {
        mFocusImage.setVisibility(View.VISIBLE);
        mAnimatorSet.start();
        if (null != presenter) presenter.setFocus();
    }

    private void goIntoSystemPhotos() {
        String path = ImageUntil.getHistoryImage();
        if (null == path) {
            return;
        }
        if (!isFileExist(path)) {
            return;
        }
        if (null != mConnection) {
            mConnection.disconnect();
        }
        mConnection = new MediaScannerConnection(this, this);
        mConnection.connect();
    }

    private boolean isFileExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    @Override
    public void onMediaScannerConnected() {
        String path = ImageUntil.getHistoryImage();
        if (null == path) {
            return;
        }
        mConnection.scanFile(path, null);
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {
        Intent intent = new Intent(REVIEW_ACTION, uri);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            try {
                intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
            }
        } finally {
            mConnection.disconnect();
            mConnection = null;
        }
    }

    @Override
    public void takePhotonComplete() {
        presenter.startPreview(CameraActivity.this, mSurfaceView);
        mSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mSurfaceView.getHolder().addCallback(CameraActivity.this);
    }

    @Override
    public void dealPhotoComplete(Bitmap bitmap) {
        mThumbImg.setImageBitmap(bitmap);
    }

    @Override
    public void focusComplete() {
        mFocusImage.setVisibility(View.INVISIBLE);
        mFocusImage.setScaleY(1f);
        mFocusImage.setScaleX(1f);
    }

    @Override
    public void startTakePhotoAnim(int time) {
        mTimeTv.setVisibility(View.VISIBLE);
        animDemo(mTimeTv, time);
    }

    @Override
    public void showPermissionDialog() {

        show1NewMidDialog(R.string.camera_no_permission_tip, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //hideDialog();
                finish();
                overridePendingTransition(R.anim.activity_out_bit, R.anim.activity_out_bit);
            }
        });
    }

    private void animDemo(final TextView tv, final int values) {
        tv.setText(values + "");
        Animation scaleAnimation =
                new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
        scaleAnimation.setDuration(1000);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            int value = values;

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                value--;
                if (value == 0) {
                    presenter.photo();
                }
                tv.setText(value + "");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                value--;
                tv.setText(value + "");
            }
        });

        Animation alphaAnim = new AlphaAnimation(1.0f, 0f);
        alphaAnim.setRepeatCount(values - 1);
        alphaAnim.setDuration(1000);
        alphaAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                tv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                tv.setAlpha(1.0f);
            }
        });
        scaleAnimation.setRepeatCount(values - 1);
        AnimationSet set = new AnimationSet(true);
        set.addAnimation(alphaAnim);
        set.addAnimation(scaleAnimation);
        tv.startAnimation(set);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            overridePendingTransition(R.anim.activity_out_bit, R.anim.activity_out_bit);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void focus() {
        startFocus();
    }
}
