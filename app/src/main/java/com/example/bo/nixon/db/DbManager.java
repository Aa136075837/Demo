package com.example.bo.nixon.db;

import android.content.Context;

import com.example.bo.nixon.bean.DbWatchBean;

import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/11 20:24
 * @说明
 */
public class DbManager {

    private Context context;

    private static DbManager mDbManager;
    private static WatchHelper mWatchHelper;

    public static DbManager getInstance(Context context) {
        if (null == mDbManager) {
            synchronized (DbManager.class) {
                if (null == mDbManager) {
                    mWatchHelper = new WatchHelper(context);
                    mDbManager = new DbManager();
                }
            }
        }
        return mDbManager;
    }

    private DbManager() {

    }

    public synchronized static void insertDbWatchBean(List<DbWatchBean> watchBeen) {
        mWatchHelper.insert(watchBeen);
    }

    public synchronized static List<DbWatchBean> getDbWatchBean(String userId,String deviceMac) {
        return mWatchHelper.getDbWatchBean(userId,deviceMac);
    }

    public synchronized static int delete(String userId,String deviceMac) {
        return mWatchHelper.deleteDbWatchBean(userId,deviceMac);
    }

}
