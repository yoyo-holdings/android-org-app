package com.dt.myapplication.main.fragment;

import com.dt.myapplication.main.viewholder.NoteListItemViewHolder;
import com.dt.myapplication.model.Note;

/**
 * Created by DT on 28/05/2016.
 */
public interface NoteListContainer {

    void setMaxLines(NoteListItemViewHolder noteListItemViewHolder, int lines);

    void showEditNoteDialog(Note note);

    void showConvertTodoDialog(Note note);

    void toggleEditMode();

    void toggleConvertMode();
}
