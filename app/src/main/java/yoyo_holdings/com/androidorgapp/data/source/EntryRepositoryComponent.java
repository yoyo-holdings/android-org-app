package yoyo_holdings.com.androidorgapp.data.source;

import javax.inject.Singleton;

import dagger.Component;
import yoyo_holdings.com.androidorgapp.ApplicationModule;
import yoyo_holdings.com.androidorgapp.features.createupdate.UpsertNotesFragment;
import yoyo_holdings.com.androidorgapp.features.notes.NotesFragment;
import yoyo_holdings.com.androidorgapp.features.todo.TodoFragment;

/**
 * Created by aconcepcion on 5/9/16.
 */

@Singleton
@Component(modules = {EntryRepositoryModule.class, ApplicationModule.class})
public interface EntryRepositoryComponent {

    void inject(NotesFragment fragment);
    void inject(UpsertNotesFragment fragment);
    void inject(TodoFragment fragment);

    EntryRepository getShoppingRepository();
}

