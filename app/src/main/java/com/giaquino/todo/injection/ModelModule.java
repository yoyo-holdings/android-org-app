package com.giaquino.todo.injection;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import com.giaquino.todo.BuildConfig;
import com.giaquino.todo.TodoApplication;
import com.giaquino.todo.flux.action.ChecklistActionCreator;
import com.giaquino.todo.flux.action.NoteActionCreator;
import com.giaquino.todo.flux.dispatcher.Dispatcher;
import com.giaquino.todo.flux.store.ChecklistStore;
import com.giaquino.todo.flux.store.NoteStore;
import com.giaquino.todo.model.ChecklistModel;
import com.giaquino.todo.model.NoteModel;
import com.giaquino.todo.model.db.Database;
import com.giaquino.todo.model.db.TodoDatabase;
import com.giaquino.todo.model.db.TodoOpenHelper;
import dagger.Module;
import dagger.Provides;
import java.util.Arrays;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
@Module public class ModelModule {

    private String databaseName;
    private int databaseVersion;

    public ModelModule(@NonNull String databaseName, @IntRange(from = 1) int databaseVersion) {
        this.databaseName = databaseName;
        this.databaseVersion = databaseVersion;
    }

    @Provides @Singleton public Database provideDatabase(TodoApplication application) {
        return new TodoDatabase(
            new TodoOpenHelper(application, databaseName, null, databaseVersion),
            BuildConfig.DEBUG);
    }

    @Provides @Singleton public NoteModel provideNoteModel(Database database) {
        return new NoteModel(database);
    }

    @Provides @Singleton public ChecklistModel provideChecklistModel(Database database) {
        return new ChecklistModel(database);
    }

    @Provides @Singleton public NoteStore provideNoteStore() {
        return new NoteStore();
    }

    @Provides @Singleton public ChecklistStore provideChecklistStore() {
        return new ChecklistStore();
    }

    @Provides @Singleton
    public Dispatcher provideDispatcher(NoteStore noteStore, ChecklistStore checklistStore) {
        return new Dispatcher(Arrays.asList(noteStore, checklistStore));
    }

    @Provides @Singleton
    public NoteActionCreator provideNoteActionCreator(Dispatcher dispatcher, NoteModel model) {
        return new NoteActionCreator(dispatcher, model);
    }

    @Provides @Singleton
    public ChecklistActionCreator provideChecklistActionCreator(Dispatcher dispatcher,
        ChecklistModel model) {
        return new ChecklistActionCreator(dispatcher, model);
    }
}
