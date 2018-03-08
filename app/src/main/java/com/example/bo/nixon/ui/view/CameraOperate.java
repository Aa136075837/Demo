package com.example.bo.nixon.ui.view;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.example.bo.nixon.utils.CameraSizeUntil;
import java.io.IOException;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2016年4月23日 下午5:57:12
 * @说明 相机操作类，由于单列模式，需要在界面退出时执行cleanCameraOption（）
 * 方法，把记录的相机状态变为原始状态。
 */
public class CameraOperate {

    private static CameraOperate mCameraOperate;
    private Camera mCamera;
    private int mStatus = 0;
    private int mCameraId = -1;
    private int mSurfaceViewOrientation = 0;
    private int mPictureOrientation = 0;

    public static synchronized CameraOperate getInstance() {
        if (null == mCameraOperate) {
            mCameraOperate = new CameraOperate();
        }
        return mCameraOperate;
    }

    private CameraOperate () {

    }

    /**
     * 绑定相机实例
     */
    public void openCamera() {
        if (mCameraId <= -1) {
            int numberOfCameras = Camera.getNumberOfCameras();
            CameraInfo cameraInfo = new CameraInfo();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    mCamera = Camera.open(i);
                    mCameraId = i;
                }
            }
        } else {
            mCamera = Camera.open(mCameraId);
        }
    }

    /**
     * 绑定与当前摄像头相反的相机实例
     *
     * @param
     */

    public void changeCamera() {
        int cameraCount = 0;
        int id = -1;
        CameraInfo cameraInfo = new CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int cameraId = 0; cameraId < cameraCount; cameraId++) {
            Camera.getCameraInfo(cameraId, cameraInfo);
            if (mStatus == 0) {
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    stopPreview();
                    stopCamera();
                    try {
                        mCamera = Camera.open(cameraId);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        return;
                    }
                    mCameraId = cameraId;
                    mStatus = 1;
                    break;
                }
            } else {
                if (cameraInfo.facing == CameraInfo.CAMERA_FACING_BACK) {
                    stopPreview();
                    stopCamera();
                    mCamera = Camera.open(cameraId);
                    mCameraId = cameraId;
                    mStatus = 0;
                    break;
                }
            }
        }
    }

    /**
     * 获取当前相机的
     * @return
     */
    public boolean isFrontCamera(){
        return mStatus == 1;
    }

    /**
     * 获取相机实例
     *
     * @return
     */

    public Camera getCameraInstance() {
        return mCamera;
    }

    /**
     * 释放Camera实例
     */
    public void stopCamera() {
        if (null == mCamera)
            return;
        try {
            mCamera.release();
            mCamera = null;
        } catch (Exception e) {
        }
    }


    /**
     * 设置相机属性（前后摄像头，闪光，聚焦，相片和预览尺寸）
     *
     * @param parmars
     */
    public void setParameters(Parameters parmars) {
        if (null == mCamera)
            return;
        try {
            mCamera.setParameters(parmars);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 绑定预览surfaceHolder
     *
     * @param holder
     * @throws IOException
     */
    public void setPreviewDisplay(SurfaceHolder holder) {
        if (null == mCamera)
            return;
        try {
            mCamera.setPreviewDisplay(holder);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 适配预览相片与surfaceView 的大小
     *
     * @param surfaceView
     */

    private void setupPreviewSize(SurfaceView surfaceView) {
        Parameters parameters = mCamera.getParameters();
        List<Size> sizes = parameters.getSupportedPreviewSizes();
        int width = surfaceView.getWidth();
        int height = surfaceView.getHeight();
        Size size = CameraSizeUntil.getOptimalPreviewSize(sizes, height, width);
        parameters.setPreviewSize(size.width, size.height);
        if (parameters.isZoomSupported()) {
            parameters.setZoom(0);
        }
        setParameters(parameters);
    }

    /**
     * 设置拍照相片尺寸
     *
     * @param context
     */
    private void setUpTakePictureSize(Context context) {
        Parameters parameters = mCamera.getParameters();
        List<Size> sizes = parameters.getSupportedPictureSizes();
        Size size = CameraSizeUntil.getOptimalTakePictrueSize(sizes);
        parameters.setPictureSize(size.width, size.height);
        mPictureOrientation = getTakePicRotation(context);
        parameters.setRotation(mPictureOrientation);
        setParameters(parameters);
    }

    /**
     * 开始预览照片
     */
    public void startPreview(Activity activity, SurfaceView surfaceView) {
        if (null == mCamera)
            return;
        setCameraDisplayOrientation(activity);
        setupPreviewSize(surfaceView);
        try {
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止预览照片
     */
    public void stopPreview() {
        if (null == mCamera)
            return;
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
        }
    }

    /**
     * 获取相机相片
     *
     * @param shutter
     * @param raw
     * @param jpeg
     */
    public void takePhoto(Context context, ShutterCallback shutter, PictureCallback raw, PictureCallback jpeg) {
        if (null == mCamera)
            return;
        setUpTakePictureSize(context);
        try {
            mCamera.takePicture(shutter, raw, jpeg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动对焦
     *
     * @param cb
     */
    public void autoFocus(AutoFocusCallback cb) {
        if (null == mCamera)
            return;
        try {
            mCamera.autoFocus(cb);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 增加CameraZoom
     *
     * @param value
     */
    public void addCameraZoom(int value) {
        Parameters parameters = mCamera.getParameters();
        if (!parameters.isZoomSupported())
            return;
        int maxzoom = parameters.getMaxZoom();
        int zoom = parameters.getZoom();
        if (zoom >= maxzoom)
            return;
        zoom += value;
        if (zoom > maxzoom)
            zoom = maxzoom;
        parameters.setZoom(zoom);
        setParameters(parameters);
    }

    /**
     * 相机反向
     */
    public void rebackCamera() {
        mCamera.startFaceDetection();
    }

    /**
     * 获取界面的角度
     *
     * @param activity
     */
    public int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    /**
     * 设置相机的照片的选择角度
     *
     * @param activity
     */
    public void setCameraDisplayOrientation(Activity activity) {

        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        int degrees = getDisplayRotation(activity);
        int result;
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    /**
     * 判断拍照时相机的角度(不能适配全部机型)
     *
     * @param context
     * @return
     */

    private int getTakePicRotation(Context context) {
        if (null == mCameraOperate)
            return 0;
        CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(mCameraId, info);
        int degree = mSurfaceViewOrientation;
        //if ((degree >= 0 && degree <=45)  || degree  >= 315 ) {
        //	return info.orientation;
        //}
        int plusOrMinus = 1;
        if (1 == mStatus)
            plusOrMinus = -1;
        if (degree <= 315 && degree >= 225) {
            return Math.abs(info.orientation + plusOrMinus * 270) % 360;
        } else if (degree >= 45 && degree < 135) {
            return Math.abs(info.orientation + plusOrMinus * 90) % 360;
        } else if (degree >= 135 && degree < 225) {
            return Math.abs(info.orientation + plusOrMinus * 180) % 360;
        }
        return Math.abs(info.orientation) % 360;
    }

    /**
     * 设置手机拍照时手机的旋转角度
     *
     * @param orientation
     */
    public void setSurfaceViewOrientation(int orientation) {

        if (null != mCameraOperate) {
            mSurfaceViewOrientation = orientation;
        }
    }

    /**
     * 清除相机状态设置
     */
    public void cleanCameraOption() {
        mCameraId = -1;
        mStatus = 0;
    }

    public List<Size> getTakeSizes() {
        if (null != mCamera)
            return mCamera.getParameters().getSupportedPictureSizes();
        return null;
    }

    public float getPictureOrientation() {
        return mPictureOrientation;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setCameraVoice(boolean camera) {
        try {
            mCamera.enableShutterSound(camera);
        } catch (Exception e) {

        }
    }

    public boolean canDisableSound() {
      /*  try {
            CameraInfo cameraInfo = new CameraInfo();
            mCamera.getCameraInfo(mCameraId, cameraInfo);
            return cameraInfo.canDisableShutterSound;
        } catch (Exception e) {

        }
        return false; */
        return true;
    }

}
