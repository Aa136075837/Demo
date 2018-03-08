package com.smart.cameracomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 18:56
 * @说明
 */
public class CameraComponent extends AbCameraComponent {

    private Timer mTimer;

    public CameraComponent(SmartManager smartManager) {
        super(smartManager);
    }

    @Override
    public void start() {

    }

    @Override
    public void dealAction(SmartAction action) {
        switch (action.getAction()) {
            case REQUEST_ACTION_TAKE_PHOTON:
                dispatchTakePhoto();
                break;
        }
    }

    public void intoCamera() {
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CAMERA, true);
        if (null != mTimer) {
            mTimer.cancel();
        }
        mTimer = new Timer();
        mTimer.schedule(new TimeHeart(this), 0, 10 * 1000);
    }

    public void outCamera() {
        if (null != mTimer) {
            mTimer.cancel();
        }
        mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_CAMERA, false);
    }

    private static class TimeHeart extends TimerTask {

        public WeakReference<CameraComponent> mTarget;

        public TimeHeart(CameraComponent component) {
            mTarget = new WeakReference<>(component);
        }

        @Override
        public void run() {
            if (null != mTarget.get()) {
                mTarget.get().mSmartManager.getIClient().doAction(Action.REQUEST_ACTION_HEART_CAMERA);
            }
        }
    }



}
