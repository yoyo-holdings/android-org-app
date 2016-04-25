package com.yoyotest.h2owl.h2owltestapp.model;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

/**
 * Created by h2owl on 16/04/25.
 */
public class MyRealmMigration implements RealmMigration {

    @Override
    public void migrate(final DynamicRealm realm, long oldVersion, long newVersion) {
        // During a migration, a DynamicRealm is exposed. A DynamicRealm is an untyped variant of a normal Realm, but
        // with the same object creation and query capabilities.
        // A DynamicRealm uses Strings instead of Class references because the Classes might not even exist or have been
        // renamed.

        // Access the Realm schema in order to create, modify or delete classes and their fields.
        RealmSchema schema = realm.getSchema();

        // Migrate from version 0 to version 1
        if (oldVersion == 0) {
            //realm.deleteAll();
        }

    }

    // http://sakebook.hatenablog.com/entry/2016/02/07/050930
    @Override
    public boolean equals(Object o) {
        return o instanceof MyRealmMigration;
    }
}
