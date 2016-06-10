package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class NewEvent<O> {
    private O object;

    public NewEvent(O object) {
        this.object = object;
    }

    public O getObject() {
        return object;
    }
}
