package com.kiiro.yoyo.androidorgapp.db;

import android.provider.BaseColumns;

public class TodosContract {

    public TodosContract() {
    }

    public static abstract class Todos implements BaseColumns {
        public static final String TABLE_NAME = "todo";
        public static final String COLUMN_NAME_TASK = "task";
        public static final String COLUMN_NAME_DATE = "date";
        public static final String COLUMN_NAME_DONE = "done";
    }
}
