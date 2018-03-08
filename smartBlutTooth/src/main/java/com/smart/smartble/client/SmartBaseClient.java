package com.smart.smartble.client;

import com.smart.smartble.event.Action;
import com.smart.smartble.smartBle.BleHelper;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2016/12/19 10:30
 * @说明
 */
public class SmartBaseClient extends IClient {

    public SmartBaseClient(BleHelper bleHelper) {
        super(bleHelper);
    }

    @Override
    public void doAction(Action action) {

    }

    @Override
    public void doAction(Action action, int arg1) {

    }

    @Override
    public void doAction(Action action, Object... objects) {

    }

    @Override
    public void doAction(Action action, boolean b) {

    }

    @Override
    public void doAction(Action action, int arg1, int arg2, Date date) {

    }

}
