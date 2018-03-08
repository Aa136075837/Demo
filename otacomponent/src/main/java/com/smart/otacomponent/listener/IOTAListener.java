package com.smart.otacomponent.listener;

/**
 * @author ARZE
 * @version 创建时间：2017/6/1 9:53
 * @说明
 */
public interface IOTAListener {

    void dealFile();

    void otaStart();

    void onProgress(int max, int progress);

    void onComplete(int complete);

    void onFail();

}
