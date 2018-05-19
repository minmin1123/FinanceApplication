package com.application.example.financeapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *   author:minmin
 *   email:775846180@qq.com
 *   time:2017/10/11
 *   desc:数据库的帮助类
 *   version:1.0
 */

public class AccountDatabaseHelper extends SQLiteOpenHelper{

    private final static String CREATE_ACCOUNT = "create table Account(id text," +
            "year text," +
            "month text," +
            "day text," +
            "type integer," +
            "money text," +
            "name text)";

    private final static String CREATE_ITEM = "create table Item(name text," +
            "type integer)";

    public AccountDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_ACCOUNT);
        sqLiteDatabase.execSQL(CREATE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
