package com.smart.smartble.c001;

import com.smart.smartble.event.Action;
import com.smart.smartble.event.ISmartActionOperator;
import com.smart.smartble.event.SmartAction;

import java.util.Queue;

/**
 * @author ARZE
 * @version 创建时间：2017/6/30 18:19
 * @说明
 */
public class SmartActionOperatorC001 implements ISmartActionOperator {


    @Override
    public SmartAction createSmartAction(byte[] bytes, String uuid) {
        return null;
    }

    @Override
    public boolean isSameInstruction(Queue<SmartAction> dataPool, byte[] bytes) {
        return true;
    }

    @Override
    public boolean isNormalAck(byte[] bytes) {
        return 6 == bytes.length;
    }

    @Override
    public SmartAction reBuildSmartAction(SmartAction smartAction) {
        return new SmartAction.SmartBuilder().action(Action.NONE).build();
    }

    @Override
    public boolean isOTAData(byte[] bytes, String uuid) {
        return false;
    }

    @Override
    public boolean isUserData(byte[] bytes, String uuid) {
        return false;
    }

}
