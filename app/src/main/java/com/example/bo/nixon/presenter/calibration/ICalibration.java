package com.example.bo.nixon.presenter.calibration;

import java.util.Date;

/**
 * @author ARZE
 * @version 创建时间：2017/6/8 18:42
 * @说明
 */
public interface ICalibration {

    void inCalibration();

    void outCalibration();

    void sendTime(int position, int value, Date date);

    void intoSmall(int position,int value);
}
