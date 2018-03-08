package com.smart.timecomponent;

import com.smart.smartble.event.SmartAction;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/6/8 18:25
 * @说明
 */
public interface ITime {

    void dealAction(SmartAction smartAction);

    void startTime(long time);

    void stopTime();

    void intoSmallClock(int position, int value);

    void setMcuTime(int position, Date date);

    void setPosition(int position, int value);

    void setSecondCity(Date date, int arg1, int arg2);

    void getSecondCity();

}
