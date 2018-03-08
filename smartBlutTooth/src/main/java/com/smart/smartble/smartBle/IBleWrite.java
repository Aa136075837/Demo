package com.smart.smartble.smartBle;

import com.smart.smartble.event.SmartAction;

import java.util.Queue;

/**
 * @author ARZE
 * @version 创建时间：2016/12/15 10:34
 * @说明 蓝牙写入数据
 */
public interface IBleWrite {

    void write(Queue<SmartAction> dataPool);

    void writeSuccessfully(Queue<SmartAction> dataPool, byte[] bytes,String uuid);

    void writeReply(Queue<SmartAction> dataPool, byte[] bytes,String uuid);

    void writeError(Queue<SmartAction> dataPool);
}
