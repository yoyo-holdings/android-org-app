package com.giaquino.todo.flux.store;

import android.support.annotation.NonNull;
import com.giaquino.todo.flux.action.Action;
import com.giaquino.todo.model.entity.Checklist;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class ChecklistStore extends Store<ChecklistStore> {

    public static final String ACTION_CHECKLIST_UPDATED = "com.giaquino.todo.flux.store.ChecklistStore.ACTION_CHECKLIST_UPDATED";

    private List<Checklist> checklists = new ArrayList<>();

    @NonNull public List<Checklist> checklists() {
        return Collections.unmodifiableList(checklists);
    }

    @SuppressWarnings("unchecked") @Override public void dispatchAction(@NonNull Action action) {
        switch (action.type()) {
            case ACTION_CHECKLIST_UPDATED:
                checklists = (List<Checklist>) action.data();
                notifyStoreChanged(this);
                break;
        }
    }
}
