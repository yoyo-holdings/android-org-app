package com.dt.myapplication.main.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dt.myapplication.R;
import com.dt.myapplication.data.dao.NotesDao;
import com.dt.myapplication.main.activity.MainActivity;
import com.dt.myapplication.main.adapter.NotesCursorAdapter;
import com.dt.myapplication.main.dialog.NoteDialog;
import com.dt.myapplication.main.dialog.TodoDialog;
import com.dt.myapplication.main.presenter.NoteListPresenter;
import com.dt.myapplication.main.presenter.impl.NoteListPresenterImpl;
import com.dt.myapplication.main.viewholder.NoteListItemViewHolder;
import com.dt.myapplication.model.Note;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 27/05/2016.
 */
public class NoteListFragment extends Fragment
    implements NoteListContainer, LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    @Bind(R.id.list_view) public ListView notesListView;

    ListAdapter notesListAdapter;
    NoteListPresenter noteListPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        buildAdapterAndListView();
        noteListPresenter = new NoteListPresenterImpl(this);
        return view;
    }

    private void buildAdapterAndListView() {
        setListAdapter(new NotesCursorAdapter(getContext()));
        notesListView.setAdapter(getListAdapter());
        notesListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    public ListAdapter getListAdapter() {
        return notesListAdapter;
    }

    public void setListAdapter(ListAdapter notesListAdapter) {
        this.notesListAdapter = notesListAdapter;
    }

    private NotesCursorAdapter getNotesCursorAdapter() {
        return (NotesCursorAdapter) getListAdapter();
    }

    @Override public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return NotesDao.getNotesCursorLoader(getActivity());
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (getListAdapter() != null && cursor != null && cursor.getCount() > 0) {
            getNotesCursorAdapter().swapCursor(cursor);
            getNotesCursorAdapter().notifyDataSetChanged();
        }
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        getNotesCursorAdapter().changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Cursor cursor = ((CursorAdapter) getListAdapter()).getCursor();
        cursor.moveToPosition(position);
        Note note = new Note(cursor);
        NoteListItemViewHolder noteListItemViewHolder = new NoteListItemViewHolder(view);
        noteListPresenter.onItemClicked(note, getNotesCursorAdapter().getCurrentMode(),
            noteListItemViewHolder, noteListItemViewHolder.noteTextTextView.getMaxLines());
    }

    @Override public void setMaxLines(NoteListItemViewHolder noteListItemViewHolder, int lines) {
        noteListItemViewHolder.noteTextTextView.setMaxLines(lines);
    }

    @Override public void showEditNoteDialog(Note note) {
        ((MainActivity) getActivity()).showNoteDialog(NoteDialog.MODE_EDIT, note);
    }

    @Override public void showConvertTodoDialog(Note note) {
        ((MainActivity) getActivity()).showTodoDialog(TodoDialog.MODE_CONVERT,
            new Todo(note.getId(), note.getTitle() + " " + note.getText(), false));
    }

    @Override public void toggleEditMode() {
        if (getNotesCursorAdapter() != null) {
            getNotesCursorAdapter().toggleEditMode();
        }
    }

    @Override public void toggleConvertMode() {
        if (getNotesCursorAdapter() != null) {
            getNotesCursorAdapter().toggleConvertMode();
        }
    }
}
