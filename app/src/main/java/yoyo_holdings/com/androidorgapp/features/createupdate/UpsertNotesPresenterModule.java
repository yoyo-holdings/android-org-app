package yoyo_holdings.com.androidorgapp.features.createupdate;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aconcepcion on 5/12/16.
 */
@Module
public class UpsertNotesPresenterModule {

    private final UpsertNotesContract.View mView;

    public UpsertNotesPresenterModule(UpsertNotesContract.View view) {
        mView = view;
    }

    @Provides
    UpsertNotesContract.View provideContractView() {
        return mView;
    }
}