package com.dt.myapplication.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.dt.myapplication.data.table.NotesTable;
import com.dt.myapplication.data.table.TodosTable;

/**
 * Created by DT on 26/05/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "notepad";
    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static DatabaseHelper sInstance;
    private Context mContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
    }

    public static DatabaseHelper getInstance(Context context) {
        if (sInstance == null || sInstance.mContext != context) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    @Override public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(NotesTable.CREATE);
        sqLiteDatabase.execSQL(TodosTable.CREATE);
    }

    @Override public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                // for future usage
                break;
            default:
                break;
        }
    }
}
