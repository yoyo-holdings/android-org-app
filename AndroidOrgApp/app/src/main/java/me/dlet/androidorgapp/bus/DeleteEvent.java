package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class DeleteEvent<O> {
    private O object;

    public DeleteEvent(O clazz) {
        this.object = clazz;
    }

    public O getObject() {
        return object;
    }
}
