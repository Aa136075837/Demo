package com.smart.smartble.client.actionFactory;

import com.smart.smartble.event.Action;
import com.smart.smartble.event.SmartAction;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/3/8 14:40
 * @说明
 */
public interface IDetailAction {

    /**
     * 回复普通的ack
     *
     * @param action 事件
     * @param flow   流控
     * @param status 命令状态  1为异常 0 为成功
     * @return
     */
    SmartAction buildNormalAck(Action action, int flow, int status);

    /**
     * 授权命令
     *
     * @param action 事件
     * @param b      true 为普通授权（需要手表回复），false为强制授权
     * @return
     */

    SmartAction buildAuthorization(Action action, boolean b);


    /**
     * 拍照
     *
     * @param action 事件
     * @param b      true 为进入拍照，false为退出拍照
     * @return
     */
    SmartAction buildCamera(Action action, boolean b);

    /**
     * 拍照心跳协议，进入拍照后需要发送心跳协议
     *
     * @param action 事件
     * @return
     */
    SmartAction buildHeartCamera(Action action);


    SmartAction buildSportTarget(Action action, int target);


    /**
     * 消息通知设置
     *
     * @param action   事件
     * @param position 设置通知消息开关值
     * @return
     */
    SmartAction buildNotifySetting(Action action, int position);

    /**
     * 通知事件提醒
     *
     * @param action   事件
     * @param position 对应通知事件值
     * @return
     */
    SmartAction buildNotify(Action action, int position);

    /**
     * 取消通知事件提醒
     *
     * @param action   事件
     * @param position 对应通知事件值
     * @return
     */
    SmartAction buildCancelNotify(Action action, int position);

    SmartAction buildDisconnectNotify(Action action, boolean notify);

    SmartAction buildGetDisconnectNotifySetting(Action action);
    
    SmartAction buildAlarmSetting(Action action);


    /**
     * 校时
     *
     * @param action 事件
     * @param b      true 为进入校时 false为退出校时
     * @return
     */
    SmartAction buildTime(Action action, boolean b);


    /**
     * 校时心跳协议
     *
     * @param action 事件
     * @return
     */
    SmartAction buildHearTime(Action action);

    /**
     * 发送指针位置指令
     *
     * @param action   事件
     * @param position 对应表盘
     * @param value    指针位置值
     * @return
     */
    SmartAction buildPositionTime(Action action, int position, int value);

    /**
     * 发送MCU时间
     *
     * @param action   事件
     * @param position 对应表盘
     * @param date     mcu时间
     * @return
     */
    SmartAction buildMcuTime(Action action, int position, Date date);

    SmartAction buildAskTime(Action action, int position,Date date);

    /**
     * 发送第二时区
     *
     * @param action 事件
     * @param date   时间
     * @param arg1   第二时区的小时
     * @param arg2   第二时区的分钟 用小数表示（0 ~ 99）
     * @return
     */
    SmartAction buildSecondTime(Action action, Date date, int arg1, int arg2);

    SmartAction buildGetSecondCity(Action action);

    /**
     * 开始ota协议
     *
     * @param action 事件
     * @param test   测试版本号
     * @param minor  副版本号
     * @param main   主版本号
     * @return
     */
    SmartAction buildStartOta(Action action, int test, int minor, int main);


    /**
     * 回复文件信息
     *
     * @param action    事件
     * @param flag      流控
     * @param id        bin文件id
     * @param main      主版本号
     * @param minor     副版本号
     * @param test      测试版本号
     * @param fileCount 子文件数量
     * @param dataCount 包数 （16字节为一包）
     * @param index
     * @return
     */
    SmartAction buildAskFile(Action action, int flag, int id, int main, int minor, int test, int fileCount, int dataCount, int index);

    /**
     * mcu请求文件数据时回复
     * @param action  事件
     * @param flag    流控
     * @param id      bin文件id
     * @param status  状态（0 为请求成功，1为id不存在，2为无效的镜像序号）
     * @return
     */
    SmartAction buildAskFileStatus(Action action, int flag, int id, int status);

    /**
     * 发送文件数据
     * @param action 序号
     * @param index  数据索引
     * @param bytes  文件数据，index为开始的数据索引
     * @return
     */
    SmartAction buildAskFileData(Action action, int index, byte[][] bytes);

    /**
     * ota结束
     * @param action  事件
     * @param flag    流控
     * @param status  状态
     * @return
     */
    SmartAction buildOtaEnd(Action action, int flag, int status);

    /**
     *
     * @param action  事件
     * @param main    主版本号
     * @param minor   副版本号
     * @param test    测试版本号
     * @return
     */
    SmartAction buildUpdateMcu(Action action, int main, int minor, int test);

    /**
     * 更新手表数据
     * @param action 事件
     * @return
     */
    SmartAction buildUpdateData(Action action);

    /**
     * 设置手表运动目标
     * @param action 事件
     * @param target 目标值
     * @return
     */
    SmartAction buildSetTarget(Action action, int target);

    SmartAction buildGetSportTarget(Action action);

    /**
     * 获取当天总步数
     * @param action  事件
     * @return
     */
    SmartAction buildGetSumStep(Action action);

    /**
     * 通过索引获取具体包数的数据
     * @param action 事件
     * @param index  index 为索引
     * @return
     */
    SmartAction buildGetDataByIndex(Action action, int index);

    /**
     * 获取目录
     * @param action 事件
     * @param style  style 为数据类型
     * @return
     */
    SmartAction buildGetDataContent(Action action, int style);

    /**
     * 获取目录信息
     * @param action 事件
     * @param style 为数据类型
     * @param index 目录索引
     * @return
     */
    SmartAction buildContentMessage(Action action, int style, int index);

    /**
     * 获取目录下的数据
     * @param action 事件
     * @param style 为数据类型
     * @param mtc mtc时间
     * @param index 数据索引
     * @return
     */
    SmartAction buildContentIndexData(Action action, int style, int mtc, int index);

    SmartAction buildDeleteDataByStyle(Action action, int style);

    SmartAction buildDeleteContentByUtc(Action action,int style,int utc);
    /**
     * 进入小表盘
     * @param action   事件
     * @param position
     * @param small
     * @return
     */
    SmartAction buildIntoSmall(Action action, int position, int small);

    /**
     * 设置闹钟
     * @param action
     * @param index
     * @param time
     * @param open
     * @param week
     * @return
     */
    SmartAction buildSetAlarm(Action action, int index, int time, int open, int week);

    /**
     * 获取闹钟
     * @param action
     * @param index
     * @return
     */
    SmartAction buildGetAlarm(Action action, int index);

    /**
     * attrs
     */

    SmartAction buildGetElectricity(Action action);

    SmartAction buildChangeBle(Action action, String str);

    SmartAction buildGetVersion(Action action);

    SmartAction buildGetSn(Action action);

    /**
     * 蓝牙
     */


    /**
     * 修改蓝牙间隔
     * @param action
     * @param value
     * @return
     */
    SmartAction buildChangeBleInterval(Action action, int value);


}
