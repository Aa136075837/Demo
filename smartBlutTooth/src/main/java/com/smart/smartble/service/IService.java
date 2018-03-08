package com.smart.smartble.service;

import com.smart.smartble.event.SmartAction;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 20:12
 * @说明
 */
public abstract class IService extends AbObserver {

    public abstract void dealActon(SmartAction smartAction);

}
