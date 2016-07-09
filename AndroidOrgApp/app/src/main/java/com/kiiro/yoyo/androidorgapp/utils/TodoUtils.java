package com.kiiro.yoyo.androidorgapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kiiro.yoyo.androidorgapp.MainActivity;
import com.kiiro.yoyo.androidorgapp.R;
import com.kiiro.yoyo.androidorgapp.db.NotesContract;
import com.kiiro.yoyo.androidorgapp.db.OrgAppContentProvider;
import com.kiiro.yoyo.androidorgapp.db.OrgAppDbHelper;
import com.kiiro.yoyo.androidorgapp.db.TodosContract;
import com.kiiro.yoyo.androidorgapp.fragments.NotesListFragment;
import com.kiiro.yoyo.androidorgapp.fragments.TodosListFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoUtils {

    public static void startAddTodoOperation(Context context, TodosListFragment todoListFragment) {
        AlertDialog d = createAddTodoDialog(context, todoListFragment);
        d.show();
    }

    private static AlertDialog createAddTodoDialog(final Context context, final TodosListFragment todoListFragment) {
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_entry, null);
        final TextView txtTitle = (TextView) contentView.findViewById(R.id.txtHeader);
        txtTitle.setText(R.string.add_task);
        final TextView txtInput = (TextView) contentView.findViewById(R.id.note);

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(contentView).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = txtInput.getText().toString();
                long id = add(context, input);
                if (-1 == id) {
                    Toast.makeText(context, R.string.add_task_failed, Toast.LENGTH_SHORT).show();
                } else {
                    todoListFragment.restartLoader();
                    Toast.makeText(context, R.string.task_added, Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return adb.create();
    }

    public static void startEditNoteOperation(Context context, TodosListFragment todosListFragment, long id, String task) {
        AlertDialog d = createEditTaskDialog(context, todosListFragment, id, task);
        d.show();
    }

    private static AlertDialog createEditTaskDialog(final Context context, final TodosListFragment todosListFragment, final long taskId, String task) {
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_entry, null);
        final TextView txtTitle = (TextView) contentView.findViewById(R.id.txtHeader);
        txtTitle.setText(R.string.edit_task);
        final TextView txtInput = (TextView) contentView.findViewById(R.id.note);
        txtInput.setText(task);

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(contentView).setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = txtInput.getText().toString();
                long id = edit(context, taskId, input);
                if (-1 == id) {
                    Toast.makeText(context, R.string.edit_task_failed, Toast.LENGTH_SHORT).show();
                } else {
                    todosListFragment.restartLoader();
                    Toast.makeText(context, R.string.task_edited, Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNeutralButton(R.string.convert_as_note, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(context, taskId);
                NoteUtils.add(context, txtInput.getText().toString());
                ((MainActivity)todosListFragment.getActivity()).restartLoaders();
            }
        });
        return adb.create();
    }

    public static long add(Context context, String note) {
        ContentValues cv = new ContentValues();
        cv.put(TodosContract.Todos.COLUMN_NAME_TASK, note);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        cv.put(TodosContract.Todos.COLUMN_NAME_DATE, date);
        cv.put(TodosContract.Todos.COLUMN_NAME_DONE, 0);

        Uri url = context.getContentResolver().insert(OrgAppContentProvider.TODOS_URI, cv);
        return Long.parseLong(url.getLastPathSegment());
    }

    public static int edit(Context context, long id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TodosContract.Todos.COLUMN_NAME_TASK, task);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        cv.put(TodosContract.Todos.COLUMN_NAME_DATE, date);

        return context.getContentResolver().update(OrgAppContentProvider.TODOS_URI, cv, NotesContract.Notes._ID + "=?", new String[]{id + ""});
    }

    public static int delete(Context context, long id) {
        return context.getContentResolver().delete(OrgAppContentProvider.TODOS_URI, TodosContract.Todos._ID + "=?", new String[]{id + ""});
    }

    public static int toggleDone(Context context, boolean isChecked, long id) {
        Log.v("kyoyo", id + " > " + isChecked);

        ContentValues cv = new ContentValues();
        cv.put(TodosContract.Todos.COLUMN_NAME_DONE, isChecked ? 1 : 0);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        cv.put(TodosContract.Todos.COLUMN_NAME_DATE, date);

        return context.getContentResolver().update(OrgAppContentProvider.TODOS_URI, cv, NotesContract.Notes._ID + "=?", new String[]{id + ""});
    }
}
