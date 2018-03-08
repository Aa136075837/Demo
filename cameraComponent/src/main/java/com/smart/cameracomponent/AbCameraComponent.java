package com.smart.cameracomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 18:53
 * @说明
 */
public abstract class AbCameraComponent extends AbComponent {


    public AbCameraComponent(SmartManager smartManager) {
        super(smartManager);
    }

    @Override
    public void registerComponent() {
        mSmartManager.getIService().registerComponent(this);
    }

    @Override
    public void unRegisterComponent() {
        mSmartManager.getIService().unRegisterComponent(this);
    }

    public void addCameraListener(ICamera iCamera) {
        if (!ListenerInfo.iCameras.contains(iCamera))
            ListenerInfo.iCameras.add(iCamera);
    }

    public void removeCameraListener(ICamera iCamera) {
        if (ListenerInfo.iCameras.contains(iCamera))
            ListenerInfo.iCameras.remove(iCamera);
    }

    public void dispatchTakePhoto() {
        for (ICamera iCamera : ListenerInfo.iCameras) {
            iCamera.takePhone();
        }
    }

    private static class ListenerInfo {
        private static List<ICamera> iCameras = new ArrayList<>();
    }


}
