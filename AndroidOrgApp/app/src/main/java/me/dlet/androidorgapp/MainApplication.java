package me.dlet.androidorgapp;

import android.app.Application;

import me.dlet.androidorgapp.database.DaoHelper;

/**
 * Created by darwinlouistoledo on 6/6/16.
 * Email: darwin.louis@ymail.com
 */
public class MainApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        DaoHelper.init(this);

    }
}
