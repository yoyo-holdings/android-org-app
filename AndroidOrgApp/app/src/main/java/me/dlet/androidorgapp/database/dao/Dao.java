package me.dlet.androidorgapp.database.dao;

import java.util.List;

/**
 * Created by darwinlouistoledo on 6/6/16.
 * Email: darwin@creativehothouse.com
 */
public interface Dao<O> {
    List<O> getAll();
    O get(int id);
    long insert(O object);
    void update(O object);
    void delete(long id);
}
