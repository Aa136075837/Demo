package com.smart.notifycomponent;

/**
 * @author ARZE
 * @version 创建时间：2017/6/13 18:09
 * @说明
 */
public interface INotify {

    void setNotifySetting(int value);

    void setNotify(int value);

    void setDisconnectNotify(boolean notify);

    void getDisconnectNotifySetting();

    void cancelNotify(int value);

}
