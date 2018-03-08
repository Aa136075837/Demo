package com.example.bo.nixon.bean;

import java.io.Serializable;

/**
 * @author bo.
 * @Date 2017/6/2.
 * @desc
 */

public class PersonalInfoBean implements Serializable{


    private String unitName;
    private String textLeft;
    private String textRight;
    /**
     * 1.personal页面是否显示switchView
     * 2.unit页面是否是公制
     */
    private boolean isShowSwitch;
    private int tag;
    private boolean isSelect;

    /**
     * 英制，公制 单位设置
     */
    public PersonalInfoBean (String unitName, String textLeft, String textRight, boolean isShowSwitch, int tag) {
        this.unitName = unitName;
        this.textLeft = textLeft;
        this.textRight = textRight;
        this.isShowSwitch = isShowSwitch;
        this.tag = tag;
    }

    public String getUnitName () {
        return unitName;
    }

    public boolean isShowSwitch () {
        return isShowSwitch;
    }

    public void setShowSwitch (boolean showSwitch) {
        isShowSwitch = showSwitch;
    }

    /**
     * 两个textview 时调用
     */
    public PersonalInfoBean (String textLeft, String textRight) {
        this.textLeft = textLeft;
        this.textRight = textRight;
    }

    public PersonalInfoBean (String textLeft, String textRight, int tag) {
        this.textLeft = textLeft;
        this.textRight = textRight;
        this.tag = tag;
    }

    /**
     * 一个textview 一个 switch 时 调用
     */
    public PersonalInfoBean (String textLeft, boolean isShowSwitch) {
        this.textLeft = textLeft;
        this.isShowSwitch = isShowSwitch;
    }

    public PersonalInfoBean (String textLeft, boolean isShowSwitch, int tag) {
        this.textLeft = textLeft;
        this.isShowSwitch = isShowSwitch;
        this.tag = tag;
    }

    /**
     * 只有一个textview 时 调用
     */
    public PersonalInfoBean (String textLeft) {
        this.textLeft = textLeft;
    }

    public PersonalInfoBean (String textLeft, int tag) {
        this.textLeft = textLeft;
        this.tag = tag;
    }

    public void setTextRight (String textRight) {
        this.textRight = textRight;
    }

    public String getTextLeft () {
        return textLeft;
    }

    public String getTextRight () {
        return textRight;
    }

    public int getTag () {
        return tag;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
