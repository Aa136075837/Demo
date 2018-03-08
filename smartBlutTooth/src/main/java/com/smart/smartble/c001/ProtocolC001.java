package com.smart.smartble.c001;

import com.smart.smartble.c007.ProtocolC007;
import com.smart.smartble.client.protocolImp.IProtocol;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/6/30 17:51
 * @说明
 */
public class ProtocolC001 implements IProtocol {

    private static int mTag = 0;

    private ProtocolC001() {

    }

    private static ProtocolC001 mProtocolC001;

    public static IProtocol getInstance() {
        if (null == mProtocolC001) {
            synchronized (ProtocolC007.class) {
                if (null == mProtocolC001) {
                    mProtocolC001 = new ProtocolC001();
                }
            }
        }
        mTag++;
        return mProtocolC001;
    }


    @Override
    public byte makeProtocolCheck(byte[] bytes) {
        if (null == bytes)
            return 0;
        byte flag = 0;
        if (null != bytes && bytes.length >= 4) {
            for (int i = 3; i < bytes.length - 1; i++) {
                flag = (byte) ((bytes[i] + flag) & 0xFF);
            }
        }
        return flag;
    }

    @Override
    public int makeProtocolLength(byte[] bytes) {
        return bytes.length - 3;
    }

    @Override
    public byte[] makeNormalAck(int flow, int status) {
        return new byte[0];
    }

    @Override
    public byte[] makeAuthorized(boolean isAuto) {
        byte[] bytes = {0x24, 0x04, 0x02, 0x0A, 0x01, 0x00, 0x00};
        bytes[5] = (byte) (isAuto ? 0x03 : 0x04);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeIntoCamera(boolean isInto) {
        byte[] bytes = {0x24, 0x04, 0x02, 0x03, 0x02, 0x01, 0x00};
        bytes[5] = (byte) (isInto ? 0x01 : 0x02);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeCameraHeart() {
        byte[] bytes = {0x24, 0x04, 0x02, 0x03, 0x02, 0x01, 0x00};
        bytes[5] = (byte) 0x01;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makePhoneLostType(boolean isLost) {
        byte[] bytes = {0x24, 0x04, 0x02, 0x03, 0x04, 0x01, 0x12};
        bytes[5] = (byte) (isLost ? 0x01 : 0x02);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeSportTarget(int target) {
        byte[] bytes = {0x24, 0x08, 0x02, 0x0a, 0x01, 0x29, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[6] = (byte) target;
        bytes[7] = (byte) (target >> 8);
        bytes[8] = (byte) (target >> 16);
        bytes[9] = (byte) (target >> 24);
        return bytes;
    }

    @Override
    public byte[] makeNotifySetting(int position) {
        return new byte[0];
    }

    @Override
    public byte[] makeNotify(int position) {
        return new byte[0];
    }

    @Override
    public byte[] makeCancelNotify(int value) {
        return new byte[0];
    }

    @Override
    public byte[] makeDisconnectNotify(boolean notify) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetDisconnectNotifySetting() {
        return new byte[0];
    }

    @Override
    public byte[] makeSetAlarm(int index, int time, int open, int week) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetAlarm(int index) {
        return new byte[0];
    }

    @Override
    public byte[] makeIntoTime(boolean isInto) {
        return new byte[0];
    }

    @Override
    public byte[] makeTimeHeart() {
        return new byte[0];
    }

    @Override
    public byte[] makePositionTime(int position, int value) {
        return new byte[0];
    }

    @Override
    public byte[] makeMcuTime(int position, Date date) {
        return new byte[0];
    }

    @Override
    public byte[] makeAskTime(int position, Date date) {
        return new byte[0];
    }

    @Override
    public byte[] makeIntoSmall(int position, int value) {
        return new byte[0];
    }

    @Override
    public byte[] makeSecondTime(Date date, int arg1, int arg2) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetSecondTime() {
        return new byte[0];
    }

    @Override
    public byte[] makeStartOta(int test, int minor, int main) {
        return new byte[0];
    }

    @Override
    public byte[] makeAskFile(int flag, int id, int main, int minor, int test, int fileCount, int dataCount, int index) {
        return new byte[0];
    }

    @Override
    public byte[] makeAskFileStatus(int flag, int id, int status) {
        return new byte[0];
    }

    @Override
    public byte[] makeAskFileData(int index, byte[][] bytes) {
        return new byte[0];
    }

    @Override
    public byte[] makeOtaEnd(int flag, int status) {
        return new byte[0];
    }

    @Override
    public byte[] makeMcuUpdate(int main, int minor, int test) {
        return new byte[0];
    }

    @Override
    public byte[] makeUpdateData() {
        return new byte[0];
    }

    @Override
    public byte[] makeGetSumStep() {
        return new byte[0];
    }

    @Override
    public byte[] makeGetDataByIndex(int index) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetDataContent(int style) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetContentMessage(int style, int index) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetContentIndexData(int style, int mtc, int index) {
        return new byte[0];
    }

    @Override
    public byte[] makeSetTarget(int target) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetTarget() {
        return new byte[0];
    }

    @Override
    public byte[] makeGetDeleteDataByStyle(int style) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetDeleteDataByUtc(int style, int utc) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetElectricity() {
        return new byte[0];
    }

    @Override
    public byte[] makeChangeBle(String str) {
        return new byte[0];
    }

    @Override
    public byte[] makeGetVersion() {
        return new byte[0];
    }

    @Override
    public byte[] makeGetSn() {
        return new byte[0];
    }

    @Override
    public byte[] makeChangeBleInterval(int value) {
        return new byte[0];
    }
}
