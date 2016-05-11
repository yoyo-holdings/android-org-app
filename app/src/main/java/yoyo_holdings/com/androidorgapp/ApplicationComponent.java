package yoyo_holdings.com.androidorgapp;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.requery.Persistable;
import io.requery.rx.SingleEntityStore;

/**
 * Created by andrewconcepcion on 15/02/2016.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApp baseApp);
    @ApplicationContext Context context();
    Application application();
    SingleEntityStore<Persistable> data();

}
