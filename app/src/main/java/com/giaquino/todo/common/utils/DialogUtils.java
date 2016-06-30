package com.giaquino.todo.common.utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import com.giaquino.todo.common.widget.ChecklistView;
import com.giaquino.todo.common.widget.NoteView;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class DialogUtils {

    public static AlertDialog createNoteDialog(Context context, NoteView noteView) {
        return new AlertDialog.Builder(context).setTitle("Create Note")
            .setView(noteView)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create();
    }

    public static AlertDialog createChecklistDialog(Context context, ChecklistView checklistView) {
        return new AlertDialog.Builder(context).setTitle("Create Checklist")
            .setView(checklistView)
            .setPositiveButton("Create", null)
            .setNegativeButton("Cancel", null)
            .setCancelable(false)
            .create();
    }

    public static AlertDialog updateChecklistDialog(Context context, ChecklistView checklistView) {
        return new AlertDialog.Builder(context).setTitle("Update Checklist")
            .setView(checklistView)
            .setPositiveButton("Update", null)
            .setNegativeButton("Delete", null)
            .setNeutralButton("Move to Note", null)
            .create();
    }

    public static AlertDialog updateNoteDialog(Context context, NoteView noteView) {
        return new AlertDialog.Builder(context).setTitle("Update Note")
            .setView(noteView)
            .setPositiveButton("Update", null)
            .setNegativeButton("Delete", null)
            .setNeutralButton("Move to Checklist", null)
            .create();
    }
}
