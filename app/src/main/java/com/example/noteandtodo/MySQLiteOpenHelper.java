package com.example.noteandtodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created on 2016/04/09.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
//    public static final Uri CONTENT_URI = Uri.parse("content://com.example.notoandtodo/note");
    private static final String DB_NAME = "noteandtodo.db";
    private static final int DB_VERSION = 7;
    public static final String TABLE_NOTE  = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    public static final String TABLE_TODO = "todo";
    public static final String COLUMN_ENTRY = "entry";
    public static final String COLUMN_DONE = "done";


    private static final String CREATE_TABLE_NOTE =
            "create table " + TABLE_NOTE + " "
            + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_TITLE + " text not null,"
            + COLUMN_TEXT + " text not null,"
            + COLUMN_DATE + " text not null)";

    private static final String CREATE_TABLE_TODO =
            "create table " + TABLE_TODO +  " "
            + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_ENTRY + " text not null,"
            + COLUMN_DONE + " integer not null,"
            + COLUMN_DATE + " text not null)";

/* doesn't work
    private static final String DROP_TABLE =
            "drop table " + TABLE_NOTE + "," + TABLE_TODO;
*/
    private static final String DROP_TABLE_NOTE =
            "drop table if exists " + TABLE_NOTE;
    private static final String DROP_TABLE_TODO =
            "drop table if exists  " + TABLE_TODO;

    public MySQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_NOTE);
        db.execSQL(CREATE_TABLE_TODO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_NOTE);
        db.execSQL(DROP_TABLE_TODO);
        onCreate(db);
    }

}
