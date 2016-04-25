package com.yoyotest.h2owl.h2wltestapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by h2owl on 16/04/25.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "my_note.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "notes";
    private static final String CREATE_TABLE_NOTE =
            "CREATE TABLE "+TABLE_NAME+" (" +
                "id integer unsigned primary key autoincrement," +
                "date integer unsigned default 0,"+
                "group integer unsigned default 0,"+
                "type integer unsigned default 0,"+
                "title text unsigned not null,"+
                "content text unsigned default null"+
            ")";

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
