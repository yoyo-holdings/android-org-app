package com.example.noteandtodo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

/**
 * Created on 2016/04/09.
 */
public class NoteSQLiteOpenHelper extends SQLiteOpenHelper {
    public static final Uri CONTENT_URI = Uri.parse("content://com.example.notoandtodo/note");
    private static final String DB_NAME = "note.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NOTE  = "note";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_DATE = "date";

    private static final String CREATE_TABLE =
            "create table " + TABLE_NOTE + " "
            + "(" + COLUMN_ID + " integer primary key autoincrement,"
            + COLUMN_SUBJECT + " text not null,"
            + COLUMN_TEXT + " text not null,"
            + COLUMN_DATE + " text not null)";

    private static final String DROP_TABLE = "drop table " + TABLE_NOTE;

    public NoteSQLiteOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

}
