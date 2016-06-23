package me.dlet.androidorgapp.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import me.dlet.androidorgapp.database.DatabaseHelper;
import me.dlet.androidorgapp.models.Note;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin@creativehothouse.com
 */
public class NotesDaoImpl implements Dao<Note> {

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase dbWritable;
    private SQLiteDatabase dbReadable;

    public NotesDaoImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        dbReadable = databaseHelper.getReadableDatabase();
        dbWritable = databaseHelper.getWritableDatabase();
    }

    @Override
    public List<Note> getAll() {
        List<Note> notes = new ArrayList<>();
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + DatabaseHelper.Constants.Notes.TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Note note = new Note(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.TITLE)),
                        cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.TEXT)),
                        cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.LASTUPDATED)));
                note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.ID)));

                notes.add(note);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return notes;
    }

    @Override
    public Note get(int id) {
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + DatabaseHelper.Constants.Notes.TABLE_NAME
                + "WHERE " + DatabaseHelper.Constants.Notes.Columns.ID + "=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst() && cursor.getCount()== 1) {
            Note note = new Note(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.TITLE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.TEXT)),
                    cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.LASTUPDATED)));
            note.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Notes.Columns.ID)));

            return note;
        }

        cursor.close();
        return null;
    }

    @Override
    public long insert(Note note) {
        String sqlStatement = "INSERT INTO " + DatabaseHelper.Constants.Notes.TABLE_NAME
                + " (" + DatabaseHelper.Constants.Notes.Columns.TITLE + ","
                + DatabaseHelper.Constants.Notes.Columns.TEXT + ","
                + DatabaseHelper.Constants.Notes.Columns.LASTUPDATED + ")"
                + " VALUES (?,?,?)";

        SQLiteStatement insertStmt = dbWritable.compileStatement(sqlStatement);
        insertStmt.clearBindings();
        insertStmt.bindString(1, note.getTitle());
        insertStmt.bindString(2, note.getText());
        insertStmt.bindLong(3, note.getLastUpdated());
        return insertStmt.executeInsert();
    }

    @Override
    public void update(Note note) {
        String sqlStatement = "UPDATE " + DatabaseHelper.Constants.Notes.TABLE_NAME + " SET "
                + DatabaseHelper.Constants.Notes.Columns.TITLE + " = ?,"
                + DatabaseHelper.Constants.Notes.Columns.TEXT + " = ?,"
                + DatabaseHelper.Constants.Notes.Columns.LASTUPDATED + " = ?"
                + " WHERE " + DatabaseHelper.Constants.Notes.Columns.ID + " = ? ";

        SQLiteStatement updateStmt = dbWritable.compileStatement(sqlStatement);
        updateStmt.clearBindings();
        updateStmt.bindString(1, note.getTitle());
        updateStmt.bindString(2, note.getText());
        updateStmt.bindLong(3, note.getLastUpdated());
        updateStmt.bindLong(4, note.getId());
        updateStmt.executeUpdateDelete();
    }

    @Override
    public void delete(long id) {
        String sqlStatement = "DELETE FROM " + DatabaseHelper.Constants.Notes.TABLE_NAME
                + " WHERE " + DatabaseHelper.Constants.Notes.Columns.ID + " = ? ";

        SQLiteStatement delStmt = dbWritable.compileStatement(sqlStatement);
        delStmt.clearBindings();
        delStmt.bindLong(1, id);
        delStmt.executeUpdateDelete();
    }
}
