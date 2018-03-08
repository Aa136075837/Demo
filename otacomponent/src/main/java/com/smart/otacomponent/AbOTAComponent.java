package com.smart.otacomponent;


import com.smart.otacomponent.listener.IOTAListener;
import com.smart.smartble.SmartManager;
import com.smart.smartble.component.AbComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/5/31 17:05
 * @说明
 */
public abstract class AbOTAComponent extends AbComponent {


    private ListenerInfo mListenerInfo = new ListenerInfo();

    public AbOTAComponent(SmartManager smartManager) {
        super(smartManager);
    }

    public void addOTAListener(IOTAListener listener) {
        if (!mListenerInfo.iotaListeners.contains(listener))
            mListenerInfo.iotaListeners.add(listener);
    }

    public void removeOTAListener(IOTAListener listener) {
        if (mListenerInfo.iotaListeners.contains(listener))
            mListenerInfo.iotaListeners.remove(listener);
    }

    protected void dispatchDealFile() {
        for (IOTAListener listener : mListenerInfo.iotaListeners) {
            listener.dealFile();
        }
    }

    protected void dispatchStart() {
        for (IOTAListener listener : mListenerInfo.iotaListeners) {
            listener.otaStart();
        }
    }

    protected void dispatchProgress(int max, int progress) {
        for (IOTAListener listener : mListenerInfo.iotaListeners) {
            listener.onProgress(max, progress);
        }
    }

    protected void dispatchComplete(int complete) {
        for (IOTAListener listener : mListenerInfo.iotaListeners) {
            listener.onComplete(complete);
        }
    }

    protected void dispatchFail() {
        for (IOTAListener listener : mListenerInfo.iotaListeners) {
            listener.onFail();
        }
    }

    private class ListenerInfo {
        List<IOTAListener> iotaListeners = new ArrayList<>();
    }

}
