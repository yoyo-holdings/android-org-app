package com.dt.myapplication.model;

import android.database.Cursor;
import android.text.TextUtils;
import com.dt.myapplication.data.table.NotesTable;

/**
 * Created by DT on 26/05/2016.
 */
public class Note {
    private String id;
    private String title;
    private String text;

    public Note(String id, String title, String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Note(Cursor noteCursor) {
        this.id = noteCursor.getString(noteCursor.getColumnIndex(NotesTable.ID));
        this.title = noteCursor.getString(noteCursor.getColumnIndex(NotesTable.TITLE));
        this.text = noteCursor.getString(noteCursor.getColumnIndex(NotesTable.TEXT));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        if(TextUtils.isEmpty(title)) {
            return "";
        } else {
            return title;
        }
    }

    public void setTitle(String title) {
        this.title = title;
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
}
