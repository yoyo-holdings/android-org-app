package com.dt.myapplication.data.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import com.dt.myapplication.data.provider.NotesProvider;
import com.dt.myapplication.data.table.NotesTable;
import com.dt.myapplication.model.Note;

/**
 * Created by DT on 26/05/2016.
 */
public class NotesDao {

    public static ContentValues buildContentValues(Note note) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(note.getTitle())) {
            values.put(NotesTable.TITLE, note.getTitle());
        }
        if (!TextUtils.isEmpty(note.getText())) {
            values.put(NotesTable.TEXT, note.getText());
        }
        return values;
    }

    public static ContentValues buildContentValues(String title, String text) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(title)) {
            values.put(NotesTable.TITLE, title);
        }
        if (!TextUtils.isEmpty(text)) {
            values.put(NotesTable.TEXT, text);
        }
        return values;
    }

    public static Uri insertNoteEntry(Context context, ContentValues values)
        throws NullPointerException {
        return context.getContentResolver().insert(NotesProvider.CONTENT_URI, values);
    }

    public static Cursor queryAllNotes(Context context, String jid) throws NullPointerException {
        return context.getContentResolver()
            .query(NotesProvider.CONTENT_URI, null, null, null, NotesTable.ID + " DESC");
    }

    public static int updateNoteById(Context context, String id, ContentValues values) {
        return context.getContentResolver()
            .update(NotesProvider.CONTENT_URI, values, NotesTable.ID + " = ?", new String[] { id });
    }

    public static int deleteNoteById(Context context, String id) throws NullPointerException {
        return context.getContentResolver()
            .delete(NotesProvider.CONTENT_URI, NotesTable.ID + " = ?", new String[] { id });
    }

    public static CursorLoader getNotesCursorLoader(Activity activity) {
        return new CursorLoader(activity, NotesProvider.CONTENT_URI, null, null, null,
            NotesTable.ID + " DESC");
    }
}
