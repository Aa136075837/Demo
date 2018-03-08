package com.smart.smartble.event;

import java.util.Arrays;

/**
 * @author ARZE
 * @version 创建时间：2016/12/13 16:14
 * @说明
 */
public class SmartAction {

    /**
     * 发送失败时重发上限
     */
    private int MAX = 3;

    /**
     * {@link com.smart.smartble.event.Action}
     *  协议相对应
     */
    private Action mAction;

    private byte[] mValue;
    private String mServiceUUID;

    /**
     * 发送协议通道
     */
    private String mCharacteristicUUID;

    /**
     * 发送次数
     */
    private int mSendCount = 0;

    /**
     * true 需要获取即时value
     */
    private boolean isImmediate = false;

    /**
     * true 远程remote不会回复
     */
    private boolean isNoReply = false;

    /**
     * true 发送时将添加到队列头部
     */
    private boolean isHead = false;

    /**
     * true 获取远程remote回复时需要即时移除
     */
    private boolean isRemove = true;

    /**
     * 用来判断ota
     */
    private boolean isOTA = false;


    private SmartAction(Action action,byte[] value,String service,String characteristic,
                        int sendCount,boolean isImmediate,boolean isNoReply,boolean isHead,
                        boolean isRemove,boolean isOTA,int max) {
        mAction = action;
        mValue = value;
        mServiceUUID = service;
        mCharacteristicUUID = characteristic;
        mSendCount = sendCount;
        this.isImmediate = isImmediate;
        this.isNoReply  = isNoReply;
        this.isHead = isHead;
        this.isRemove = isRemove;
        this.isOTA = isOTA;
        MAX = max;
    }

    public Action getAction() {
        return mAction;
    }

    public void setAction(Action mAction) {
        this.mAction = mAction;
    }

    public byte[] getBytes() {
        return mValue;
    }

    public void setBytes(byte[] bytes) {
        this.mValue = bytes;
    }

    public String getServiceUUID() {
        return mServiceUUID;
    }

    public void setServiceUUID(String serviceUUID) {
        this.mServiceUUID = serviceUUID;
    }

    public String getCharacteristicUUID() {
        return mCharacteristicUUID;
    }

    public void setCharacUUID(String characteristicUUID) {
        this.mCharacteristicUUID = characteristicUUID;
    }

    public int getResendCount() {
        return mSendCount;
    }

    public void setSendCount(int resendCount) {
        this.mSendCount = resendCount;
    }

    public boolean isImmediate() {
        return isImmediate;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }

    public boolean isRemove() {
        return isRemove;
    }

    public void setRemove(boolean remove) {
        isRemove = remove;
    }

    public void setImmediate(boolean immediate) {
        isImmediate = immediate;
    }

    public boolean isInterruption() {
        return mSendCount == MAX;
    }

    public boolean isNoReply() {
        return isNoReply;
    }

    public void setNoReply(boolean noReply) {
        isNoReply = noReply;
    }

    public boolean isOTA() {
        return isOTA;
    }

    public void setOTA(boolean OTA) {
        isOTA = OTA;
    }

    public static class SmartBuilder {

        private Action mAction;
        private byte[] mValue;
        private String mServiceUUID;
        private String mCharacteristicUUID;
        private int mSendCount = 0;
        private int mMax = 3;

        private boolean isImmediate = false;
        private boolean isNoReply = false;
        private boolean isHead = false;
        private boolean isRemove = false;
        private boolean isOTA = false;


        public SmartBuilder() {

        }

        public SmartBuilder action(Action action) {
            mAction = action;
            return this;
        }

        public SmartBuilder value(byte[] value) {
            mValue = value;
            return this;
        }

        public SmartBuilder serviceUUID(String service) {
            mServiceUUID = service;
            return this;
        }

        public SmartBuilder characteristicUUID(String characteristic) {
            mCharacteristicUUID = characteristic;
            return this;
        }

        public SmartBuilder sendCount(int sendCount) {
            mSendCount = sendCount;
            return this;
        }

        public SmartBuilder isImmediate(boolean isImmediate) {
            this.isImmediate = isImmediate;
            return this;
        }

        public SmartBuilder isNoReply(boolean isNoReply) {
            this.isNoReply = isNoReply;
            return this;
        }

        public SmartBuilder isHead(boolean isHead) {
            this.isHead = isHead;
            return this;
        }

        public SmartBuilder isRemove (boolean isRemove) {
            this.isRemove = isRemove;
            return this;
        }

        public SmartBuilder isOTA (boolean isOTA) {
            this.isOTA = isOTA;
            return this;
        }

        public SmartBuilder max (int max) {
            this.mMax = max;
            return this;
        }

        public SmartAction build() {
            return new SmartAction(mAction,mValue,mServiceUUID,mCharacteristicUUID,mSendCount,
                    isImmediate,isNoReply,isHead,isRemove,isOTA,mMax);
        }
    }

    @Override
    public String toString() {
        return "SmartAction{" +
                "MAX=" + MAX +
                ", mAction=" + mAction +
                ", mValue=" + Arrays.toString(mValue) +
                ", mServiceUUID='" + mServiceUUID + '\'' +
                ", mCharacteristicUUID='" + mCharacteristicUUID + '\'' +
                ", mSendCount=" + mSendCount +
                ", isImmediate=" + isImmediate +
                ", isNoReply=" + isNoReply +
                ", isHead=" + isHead +
                ", isRemove=" + isRemove +
                ", isOTA=" + isOTA +
                '}';
    }
}
