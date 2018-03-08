package com.example.bo.nixon.bean;

/**
 * @author ARZE
 * @version 创建时间：2017/7/11 18:14
 * @说明
 */
public class DbWatchBean {

    private long time;

    private int value;

    private String deivceMac;

    private String userId;
    /**
     * 区别 运动，睡眠 ====
     */
    private int style;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDeivceMac() {
        return deivceMac;
    }

    public void setDeivceMac(String deivceMac) {
        this.deivceMac = deivceMac;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
