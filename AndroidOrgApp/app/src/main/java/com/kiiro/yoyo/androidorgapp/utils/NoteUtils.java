package com.kiiro.yoyo.androidorgapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.kiiro.yoyo.androidorgapp.MainActivity;
import com.kiiro.yoyo.androidorgapp.R;
import com.kiiro.yoyo.androidorgapp.db.NotesContract;
import com.kiiro.yoyo.androidorgapp.db.OrgAppContentProvider;
import com.kiiro.yoyo.androidorgapp.db.OrgAppDbHelper;
import com.kiiro.yoyo.androidorgapp.fragments.NotesListFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteUtils {

    public static void startAddNoteOperation(Context context, NotesListFragment notesListFragment) {
        AlertDialog d = createAddNoteDialog(context, notesListFragment);
        d.show();
    }

    private static AlertDialog createAddNoteDialog(final Context context, final NotesListFragment notesListFragment) {
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_entry, null);
        final TextView txtTitle = (TextView) contentView.findViewById(R.id.txtHeader);
        txtTitle.setText(R.string.add_note);
        final TextView txtInput = (TextView) contentView.findViewById(R.id.note);

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(contentView).setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = txtInput.getText().toString();
                long id = add(context, input);
                if (-1 == id) {
                    Toast.makeText(context, R.string.add_note_failed, Toast.LENGTH_SHORT).show();
                } else {
                    notesListFragment.restartLoader();
                    Toast.makeText(context, R.string.note_added, Toast.LENGTH_SHORT).show();
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

    public static void startEditNoteOperation(Context context, NotesListFragment notesListFragment, long noteId, String note) {
        AlertDialog d = createEditNoteDialog(context, notesListFragment, noteId, note);
        d.show();
    }

    private static android.support.v7.app.AlertDialog createEditNoteDialog(final Context context, final NotesListFragment notesListFragment, final long noteId, String note) {
        final View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_entry, null);
        final TextView txtTitle = (TextView) contentView.findViewById(R.id.txtHeader);
        txtTitle.setText(R.string.edit_note);
        final TextView txtInput = (TextView) contentView.findViewById(R.id.note);
        txtInput.setText(note);

        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setView(contentView).setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = txtInput.getText().toString();
                long id = edit(context, noteId, input);
                if (-1 == id) {
                    Toast.makeText(context, R.string.edit_note_failed, Toast.LENGTH_SHORT).show();
                } else {
                    notesListFragment.restartLoader();
                    Toast.makeText(context, R.string.note_edited, Toast.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setNeutralButton(R.string.convert_as_todo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete(context, noteId);
                TodoUtils.add(context, txtInput.getText().toString());
                ((MainActivity)notesListFragment.getActivity()).restartLoaders();
            }
        });
        return adb.create();
    }

    public static long add(Context context, String note) {
        ContentValues cv = new ContentValues();
        cv.put(NotesContract.Notes.COLUMN_NAME_CONTENT, note);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        cv.put(NotesContract.Notes.COLUMN_NAME_DATE, date);

        Uri url = context.getContentResolver().insert(OrgAppContentProvider.NOTES_URI, cv);
        return Long.parseLong(url.getLastPathSegment());
    }

    public static int edit(Context context, long id, String note) {
        ContentValues cv = new ContentValues();
        cv.put(NotesContract.Notes.COLUMN_NAME_CONTENT, note);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());
        cv.put(NotesContract.Notes.COLUMN_NAME_DATE, date);

        return context.getContentResolver().update(OrgAppContentProvider.NOTES_URI, cv, NotesContract.Notes._ID + "=?", new String[]{id + ""});
    }

    public static int delete(Context context, long id) {
        return context.getContentResolver().delete(OrgAppContentProvider.NOTES_URI, NotesContract.Notes._ID + "=?", new String[]{id + ""});
    }
}
