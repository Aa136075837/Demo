package com.smart.smartble.client.protocolImp;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/3/4 16:58
 * @说明
 */
public interface IProtocol {

    /**
     * 检验协议
     *
     * @param bytes
     * @return
     */
    byte makeProtocolCheck(byte[] bytes);

    /**
     * 获取协议的长度
     *
     * @param bytes
     * @return
     */
    int makeProtocolLength(byte[] bytes);

    /**
     * 返回ack给mcu
     */

    byte[] makeNormalAck(int flow, int status);

    /**
     * 获取授权协议
     *
     * @param isAuto
     * @return
     */

    byte[] makeAuthorized(boolean isAuto);


    /**
     * 获取拍照协议
     *
     * @param isInto
     * @return
     */
    byte[] makeIntoCamera(boolean isInto);

    /**
     * 拍照模式心跳包
     *
     * @return
     */
    byte[] makeCameraHeart();

    /**
     * 获取手机丢失协议
     *
     * @param isLost
     * @return
     */
    byte[] makePhoneLostType(boolean isLost);

    byte[] makeSportTarget(int target);

    byte[] makeNotifySetting(int position);

    byte[] makeNotify(int position);

    byte[] makeCancelNotify(int value);

    byte[] makeDisconnectNotify(boolean notify);

    byte[] makeGetDisconnectNotifySetting();

    byte[] makeSetAlarm(int index, int time, int open, int week);

    byte[] makeGetAlarm(int index);


    /**
     * 校时
     */

    byte[] makeIntoTime(boolean isInto);

    byte[] makeTimeHeart();

    byte[] makePositionTime(int position, int value);

    byte[] makeMcuTime(int position, Date date);

    byte[] makeAskTime(int position,Date date);

    byte[] makeIntoSmall(int position, int value);

    byte[] makeSecondTime(Date date, int arg1, int arg2);

    byte[] makeGetSecondTime();
    /**
     * ota
     */

    byte[] makeStartOta(int main, int minor, int test);

    byte[] makeAskFile(int flag, int id, int main, int minor, int test, int fileCount, int dataCount, int index);

    byte[] makeAskFileStatus(int flag, int id, int status);

    byte[] makeAskFileData(int index, byte[][] bytes);

    byte[] makeOtaEnd(int flag, int status);

    byte[] makeMcuUpdate(int main, int minor, int test);

    /**
     * 数据传输
     */

    byte[] makeUpdateData();

    byte[] makeGetSumStep();

    byte[] makeGetDataByIndex(int index);

    byte[] makeGetDataContent(int style);

    byte[] makeGetContentMessage(int style, int index);

    byte[] makeGetContentIndexData(int style, int mtc, int index);

    byte[] makeSetTarget(int target);

    byte[] makeGetTarget();

    byte[] makeGetDeleteDataByStyle(int style);

    byte[] makeGetDeleteDataByUtc(int style,int utc);

    /**
     * 属性
     */

    byte[] makeGetElectricity();

    byte[] makeChangeBle(String str);

    byte[] makeGetVersion();

    byte[] makeGetSn();

    /**
     * 蓝牙操作
     */

    byte[] makeChangeBleInterval(int value);

}
