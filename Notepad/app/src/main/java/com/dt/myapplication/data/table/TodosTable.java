package com.dt.myapplication.data.table;

/**
 * Created by DT on 26/05/2016.
 */
public class TodosTable {
    public static final String TABLE_NAME = "todos";
    public static final String ID = "_id";
    public static final String TEXT = "text";
    public static final String IS_DONE = "is_done";

    public static final String CREATE = "CREATE TABLE "
        + TABLE_NAME
        + "("
        + ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + TEXT
        + " TEXT, "
        + IS_DONE
        + " TEXT"
        + ")";
}
