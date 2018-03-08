package com.smart.smartble.c001;

import com.smart.smartble.client.actionFactory.IDetailAction;
import com.smart.smartble.client.actionFactory.IFactory;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.AlarmEvent;
import com.smart.smartble.event.SmartAction;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/6/30 18:48
 * @说明
 */
public class FactoryC001 implements IFactory, IDetailAction{

    @Override
    public SmartAction buildNormalAck(Action action, int flow, int status) {
        return null;
    }

    @Override
    public SmartAction buildAuthorization(Action action, boolean b) {
        return null;
    }

    @Override
    public SmartAction buildCamera(Action action, boolean b) {
        return null;
    }

    @Override
    public SmartAction buildHeartCamera(Action action) {
        return null;
    }

    @Override
    public SmartAction buildSportTarget(Action action, int target) {
        return null;
    }

    @Override
    public SmartAction buildNotifySetting(Action action, int position) {
        return null;
    }

    @Override
    public SmartAction buildNotify(Action action, int position) {
        return null;
    }

    @Override
    public SmartAction buildCancelNotify(Action action, int value) {
        return null;
    }

    @Override
    public SmartAction buildDisconnectNotify(Action action, boolean notify) {
        return null;
    }

    @Override
    public SmartAction buildGetDisconnectNotifySetting(Action action) {
        return null;
    }

    @Override
    public SmartAction buildAlarmSetting(Action action) {
        return null;
    }

    @Override
    public SmartAction buildTime(Action action, boolean b) {
        return null;
    }

    @Override
    public SmartAction buildHearTime(Action action) {
        return null;
    }

    @Override
    public SmartAction buildPositionTime(Action action, int position, int value) {
        return null;
    }

    @Override
    public SmartAction buildMcuTime(Action action, int position, Date date) {
        return null;
    }

    @Override
    public SmartAction buildAskTime(Action action, int position, Date date) {
        return null;
    }

    @Override
    public SmartAction buildSecondTime(Action action, Date date, int arg1, int arg2) {
        return null;
    }

    @Override
    public SmartAction buildGetSecondCity(Action action) {
        return null;
    }

    @Override
    public SmartAction buildStartOta(Action action, int test, int minor, int main) {
        return null;
    }

    @Override
    public SmartAction buildAskFile(Action action, int flag, int id, int main, int minor, int test, int fileCount, int dataCount, int index) {
        return null;
    }

    @Override
    public SmartAction buildAskFileStatus(Action action, int flag, int id, int status) {
        return null;
    }

    @Override
    public SmartAction buildAskFileData(Action action, int index, byte[][] bytes) {
        return null;
    }

    @Override
    public SmartAction buildOtaEnd(Action action, int flag, int status) {
        return null;
    }

    @Override
    public SmartAction buildUpdateMcu(Action action, int main, int minor, int test) {
        return null;
    }

    @Override
    public SmartAction buildUpdateData(Action action) {
        return null;
    }

    @Override
    public SmartAction buildSetTarget(Action action, int target) {
        return null;
    }

    @Override
    public SmartAction buildGetSportTarget(Action action) {
        return null;
    }

    @Override
    public SmartAction buildGetSumStep(Action action) {
        return null;
    }

    @Override
    public SmartAction buildGetDataByIndex(Action action, int index) {
        return null;
    }

    @Override
    public SmartAction buildGetDataContent(Action action, int style) {
        return null;
    }

    @Override
    public SmartAction buildContentMessage(Action action, int style, int index) {
        return null;
    }

    @Override
    public SmartAction buildContentIndexData(Action action, int style, int mtc, int index) {
        return null;
    }

    @Override
    public SmartAction buildDeleteDataByStyle(Action action, int style) {
        return null;
    }

    @Override
    public SmartAction buildDeleteContentByUtc(Action action, int style, int utc) {
        return null;
    }

    @Override
    public SmartAction buildIntoSmall(Action action, int position, int small) {
        return null;
    }

    @Override
    public SmartAction buildSetAlarm(Action action, int index, int time, int open, int week) {
        return null;
    }

    @Override
    public SmartAction buildGetAlarm(Action action, int index) {
        return null;
    }

    @Override
    public SmartAction buildGetElectricity(Action action) {
        return null;
    }

    @Override
    public SmartAction buildChangeBle(Action action, String str) {
        return null;
    }

    @Override
    public SmartAction buildGetVersion(Action action) {
        return null;
    }

    @Override
    public SmartAction buildGetSn(Action action) {
        return null;
    }

    @Override
    public SmartAction buildChangeBleInterval(Action action, int value) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action, boolean b) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action, int arg1) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action, Object... objects) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action, AlarmEvent alarmEvent) {
        return null;
    }

    @Override
    public SmartAction buildSmartAction(Action action, int arg1, int arg2, Object obj) {
        return null;
    }

    @Override
    public void reBuildAskedAction(SmartAction smartAction) {

    }
}
