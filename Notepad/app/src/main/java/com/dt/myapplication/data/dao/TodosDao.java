package com.dt.myapplication.data.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.content.CursorLoader;
import android.text.TextUtils;
import com.dt.myapplication.data.provider.TodosProvider;
import com.dt.myapplication.data.table.TodosTable;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 26/05/2016.
 */
public class TodosDao {

    public static ContentValues buildContentValues(Todo todo) {
        ContentValues values = new ContentValues();
        if (todo.getText() != null) {
            values.put(TodosTable.TEXT, todo.getText());
        }
        values.put(TodosTable.IS_DONE, todo.isDone());
        return values;
    }

    public static ContentValues buildContentValues(String text, String isDone) {
        ContentValues values = new ContentValues();
        if (!TextUtils.isEmpty(text)) {
            values.put(TodosTable.TEXT, text);
        }
        values.put(TodosTable.IS_DONE, isDone);
        return values;
    }

    public static Uri insertTodoEntry(Context context, ContentValues values)
        throws NullPointerException {
        return context.getContentResolver().insert(TodosProvider.CONTENT_URI, values);
    }

    public static Cursor queryAllTodos(Context context, String jid) throws NullPointerException {
        return context.getContentResolver()
            .query(TodosProvider.CONTENT_URI, null, null, null, TodosTable.ID + " DESC");
    }

    public static int updateTodoById(Context context, String id, ContentValues values) {
        return context.getContentResolver()
            .update(TodosProvider.CONTENT_URI, values, TodosTable.ID + " = ?", new String[] { id });
    }

    public static int deleteTodoById(Context context, String id) throws NullPointerException {
        return context.getContentResolver()
            .delete(TodosProvider.CONTENT_URI, TodosTable.ID + " = ?", new String[] { id });
    }

    public static CursorLoader getTodosCursorLoader(Activity activity) {
        return new CursorLoader(activity, TodosProvider.CONTENT_URI, null, null, null,
            TodosTable.ID + " DESC");
    }
}
