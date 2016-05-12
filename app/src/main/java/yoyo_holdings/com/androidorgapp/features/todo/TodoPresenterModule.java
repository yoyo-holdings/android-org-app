package yoyo_holdings.com.androidorgapp.features.todo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by aconcepcion on 5/12/16.
 */
@Module
public class TodoPresenterModule {

    private final TodoContract.View mView;

    public TodoPresenterModule(TodoContract.View view) {
        mView = view;
    }

    @Provides
    TodoContract.View provideContractView() {
        return mView;
    }
}