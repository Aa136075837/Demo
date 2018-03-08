package com.example.bo.nixon.ui.view.smartGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/6/5 17:24
 * @说明
 */
public class SmartLine {

    private List<SmartPoint> mList = new ArrayList<>();

    private boolean hasShape = true;

    private float mDistance = 200;

    public List<SmartPoint> getPoints() {
        return mList;
    }

    public void setetPoints(List<SmartPoint> mList) {
        this.mList = mList;
    }

    public boolean isHasShape() {
        return hasShape;
    }

    public void setHasShape(boolean hasShape) {
        this.hasShape = hasShape;
    }


    public float getmDistance() {
        return mDistance;
    }

    public void setmDistance(float mDistance) {
        this.mDistance = mDistance;
    }

    public List<SmartPoint> getmList() {
        return mList;
    }

    public void setmList(List<SmartPoint> mList) {
        this.mList = mList;
    }
}
