

package yoyo_holdings.com.androidorgapp.data.source.local;

/**
 * Created by aconcepcion on 5/8/16.
 */

import android.support.annotation.NonNull;

import javax.inject.Singleton;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.rx.SingleEntityStore;
import rx.Subscriber;
import rx.functions.Action1;
import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;

@Singleton
public class EntryDataSourceImpl implements EntryDataSource {

    private final SingleEntityStore<Persistable> data;
    private boolean noData;

    public EntryDataSourceImpl(SingleEntityStore<Persistable> data) {
        this.data = data;
    }

    @Override
    public void getEntries(@NonNull final LoadEntriesCallback callback) {
        data.select(Entry.class)
        .orderBy(EntryEntity.ID.desc())
        .get()
        .toSelfObservable()
        .subscribe(new Subscriber<Result<Entry>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                callback.onDataNotAvailable();
            }

            @Override
            public void onNext(Result<Entry> entries) {
                callback.onEntriesLoaded(entries.toList());
            }
        });
    }

    @Override
    public void addEntry(@NonNull Entry entry, final AddEntryCallback callback) {
        data.insert(entry)
                .toObservable()
                .subscribe(new Action1<Entry>() {
                    @Override
                    public void call(Entry entry) {
                        if (callback != null) {
                            callback.onEntryAdded();
                        }
                    }
                });
    }

    @Override
    public void updateEntry(@NonNull Entry entry, final AddEntryCallback callback) {
        data.update(entry)
                .toObservable()
                .subscribe(new Action1<Entry>() {
                    @Override
                    public void call(Entry entry) {
                        if (callback != null) {
                            callback.onEntryAdded();
                        }
                    }
                });
    }

    @Override
    public void removeEntry(@NonNull Entry entry, final RemoveEntryCallback callback) {
        data.delete(entry)
                .toObservable()
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (callback != null) {
                            callback.onEntryRemoved();
                        }
                    }
                });

    }
}
