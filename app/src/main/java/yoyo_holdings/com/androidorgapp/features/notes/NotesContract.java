package yoyo_holdings.com.androidorgapp.features.notes;

import android.support.annotation.NonNull;

import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by aconcepcion on 5/12/16.
 */
public interface NotesContract {
    interface View {
        void setProgressIndicator(boolean active);
        void showEntryDetailsUi(Entry entry);
        void showLoadingEntryError();
    }

    interface UserActionsListener {
        void openEntryDetails(@NonNull Entry entry);
        void editEntry(Entry entry);
    }
}
