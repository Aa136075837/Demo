package com.smart.dataComponent.listener;

import com.smart.dataComponent.WatchBean;

import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/29 11:07
 * @说明
 */
public interface IRequestData {

    void startRequest();

    void requestProgress(int max,int progress);

    void requestComplete(List<WatchBean> watchBeen);

    void requestError(int errorCode,String errorMsg);
}
