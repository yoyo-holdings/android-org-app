package com.josephimari.myapplication.database;

import com.raizlabs.android.dbflow.annotation.Database;


/**
 * Created by Joseph on 5/30/2016.
 */
@Database(name = MemoDB.NAME, version = MemoDB.VERSION)
public class MemoDB {
  public static final String NAME = "Memo";
  public static final int VERSION = 1;
}
