package com.smart.smartble.event;

import java.util.Queue;

/**
 * @author ARZE
 * @version 创建时间：2017/5/26 18:17
 * @说明   蓝牙数据兼容不同协议版本
 *
 */
public interface ISmartActionOperator {

    /**
     * 判断返回的协议具体功能
     *
     * @param bytes
     * @return
     */
    SmartAction createSmartAction(byte[] bytes,String uuid);

    /**
     * 判断返回数据和队列里的流控是否相同
     *
     * @param dataPool
     * @param bytes
     * @return
     */
    boolean isSameInstruction(Queue<SmartAction> dataPool, byte[] bytes);

    /**
     * 判断是否为普通的ack
     *
     * @param bytes
     * @return
     */

    boolean isNormalAck(byte[] bytes);

    /**
     * 对byte[]数据的信息重新赋值，
     * 例如校时的信息防止在队列里排队时导致时间误差，需要重新获取时间
     *
     * @param smartAction
     * @return
     */

    SmartAction reBuildSmartAction(SmartAction smartAction);

    /**
     * 判断为ota数据
     * @param bytes
     * @param uuid
     * @return
     */
    boolean isOTAData(byte[] bytes, String uuid);


    /**
     * 判断手表数据
     * @param bytes
     * @param uuid
     * @return
     */
    boolean isUserData(byte[] bytes,String uuid);

}
