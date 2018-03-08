package com.smart.smartble.component;

import com.smart.smartble.SmartManager;
import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2017/3/8 11:01
 * @说明  基础组件
 */
public abstract class AbComponent {

    protected  SmartManager mSmartManager;

    public AbComponent(SmartManager smartManager){
        mSmartManager = smartManager;
    }
    /**
     * 注册组件
     */
    public abstract void registerComponent();

    /**
     * 注销组件
     */

    public abstract void unRegisterComponent();

    public abstract void start ();

    public abstract void dealAction (SmartAction action);

}
