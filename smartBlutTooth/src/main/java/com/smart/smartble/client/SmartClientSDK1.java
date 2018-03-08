package com.smart.smartble.client;

import com.smart.smartble.c007.FactoryC007;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleHelper;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:25
 * @说明
 */
public class SmartClientSDK1 extends IClient {

    private static final String TAG = "SmartClientSDK1";

    public SmartClientSDK1(BleHelper bleHelper) {
        super(bleHelper);
        mIFactory = new FactoryC007();
    }

    @Override
    public void doAction(Action action) {
        SmartAction smartAction = mIFactory.buildSmartAction(action);
        mBleHelper.toWriteAction(smartAction);
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
