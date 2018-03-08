package com.smart.smartble.service;

import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 20:14
 * @说明
 */
public class SmartService extends IService {


    /**
     * 处理事件，分发各注册组件
     * @param smartAction
     */
    @Override
    public void dealActon(SmartAction smartAction) {
        dispatchComponent(smartAction);
    }
}
