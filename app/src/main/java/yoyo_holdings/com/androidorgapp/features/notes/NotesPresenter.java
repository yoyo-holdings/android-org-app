package yoyo_holdings.com.androidorgapp.features.notes;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import yoyo_holdings.com.androidorgapp.data.model.Entry;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepository;
import yoyo_holdings.com.androidorgapp.data.source.local.EntryDataSource;

/**
 * Created by aconcepcion on 5/12/16.
 */

/**
 * Listens to user actions from the UI ({@link NotesFragment}), retrieves the data and updates the
 * UI as required.
 * <p />
 * By marking the constructor with {@code @Inject}, Dagger injects the dependencies required to
 * create an instance of the TasksPresenter (if it fails, it emits a compiler error).  It uses
 * {@link NotesPresenterModule} to do so, and the constructed instance is available in
 * {@link NotesFragmentComponent}.
 * <p/>
 * Dagger generated code doesn't require public access to the constructor or class, and
 * therefore, to ensure the developer doesn't instantiate the class manually and bypasses Dagger,
 * it's good practice minimise the visibility of the class/constructor as much as possible.
 **/
final class NotesPresenter implements NotesContract.UserActionsListener {

    private final EntryRepository entryRepository;

    private final NotesContract.View view;

    /**
     * Dagger strictly enforces that arguments not marked with {@code @Nullable} are not injected
     * with {@code @Nullable} values.
     */
    @Inject
    NotesPresenter(EntryRepository entryRepository, NotesContract.View view) {
        this.entryRepository = entryRepository;
        this.view = view;
    }


    @Override
    public void openEntryDetails(@NonNull Entry entry) {
        view.showEntryDetailsUi(entry);
    }

    @Override
    public void editEntry(@NonNull Entry entry) {
        view.showEntryDetailsUi(entry);
    }

    @Override
    public void removeEntry(EntryEntity entry) {
        entryRepository.removeEntry(entry, new EntryDataSource.RemoveEntryCallback() {
            @Override
            public void onEntryRemoved() {
                view.removeEntryDone();
            }

            @Override
            public void onEntryRemoveFailed() {

            }
        });
    }
}