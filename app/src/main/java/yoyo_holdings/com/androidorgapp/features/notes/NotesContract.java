package yoyo_holdings.com.androidorgapp.features.notes;

import android.support.annotation.NonNull;

import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;

/**
 * Created by aconcepcion on 5/12/16.
 */
public interface NotesContract {
    interface View {
        void setProgressIndicator(boolean active);
        void showEntryDetailsUi(Entry entry);
        void showLoadingEntryError();
        void removeEntryDone();
    }

    interface UserActionsListener {
        void openEntryDetails(@NonNull Entry entry);
        void editEntry(Entry entry);
        void removeEntry(EntryEntity entry);
    }
}
