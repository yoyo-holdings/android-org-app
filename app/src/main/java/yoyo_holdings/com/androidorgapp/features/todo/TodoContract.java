package yoyo_holdings.com.androidorgapp.features.todo;

import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;

/**
 * Created by aconcepcion on 5/12/16.
 */
public interface TodoContract {
    interface View {
        void setProgressIndicator(boolean active);
        void showSaveEntryDone();
        void showEditTodoUi(EntryEntity entry);
    }

    interface UserActionsListener {
        void editEntry(EntryEntity entry);
        void updateEntry(EntryEntity entry);
    }
}
