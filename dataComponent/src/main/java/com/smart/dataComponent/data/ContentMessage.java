package com.smart.dataComponent.data;

import android.util.Log;

import com.smart.dataComponent.DataStyle;
import com.smart.dataComponent.WatchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/8 20:57
 * @说明
 */
public class ContentMessage {

    public static final int PER_COUNT = 16;
    private static final int INTERVAL_TIME = 28800;
    private int mDataLength;
    private int mUtc;
    private int mCount;
    private int mIndex;
    private int mPerTime;
    private int mMtu;
    private List<ContentData> mContentDatas = new ArrayList<>();
    private boolean isDelete = false;

    public int getmDataLength() {
        return mDataLength;
    }

    public ContentMessage setmDataLength(int mDataLength) {
        this.mDataLength = mDataLength;
        return this;
    }

    public int getmUtc() {
        return mUtc;
    }

    public ContentMessage setmUtc(int mUtc) {
        this.mUtc = mUtc;
        return this;
    }

    public int getmCount() {
        return mCount;
    }

    public ContentMessage setmCount(int mCount) {
        this.mCount = mCount;
        return this;
    }

    public int getmIndex() {
        return mIndex;
    }

    public ContentMessage setmIndex(int mIndex) {
        this.mIndex = mIndex;
        return this;
    }

    public int getmPerTime() {
        return mPerTime;
    }

    public ContentMessage setmPerTime(int mPerTime) {
        this.mPerTime = mPerTime;
        return this;
    }

    public int getmMtu() {
        return mMtu;
    }

    public ContentMessage setmMtu(int mMtu) {
        this.mMtu = mMtu;
        return this;
    }

    public void addContentData(ContentData contentData) {
        mContentDatas.add(contentData);
    }

    public boolean isComplete() {
        return mContentDatas.size() == mCount;
    }

    public int getCurrentIndex() {
        return mContentDatas.size();
    }

    public boolean isVailContentData(ContentData contentData) {
        return contentData.getmIndex() == mContentDatas.size();
    }

    public boolean isLostContentData(ContentData contentData) {
        return contentData.getmIndex() > mContentDatas.size();
    }


    public List<WatchBean> createWatchBean(int dataStyle) {
        List<WatchBean> list = new ArrayList<>();
        for (ContentData contentData : mContentDatas) {
            list.addAll(contentData.createWatchBeen(mDataLength,mCount));
        }
        Log.w("DataComponent", " createWatchBean::   " + mContentDatas.size());
       return setAttribute(list,dataStyle);
    }

    private List<WatchBean> setAttribute(List<WatchBean> list, int dataStyle) {
        for (int i = 0; i < list.size(); i++) {
            WatchBean watchBean = list.get(i);
            long time = mUtc - INTERVAL_TIME + i * 300;
            watchBean.setmTime(time * 1000);
            DataStyle style = dataStyle == 0 ? DataStyle.STEP : DataStyle.SLEEP;
            watchBean.setmDataStyle(style);
        }
        return list;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }
}
