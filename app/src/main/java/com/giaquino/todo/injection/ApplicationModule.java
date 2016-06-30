package com.giaquino.todo.injection;

import android.support.annotation.NonNull;
import com.giaquino.todo.TodoApplication;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
@Module
public class ApplicationModule {

    private TodoApplication application;

    public ApplicationModule(@NonNull TodoApplication application) {
        this.application = application;
    }

    @Provides @Singleton public TodoApplication provideTodoApplication() {
        return application;
    }
}
