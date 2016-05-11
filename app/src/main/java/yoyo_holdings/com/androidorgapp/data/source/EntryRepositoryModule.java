package yoyo_holdings.com.androidorgapp.data.source;

import android.content.Context;


import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import yoyo_holdings.com.androidorgapp.BaseApp;
import yoyo_holdings.com.androidorgapp.data.source.local.EntryDataSourceImpl;

/**
 * Created by aconcepcion on 5/9/16.
 */
@Module
public class EntryRepositoryModule {
    private Context context;

    EntryDataSourceImpl entryDataSourceImpl;

    public EntryRepositoryModule(Context context) {
        this.context = context;
    }

    @Provides
    @Singleton
    EntryRepository provideShoppingRepository() {
        if (entryDataSourceImpl == null) {
            entryDataSourceImpl = new EntryDataSourceImpl(((BaseApp)context.getApplicationContext()).getDataSource());
        }
        return new EntryRepository(entryDataSourceImpl);
    }
}
