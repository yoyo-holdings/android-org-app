package yoyo_holdings.com.androidorgapp.features.todo;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepository;
import yoyo_holdings.com.androidorgapp.data.source.local.EntryDataSource;

/**
 * Created by aconcepcion on 5/12/16.
 */

/**
 * Listens to user actions from the UI ({@link TodoFragment}), retrieves the data and updates the
 * UI as required.
 * <p />
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link TodoPresenterModule} to do so, and the constructed instance is available in
 * {@link TodoFragmentComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
final class TodoPresenter implements TodoContract.UserActionsListener {

    private final EntryRepository entryRepository;

    private final TodoContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    TodoPresenter(EntryRepository entryRepository, TodoContract.View view) {
        this.entryRepository = entryRepository;
        this.view = view;
    }

    @Override
    public void editEntry(@NonNull EntryEntity entry) {
        view.showEditTodoUi(entry);
    }

    @Override
    public void updateEntry(EntryEntity entry) {
        entryRepository.updateEntry(entry, new EntryDataSource.AddEntryCallback() {
            @Override
            public void onEntryAdded() {
                view.showSaveEntryDone();
            }

            @Override
            public void onEntryAddFailed() {

            }
        });
    }
}