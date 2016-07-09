package com.kiiro.yoyo.androidorgapp.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kiiro.yoyo.androidorgapp.R;
import com.kiiro.yoyo.androidorgapp.db.NotesContract;
import com.kiiro.yoyo.androidorgapp.db.OrgAppContentProvider;
import com.kiiro.yoyo.androidorgapp.db.OrgAppDbHelper;
import com.kiiro.yoyo.androidorgapp.utils.NoteUtils;

public class NotesListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER_ID = 39;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.fragment_notes, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lv = (ListView) view.findViewById(android.R.id.list);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, final long id) {
                new AlertDialog.Builder(getContext()).setMessage(R.string.confirm_delete_item).setTitle(R.string.delete_note).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (0 != NoteUtils.delete(getContext(), id)) {
                            Toast.makeText(getContext(), "Note deleted.", Toast.LENGTH_SHORT).show();
                            restartLoader();
                        } else {
                            Toast.makeText(getContext(), "Delete note failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
                return true;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id != LOADER_ID) {
            return null;
        }
        return new CursorLoader(getContext(), OrgAppContentProvider.NOTES_URI, new String[]{NotesContract.Notes._ID, NotesContract.Notes.COLUMN_NAME_CONTENT, NotesContract.Notes.COLUMN_NAME_DATE}, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getListAdapter() != null) {
            ((CursorAdapter) getListAdapter()).swapCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (getListAdapter() != null) {
            ((CursorAdapter) getListAdapter()).swapCursor(null);
        }
    }

    @Override
    public void setListAdapter(ListAdapter adapter) {
        super.setListAdapter(adapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        getLoaderManager().initLoader(LOADER_ID, null, this);
    }

    public void restartLoader() {
        getLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView tv = (TextView) v.findViewById(android.R.id.text1);
        NoteUtils.startEditNoteOperation(getContext(), this, id, tv.getText().toString());
    }

}
