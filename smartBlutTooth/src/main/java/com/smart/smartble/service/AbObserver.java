package com.smart.smartble.service;

import com.smart.smartble.component.AbComponent;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.utils.L;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/3/8 10:57
 * @说明
 */
public abstract class AbObserver {

    private final static String TAG = "AbObserver";

    private static List<AbComponent> mComponents = new ArrayList<>();

    public synchronized void registerComponent(AbComponent component) {
        L.i(TAG," registerComponent mComponents size:" + mComponents.size());
        if (!mComponents.contains(component)) {
            L.i(TAG,"add "  );
            mComponents.add(component);
        }
    }

    public synchronized void unRegisterComponent(AbComponent component) {
        if (mComponents.contains(component)) {
            mComponents.remove(component);
        }
    }

    protected void dispatchComponent(SmartAction action){
        L.i(TAG,"mComponents size:" + mComponents.size());
        for (AbComponent component : mComponents) {
            L.i(TAG,"dispatchComponent in");
            component.dealAction(action);
        }
    }

}
