package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class UpdateEvent<O> {
    private O object;
    private int position;

    public UpdateEvent(O object, int position) {
        this.object = object;
        this.position = position;
    }

    public O getObject() {
        return object;
    }

    public int getPosition() {
        return position;
    }
}
