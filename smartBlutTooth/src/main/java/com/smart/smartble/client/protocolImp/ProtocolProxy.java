package com.smart.smartble.client.protocolImp;

import com.smart.smartble.FetchSDK;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/3/4 16:59
 * @说明
 */
public class ProtocolProxy implements IProtocol {

    private IProtocol mIProtocol = FetchSDK.fetchProtocol();

    public void initInstance() {
        mIProtocol = FetchSDK.fetchProtocol();
    }

    private IProtocol getIProtocol() {
        mIProtocol = FetchSDK.fetchProtocol();
        return mIProtocol;
    }

    @Override
    public byte makeProtocolCheck(byte[] bytes) {
        return 0;
    }

    @Override
    public int makeProtocolLength(byte[] bytes) {
        return 0;
    }

    @Override
    public byte[] makeNormalAck(int flow, int status) {
        return getIProtocol().makeNormalAck(flow,status);
    }

    @Override
    public byte[] makeAuthorized(boolean isAuto) {
        return getIProtocol().makeAuthorized(isAuto);
    }

    @Override
    public byte[] makeIntoCamera(boolean isInto) {
        return getIProtocol().makeIntoCamera(isInto);
    }

    @Override
    public byte[] makeCameraHeart() {
        return getIProtocol().makeCameraHeart();
    }

    @Override
    public byte[] makePhoneLostType(boolean isLost) {
        return getIProtocol().makePhoneLostType(isLost);
    }

    @Override
    public byte[] makeSportTarget(int target) {
        return getIProtocol().makeSportTarget(target);
    }

    @Override
    public byte[] makeNotifySetting(int position) {
        return getIProtocol().makeNotifySetting(position);
    }

    @Override
    public byte[] makeNotify(int position) {
        return getIProtocol().makeNotify(position);
    }

    @Override
    public byte[] makeCancelNotify(int value) {
        return getIProtocol().makeCancelNotify(value);
    }

    @Override
    public byte[] makeDisconnectNotify(boolean notify) {
        return getIProtocol().makeDisconnectNotify(notify);
    }

    @Override
    public byte[] makeGetDisconnectNotifySetting() {
        return getIProtocol().makeGetDisconnectNotifySetting();
    }

    @Override
    public byte[] makeSetAlarm(int index, int time, int open,int week) {
        return getIProtocol().makeSetAlarm(index, time, open,week);
    }

    @Override
    public byte[] makeGetAlarm(int index) {
        return getIProtocol().makeGetAlarm(index);
    }

    @Override
    public byte[] makeIntoTime(boolean isInto) {
        return getIProtocol().makeIntoTime(isInto);
    }

    @Override
    public byte[] makeTimeHeart() {
        return getIProtocol().makeTimeHeart();
    }

    @Override
    public byte[] makePositionTime(int position, int value) {
        return getIProtocol().makePositionTime(position, value);
    }

    @Override
    public byte[] makeMcuTime(int position, Date date) {
        return getIProtocol().makeMcuTime(position, date);
    }

    @Override
    public byte[] makeAskTime(int position, Date date) {
        return getIProtocol().makeAskTime(position, date);
    }

    @Override
    public byte[] makeIntoSmall(int position, int value) {
        return getIProtocol().makeIntoSmall(position, value);
    }

    @Override
    public byte[] makeSecondTime(Date date,int arg1,int arg2) {
        return getIProtocol().makeSecondTime(date,arg1,arg2);
    }

    @Override
    public byte[] makeGetSecondTime() {
        return getIProtocol().makeGetSecondTime();
    }

    @Override
    public byte[] makeStartOta(int main, int minor, int test) {
        return getIProtocol().makeStartOta(main, minor, test);
    }

    @Override
    public byte[] makeAskFile(int flag, int id, int main, int minor, int test, int fileCount, int dataCount, int index) {
        return getIProtocol().makeAskFile(flag, id, main, minor, test, fileCount, dataCount, index);
    }

    @Override
    public byte[] makeAskFileStatus(int flag, int id, int status) {
        return getIProtocol().makeAskFileStatus(flag, id, status);
    }

    @Override
    public byte[] makeAskFileData(int index, byte[][] bytes) {
        return getIProtocol().makeAskFileData(index, bytes);
    }

    @Override
    public byte[] makeOtaEnd(int flag, int status) {
        return getIProtocol().makeOtaEnd(flag, status);
    }

    @Override
    public byte[] makeMcuUpdate(int main ,int minor,int test) {
        return getIProtocol().makeMcuUpdate(main,minor,test);
    }

    @Override
    public byte[] makeUpdateData() {
        return getIProtocol().makeUpdateData();
    }

    @Override
    public byte[] makeGetSumStep() {
        return getIProtocol().makeGetSumStep();
    }

    @Override
    public byte[] makeGetDataByIndex(int index) {
        return getIProtocol().makeGetDataByIndex(index);
    }

    @Override
    public byte[] makeGetDataContent(int style) {
        return getIProtocol().makeGetDataContent(style);
    }

    @Override
    public byte[] makeGetContentMessage(int style, int index) {
        return getIProtocol().makeGetContentMessage(style,index);
    }

    @Override
    public byte[] makeGetContentIndexData(int style, int mtc, int index) {
        return getIProtocol().makeGetContentIndexData(style,mtc,index);
    }

    @Override
    public byte[] makeSetTarget(int target) {
        return getIProtocol().makeSetTarget(target);
    }

    @Override
    public byte[] makeGetTarget() {
        return getIProtocol().makeGetTarget();
    }

    @Override
    public byte[] makeGetDeleteDataByStyle(int style) {
        return getIProtocol().makeGetDeleteDataByStyle(style);
    }

    @Override
    public byte[] makeGetDeleteDataByUtc(int style, int utc) {
        return getIProtocol().makeGetDeleteDataByUtc(style,utc);
    }

    @Override
    public byte[] makeGetElectricity() {
        return getIProtocol().makeGetElectricity();
    }

    @Override
    public byte[] makeChangeBle(String str) {
        return getIProtocol().makeChangeBle(str);
    }

    @Override
    public byte[] makeGetVersion() {
        return getIProtocol().makeGetVersion();
    }

    @Override
    public byte[] makeGetSn() {
        return getIProtocol().makeGetSn();
    }

    @Override
    public byte[] makeChangeBleInterval(int value) {
        return getIProtocol().makeChangeBleInterval(value);
    }

}
