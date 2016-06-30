package com.giaquino.todo.injection;

import android.support.annotation.NonNull;
import com.giaquino.todo.MainActivity;
import com.giaquino.todo.ui.checklist.ChecklistFragment;
import com.giaquino.todo.ui.note.NoteFragment;
import dagger.Component;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
@Singleton @Component(modules = { ApplicationModule.class, ModelModule.class })
public interface ApplicationComponent {

    void inject(@NonNull MainActivity activity);

    void inject(@NonNull NoteFragment fragment);

    void inject(@NonNull ChecklistFragment fragment);
}
