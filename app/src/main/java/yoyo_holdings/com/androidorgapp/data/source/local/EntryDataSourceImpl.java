

package yoyo_holdings.com.androidorgapp.data.source.local;

/**
 * Created by aconcepcion on 5/8/16.
 */

import android.support.annotation.NonNull;
import javax.inject.Inject;
import javax.inject.Singleton;

import io.requery.Persistable;
import io.requery.query.Result;
import io.requery.rx.SingleEntityStore;
import rx.Subscriber;
import rx.functions.Action1;
import yoyo_holdings.com.androidorgapp.data.model.Entry;

@Singleton
public class EntryDataSourceImpl implements EntryDataSource {

    private final SingleEntityStore<Persistable> data;
    private boolean noData;

    public EntryDataSourceImpl(SingleEntityStore<Persistable> data) {
        this.data = data;
    }

    @Override
    public void getEntries(@NonNull final LoadEntriesCallback callback) {
        data.select(Entry.class).get().toSelfObservable().subscribe(new Subscriber<Result<Entry>>() {
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
    public void removeEntry(@NonNull Entry entry, final RemoveEntryCallback callback) {
        data.insert(entry)
                .toObservable()
                .subscribe(new Action1<Entry>() {
                    @Override
                    public void call(Entry entry) {
                        if (callback != null) {
                            callback.onEntryRemoved();
                        }
                    }
                });

    }
}
