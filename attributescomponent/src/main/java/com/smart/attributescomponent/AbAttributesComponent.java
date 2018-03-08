package com.smart.attributescomponent;

import android.util.Log;

import com.smart.attributescomponent.listener.IPower;
import com.smart.attributescomponent.listener.ISn;
import com.smart.attributescomponent.listener.IVersion;
import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 15:49
 * @说明
 */
public abstract class AbAttributesComponent extends AbComponent {

    public void addPowerListener(IPower iPower) {
        if (!Listeners.iPowers.contains(iPower))
            Listeners.iPowers.add(iPower);
    }

    public void removePowerListener(IPower iPower) {
        if (Listeners.iPowers.contains(iPower)) {
            Listeners.iPowers.remove(iPower);
        }
    }

    public void addVersionListener(IVersion iVersion) {
        if (!Listeners.iVersions.contains(iVersion)) {
            Listeners.iVersions.add(iVersion);
        }
    }

    public void removeVersionListener(IVersion iVersion) {
        if (Listeners.iVersions.contains(iVersion)) {
            Listeners.iVersions.remove(iVersion);
        }
    }

    public void addSn(ISn iSn) {
        if (!Listeners.iSns.contains(iSn)) {
            Listeners.iSns.add(iSn);
        }
    }

    public void removeSn(ISn iSn) {
        if (Listeners.iSns.contains(iSn)) {
            Listeners.iSns.remove(iSn);
        }
    }

    protected void dispatchPower(int value) {
        for (IPower power : Listeners.iPowers) {
            power.electricity(value);
        }
    }

    public void dispatchVersion(int main, int minor, int test) {
        for (IVersion iVersion : Listeners.iVersions) {
            Log.e("DownLoaderTask", "  分发固件版本 ");
            iVersion.version(main, minor, test);
        }
    }

    public void dispatchSn(String sn) {
        for (ISn iSn : Listeners.iSns) {
            iSn.sn(sn);
        }
    }

    public AbAttributesComponent(SmartManager smartManager) {
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
        static List<IPower> iPowers = new ArrayList<>();
        static List<IVersion> iVersions = new ArrayList<>();
        static List<ISn> iSns = new ArrayList<>();
    }

}
