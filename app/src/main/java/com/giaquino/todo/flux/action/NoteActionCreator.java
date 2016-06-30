package com.giaquino.todo.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.todo.flux.dispatcher.Dispatcher;
import com.giaquino.todo.flux.store.NoteStore;
import com.giaquino.todo.model.NoteModel;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class NoteActionCreator {

    private NoteModel noteModel;

    public NoteActionCreator(@NonNull Dispatcher dispatcher, @NonNull NoteModel model) {
        noteModel = model;
        noteModel.notes().subscribe(notes -> {
            dispatcher.dispatch(Action.create(NoteStore.ACTION_NOTES_UPDATED, notes));
        });
    }

    public void createNote(@NonNull String title, @NonNull String text) {
        noteModel.create(title, text);
    }

    public void updateNote(long id, @NonNull String title, @NonNull String text) {
        noteModel.update(id, title, text);
    }

    public void deleteNote(long id) {
        noteModel.delete(id);
    }
}
