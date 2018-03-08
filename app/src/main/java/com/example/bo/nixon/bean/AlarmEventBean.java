package com.example.bo.nixon.bean;

public class AlarmEventBean {
    private int alarmTime;// 闹钟时间
    private boolean openType;// 开关状态
    private boolean repeat;// 是否重复
    private int repeatTime;// 重复的时间
    private String mAction;

    private int index;
    private boolean isSelect = false;
    private int id;

    @SuppressWarnings ("unused") private AlarmEventBean () {
    }

    public AlarmEventBean (int alarmTime, boolean openType, boolean repeat, int repeatTime, String action) {
        this.alarmTime = alarmTime;
        this.openType = openType;
        this.repeat = repeat;
        this.repeatTime = repeatTime;
        mAction = action;
    }
    public AlarmEventBean (int alarmTime, boolean openType, boolean repeat, int repeatTime, String action,int id) {
        this.alarmTime = alarmTime;
        this.openType = openType;
        this.repeat = repeat;
        this.repeatTime = repeatTime;
        this.id = id;
        mAction = action;
    }

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public int getAlarmTime () {
        return alarmTime;
    }

    public void setAlarmTime (int alarmTime) {
        this.alarmTime = alarmTime;
    }

    public boolean isOpenType () {
        return openType;
    }

    public void setOpenType (boolean openType) {
        this.openType = openType;
    }

    public boolean isRepeat () {
        return repeat;
    }

    public void setRepeat (boolean repeat) {
        this.repeat = repeat;
    }

    public int getRepeatTime () {
        return repeatTime;
    }

    public void setRepeatTime (int repeatTime) {
        this.repeatTime = repeatTime;
    }

    public String getmAction () {
        return mAction;
    }

    public void setmAction (String mAction) {
        this.mAction = mAction;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "AlarmEventBean{" +
                "alarmTime=" + alarmTime +
                ", openType=" + openType +
                ", repeat=" + repeat +
                ", repeatTime=" + repeatTime +
                ", mAction='" + mAction + '\'' +
                ", index=" + index +
                ", isSelect=" + isSelect +
                ", id=" + id +
                '}';
    }
}