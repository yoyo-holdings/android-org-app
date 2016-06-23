package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/8/16.
 * Email: darwin.louis@ymail.com
 */
public class ConvertNoteEvent {
    private int position;

    public ConvertNoteEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
