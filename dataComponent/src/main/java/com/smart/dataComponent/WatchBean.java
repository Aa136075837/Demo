package com.smart.dataComponent;

/**
 * @author ARZE
 * @version 创建时间：2017/6/29 11:22
 * @说明
 */
public class WatchBean {

    private int mValue;

    private DataStyle mDataStyle;

    private long mTime;

    public WatchBean(int mValue) {
        this.mValue = mValue;
    }

    public WatchBean() {

    }

    public DataStyle getmDataStyle() {
        return mDataStyle;
    }

    public void setmDataStyle(DataStyle mDataStyle) {
        this.mDataStyle = mDataStyle;
    }

    public long getmTime() {
        return mTime;
    }

    public void setmTime(long mTime) {
        this.mTime = mTime;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
    }

    @Override
    public String toString() {
        return "WatchBean{" +
                "mValue=" + mValue +
                ", mDataStyle=" + mDataStyle +
                ", mTime=" + mTime +
                '}';
    }
}
