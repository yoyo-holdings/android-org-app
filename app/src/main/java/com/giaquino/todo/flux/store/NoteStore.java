package com.giaquino.todo.flux.store;

import android.support.annotation.NonNull;
import com.giaquino.todo.flux.action.Action;
import com.giaquino.todo.model.entity.Note;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class NoteStore extends Store<NoteStore> {

    public static final String ACTION_NOTES_UPDATED = "com.giaquino.todo.flux.store.NoteStore.ACTION_NOTES_UPDATED";

    private List<Note> notes = new ArrayList<>();

    @NonNull public List<Note> notes() {
        return Collections.unmodifiableList(notes);
    }

    @SuppressWarnings("unchecked") @Override public void dispatchAction(@NonNull Action action) {
        switch (action.type()) {
            case ACTION_NOTES_UPDATED:
                notes = (List<Note>) action.data();
                notifyStoreChanged(this);
                break;
        }
    }
}
