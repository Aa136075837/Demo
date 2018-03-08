package com.example.bo.nixon.ui.view.smartGraph;

import android.graphics.RectF;

import com.example.bo.nixon.bean.NetSportStepBean;

/**
 * @author ARZE
 * @version 创建时间：2017/6/5 17:23
 * @说明
 */
public class SmartPoint {

    private float mPointX;
    private float mPointY;
    private int mValue;

    private float mPointWidth = 5f;

    private int mPointColor = 0xFFFFFF;
    private RectF mPointLine;
    private String mDate;

    private NetSportStepBean.ObjectBean mObjectBean;

    private SmartPoint(int value, float mPointWidth, int mPointColor, String date) {
        mValue = value;
        this.mPointWidth = mPointWidth;
        this.mPointColor = mPointColor;
        this.mDate = date;
    }

    public float getmPointWidth() {
        return mPointWidth;
    }

    public void setmPointWidth(float mPointWidth) {
        this.mPointWidth = mPointWidth;
    }

    public float getmPointY() {
        return mPointY;
    }

    public void setmPointY(float mPointY) {
        this.mPointY = mPointY;
    }

    public float getmPointX() {
        return mPointX;
    }

    public void setmPointX(float mPointX) {
        this.mPointX = mPointX;
    }

    public int getmPointColor() {
        return mPointColor;
    }

    public void setmPointColor(int mPointColor) {
        this.mPointColor = mPointColor;
    }

    public RectF getmPointLine() {
        return mPointLine;
    }

    public void setmPointLine(RectF mPointLine) {
        this.mPointLine = mPointLine;
    }

    public int getmValue() {
        return mValue;
    }

    public void setmValue(int mValue) {
        this.mValue = mValue;
    }

    public String getmDate() {
        return mDate;
    }

    public void setmDate(String mDate) {
        this.mDate = mDate;
    }

    public static class Builder {
        int value;
        float pointWidth = 5f;
        int pointColor = 0xfffff;
        String date = "6.17";

        public Builder value(int v) {
            value = v;
            return this;
        }

        public Builder width(float w) {
            pointWidth = w;
            return this;
        }

        public Builder color(int color) {
            pointColor = color;
            return this;
        }

        public Builder date(String date) {
            this.date = date;
            return this;
        }

        public SmartPoint build() {
            return new SmartPoint(value, pointWidth, pointColor, date);
        }

    }

    public NetSportStepBean.ObjectBean getmObjectBean() {
        return mObjectBean;
    }

    public void setmObjectBean(NetSportStepBean.ObjectBean mObjectBean) {
        this.mObjectBean = mObjectBean;
    }

    @Override
    public String toString() {
        return "SmartPoint{" +
                "mPointX=" + mPointX +
                ", mPointY=" + mPointY +
                ", mValue=" + mValue +
                ", mPointWidth=" + mPointWidth +
                ", mPointColor=" + mPointColor +
                ", mPointLine=" + mPointLine +
                ", mDate='" + mDate + '\'' +
                ", mObjectBean=" + mObjectBean +
                '}';
    }
}
