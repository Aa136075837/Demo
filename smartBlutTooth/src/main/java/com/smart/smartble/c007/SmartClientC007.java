package com.smart.smartble.c007;

import com.smart.smartble.c007.FactoryC007;
import com.smart.smartble.client.IClient;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleHelper;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 19:30
 * @说明  C007协议
 */
public class SmartClientC007 extends IClient {

    private static final String TAG = "SmartClientC007";

    public SmartClientC007(BleHelper bleHelper) {
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
        SmartAction smartAction = mIFactory.buildSmartAction(action,arg1);
        mBleHelper.toWriteAction(smartAction);
    }

    @Override
    public void doAction(Action action, Object... objects) {
        SmartAction smartAction = mIFactory.buildSmartAction(action,objects);
        mBleHelper.toWriteAction(smartAction);
    }

    @Override
    public void doAction(Action action, boolean b) {
        SmartAction smartAction = mIFactory.buildSmartAction(action,b);
        mBleHelper.toWriteAction(smartAction);
    }

    @Override
    public void doAction(Action action, int arg1, int arg2, Date date) {

    }
}
