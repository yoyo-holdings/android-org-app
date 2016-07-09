package com.kiiro.yoyo.androidorgapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class OrgAppDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "orgapp.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String BOOLEAN_TYPE = " BOOLEAN";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_NOTE_ENTRIES =
            "CREATE TABLE " + NotesContract.Notes.TABLE_NAME + " (" +
                    NotesContract.Notes._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NotesContract.Notes.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    NotesContract.Notes.COLUMN_NAME_DATE + DATETIME_TYPE + " )";

    private static final String SQL_CREATE_TODO_ENTRIES =
            "CREATE TABLE " + TodosContract.Todos.TABLE_NAME + " (" +
                    TodosContract.Todos._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    TodosContract.Todos.COLUMN_NAME_TASK + TEXT_TYPE + COMMA_SEP +
                    TodosContract.Todos.COLUMN_NAME_DONE + BOOLEAN_TYPE + COMMA_SEP +
                    TodosContract.Todos.COLUMN_NAME_DATE + DATETIME_TYPE + " )";

    private static final String SQL_DELETE_NOTE_ENTRIES =
            "DROP TABLE IF EXISTS " + NotesContract.Notes.TABLE_NAME;

    private static final String SQL_DELETE_TODO_ENTRIES =
            "DROP TABLE IF EXISTS " + TodosContract.Todos.TABLE_NAME;

    private static final String SORT_DATETTIME_DESC = "datetime(" + NotesContract.Notes.COLUMN_NAME_DATE + ") DESC";
    private static final String SORT_DATETTIME_ASC = "datetime(" + NotesContract.Notes.COLUMN_NAME_DATE + ") ASC";
    private static String sortOrder = SORT_DATETTIME_DESC;

    public OrgAppDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_NOTE_ENTRIES);
        db.execSQL(SQL_CREATE_TODO_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_NOTE_ENTRIES);
        db.execSQL(SQL_DELETE_TODO_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void toggleSortOrder() {
        if (sortOrder == SORT_DATETTIME_DESC) {
            sortOrder = SORT_DATETTIME_ASC;
        } else {
            sortOrder = SORT_DATETTIME_DESC;
        }
    }
}
