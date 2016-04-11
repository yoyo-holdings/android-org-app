package com.yoyo.exam.pdatu.myapplication.models;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Patrich on 4/9/2016.
 */
public class Todo extends SugarRecord implements Comparable{

    private String content;
    private boolean done;
    private boolean deleted;
    private String dateCreated;
    private String dateModified;

    public Todo() {

    }

    public Todo(String content) {
        this.content = content;
        deleted = false;
        done = false;
        setDates();
    }

    public Todo(Todo todo) {
        this.content = todo.getContent();
        this.done = todo.isDone();
        this.dateModified = todo.getDateModified();
        this.dateCreated = todo.getDateCreated();
    }

    private void setDates() {
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        dateCreated = sdf.format(now.getTime());
        dateModified = dateCreated;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @Override
    public int compareTo(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        Todo otherTodo = (Todo) o;
        Date date1 = null;
        try {
            date1 = sdf.parse(otherTodo.getDateModified());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        Date dateThis = null;
        try {
            dateThis = sdf.parse(this.getDateModified());
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        int result = 0;
        try {
            result = dateThis.compareTo(date1);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return (-1)*result;
    }
}
