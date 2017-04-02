package yoyo_holdings.com.androidorgapp.data.source.local;

import android.support.annotation.NonNull;


import java.util.List;

import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by aconcepcion on 5/8/16.
 */
public interface EntryDataSource {

    interface LoadEntriesCallback {
        void onEntriesLoaded(List<Entry> entries);
        void onDataNotAvailable();
    }

    interface AddEntryCallback {
        void onEntryAdded();
        void onEntryAddFailed();
    }

    interface RemoveEntryCallback {
        void onEntryRemoved();
        void onEntryRemoveFailed();
    }

    void getEntries(@NonNull LoadEntriesCallback callback);
    void addEntry(@NonNull Entry entry, AddEntryCallback callback);
    void updateEntry(@NonNull Entry entry, AddEntryCallback callback);
    void removeEntry(@NonNull Entry entry, RemoveEntryCallback callback);

}