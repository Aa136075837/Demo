package com.smart.dataComponent.data;

import com.smart.dataComponent.WatchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/9 13:53
 * @说明
 */
public class ContentData {

    private int mIndex;
    private byte[] bytes;
    private int mStyle;

    public int getmIndex() {
        return mIndex;
    }

    public ContentData setmIndex(int mIndex) {
        this.mIndex = mIndex;
        return this;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public ContentData setBytes(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    public int getmDataStyle() {
        return mStyle;
    }

    public ContentData setmDataStyle(int mDataStyle) {
        this.mStyle = mDataStyle;
        return this;
    }

    public List<WatchBean> createWatchBeen(int length, int count) {
        List<WatchBean> list = new ArrayList<>();
        if (null != bytes && 20 == bytes.length) {
            int lessCount = 20;
            if (count - 1 == mIndex && 0 != (length % 16)) {
                lessCount = (length % 16) + 4;
            }
            for (int i = 4; i < lessCount; i += 2) {
                int high = (bytes[i] & 0xff);
                int low = (bytes[i + 1] & 0xff);
                int value = (high << 8) + low;
                WatchBean watchBean = new WatchBean(value);
                list.add(watchBean);
            }
        }
        return list;
    }
}
