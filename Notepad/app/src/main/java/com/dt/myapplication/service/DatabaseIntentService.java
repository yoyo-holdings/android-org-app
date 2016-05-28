package com.dt.myapplication.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import com.dt.myapplication.data.dao.NotesDao;
import com.dt.myapplication.data.dao.TodosDao;

/**
 * Created by DT on 26/05/2016.
 */
public class DatabaseIntentService extends IntentService {

    public static final String ACTION = "database_action";
    private static final int DEFAULT_ACTION = -1;
    private static final int ACTION_ADD_NOTE = 1;
    private static final int ACTION_EDIT_NOTE = 2;
    private static final int ACTION_DELETE_NOTE = 3;
    private static final int ACTION_ADD_TODO = 4;
    private static final int ACTION_EDIT_TODO = 5;
    private static final int ACTION_DELETE_TODO = 6;

    private static final String KEY_NOTE_ID = "key_note_id";
    private static final String KEY_NOTE_TITLE = "key_note_title";
    private static final String KEY_NOTE_TEXT = "key_note_text";
    private static final String KEY_TODO_ID = "key_todo_id";
    private static final String KEY_TODO_TEXT = "key_todo_text";
    private static final String KEY_TODO_IS_DONE = "key_todo_is_done";

    public DatabaseIntentService() {
        super("DatabaseIntentService");
    }

    @Override protected void onHandleIntent(Intent intent) {
        switch (intent.getIntExtra(ACTION, DEFAULT_ACTION)) {
            case ACTION_ADD_NOTE: {
                NotesDao.insertNoteEntry(this, extractNoteContentValues(intent));
            }
            break;
            case ACTION_EDIT_NOTE: {
                NotesDao.updateNoteById(this, intent.getStringExtra(KEY_NOTE_ID),
                    extractNoteContentValues(intent));
            }
            break;
            case ACTION_DELETE_NOTE: {
                NotesDao.deleteNoteById(this, intent.getStringExtra(KEY_NOTE_ID));
            }
            break;
            case ACTION_ADD_TODO: {
                TodosDao.insertTodoEntry(this, extractTodoContentValues(intent));
            }
            break;
            case ACTION_EDIT_TODO: {
                TodosDao.updateTodoById(this, intent.getStringExtra(KEY_TODO_ID),
                    extractTodoContentValues(intent));
            }
            break;
            case ACTION_DELETE_TODO: {
                TodosDao.deleteTodoById(this, intent.getStringExtra(KEY_TODO_ID));
            }
            break;
            default:
                break;
        }
    }

    private ContentValues extractNoteContentValues(Intent intent) {
        return NotesDao.buildContentValues(intent.getStringExtra(KEY_NOTE_TITLE),
            intent.getStringExtra(KEY_NOTE_TEXT));
    }

    private ContentValues extractTodoContentValues(Intent intent) {
        return TodosDao.buildContentValues(intent.getStringExtra(KEY_TODO_TEXT),
            intent.getStringExtra(KEY_TODO_IS_DONE));
    }

    public static void addNote(Context context, String title, String text) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_ADD_NOTE);
        intentService.putExtra(KEY_NOTE_TITLE, title);
        intentService.putExtra(KEY_NOTE_TEXT, text);
        context.startService(intentService);
    }

    public static void editNote(Context context, String id, String title, String text) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_EDIT_NOTE);
        intentService.putExtra(KEY_NOTE_ID, id);
        intentService.putExtra(KEY_NOTE_TITLE, title);
        intentService.putExtra(KEY_NOTE_TEXT, text);
        context.startService(intentService);
    }

    public static void deleteNote(Context context, String id) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_DELETE_NOTE);
        intentService.putExtra(KEY_NOTE_ID, id);
        context.startService(intentService);
    }

    public static void addTodo(Context context, String text, boolean isDone) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_ADD_TODO);
        intentService.putExtra(KEY_TODO_TEXT, text);
        intentService.putExtra(KEY_TODO_IS_DONE, String.valueOf(isDone));
        context.startService(intentService);
    }

    public static void editTodo(Context context, String id, String text, boolean isDone) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_EDIT_TODO);
        intentService.putExtra(KEY_TODO_ID, id);
        intentService.putExtra(KEY_TODO_TEXT, text);
        intentService.putExtra(KEY_TODO_IS_DONE, String.valueOf(isDone));
        context.startService(intentService);
    }

    public static void deleteTodo(Context context, String id) {
        Intent intentService = new Intent(context, DatabaseIntentService.class);
        intentService.putExtra(ACTION, ACTION_DELETE_TODO);
        intentService.putExtra(KEY_TODO_ID, id);
        context.startService(intentService);
    }

}
