package com.smart.smartble.c007;

import android.util.Log;

import com.smart.smartble.ByteToString;
import com.smart.smartble.client.protocolImp.BCDHelper;
import com.smart.smartble.client.protocolImp.IProtocol;
import com.smart.smartble.utils.TimeZoneUtil;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/5/26 14:09
 * @说明
 */
public class ProtocolC007 implements IProtocol {

    private static int mTag = 0;

    private ProtocolC007() {

    }

    private static ProtocolC007 mProtocolC007;

    public static IProtocol getInstance() {
        if (null == mProtocolC007) {
            synchronized (ProtocolC007.class) {
                if (null == mProtocolC007) {
                    mProtocolC007 = new ProtocolC007();
                }
            }
        }
        mTag++;
        if (mTag == 0x80) {
            mTag = 0;
        }
        return mProtocolC007;
    }

    @Override
    public byte makeProtocolCheck(byte[] bytes) {
        if (null == bytes)
            return 0;
        int sum = 0;
        for (int i = 0; i < bytes.length - 1; i++) {
            sum += (byte) ((bytes[i] ^ i) & 0xFF);
        }
        Log.w("BleService", "run---------------->1 ::::" + sum);
        sum = sum & 255;
        Log.w("BleService", "run---------------->2 ::::" + sum);
        return (byte) sum;

    }

    @Override
    public int makeProtocolLength(byte[] bytes) {
        return bytes.length - 6;
    }

    @Override
    public byte[] makeNormalAck(int flow, int status) {
        byte[] bytes = {0x23, 0x01, 0x01, 0x31, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) flow;
        bytes[5] = (byte) status;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeAuthorized(boolean isAuto) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x04, 0x31, (byte) 0x00,
                0x04, 0x01, (byte) 0x05, 0x00, 0x00};
        bytes[4] = (byte) mTag;
        if (isAuto) {
            bytes[8] = 0x01;
        }
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeIntoCamera(boolean isInto) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x04, 0x31, (byte) 0x00,
                0x04, 0x03, (byte) 0x21, 0x01, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (isInto ? 0x01 : 0x00);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeCameraHeart() {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, (byte) 0x80, 0x03, 0x23, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makePhoneLostType(boolean isLost) {
        return new byte[0];
    }

    @Override
    public byte[] makeSportTarget(int target) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x01, 0x10, 0x02, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (target >> 8);
        bytes[9] = (byte) (target);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeNotifySetting(int position) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x07, 0x31, 0x00, 0x01, 0x03, 0x02, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        /*
        bytes[8] = (byte) (position >> 24);
        bytes[9] = (byte) (position >> 16);
        bytes[10] = (byte) (position >> 8);
        bytes[11] = (byte) (position);
         */
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeNotify(int position) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x07, 0x31, 0x00, 0x04, 0x03, 0x01, 0x00,
                0x00, 0x00, 0x02, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (position >> 24);
        bytes[9] = (byte) (position >> 16);
        bytes[10] = (byte) (position >> 8);
        bytes[11] = (byte) (position);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeCancelNotify(int value) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x07, 0x31, 0x00, 0x04, 0x03, 0x04, 0x00,
                0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (value >> 24);
        bytes[9] = (byte) (value >> 16);
        bytes[10] = (byte) (value >> 8);
        bytes[11] = (byte) (value);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeDisconnectNotify(boolean notify) {
        byte[] bytes = {0x23, 0x01, 0x04, 0x31, 0x00, 0x01, 0x00, 0x11, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (notify ? 1 : 0);
        return bytes;
    }

    @Override
    public byte[] makeGetDisconnectNotifySetting() {
        byte[] bytes = {0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x00, 0x11, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        return bytes;
    }

    @Override
    public byte[] makeSetAlarm(int index, int time, int open, int repeat) {

        byte[] bytes = new byte[]{0x23, 0x01, 0x08, 0x31, 0x00, 0x01, 0x01, 0x0A, 0x00,
                0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) index;
        int hour = time / 60;
        int min = time % 60;
        byte[] hourByte = BCDHelper.str2Bcd(String.valueOf(hour));
        byte[] minByte = BCDHelper.str2Bcd(String.valueOf(min));
        bytes[9] = hourByte[0];
        bytes[10] = minByte[0];
        bytes[11] = (byte) repeat;
        bytes[12] = (byte) open;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetAlarm(int index) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x04, 0x31, 0x00, 0x02, 0x01, 0x0A, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) index;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeIntoTime(boolean isInto) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, 0x04, 0x03, 0x31, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (isInto ? 1 : 0);
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeTimeHeart() {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, (byte) 0x80, 0x03, 0x34, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makePositionTime(int position, int value) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x06, 0x31, 0x00, 0x04, 0x03, 0x33, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) position;
        bytes[9] = (byte) (value >> 8);
        bytes[10] = (byte) value;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeMcuTime(int position, Date date) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x0B, 0x31, 0x00, 0x01, 0x01, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = 0x17;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();
        byte[] monthByte = BCDHelper.str2Bcd(String.valueOf(month));
        byte[] dayByte = BCDHelper.str2Bcd(String.valueOf(day));
        byte[] hourByte = BCDHelper.str2Bcd(String.valueOf(hour));
        byte[] minByte = BCDHelper.str2Bcd(String.valueOf(min));
        byte[] secByte = BCDHelper.str2Bcd(String.valueOf(sec));
        bytes[9] = monthByte[0];
        bytes[10] = dayByte[0];
        bytes[11] = hourByte[0];
        bytes[12] = minByte[0];
        bytes[13] = secByte[0];
        Calendar calendar = Calendar.getInstance();
        String tz = TimeZoneUtil.getCurrentTimeZone(calendar.getTimeZone());
        int zoneMin = TimeZoneUtil.getMinFromThereZone(tz);
        float zoneLow = ((float) (Math.abs(zoneMin) % 60)) / 60;
        BigDecimal bigDecimal = new BigDecimal(zoneLow);
        zoneLow = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        zoneLow = zoneLow * 100;
        int zoneHigh = zoneMin / 60;
        bytes[14] = (byte) zoneHigh;
        bytes[15] = (byte) (int) zoneLow;
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        bytes[16] = (byte) week;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);

        Log.w("TimeComponent", "run-------->" + ByteToString.byteToString(bytes));
        return bytes;
    }

    @Override
    public byte[] makeAskTime(int position, Date date) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x0B, 0x31, 0x00, 0x08, 0x01, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = 0x17;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();
        byte[] monthByte = BCDHelper.str2Bcd(String.valueOf(month));
        byte[] dayByte = BCDHelper.str2Bcd(String.valueOf(day));
        byte[] hourByte = BCDHelper.str2Bcd(String.valueOf(hour));
        byte[] minByte = BCDHelper.str2Bcd(String.valueOf(min));
        byte[] secByte = BCDHelper.str2Bcd(String.valueOf(sec));
        bytes[9] = monthByte[0];
        bytes[10] = dayByte[0];
        bytes[11] = hourByte[0];
        bytes[12] = minByte[0];
        bytes[13] = secByte[0];
        Calendar calendar = Calendar.getInstance();
        String tz = TimeZoneUtil.getCurrentTimeZone(calendar.getTimeZone());
        int zoneMin = TimeZoneUtil.getMinFromThereZone(tz);
        float zoneLow = ((float) (Math.abs(zoneMin) % 60)) / 60;
        BigDecimal bigDecimal = new BigDecimal(zoneLow);
        zoneLow = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        zoneLow = zoneLow * 100;
        int zoneHigh = zoneMin / 60;
        bytes[14] = (byte) zoneHigh;
        bytes[15] = (byte) (int) zoneLow;
        int week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        bytes[16] = (byte) week;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);

        Log.w("TimeComponent", "run-------->" + (bytes[14] & 0xff));
        return bytes;
    }

    @Override
    public byte[] makeIntoSmall(int position, int value) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x04, 0x03, 0x32, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) position;
        bytes[9] = (byte) value;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeSecondTime(Date date, int arg1, int arg2) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x0E, 0x31, 0x00, 0x01, 0x01, 0x09, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,0x00,0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = 0x17;
        int month = date.getMonth() + 1;
        int day = date.getDate();
        int hour = date.getHours();
        int min = date.getMinutes();
        int sec = date.getSeconds();
        byte[] monthByte = BCDHelper.str2Bcd(String.valueOf(month));
        byte[] dayByte = BCDHelper.str2Bcd(String.valueOf(day));
        byte[] hourByte = BCDHelper.str2Bcd(String.valueOf(hour));
        byte[] minByte = BCDHelper.str2Bcd(String.valueOf(min));
        byte[] secByte = BCDHelper.str2Bcd(String.valueOf(sec));
        bytes[9] = monthByte[0];
        bytes[10] = dayByte[0];
        bytes[11] = hourByte[0];
        bytes[12] = minByte[0];
        bytes[13] = secByte[0];
        bytes[14] = (byte) arg1;
        bytes[15] = (byte) arg2;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int week = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) - 1;
        bytes[16] = (byte) week;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetSecondTime() {
        byte[] bytes = {0x23, 0x01, 0x36, 0x31, 0x00, 0x02, 0x01, 0x09, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeStartOta(int main, int minor, int test) {
        byte[] bytes = {0x23, 0x01, 0x06, 0x31, 0x00, 0x04, 0x06, 0x01, (byte) main,
                (byte) minor, (byte) test, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeAskFile(int flag, int id, int main, int minor, int test,
                              int fileCount, int dataCount, int index) {
        byte[] bytes = {0x23, 0x01, 0x0c, 0x31, 0x00, 0x08, 0x06, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) flag;
        bytes[8] = (byte) (id >> 8);
        bytes[9] = (byte) id;
        bytes[10] = (byte) main;
        bytes[11] = (byte) minor;
        bytes[12] = (byte) test;
        bytes[13] = (byte) fileCount;
        bytes[14] = (byte) (dataCount >> 8);
        bytes[15] = (byte) dataCount;
        bytes[16] = (byte) (index >> 8);
        bytes[17] = (byte) index;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeAskFileStatus(int flag, int id, int status) {
        byte[] bytes = {0x23, 0x01, 0x06, 0x31, 0x00, 0x08, 0x06, 0x03, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) flag;
        bytes[8] = (byte) (id >> 8);
        bytes[9] = (byte) id;
        bytes[10] = (byte) status;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeAskFileData(int index, byte[][] dataBytes) {
        byte[][] bytes = new byte[16][20];
        for (int i = 0; i < dataBytes.length; i++) {
            byte[] partBytes = new byte[20];
            partBytes[0] = (byte) ((i + index) >> 8);
            partBytes[1] = (byte) (i + index);
            for (int j = 0; j < dataBytes[i].length; j++) {
                partBytes[4 + j] = dataBytes[i][j];
            }
            bytes[i] = partBytes;
        }
        return concatAll(bytes);
    }

    @Override
    public byte[] makeOtaEnd(int flag, int status) {
        byte[] bytes = {0x23, 0x01, 0x01, 0x21, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) flag;
        bytes[5] = (byte) status;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeMcuUpdate(int main, int minor, int test) {
        byte[] bytes = {0x23, 0x01, 03, 0x31, 0x00, 0x04, 0x06, 0x05, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) main;
        bytes[9] = (byte) minor;
        bytes[10] = (byte) test;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeUpdateData() {
        byte[] bytes = {0x23, 0x01, 0x06, 0x31, 0x00, 0x02, 0x10, 0x04, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetSumStep() {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x10, 0x03, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetDataByIndex(int index) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x02, 0x10, 0x05, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (index >> 8);
        bytes[9] = (byte) index;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetDataContent(int style) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x02, (byte) 0xf0, 0x10, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (style >> 8);
        bytes[9] = (byte) style;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetContentMessage(int style, int index) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x07, 0x31, 0x00, 0x02, (byte) 0xf0, 0x11, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (style >> 8);
        bytes[9] = (byte) style;
        bytes[10] = (byte) (index >> 8);
        bytes[11] = (byte) index;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetContentIndexData(int style, int mtc, int index) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x0b, 0x31, 0x00, 0x02, (byte) 0xf0, 0x12, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (style >> 8);
        bytes[9] = (byte) style;
        bytes[10] = (byte) (mtc >> 24);
        bytes[11] = (byte) (mtc >> 16);
        bytes[12] = (byte) (mtc >> 8);
        bytes[13] = (byte) mtc;
        bytes[14] = (byte) (index >> 8);
        bytes[15] = (byte) index;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeSetTarget(int target) {
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x01, 0x10, 0x02, 0x00, 0x00, 0x00};
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (target >> 8);
        bytes[9] = (byte) target;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetTarget() {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x10, 0x02, 0x00};
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetDeleteDataByStyle(int style) {
        Log.w("DataComponent", "makeGetDeleteDataByStyle");
        byte[] bytes = new byte[]{0x23, 0x01, 0x05, 0x31, 0x00, 0x04, (byte) 0xf0, 0x31, 0x00, 0x00, 0x00};
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (style >> 8);
        bytes[9] = (byte) style;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetDeleteDataByUtc(int style, int utc) {
        Log.w("DataComponent", "makeGetDeleteDataByUtc");
        byte[] bytes = new byte[]{0x23, 0x01, 0x09, 0x31, 0x00, 0x04, (byte) 0xf0, 0x32, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) (style >> 8);
        bytes[9] = (byte) style;
        bytes[10] = (byte) (utc >> 24);
        bytes[11] = (byte) (utc >> 16);
        bytes[12] = (byte) (utc >> 8);
        bytes[13] = (byte) utc;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetElectricity() {
        byte[] bytes = new byte[]{0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x01, 0x04, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeChangeBle(String str) {
        byte[] normal = {0x23, 0x01, 0x00, 0x31, 0x00, 0x01, 0x00, 0x03, 0x00};
        char[] chars = str.toCharArray();
        int length = normal.length + chars.length > 20 ? 20 : normal.length + chars.length;
        byte[] bytes = new byte[length];
        for (int i = 0; i < length - 1; i++) {
            if (i < normal.length - 1) {
                bytes[i] = normal[i];
            } else {
                bytes[i] = (byte) chars[i - (normal.length - 1)];
            }
        }
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetVersion() {
        byte[] bytes = {0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x01, 0x02, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeGetSn() {
        byte[] bytes = {0x23, 0x01, 0x03, 0x31, 0x00, 0x02, 0x01, 0x06, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    @Override
    public byte[] makeChangeBleInterval(int value) {
        byte[] bytes = {0x23, 0x01, 0x04, 0x31, 0x00, 0x04, 0x00, 0x05, 0x00, 0x00};
        bytes[2] = (byte) makeProtocolLength(bytes);
        bytes[4] = (byte) mTag;
        bytes[8] = (byte) value;
        bytes[bytes.length - 1] = makeProtocolCheck(bytes);
        return bytes;
    }

    public static byte[] concatAll(byte[][] bytes) {
        int totalLength = 0;
        for (int i = 0; i < bytes.length; i++) {
            totalLength += bytes[i].length;
        }
        byte[] result = new byte[totalLength];
        int offset = 0;
        for (int i = 0; i < bytes.length; i++) {
            byte[] partByte = bytes[i];
            System.arraycopy(partByte, 0, result, offset, partByte.length);
            offset += partByte.length;
        }
        return result;
    }

}
