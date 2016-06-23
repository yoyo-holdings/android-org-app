package me.dlet.androidorgapp.tasks;

import java.util.List;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public interface OnLoadListener<O> {
    void onFinish(List<O> objlist);
}
