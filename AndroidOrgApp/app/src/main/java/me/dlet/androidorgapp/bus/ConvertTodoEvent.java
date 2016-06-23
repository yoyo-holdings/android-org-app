package me.dlet.androidorgapp.bus;

/**
 * Created by darwinlouistoledo on 6/8/16.
 * Email: darwin.louis@ymail.com
 */
public class ConvertTodoEvent {
    private int position;

    public ConvertTodoEvent(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
