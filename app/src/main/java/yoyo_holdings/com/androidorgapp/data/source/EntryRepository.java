package yoyo_holdings.com.androidorgapp.data.source;

import android.support.annotation.NonNull;
import javax.inject.Inject;

import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.source.local.EntryDataSource;
import yoyo_holdings.com.androidorgapp.data.source.local.EntryDataSourceImpl;

/**
 * Created by aconcepcion on 5/8/16.
 */
public class EntryRepository implements EntryDataSource {
    private final EntryDataSourceImpl entryDataSource;

    @Inject
    EntryRepository(@Local EntryDataSourceImpl entryDataSource) {
        this.entryDataSource = entryDataSource;
    }

    @Override
    public void getEntries(@NonNull LoadEntriesCallback callback) {
        entryDataSource.getEntries(callback);
    }

    @Override
    public void addEntry(@NonNull Entry entry, AddEntryCallback callback) {
        entryDataSource.addEntry(entry, callback);
    }

    @Override
    public void updateEntry(@NonNull Entry entry, AddEntryCallback callback) {
        entryDataSource.updateEntry(entry, callback);
    }

    @Override
    public void removeEntry(@NonNull Entry entry, RemoveEntryCallback callback) {
        entryDataSource.removeEntry(entry, callback);
    }
}
