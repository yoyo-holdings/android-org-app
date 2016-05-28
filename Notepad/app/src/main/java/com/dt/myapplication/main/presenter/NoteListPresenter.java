package com.dt.myapplication.main.presenter;

import com.dt.myapplication.main.viewholder.NoteListItemViewHolder;
import com.dt.myapplication.model.Note;

/**
 * Created by DT on 28/05/2016.
 */
public interface NoteListPresenter {

    void onItemClicked(Note note, int mode, NoteListItemViewHolder noteListItemViewHolder,
        int maxLines);
}
