package yoyo_holdings.com.androidorgapp.features.notes;

import dagger.Component;
import yoyo_holdings.com.androidorgapp.data.source.EntryRepositoryComponent;
import yoyo_holdings.com.androidorgapp.util.FragmentScoped;

/**
 * Created by aconcepcion on 5/12/16.
 */
@FragmentScoped
@Component(dependencies = EntryRepositoryComponent.class, modules = NotesPresenterModule.class)
public interface NotesFragmentComponent {
    NotesPresenter getPresenter();
}