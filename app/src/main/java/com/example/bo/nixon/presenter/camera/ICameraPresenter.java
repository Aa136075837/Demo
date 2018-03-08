package com.example.bo.nixon.presenter.camera;

import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 20:18
 * @说明
 */
public interface ICameraPresenter {

    void startCamera(Activity activity, SurfaceView surfaceView);

    void startPreview(Activity activity, SurfaceView surfaceView);

    void setPreviewDisplay(SurfaceHolder surfaceHolder);

    void photo();

    void setFocus();

    void changeCamera();

    void destroyCamera();
}
