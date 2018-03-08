package com.smart.dataComponent;

import com.smart.dataComponent.listener.IRequestData;
import com.smart.dataComponent.listener.ITarget;
import com.smart.dataComponent.listener.IUpdateSumStep;
import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/17 18:00
 * @说明
 */
public abstract class AbDataComponent extends AbComponent {

    public AbDataComponent(SmartManager smartManager) {
        super(smartManager);
    }

    public void addSumStepListener(IUpdateSumStep iUpdateSumStep) {
          if (!IListener.iUpdateSumSteps.contains(iUpdateSumStep)) {
              IListener.iUpdateSumSteps.add(iUpdateSumStep);
          }
    }

    public void removeSumStepListener(IUpdateSumStep iUpdateSumStep) {
        if (IListener.iUpdateSumSteps.contains(iUpdateSumStep)) {
            IListener.iUpdateSumSteps.remove(iUpdateSumStep);
        }
    }

    public void addRequestDataListener(IRequestData iRequestData) {
        if (!IListener.iRequestDatas.contains(iRequestData)) {
            IListener.iRequestDatas.add(iRequestData);
        }
    }

    public void removeRequestDatapListener(IRequestData iRequestData) {
        if (IListener.iRequestDatas.contains(iRequestData)) {
            IListener.iRequestDatas.remove(iRequestData);
        }
    }

    public void dispatchRequestStart() {
        for (IRequestData requestData : IListener.iRequestDatas) {
            requestData.startRequest();
        }
    }

    public void dispatchRequestProgress(int max,int progress) {
        for (IRequestData requestData : IListener.iRequestDatas) {
            requestData.requestProgress(max,progress);
        }
    }

    public void dispatchRequestComplete(List<WatchBean> watchBeen) {
        for (IRequestData requestData : IListener.iRequestDatas) {
            requestData.requestComplete(watchBeen);
        }
    }

    public void dispatchRequestError(int errorCode,String errorMsg) {
        for (IRequestData requestData : IListener.iRequestDatas) {
            requestData.requestError(errorCode,errorMsg);
        }
    }

    protected void dispatchSumStep(int sum) {
        for (IUpdateSumStep iUpdateSumStep : IListener.iUpdateSumSteps) {
            iUpdateSumStep.sumStep(sum);
        }
    }

    public void addTargetListener(ITarget iTarget) {
        if (!IListener.iTargets.contains(iTarget)) {
            IListener.iTargets.add(iTarget);
        }
    }

    public void  removeTargetListener(ITarget iTarget) {
        if (IListener.iTargets.contains(iTarget)) {
            IListener.iTargets.remove(iTarget);
        }
    }

    protected void dispatchTarget(int target) {
       for (ITarget iTarget : IListener.iTargets) {
           iTarget.target(target);
       }
    }

    @Override
    public void registerComponent() {
        mSmartManager.getIService().registerComponent(this);
    }

    @Override
    public void unRegisterComponent() {
        mSmartManager.getIService().unRegisterComponent(this);
    }

    private static class IListener {
        private static List<IUpdateSumStep> iUpdateSumSteps = new ArrayList<>();
        private static List<IRequestData> iRequestDatas = new ArrayList<>();
        private static List<ITarget> iTargets = new ArrayList<>();
    }

}
