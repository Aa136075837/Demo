package com.example.bo.nixon.ui.activity.connect;

import com.smart.smartble.smartBle.BleDevice;

import java.util.Comparator;

/**
 * @author ARZE
 * @version 创建时间：2016年9月8日 下午4:49:13
 * @说明
 */

public class SortRssi implements Comparator<BleDevice> {

    @Override
    public int compare(BleDevice lhs, BleDevice rhs) {
        if (lhs.getRssi() > rhs.getRssi()) {
            return -1;
        } else if (lhs.getRssi() < rhs.getRssi()) {
            return 1;
        }
        return 0;
    }

}
