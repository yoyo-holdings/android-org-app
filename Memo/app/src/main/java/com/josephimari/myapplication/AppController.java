package com.josephimari.myapplication;

import android.app.Application;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Joseph on 5/30/2016.
 */
public class AppController extends Application {

  @Override public void onCreate() {
    super.onCreate();
    FlowManager.init(this);
  }
}
