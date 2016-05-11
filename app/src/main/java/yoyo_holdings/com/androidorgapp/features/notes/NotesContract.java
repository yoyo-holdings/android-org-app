package yoyo_holdings.com.androidorgapp.features.notes;

import android.support.annotation.NonNull;

import java.util.List;

import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by aconcepcion on 5/12/16.
 */
public interface NotesContract {
    interface View {
        void setProgressIndicator(boolean active);
        void showEntries(List<Entry> entries);
        void showEntryDetailsUi(Entry entry);
        void showLoadingEntryError();
        void showNewNoteUi();
        void showEditNoteUi(Entry entry);
    }

    interface UserActionsListener {
        void loadEntries(boolean isTodo);
        void openEntryDetails(@NonNull Entry entry);
        void editEntry(Entry entry);
    }
}
