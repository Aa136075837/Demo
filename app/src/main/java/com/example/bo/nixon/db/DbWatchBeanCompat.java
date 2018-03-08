package com.example.bo.nixon.db;

import com.example.bo.nixon.bean.DbWatchBean;
import com.smart.dataComponent.DataStyle;
import com.smart.dataComponent.WatchBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/11 21:30
 * @说明
 */
public class DbWatchBeanCompat {

    public static List<DbWatchBean> watchBeenToDbWatchBean(List<WatchBean> list,String userId,String deiviceMac) {

        List<DbWatchBean> dbWatchBeen = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            WatchBean watchBean = list.get(i);
            DbWatchBean dbWatchBean = new DbWatchBean();
            dbWatchBean.setStyle(watchBean.getmDataStyle() == DataStyle.STEP ? 0 : 1);
            dbWatchBean.setValue(watchBean.getmValue());
            dbWatchBean.setTime(watchBean.getmTime());
            dbWatchBean.setUserId(userId);
            dbWatchBean.setDeivceMac(deiviceMac);
            dbWatchBeen.add(dbWatchBean);
        }
        return dbWatchBeen;
    }

}
