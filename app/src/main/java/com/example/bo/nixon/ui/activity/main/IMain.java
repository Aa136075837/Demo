package com.example.bo.nixon.ui.activity.main;

/**
 * @author ARZE
 * @version 创建时间：2017/6/20 16:23
 * @说明
 */
public interface IMain {

    void getElectricity();

    void onResume();

    void onPause();

    void setTargetValue(int target);

    void getSecondTime();

    void uploadAllData();
}
