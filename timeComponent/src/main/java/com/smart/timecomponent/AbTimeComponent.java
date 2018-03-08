package com.smart.timecomponent;

import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/8 18:22
 * @说明
 */
public abstract class AbTimeComponent extends AbComponent implements ITime {


    public void addSecondTimeListener(ISecondTime iSecondTime) {
        if (!Listeners.iSecondTimes.contains(iSecondTime)) {
            Listeners.iSecondTimes.add(iSecondTime);
        }
    }

    public void removeSecondTimeListener(ISecondTime iSecondTime) {
        if (Listeners.iSecondTimes.contains(iSecondTime)) {
            Listeners.iSecondTimes.remove(iSecondTime);
        }
    }

    public void dispatchSecondTime(Date date, int high, int low) {
        for (ISecondTime iSecondTime : Listeners.iSecondTimes) {
            iSecondTime.secondTime(date, high, low);
        }
    }

    public AbTimeComponent(SmartManager smartManager) {
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

    private static class Listeners {

        public static List<ISecondTime> iSecondTimes = new ArrayList<>();
    }

}
