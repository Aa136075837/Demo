package com.example.bo.nixon.presenter.camera;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.bo.nixon.base.NixonApplication;
import com.example.bo.nixon.presenter.bleBase.BaseBlePresenter;
import com.example.bo.nixon.ui.view.CameraOperate;
import com.example.bo.nixon.utils.CameraSizeUntil;
import com.example.bo.nixon.utils.Constant;
import com.example.bo.nixon.utils.ImageUntil;
import com.example.bo.nixon.utils.SPUtils;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.smart.cameracomponent.CameraComponent;
import com.smart.cameracomponent.ICamera;
import com.smart.smartble.SmartManager;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 19:13
 * @说明
 */
public interface CameraContract {

    interface CameraNixonView extends MvpView {

        void takePhotonComplete ();

        void dealPhotoComplete (Bitmap bitmap);

        void focusComplete ();

        void startTakePhotoAnim (int time);

        void showPermissionDialog ();
    }

    class ConnectPresenter extends BaseBlePresenter<CameraNixonView> implements ICamera,
            ICameraPresenter {

        private CameraComponent mCameraComponent;
        private CameraOperate mCameraOperate;
        private boolean isTakePhoto = false;
        private Handler mHandler = new Handler (Looper.getMainLooper ());

        @Override
        protected void serviceConnected (SmartManager smartManager) {
            mCameraComponent = new CameraComponent (smartManager);
            mCameraComponent.registerComponent ();
            mCameraComponent.addCameraListener (this);
            mCameraComponent.intoCamera ();
        }

        @Override
        protected void serviceDisconnected (ComponentName name) {

        }

        @Override
        public void takePhone () {
            if (!isTakePhoto) {
                isTakePhoto = true;
                int time = SPUtils.getInt (NixonApplication.getContext (), Constant
                        .TAKE_PHOTO_TIME_KEY);
                if (0 == time) {
                    photo ();
                } else {
                    //  if (mCameraOperate.isFrontCamera()) {  //前置摄像头才会有倒计时数字和动画
                    getView ().startTakePhotoAnim (time);
                    //  } else {
                    //      mHandler.postDelayed(new Runnable() {
                    //          @Override
                    //          public void run() {
                    //              photo();
                    //          }
                    //      }, time * 1000);
                    //  }
                }
            }
        }

        @Override
        public void detachView () {
            super.detachView ();
            if (null != mCameraComponent) {
                mCameraComponent.outCamera ();
                mCameraComponent.removeCameraListener (this);
                mCameraComponent.unRegisterComponent ();
            }
        }

        public boolean isFontCamera () {
            if (null != mCameraOperate && mCameraOperate.isFrontCamera ()) {
                return true;
            }
            return false;
        }

        @Override
        public void startCamera (Activity activity, SurfaceView surfaceView) {
            try {
                mCameraOperate = CameraOperate.getInstance ();
                mCameraOperate.openCamera ();
                mCameraOperate.setPreviewDisplay (surfaceView.getHolder ());
                mCameraOperate.startPreview (activity, surfaceView);
            } catch (RuntimeException r) {
                Log.w ("onRequestPermissions", "run onRequestPermissionsResult::2");
                getView ().showPermissionDialog ();
            }
        }

        @Override
        public void startPreview (Activity activity, SurfaceView surfaceView) {
            if (mCameraOperate != null) {
                mCameraOperate.startPreview (activity, surfaceView);
            }
        }

        @Override
        public void setPreviewDisplay (SurfaceHolder surfaceHolder) {
            if (mCameraOperate != null) {
                mCameraOperate.setPreviewDisplay (surfaceHolder);
            }
        }

        @Override
        public void photo () {
            if (getView () instanceof Context) {
                Context context = (Context) getView ();
                MyShutterCallback callback;
                if (SPUtils.getBoolean (NixonApplication.getContext (), Constant
                        .CAMERA_VOICE_KEY, true)) {
                    callback = new MyShutterCallback ();
                } else {
                    callback = null;
                }
                mCameraOperate.takePhoto (context, callback, new RawPictureCallback (), new
                        JpegPictureCallback ());
            }
        }

        @Override
        public void setFocus () {
            mCameraOperate.autoFocus (new Camera.AutoFocusCallback () {
                @Override
                public void onAutoFocus (boolean b, Camera camera) {
                    if (null != getView ())
                        getView ().focusComplete ();
                }
            });
        }

        @Override
        public void changeCamera () {
            mCameraOperate.changeCamera ();
        }

        @Override
        public void destroyCamera () {
            mCameraOperate.stopPreview ();
            mCameraOperate.stopCamera ();
            mCameraOperate.cleanCameraOption ();
        }

        class JpegPictureCallback implements Camera.PictureCallback {

            @Override
            public void onPictureTaken (final byte[] data, final Camera camera) {
                if (null != getView ())
                    getView ().takePhotonComplete ();
                dealPhoto (data);
                Camera.Size size = CameraSizeUntil.getMaxPictureSize (mCameraOperate.getTakeSizes
                        ());
                float degree = mCameraOperate.getPictureOrientation ();
                Bitmap bitmap = ImageUntil.getThumbFromPath (data, size, 100, 200, degree);
                if (null != getView ())
                    getView ().dealPhotoComplete (bitmap);
            }
        }

        private void dealPhoto (final byte[] data) {
            final Thread th = new Thread (new Runnable () {
                @Override
                public void run () {
                    //Context context = null;
                    //if (getView() instanceof Activity)
                    //    context = (Context) getView();
                    //if (getView() instanceof Fragment)
                    //    context = ((Fragment) getView()).getActivity();
                    //if (null == context) {
                    //    isTakePhoto = false;
                    //    return;
                    //}
                    ImageUntil.saveImge (NixonApplication.getContext (), data, System
                            .currentTimeMillis () + ".jpeg");
                    isTakePhoto = false;
                }
            });
            th.start ();
        }
    }

    class MyShutterCallback implements Camera.ShutterCallback {

        @Override
        public void onShutter () {

        }
    }

    class RawPictureCallback implements Camera.PictureCallback {

        @Override
        public void onPictureTaken (byte[] data, Camera camera) {

        }
    }

}
