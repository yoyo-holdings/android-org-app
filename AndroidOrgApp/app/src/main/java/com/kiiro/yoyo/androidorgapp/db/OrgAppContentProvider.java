package com.kiiro.yoyo.androidorgapp.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.widget.Toast;

public class OrgAppContentProvider extends ContentProvider {
    private static final String AUTHORITY = "com.kiiro.yoyo.androidorgapp.db.OrgAppContentProvider";

    public static final Uri NOTES_URI = Uri.parse("content://" + AUTHORITY + "/" + NotesContract.Notes.TABLE_NAME);
    public static final Uri TODOS_URI = Uri.parse("content://" + AUTHORITY + "/" + TodosContract.Todos.TABLE_NAME);

    private static final int NOTES_URI_ID = 1;
    private static final int NOTESID_URI_ID = 2;
    private static final int TODOS_URI_ID = 3;
    private static final int TODOID_URI_ID = 4;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, NotesContract.Notes.TABLE_NAME, NOTES_URI_ID);
        uriMatcher.addURI(AUTHORITY, NotesContract.Notes.TABLE_NAME + "/#", NOTESID_URI_ID);
        uriMatcher.addURI(AUTHORITY, TodosContract.Todos.TABLE_NAME, TODOS_URI_ID);
        uriMatcher.addURI(AUTHORITY, TodosContract.Todos.TABLE_NAME + "/#", TODOID_URI_ID);
    }

    private OrgAppDbHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new OrgAppDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor ret = null;
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case NOTES_URI_ID:
                qb.setTables(NotesContract.Notes.TABLE_NAME);
                ret = qb.query(dbHelper.getReadableDatabase(), new String[]{NotesContract.Notes._ID, NotesContract.Notes.COLUMN_NAME_CONTENT, NotesContract.Notes.COLUMN_NAME_DATE}, null, null, null, null, dbHelper.getSortOrder());
                return ret;
            case NOTESID_URI_ID:
                qb.setTables(NotesContract.Notes.TABLE_NAME);
                return ret;
            case TODOS_URI_ID:
                qb.setTables(TodosContract.Todos.TABLE_NAME);
                ret = qb.query(dbHelper.getReadableDatabase(), new String[]{NotesContract.Notes._ID, TodosContract.Todos.COLUMN_NAME_TASK, TodosContract.Todos.COLUMN_NAME_DATE, TodosContract.Todos.COLUMN_NAME_DONE}, null, null, null, null, null);
                return ret;
            case TODOID_URI_ID:
                qb.setTables(TodosContract.Todos.TABLE_NAME);
                return ret;
        }

        return ret;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.com.kiiro.yoyo.androidorgapp.db.OrgAppContentProvider";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri ret = null;

        long id;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NOTES_URI_ID:
                id = db.insert(NotesContract.Notes.TABLE_NAME, null, values);
                return Uri.parse(NOTES_URI + "/" + id);
            case NOTESID_URI_ID:
                return ret;
            case TODOS_URI_ID:
                id = db.insert(TodosContract.Todos.TABLE_NAME, null, values);
                return Uri.parse(NOTES_URI + "/" + id);
            case TODOID_URI_ID:
                return ret;
        }

        return ret;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int ret = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NOTES_URI_ID:
                return db.delete(NotesContract.Notes.TABLE_NAME, selection, selectionArgs);
            case NOTESID_URI_ID:
                return ret;
            case TODOS_URI_ID:
                return db.delete(TodosContract.Todos.TABLE_NAME, selection, selectionArgs);
            case TODOID_URI_ID:
                return ret;
        }

        return ret;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int ret = 0;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case NOTES_URI_ID:
                return db.update(NotesContract.Notes.TABLE_NAME, values, selection, selectionArgs);
            case NOTESID_URI_ID:
                return ret;
            case TODOS_URI_ID:
                return db.update(TodosContract.Todos.TABLE_NAME, values, selection, selectionArgs);
            case TODOID_URI_ID:
                return ret;
        }

        return ret;
    }
}
