package com.syvlabs.organizr.model.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.syvlabs.organizr.model.db.objects.DaoMaster;
import com.syvlabs.organizr.model.db.objects.DaoSession;

public class DaoHelper {
    private static final String SCHEMA_NAME = "syvlabs.organizr.db";

    private static DaoHelper instance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static DaoHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DaoHelper(context);
        }
        return instance;
    }

    public DaoHelper(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, SCHEMA_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public void purge() {
        daoSession.getEntryDao().deleteAll();
    }
}
