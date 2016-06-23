package me.dlet.androidorgapp.models;

import java.io.Serializable;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 *
 * Model for Note that consist of Title and Text
 */
public class Note implements Comparable<Note>, Serializable{
    private long id;
    private String title;
    private String text;
    private long lastUpdated;

    public Note(String title, String text, long lastUpdated) {
        this.title = title;
        this.text = text;
        this.lastUpdated = lastUpdated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(Note another) {
        long u = another.lastUpdated;

        return lastUpdated > u ? -1 : lastUpdated == u ? 0 : 1;
    }
}
