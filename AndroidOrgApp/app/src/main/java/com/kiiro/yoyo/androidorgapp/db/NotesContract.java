package com.kiiro.yoyo.androidorgapp.db;

import android.provider.BaseColumns;

public final class NotesContract {

    public NotesContract() {
    }

    public static abstract class Notes implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String COLUMN_NAME_CONTENT = "content";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
