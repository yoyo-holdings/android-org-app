package yoyo_holdings.com.androidorgapp.data.source;

import javax.inject.Singleton;

import dagger.Component;
import yoyo_holdings.com.androidorgapp.ApplicationModule;
import yoyo_holdings.com.androidorgapp.BaseApp;
import yoyo_holdings.com.androidorgapp.features.notes.NotesFragment;

/**
 * Created by aconcepcion on 5/9/16.
 */

@Singleton
@Component(modules = {EntryRepositoryModule.class, ApplicationModule.class})
public interface EntryRepositoryComponent {

    void inject(NotesFragment fragment);

    EntryRepository getShoppingRepository();
}

