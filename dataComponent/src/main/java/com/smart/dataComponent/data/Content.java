package com.smart.dataComponent.data;

import android.util.Log;

import com.smart.dataComponent.WatchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/8 20:55
 * @说明
 */
public class Content {

    private int mDataStyle;
    private int mContentCount;
    private int mSumDataLength;
    private List<ContentMessage> mContentMessages = new ArrayList<>();

    public int getmDataStyle() {
        return mDataStyle;
    }

    public Content setmDataStyle(int mDataStyle) {
        this.mDataStyle = mDataStyle;
        return this;
    }

    public int getmContentCount() {
        return mContentCount;
    }

    public Content setmContentCount(int mContentCount) {
        this.mContentCount = mContentCount;
        return this;
    }

    public void reset() {
        mContentMessages.clear();
    }

    public int getmSumDataLength() {
        return mSumDataLength;
    }

    public Content setmSumDataLength(int mSumDataLength) {
        this.mSumDataLength = mSumDataLength;
        return this;
    }

    public void addContentMessage(ContentMessage contentMessage) {
        mContentMessages.add(contentMessage);
        Log.w("DataComponent", " addContentMessage::   "  + mContentMessages.size());
    }

    public int getmCurrentIndex() {
        return mContentMessages.size();
    }

    public boolean hasContentMessage(ContentMessage contentMessage) {
        for (ContentMessage message : mContentMessages) {
            if (message.getmUtc() == contentMessage.getmUtc())
                return true;
        }
        return false;
    }

    public boolean isComplete() {
        return getmCurrentIndex() == mContentCount;
    }

    public List<ContentMessage> getmContentMessages() {
        return mContentMessages;
    }

    public List<WatchBean> createWatchBean() {
        List<WatchBean> watchBeen = new ArrayList<>();
        for (ContentMessage message : mContentMessages) {
            watchBeen.addAll(message.createWatchBean(mDataStyle));
        }
        return watchBeen;
    }


}
