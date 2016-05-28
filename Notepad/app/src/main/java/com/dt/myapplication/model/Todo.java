package com.dt.myapplication.model;

import android.database.Cursor;
import android.text.TextUtils;
import com.dt.myapplication.data.table.TodosTable;

/**
 * Created by DT on 26/05/2016.
 */
public class Todo {
    private String id;
    private String text;
    private boolean isDone;

    public Todo(String id, String text, boolean isDone) {
        this.id = id;
        this.text = text;
        this.isDone = isDone;
    }

    public Todo(Cursor todoCursor) {
        this.id = todoCursor.getString(todoCursor.getColumnIndex(TodosTable.ID));
        this.text = todoCursor.getString(todoCursor.getColumnIndex(TodosTable.TEXT));
        this.isDone = Boolean.parseBoolean(
            todoCursor.getString(todoCursor.getColumnIndex(TodosTable.IS_DONE)));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        if(TextUtils.isEmpty(text)) {
            return "";
        } else {
            return text;
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
