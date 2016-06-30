package com.giaquino.todo;

import android.app.Application;
import android.content.Context;
import com.giaquino.todo.injection.ApplicationComponent;
import com.giaquino.todo.injection.ApplicationModule;
import com.giaquino.todo.injection.DaggerApplicationComponent;
import com.giaquino.todo.injection.ModelModule;
import timber.log.Timber;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/28/16
 */
public class TodoApplication extends Application {

    private ApplicationComponent applicationComponent;

    public static TodoApplication get(Context context) {
        return (TodoApplication) context.getApplicationContext();
    }

    @Override public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(new ApplicationModule(this))
            .modelModule(new ModelModule("todo.db", 1))
            .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
