package com.giaquino.todo.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.giaquino.todo.model.entity.Checklist;
import com.giaquino.todo.model.entity.Note;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/28/16
 */
public class TodoOpenHelper extends SQLiteOpenHelper {

    public TodoOpenHelper(@NonNull Context context, @NonNull String name,
        @Nullable SQLiteDatabase.CursorFactory factory, @IntRange(from = 1) int version) {
        super(context, name, factory, version);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);
        db.execSQL(Checklist.CREATE_TABLE);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Note.DROP_TABLE);
        db.execSQL(Checklist.DROP_TABLE);
        onCreate(db);
    }
}
