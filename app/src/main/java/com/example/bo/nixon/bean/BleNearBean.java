package com.example.bo.nixon.bean;

/**
 * @author bo.
 * @Date 2017/6/10.
 * @desc
 */

public class BleNearBean {
    private String name;
    private int waft;

    public BleNearBean (String name, int waft) {
        this.name = name;
        this.waft = waft;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public int getWaft () {
        return waft;
    }

    public void setWaft (int waft) {
        this.waft = waft;
    }
}
