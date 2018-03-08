package com.smart.smartble.c007;

import com.smart.smartble.event.Action;
import com.smart.smartble.event.ISmartActionOperator;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleChannel;

import java.util.Date;
import java.util.Queue;

/**
 * @author ARZE
 * @version 创建时间：2017/5/26 18:23
 * @说明
 */
public class SmartActionOperatorC007 implements ISmartActionOperator {

    private static final String TAG = "SmartActionOperatorC007";

    @Override
    public SmartAction createSmartAction(byte[] bytes, String uuid) {

        if (bytes.length < 8)
            return new SmartAction.SmartBuilder().action(Action.NONE).build();
        int index5 = bytes[5] & 0xff;
        int index6 = bytes[6] & 0xff;
        int index7 = bytes[7] & 0xff;
        if (0x01 == index5 && 0x01 == index6 && 0x05 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_AUTHORIZATION).value(bytes).build();
        } else if (0x02 == index5 && 0x06 == index6 && 0x02 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_ASK_MCU_FILE).value(bytes).build();
        } else if (0x02 == index5 && 0x06 == index6 && 0x03 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_ASK_FILE_DATA).value(bytes).build();
        } else if (0x01 == index5 && 0x06 == index6 && 0x04 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_SET_OTA_END).value(bytes).build();
        } else if (0x08 == index5 && 0x10 == index6 && 0x04 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_UPDATE_DATA).value(bytes).build();
        } else if (0x04 == index5 && 0x03 == index6 && 0x22 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_TAKE_PHOTON).value(bytes).build();
        } else if (0x08 == index5 && 0x01 == index6 && 0x0A == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_ALARM).value(bytes).build();
        } else if (0x08 == index5 && 0x10 == index6 && 0x03 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_ONE_DAY).value(bytes).build();
        } else if (0x08 == index5 && 0x10 == index6 && 0x05 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_DATA_BY_INDEX).value(bytes).build();
        } else if (0x08 == index5 && 0x10 == index6 && 0x04 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_UPDATE_DATA).value(bytes).build();
        } else if (0x08 == index5 && 0x01 == index6 && 0x04 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_ELECTRICITY).value(bytes).build();
        } else if (0x04 == index5 && 0x03 == index6 && 0x11 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_FIND_PHONE).value(bytes).build();
        } else if (0x04 == index5 && 0x03 == index6 && 0x03 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_PHONE_EVENT).value(bytes).build();
        } else if (0x80 == index5 && 0x00 == index6 && 0x05 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_REPLAY_CHANGE_BLE).value(bytes).build();
        } else if (0x23 != (bytes[0] & 0xff) && BleChannel.WRITE_CODE_A802.equals(uuid)) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_REPLAY_DATA).value(bytes).build();
        } else if (0x08 == index5 && 0x01 == index6 && 0x02 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_REQUEST_VERSION).value(bytes).build();
        } else if (0x08 == index5 && 0x00 == index6 && 0x11 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_DISCONNECT_NOTIFY_SETTING).value(bytes).build();
        } else if (0x08 == index5 && 0xf0 == index6 && 0x10 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_CONTENT).value(bytes).build();
        } else if (0x08 == index5 && 0xf0 == index6 && 0x11 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_CONTENT_MESSAGE).value(bytes).build();
        } else if (0x08 == index5 && 0xf0 == index6 && 0x12 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_DATA_BY_INDEX).value(bytes).build();
        } else if (0x02 == index5 && 0x01 == index6 && 0x08 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_TIME).value(bytes).build();
        } else if (0x80 == index5 && 0xf0 == index6 && 0x32 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_DELETE_DATA_BY_UTC).value(bytes).build();
        } else if (0x08 == index5 && 0x01 == index6 && 0x09 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_SECOND_TIME).value(bytes).build();
        } else if (0x08 == index5 && 0x10 == index6 && 0x02 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_TARGET).value(bytes).build();
        } else if (0x80 == index5 && 0x01 == index6 && 0x0A == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_ALARM_REPLAY).value(bytes).build();
        } else if (0x08 == index5 && 0x01 == index6 && 0x06 == index7) {
            return new SmartAction.SmartBuilder().action(Action.REQUEST_ACTION_GET_SN).value(bytes).build();
        }
        return new SmartAction.SmartBuilder().action(Action.NONE).build();
    }

    @Override
    public boolean isSameInstruction(Queue<SmartAction> dataPool, byte[] bytes) {
        if (bytes.length < 5 || dataPool.size() <= 0)
            return false;
        SmartAction smartAction = dataPool.element();
        byte[] targetBytes = smartAction.getBytes();
        if (targetBytes.length < 5)
            return false;
        if (targetBytes[4] == bytes[4]) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isNormalAck(byte[] bytes) {
        return 7 == bytes.length;
    }

    @Override
    public SmartAction reBuildSmartAction(SmartAction smartAction) {
        switch (smartAction.getAction()) {
            case REQUEST_ACTION_SET_MCU_TIME:
                byte[] bytes = smartAction.getBytes();
                byte flag = bytes[4];
                FactoryC007 factoryC007 = new FactoryC007();
                SmartAction action = factoryC007.buildMcuTime(smartAction.getAction(), 1, new Date());
                action.setSendCount(smartAction.getResendCount());
                byte[] now = action.getBytes();
                now[4] = flag;
                now[now.length - 1] = ProtocolC007.getInstance().makeProtocolCheck(now);
                return action;
            case REQUEST_ACTION_ASK_TIME:
                bytes = smartAction.getBytes();
                flag = bytes[4];
                factoryC007 = new FactoryC007();
                action = factoryC007.buildAskTime(smartAction.getAction(), 1, new Date());
                action.setSendCount(smartAction.getResendCount());
                now = action.getBytes();
                now[4] = flag;
                now[now.length - 1] = ProtocolC007.getInstance().makeProtocolCheck(now);
                return action;
        }
        return smartAction;
    }

    @Override
    public boolean isOTAData(byte[] bytes, String uuid) {
        boolean result = false;
        if ((null != bytes && bytes.length == 320 || bytes[0] != 0x23) && BleChannel.WRITE_CODE_A803.equals(uuid)) {
            result = true;
        }
        return result;
    }

    @Override
    public boolean isUserData(byte[] bytes, String uuid) {
        boolean result = false;
        if (null != bytes && bytes[0] != 0x23 && BleChannel.WRITE_CODE_A802.equals(uuid)) {
            result = true;
        }
        return result;
    }

}
