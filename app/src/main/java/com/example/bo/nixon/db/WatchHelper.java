package com.example.bo.nixon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import com.example.bo.nixon.bean.DbWatchBean;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ARZE
 * @version 创建时间：2017/7/11 20:39
 * @说明
 */
public class WatchHelper {

    DbHelper mDbHelper;

    private static final String WATCH_NAME = "WATCH_NAME";
    private static final String TIME = "TIME";
    private static final String VALUE = "VALUE";
    private static final String DEVICE_MAC = "DEVICE_MAC";
    private static final String USER_ID = "USER_ID";
    private static final String STYLE = "STYLE";
    public static final String WATCH_TABLE = "create table " + WATCH_NAME + "("
            + TIME + " long ,"
            + VALUE + " int ,"
            + DEVICE_MAC + " varchar(30) ,"
            + USER_ID + " varchar(30) ,"
            + STYLE + " int )";

    public WatchHelper(Context context) {
        mDbHelper = new DbHelper(context);
    }

    public void insert(@NonNull List<DbWatchBean> watchBeen) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        DbWatchBean dbWatchBean = watchBeen.get (0);
        if (0 < watchBeen.size()) {
            dbWatchBean = getMaxDbWatchBean(dbWatchBean.getUserId(), dbWatchBean.getDeivceMac());
        }
        for (int i = 0; i < watchBeen.size(); i++) {
            DbWatchBean insertWatchBean = watchBeen.get(i);
            if (insertWatchBean.getTime() > dbWatchBean.getTime())
                insertDbWatchBean(db, watchBeen.get(i));
        }
        db.close();
    }

    private DbWatchBean getMaxDbWatchBean(String userId, String deviceMac) {
        DbWatchBean dbWatchBean = new DbWatchBean();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(WATCH_NAME, null, USER_ID + " =?  and " + DEVICE_MAC + " =? ",
                new String[]{userId, deviceMac}, null, null,TIME + " asc " , String.valueOf(1));
        if (null == cursor)
            return dbWatchBean;
        if (cursor.moveToNext()) {
            dbWatchBean.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
            dbWatchBean.setValue(cursor.getInt(cursor.getColumnIndex(VALUE)));
            dbWatchBean.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
            dbWatchBean.setDeivceMac(cursor.getString(cursor.getColumnIndex(DEVICE_MAC)));
            dbWatchBean.setStyle(cursor.getInt(cursor.getColumnIndex(STYLE)));
        }
        return dbWatchBean;
    }

    private void insertDbWatchBean(SQLiteDatabase db, DbWatchBean dbWatchBean) {
        try {
            ContentValues values = new ContentValues();
            values.put(TIME, dbWatchBean.getTime());
            values.put(VALUE, dbWatchBean.getValue());
            values.put(DEVICE_MAC, dbWatchBean.getDeivceMac());
            values.put(USER_ID, dbWatchBean.getUserId());
            values.put(STYLE, dbWatchBean.getStyle());
            db.insert(WATCH_NAME, null, values);
        } catch (SQLException s) {
            s.printStackTrace();
        }
    }

    public List<DbWatchBean> getDbWatchBean(String userId, String deviceMac) {
        List<DbWatchBean> watchBeen = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query(WATCH_NAME, null, USER_ID + " =?  and " + DEVICE_MAC + " =? ", new String[]{userId, deviceMac}, null, null, TIME + " asc");
            if (null == cursor)
                return watchBeen;
            while (cursor.moveToNext()) {
                DbWatchBean watchBean = new DbWatchBean();
                watchBean.setTime(cursor.getLong(cursor.getColumnIndex(TIME)));
                watchBean.setValue(cursor.getInt(cursor.getColumnIndex(VALUE)));
                watchBean.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                watchBean.setDeivceMac(cursor.getString(cursor.getColumnIndex(DEVICE_MAC)));
                watchBean.setStyle(cursor.getInt(cursor.getColumnIndex(STYLE)));
                watchBeen.add(watchBean);
            }
            cursor.close();
        } catch (SQLException s) {
            s.printStackTrace();
        }
        db.close();
        return watchBeen;
    }

    public int deleteDbWatchBean(String userId, String deviceMac) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int successful = db.delete(WATCH_NAME, USER_ID + " =?  and " + DEVICE_MAC + " =? ", new String[]{userId, deviceMac});
        return successful;
    }


}
