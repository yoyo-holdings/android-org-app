package com.yoyo.exam.pdatu.myapplication.models;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Patrich on 4/9/2016.
 */
public class Note extends SugarRecord implements Comparable{

    private String title;
    private String message;
    private boolean deleted;
    private String dateCreated;
    private String dateModified;
    @Ignore
    private boolean editMode;

    public Note() {

    }

    public Note(String title, String message) {
        this.title = title;
        this.message = message;
        deleted = false;
        editMode = true;

        setDates();
    }

    public Note(Note note) {
        this.title = note.getTitle();
        this.message = note.getMessage();
        this.dateCreated = note.getDateCreated();
        this.dateModified = note.getDateModified();
        deleted = false;
        editMode = false;
    }

    private void setDates() {
        Calendar now = new GregorianCalendar();
        now.setTime(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        dateCreated = sdf.format(now.getTime());
        dateModified = dateCreated;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    @Override
    public int compareTo(Object o) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
        Note otherNote = (Note) o;
        Date date1 = null;
        try {
            date1 = sdf.parse(otherNote.getDateModified());
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
