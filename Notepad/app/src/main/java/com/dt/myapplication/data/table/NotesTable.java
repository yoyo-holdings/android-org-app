package com.dt.myapplication.data.table;

/**
 * Created by DT on 26/05/2016.
 */
public class NotesTable {
    public static final String TABLE_NAME = "notes";
    public static final String ID = "_id";
    public static final String TITLE = "title";
    public static final String TEXT = "text";

    public static final String CREATE = "CREATE TABLE "
        + TABLE_NAME
        + "("
        + ID
        + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + TITLE
        + " TEXT, "
        + TEXT
        + " TEXT"
        + ")";
}
