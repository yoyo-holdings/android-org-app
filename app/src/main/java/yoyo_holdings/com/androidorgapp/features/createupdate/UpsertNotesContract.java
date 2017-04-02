package yoyo_holdings.com.androidorgapp.features.createupdate;

import android.support.annotation.NonNull;

import java.util.List;

import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by aconcepcion on 5/12/16.
 */
public interface UpsertNotesContract {
    interface View {
        void setProgressIndicator(boolean active);
        void showSaveEntryDone();
        void showSaveEntryError();
    }

    interface UserActionsListener {
        void saveEntry(Entry entry);
    }
}
