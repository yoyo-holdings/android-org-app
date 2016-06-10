package me.dlet.androidorgapp.models;

import java.io.Serializable;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class Todo implements Serializable{
    private long id;
    private String todo;
    private boolean isDone;
    private long lastUpdated;

    public Todo(String todo, boolean isDone, long lastUpdated) {
        this.todo = todo;
        this.isDone = isDone;
        this.lastUpdated = lastUpdated;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
