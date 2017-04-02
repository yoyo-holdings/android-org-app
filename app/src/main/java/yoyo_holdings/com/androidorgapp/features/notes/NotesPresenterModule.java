package yoyo_holdings.com.androidorgapp.features.notes;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aconcepcion on 5/12/16.
 */
@Module
public class NotesPresenterModule {

    private final NotesContract.View mView;

    public NotesPresenterModule(NotesContract.View view) {
        mView = view;
    }

    @Provides
    NotesContract.View provideContractView() {
        return mView;
    }
}