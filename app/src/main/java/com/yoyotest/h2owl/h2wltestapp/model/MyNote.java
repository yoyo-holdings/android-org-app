package com.yoyotest.h2owl.h2wltestapp.model;

import io.realm.RealmObject;

/**
 * Created by h2owl on 16/04/25.
 */
public class MyNote extends RealmObject{
    public long id;
    public int group;
    public long date;
    public int type;
    public int state;
    public String title;
    public String content;
}
