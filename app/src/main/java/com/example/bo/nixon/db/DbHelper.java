package com.example.bo.nixon.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author ARZE
 * @version 创建时间：2017/7/11 20:25
 * @说明
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB = "NIXION";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB, null, VERSION);
    }

    public DbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WatchHelper.WATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
