package com.smart.smartble.c007;

import android.util.Log;

import com.smart.smartble.ByteToString;
import com.smart.smartble.client.actionFactory.IDetailAction;
import com.smart.smartble.client.actionFactory.IFactory;
import com.smart.smartble.client.protocolImp.ProtocolProxy;
import com.smart.smartble.event.Action;
import com.smart.smartble.event.AlarmEvent;
import com.smart.smartble.event.SmartAction;
import com.smart.smartble.smartBle.BleChannel;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2016/12/19 18:08
 * @说明
 */
public class FactoryC007 implements IFactory, IDetailAction {

    private static String TAG = "FactoryC007";
    public ProtocolProxy mProtocolProxy = new ProtocolProxy ();

    @Override public SmartAction buildSmartAction (Action action) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().build ();
        switch (action) {
            case REQUEST_ACTION_HEART_CAMERA:
                smartAction = buildHeartCamera (action);
                break;
            case REQUEST_ACTION_HEART_TIME:
                smartAction = buildHearTime (action);
                break;
            case REQUEST_ACTION_UPDATE_DATA:
                smartAction = buildUpdateData (action);
                break;
            case REQUEST_ACTION_GET_ONE_DAY:
                smartAction = buildGetSumStep (action);
                break;
            case REQUEST_ACTION_GET_ELECTRICITY:
                smartAction = buildGetElectricity (action);
                break;
            case REQUEST_ACTION_REQUEST_VERSION:
                smartAction = buildGetVersion (action);
                break;
            case REQUEST_ACTION_GET_DISCONNECT_NOTIFY_SETTING:
                smartAction = buildGetDisconnectNotifySetting (action);
                break;
            case REQUEST_ACTION_GET_SECOND_TIME:
                smartAction = buildGetSecondCity (action);
                break;
            case REQUEST_ACTION_GET_TARGET:
                smartAction = buildGetSportTarget (action);
                break;
            case REQUEST_ACTION_GET_SN:
                smartAction = buildGetSn(action);
                break;
        }
        return smartAction;
    }



    @Override public SmartAction buildSmartAction (Action action, boolean b) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().build ();
        switch (action) {
            case REQUEST_ACTION_AUTHORIZATION:
                smartAction = buildAuthorization (action, b);
                break;
            case REQUEST_ACTION_CAMERA:
                smartAction = buildCamera (action, b);
                break;
            case REQUEST_ACTION_TIME:
                smartAction = buildTime (action, b);
                break;
            case REQUEST_ACTION_DISCONNECT_NOTIFY:
                smartAction = buildDisconnectNotify (action, b);
                break;
        }
        return smartAction;
    }

    @Override public SmartAction buildSmartAction (Action action, int arg1) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().build ();
        switch (action) {
            case REQUEST_ACTION_SET_NOTIFY_SETTING:
                smartAction = buildNotifySetting (action, arg1);
                break;
            case REQUEST_ACTION_SET_NOTIFY:
                smartAction = buildNotify (action, arg1);
                break;
            case REQUEST_ACTION_GET_ALARM:
                smartAction = buildGetAlarm (action, arg1);
                break;
            case REQUEST_ACTION_CANCEL_NOTIFY:
                smartAction = buildCancelNotify (action, arg1);
                break;
            case REQUEST_ACTION_SET_TARGET:
                smartAction = buildSetTarget (action, arg1);
                break;
            case REQUEST_ACTION_CHANGE_BLE_INTERVAL:
                smartAction = buildChangeBleInterval (action, arg1);
                break;
            case REQUEST_ACTION_GET_CONTENT:
                smartAction = buildGetDataContent (action, arg1);
                break;
            case REQUEST_ACTION_DELETE_DATA_BY_STYLE:
                smartAction = buildDeleteDataByStyle (action, arg1);
                break;
        }
        return smartAction;
    }

    @Override public SmartAction buildSmartAction (Action action, Object... objects) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        switch (action) {
            case REQUEST_ACTION_START_OTA:
                smartAction = dealStartOta (action, objects);
                break;
            case REQUEST_ACTION_ASK_MCU_FILE:
                smartAction = dealAskMcuFile (action, objects);
                break;
            case REQUEST_ACTION_ASK_MCU_FILE_STATUS:
                smartAction = dealAskMcuFilStatus (action, objects);
                break;
            case REQUEST_ACTION_ASK_FILE_DATA:
                smartAction = dealAskFileData (action, objects);

                break;
            case REQUEST_ACTION_SET_OTA_END:
                smartAction = dealOtaEnd (action, objects);
                break;
            case REQUEST_ACTION_TIME_SMALL_POSITION:
                smartAction = dealSmallPosition (action, objects);
                break;
            case REQUEST_ACTION_SET_MCU_TIME:
                smartAction = dealMcuTime (action, objects);
                break;
            case REQUEST_ACTION_SET_POSITION_TIME:
                smartAction = dealPositionTime (action, objects);
                break;
            case REQUEST_ACTION_SET_ALARM:
                smartAction = dealSetAlarm (action, objects);
                break;
            case REQUEST_ACTION_NORMAL_ACK:
                smartAction = dealNormalAck (action, objects);
                break;
            case REQUEST_ACTION_BEGIN_UPDATE_MCU:
                smartAction = dealOTAEnd (action, objects);
                break;
            case REQUEST_ACTION_CHANGE_BLE_CAST:
                smartAction = dealChangeBle (action, objects);
                break;
            case REQUEST_ACTION_SET_SECOND_TIME:
                smartAction = dealSecondTime (action, objects);
                break;
            case REQUEST_ACTION_GET_CONTENT_MESSAGE:
                smartAction = dealContentMessage (action, objects);
                break;
            case REQUEST_ACTION_GET_DATA_BY_INDEX:
                smartAction = dealContentIndexData (action, objects);
                break;
            case REQUEST_ACTION_ASK_TIME:
                smartAction = dealAskTime (action, objects);
                break;
            case REQUEST_ACTION_DELETE_DATA_BY_UTC:
                smartAction = dealDeleteContentByUtc (action, objects);
                break;
        }
        return smartAction;
    }

    private SmartAction dealDeleteContentByUtc (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int style = (int) objects[0];
        int utc = (int) objects[1];
        return buildDeleteContentByUtc (action, style, utc);
    }

    private SmartAction dealAskTime (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int position = (int) objects[0];
        Date date = (Date) objects[1];
        return buildAskTime (action, position, date);
    }

    private SmartAction dealContentIndexData (Action action, Object[] objects) {
        if (3 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int style = (int) objects[0];
        int mtc = (int) objects[1];
        int index = (int) objects[2];
        return buildContentIndexData (action, style, mtc, index);
    }

    private SmartAction dealContentMessage (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int style = (int) objects[0];
        int index = (int) objects[1];
        return buildContentMessage (action, style, index);
    }

    private SmartAction dealSecondTime (Action action, Object[] objects) {
        if (3 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        Date date = (Date) objects[0];
        int arg1 = (int) objects[1];
        int arg2 = (int) objects[2];
        return buildSecondTime (action, date, arg1, arg2);
    }

    private SmartAction dealChangeBle (Action action, Object[] objects) {
        if (1 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        String str = (String) objects[0];
        return buildChangeBle (action, str);
    }

    private SmartAction dealOTAEnd (Action action, Object[] objects) {
        if (3 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int mainVersion = (int) objects[0];
        int minorVersion = (int) objects[1];
        int testVersion = (int) objects[2];
        return buildUpdateMcu (action, mainVersion, minorVersion, testVersion);
    }

    private SmartAction dealNormalAck (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int flow = (int) objects[0];
        int status = (int) objects[1];
        return buildNormalAck (action, flow, status);
    }

    private SmartAction dealStartOta (Action action, Object[] objects) {
        if (3 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int main = (int) objects[0];
        int minor = (int) objects[1];
        int test = (int) objects[2];
        return buildStartOta (action, main, minor, test);
    }

    private SmartAction dealAskMcuFile (Action action, Object[] objects) {
        if (8 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int flag = (int) objects[0];
        int id = (int) objects[1];
        int main = (int) objects[2];
        int minor = (int) objects[3];
        int test = (int) objects[4];
        int fileCount = (int) objects[5];
        int dataCount = (int) objects[6];
        int index = (int) objects[7];
        return buildAskFile (action, flag, id, main, minor, test, fileCount, dataCount, index);
    }

    private SmartAction dealAskMcuFilStatus (Action action, Object[] objects) {
        if (3 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int flag = (int) objects[0];
        int id = (int) objects[1];
        int status = (int) objects[2];
        return buildAskFileStatus (action, flag, id, status);
    }

    private SmartAction dealAskFileData (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int index = (int) objects[0];
        byte[][] bytes = (byte[][]) objects[1];
        return buildAskFileData (action, index, bytes);
    }

    private SmartAction dealOtaEnd (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int flag = (int) objects[0];
        int status = (int) objects[1];
        return buildOtaEnd (action, flag, status);
    }

    private SmartAction dealSmallPosition (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int position = (int) objects[0];
        int value = (int) objects[1];
        return buildIntoSmall (action, position, value);
    }

    private SmartAction dealMcuTime (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int position = (int) objects[0];
        Date date = (Date) objects[1];
        return buildMcuTime (action, position, date);
    }

    private SmartAction dealPositionTime (Action action, Object[] objects) {
        if (2 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int position = (int) objects[0];
        int value = (int) objects[1];
        return buildPositionTime (action, position, value);
    }

    private SmartAction dealSetAlarm (Action action, Object[] objects) {
        if (4 != objects.length) return new SmartAction.SmartBuilder ().action (Action.NONE).build ();
        int index = (int) objects[0];
        int time = (int) objects[1];
        int open = (int) objects[2];
        int week = (int) objects[3];
        return buildSetAlarm (action, index, time, open, week);
    }

    @Override public SmartAction buildSmartAction (Action action, AlarmEvent alarmEvent) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().build ();
        return smartAction;
    }

    @Override public SmartAction buildSmartAction (Action action, int arg1, int arg2, Object obj) {
        SmartAction smartAction = new SmartAction.SmartBuilder ().build ();
        switch (action) {
            case REQUEST_ACTION_SEND_ALARM:
                break;
        }
        return smartAction;
    }

    @Override public void reBuildAskedAction (SmartAction smartAction) {

    }

    @Override public SmartAction buildDeleteContentByUtc (Action action, int style, int utc) {
        byte[] bytes = mProtocolProxy.makeGetDeleteDataByUtc (style, utc);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildNormalAck (Action action, int flow, int status) {
        byte[] bytes = mProtocolProxy.makeNormalAck (flow, status);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildAuthorization (Action action, boolean b) {
        byte[] bytes = mProtocolProxy.makeAuthorized (b);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        Log.w ("BleService", "run--------->buildAuthorization" + bytes.length);
        return smartAction;
    }

    @Override public SmartAction buildCamera (Action action, boolean b) {
        byte[] bytes = mProtocolProxy.makeIntoCamera (b);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildHeartCamera (Action action) {
        byte[] bytes = mProtocolProxy.makeCameraHeart ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .isNoReply (true)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildSportTarget (Action action, int target) {
        return null;
    }

    @Override public SmartAction buildNotifySetting (Action action, int position) {
        byte[] bytes = mProtocolProxy.makeNotifySetting (position);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildNotify (Action action, int position) {
        byte[] bytes = mProtocolProxy.makeNotify (position);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildCancelNotify (Action action, int value) {
        byte[] bytes = mProtocolProxy.makeCancelNotify (value);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildDisconnectNotify (Action action, boolean notify) {
        byte[] bytes = mProtocolProxy.makeDisconnectNotify (notify);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetDisconnectNotifySetting (Action action) {
        byte[] bytes = mProtocolProxy.makeGetDisconnectNotifySetting ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildAlarmSetting (Action action) {
        return null;
    }

    @Override public SmartAction buildTime (Action action, boolean b) {
        Log.w ("BleService", "rn------->buildTime");
        byte[] bytes = mProtocolProxy.makeIntoTime (b);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildHearTime (Action action) {
        byte[] bytes = mProtocolProxy.makeTimeHeart ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildPositionTime (Action action, int position, int value) {
        byte[] bytes = mProtocolProxy.makePositionTime (position, value);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildMcuTime (Action action, int position, Date date) {
        Log.w("BleService","buildMcuTime::");
        byte[] bytes = mProtocolProxy.makeMcuTime (position, date);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isImmediate (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildAskTime (Action action, int position, Date date) {
        byte[] bytes = mProtocolProxy.makeAskTime (position, date);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isImmediate (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildSecondTime (Action action, Date date, int arg1, int arg2) {
        byte[] bytes = mProtocolProxy.makeSecondTime (date, arg1, arg2);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isImmediate (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetSecondCity (Action action) {
        byte[] bytes = mProtocolProxy.makeGetSecondTime ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isImmediate (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildStartOta (Action action, int main, int minor, int test) {
        byte[] bytes = mProtocolProxy.makeStartOta (main, minor, test);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override
    public SmartAction buildAskFile (Action action, int flag, int id, int main, int minor, int test, int fileCount,
        int dataCount, int index) {
        byte[] bytes = mProtocolProxy.makeAskFile (flag, id, main, minor, test, fileCount, dataCount, index);
        Log.w ("OTAC", "run----------->" + ByteToString.byteToString (bytes));
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isOTA (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildAskFileStatus (Action action, int flag, int id, int status) {
        byte[] bytes = mProtocolProxy.makeAskFileStatus (flag, id, status);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isOTA (true)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildAskFileData (Action action, int index, byte[][] dataBytes) {
        byte[] bytes = mProtocolProxy.makeAskFileData (index, dataBytes);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A803)
            .isNoReply (true)
            .isOTA (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildOtaEnd (Action action, int flag, int status) {
        byte[] bytes = mProtocolProxy.makeOtaEnd (flag, status);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildUpdateMcu (Action action, int main, int minor, int test) {
        byte[] bytes = mProtocolProxy.makeMcuUpdate (main, main, test);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .isNoReply (true)
            .isOTA (true)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildUpdateData (Action action) {
        byte[] bytes = mProtocolProxy.makeUpdateData ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildSetTarget (Action action, int target) {
        byte[] bytes = mProtocolProxy.makeSetTarget (target);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetSportTarget (Action action) {
        byte[] bytes = mProtocolProxy.makeGetTarget ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetSumStep (Action action) {
        byte[] bytes = mProtocolProxy.makeGetSumStep ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetDataByIndex (Action action, int index) {
        byte[] bytes = mProtocolProxy.makeGetDataByIndex (index);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetDataContent (Action action, int style) {
        byte[] bytes = mProtocolProxy.makeGetDataContent (style);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildContentMessage (Action action, int style, int index) {
        byte[] bytes = mProtocolProxy.makeGetContentMessage (style, index);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildContentIndexData (Action action, int style, int mtc, int index) {
        byte[] bytes = mProtocolProxy.makeGetContentIndexData (style, mtc, index);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildDeleteDataByStyle (Action action, int style) {
        byte[] bytes = mProtocolProxy.makeGetDeleteDataByStyle (style);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildIntoSmall (Action action, int position, int small) {
        byte[] bytes = mProtocolProxy.makeIntoSmall (position, small);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildSetAlarm (Action action, int index, int time, int open, int week) {
        byte[] bytes = mProtocolProxy.makeSetAlarm (index, time, open, week);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetAlarm (Action action, int index) {
        byte[] bytes = mProtocolProxy.makeGetAlarm (index);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetElectricity (Action action) {
        byte[] bytes = mProtocolProxy.makeGetElectricity ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildChangeBle (Action action, String str) {
        byte[] bytes = mProtocolProxy.makeChangeBle (str);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .isNoReply (true)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override public SmartAction buildGetVersion (Action action) {
        byte[] bytes = mProtocolProxy.makeGetVersion ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }

    @Override
    public SmartAction buildGetSn(Action action) {
        byte[] bytes = mProtocolProxy.makeGetSn ();
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
                .value (bytes)
                .serviceUUID (BleChannel.MAIN_SERVICE)
                .characteristicUUID (BleChannel.WRITE_CODE_A801)
                .build ();
        return smartAction;
    }

    @Override public SmartAction buildChangeBleInterval (Action action, int value) {
        byte[] bytes = mProtocolProxy.makeChangeBleInterval (value);
        SmartAction smartAction = new SmartAction.SmartBuilder ().action (action)
            .value (bytes)
            .serviceUUID (BleChannel.MAIN_SERVICE)
            .characteristicUUID (BleChannel.WRITE_CODE_A801)
            .build ();
        return smartAction;
    }
}
