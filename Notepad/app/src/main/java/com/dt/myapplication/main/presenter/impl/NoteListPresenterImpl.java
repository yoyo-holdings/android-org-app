package com.dt.myapplication.main.presenter.impl;

import com.dt.myapplication.main.adapter.NotesCursorAdapter;
import com.dt.myapplication.main.fragment.NoteListContainer;
import com.dt.myapplication.main.presenter.NoteListPresenter;
import com.dt.myapplication.main.viewholder.NoteListItemViewHolder;
import com.dt.myapplication.model.Note;

/**
 * Created by DT on 28/05/2016.
 */
public class NoteListPresenterImpl implements NoteListPresenter {

    private NoteListContainer noteListContainer;

    public NoteListPresenterImpl(NoteListContainer noteListContainer) {
        this.noteListContainer = noteListContainer;
    }

    @Override
    public void onItemClicked(Note note, int mode, NoteListItemViewHolder noteListItemViewHolder,
        int maxLines) {
        switch (mode) {
            case NotesCursorAdapter.DEFAULT_MODE:
                if (maxLines == 1) {
                    noteListContainer.setMaxLines(noteListItemViewHolder, 99);
                } else {
                    noteListContainer.setMaxLines(noteListItemViewHolder, 1);
                }
                break;
            case NotesCursorAdapter.EDIT_MODE:
                noteListContainer.showEditNoteDialog(note);
                break;
            case NotesCursorAdapter.CONVERT_MODE:
                noteListContainer.showConvertTodoDialog(note);
                break;
            default:
                break;
        }
    }
}
