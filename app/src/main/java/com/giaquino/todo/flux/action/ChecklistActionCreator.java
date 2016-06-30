package com.giaquino.todo.flux.action;

import android.support.annotation.NonNull;
import com.giaquino.todo.flux.dispatcher.Dispatcher;
import com.giaquino.todo.flux.store.ChecklistStore;
import com.giaquino.todo.model.ChecklistModel;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class ChecklistActionCreator {

    private ChecklistModel checklistModel;

    public ChecklistActionCreator(@NonNull Dispatcher dispatcher, @NonNull ChecklistModel model) {
        this.checklistModel = model;
        this.checklistModel.checklists().subscribe(checklists -> {
            dispatcher.dispatch(Action.create(ChecklistStore.ACTION_CHECKLIST_UPDATED, checklists));
        });
    }

    public void createChecklist(@NonNull String entry) {
        checklistModel.create(entry);
    }

    public void updateChecklist(long id, @NonNull String entry, boolean checked) {
        checklistModel.update(id, entry, checked);
    }

    public void deleteChecklist(long id) {
        checklistModel.delete(id);
    }
}
