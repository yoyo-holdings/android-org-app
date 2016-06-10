package me.dlet.androidorgapp.database.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import me.dlet.androidorgapp.database.DatabaseHelper;
import me.dlet.androidorgapp.models.Todo;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class TodosDaoImpl implements Dao<Todo>{

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase dbWritable;
    private SQLiteDatabase dbReadable;

    public TodosDaoImpl(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
        dbReadable = databaseHelper.getReadableDatabase();
        dbWritable = databaseHelper.getWritableDatabase();
    }

    @Override
    public List<Todo> getAll() {
        List<Todo> todos = new ArrayList<>();
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + DatabaseHelper.Constants.Todos.TABLE_NAME, null);
        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                Todo todo = new Todo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.TODO)),
                                cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.IS_DONE))==1?true:false,
                                cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.LASTUPDATED)));
                todo.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.ID)));

                todos.add(todo);
                cursor.moveToNext();
            }
        }

        cursor.close();
        return todos;
    }

    @Override
    public Todo get(int id) {
        Cursor cursor = dbReadable.rawQuery("SELECT * FROM " + DatabaseHelper.Constants.Todos.TABLE_NAME
                + "WHERE " + DatabaseHelper.Constants.Todos.Columns.ID + "=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst() && cursor.getCount()== 1) {
            Todo todo = new Todo(cursor.getString(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.TODO)),
                    cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.IS_DONE))==1?true:false,
                    cursor.getLong(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.LASTUPDATED)));
            todo.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.Constants.Todos.Columns.ID)));

            return todo;
        }

        cursor.close();
        return null;
    }

    @Override
    public long insert(Todo todo) {
        String sqlStatement = "INSERT INTO " + DatabaseHelper.Constants.Todos.TABLE_NAME
                + " (" + DatabaseHelper.Constants.Todos.Columns.TODO + ","
                + DatabaseHelper.Constants.Todos.Columns.IS_DONE + ","
                + DatabaseHelper.Constants.Todos.Columns.LASTUPDATED + ")"
                + " VALUES (?,?,?)";

        SQLiteStatement insertStmt = dbWritable.compileStatement(sqlStatement);
        insertStmt.clearBindings();
        insertStmt.bindString(1, todo.getTodo());
        insertStmt.bindLong(2, todo.isDone()?1:0);
        insertStmt.bindLong(3, todo.getLastUpdated());
        return insertStmt.executeInsert();
    }

    @Override
    public void update(Todo todo) {
        String sqlStatement = "UPDATE " + DatabaseHelper.Constants.Todos.TABLE_NAME + " SET "
                + DatabaseHelper.Constants.Todos.Columns.TODO + " = ?,"
                + DatabaseHelper.Constants.Todos.Columns.IS_DONE + " = ?,"
                + DatabaseHelper.Constants.Todos.Columns.LASTUPDATED + " = ?"
                + " WHERE " + DatabaseHelper.Constants.Todos.Columns.ID + " = ? ";

        SQLiteStatement updateStmt = dbWritable.compileStatement(sqlStatement);
        updateStmt.clearBindings();
        updateStmt.bindString(1, todo.getTodo());
        updateStmt.bindLong(2, todo.isDone()?1:0);
        updateStmt.bindLong(3, todo.getLastUpdated());
        updateStmt.bindLong(4, todo.getId());
        updateStmt.executeUpdateDelete();
    }

    @Override
    public void delete(long id) {
        String sqlStatement = "DELETE FROM " + DatabaseHelper.Constants.Todos.TABLE_NAME
                + " WHERE " + DatabaseHelper.Constants.Todos.Columns.ID + " = ? ";

        SQLiteStatement delStmt = dbWritable.compileStatement(sqlStatement);
        delStmt.clearBindings();
        delStmt.bindLong(1, id);
        delStmt.executeUpdateDelete();
    }
}
