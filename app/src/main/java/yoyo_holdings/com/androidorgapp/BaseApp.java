package yoyo_holdings.com.androidorgapp;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;
import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by andrewconcepcion on 12/04/2016.
 */
public class BaseApp extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.enableDefaults();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public static BaseApp get(Context context) {
        return (BaseApp) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public SingleEntityStore<Persistable> getDataSource() {
        return mApplicationComponent.data();
    }

}