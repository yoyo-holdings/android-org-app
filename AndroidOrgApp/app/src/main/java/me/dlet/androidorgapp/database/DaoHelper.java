package me.dlet.androidorgapp.database;

import android.content.Context;

import me.dlet.androidorgapp.database.dao.Dao;
import me.dlet.androidorgapp.database.dao.NotesDaoImpl;
import me.dlet.androidorgapp.database.dao.TodosDaoImpl;
import me.dlet.androidorgapp.models.Note;
import me.dlet.androidorgapp.models.Todo;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin@creativehothouse.com
 */
public class DaoHelper {
    private static DaoHelper instance;


    private DatabaseHelper databaseHelper;
    private Dao<Note> notesDao;
    private Dao<Todo> todosDao;

    public static void init(Context context){
        instance = new DaoHelper(context);
    }

    public static DaoHelper getInstance(){
        if (instance == null)
            throw new NullPointerException("DAOHelper Instance is null. You must initiate it in Application.");

        return instance;
    }

    private DaoHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
        notesDao = new NotesDaoImpl(databaseHelper);
        todosDao = new TodosDaoImpl(databaseHelper);
    }

    public Dao<Note> getNotesDao() {
        return notesDao;
    }

    public Dao<Todo> getTodosDao() {
        return todosDao;
    }
}
