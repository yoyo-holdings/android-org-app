package com.example.noteandtodo;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ContentProvider for note and todo
 *
 * Created on 2016/04/12.
 */
public class MyContentProvider extends ContentProvider{

    private static final int NOTE = 1;
    private static final int NOTE_ID = 2;
    private static final int TODO = 3;
    private static final int TODO_ID = 4;

    private static final String AUTHORITY = "com.example.noteandtodo.MyContentProvider";
    private static final String NOTE_PATH = "note";
    private static final String TODO_PATH = "todo";

    public static final Uri CONTENT_URI_NOTE =
            Uri.parse("content://" + AUTHORITY + "/" + NOTE_PATH);
    public static final Uri CONTENT_URI_TODO =
            Uri.parse("content://" + AUTHORITY + "/" + TODO_PATH);

    public static final String CONTENT_TYPE_NOTE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/note";
    public static final String CONTENT_ITEM_TYPE_NOTE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/note";
    public static final String CONTENT_TYPE_TODO = ContentResolver.CURSOR_DIR_BASE_TYPE + "/todo";
    public static final String CONTENT_ITEM_TYPE_TODO = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/todo";

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY, NOTE_PATH, NOTE);
        uriMatcher.addURI(AUTHORITY, NOTE_PATH + "/#", NOTE_ID);
        uriMatcher.addURI(AUTHORITY, TODO_PATH, TODO);
        uriMatcher.addURI(AUTHORITY, TODO_PATH + "/#", TODO_ID);
    }

    private MySQLiteOpenHelper mHelper;

    @Override
    public boolean onCreate() {
        mHelper = new MySQLiteOpenHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri,
                        String[] projection,
                        String selection,
                        String[] selectionArgs,
                        String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        switch (uriMatcher.match(uri)) {
            case NOTE:
            case NOTE_ID:
                builder.setTables(MySQLiteOpenHelper.TABLE_NOTE);
                break;
            case TODO:
            case TODO_ID:
                builder.setTables(MySQLiteOpenHelper.TABLE_TODO);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String insertTable;
        Uri contentUri;
        switch (uriMatcher.match(uri)){
            case NOTE:
                insertTable = MySQLiteOpenHelper.TABLE_NOTE;
                contentUri = CONTENT_URI_NOTE;
                break;
            case TODO:
                insertTable = MySQLiteOpenHelper.TABLE_TODO;
                contentUri = CONTENT_URI_TODO;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long rowId = db.insert(insertTable, null, values);
        if(rowId > 0) {
            Uri returnUri = ContentUris.withAppendedId(contentUri, rowId);
            getContext().getContentResolver().notifyChange(returnUri, null);
            return returnUri;
        }
        else {
            throw new IllegalArgumentException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int update(Uri uri,
                      ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count;
        String table = uri.getPathSegments().get(0);
        count = db.update(table, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count;
        switch (uriMatcher.match(uri)) {
            case NOTE:
            case NOTE_ID:
                count = db.delete(MySQLiteOpenHelper.TABLE_NOTE, selection, selectionArgs);
                break;
            case TODO:
            case TODO_ID:
                count = db.delete(MySQLiteOpenHelper.TABLE_TODO, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
     }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case NOTE:
                return CONTENT_TYPE_NOTE;
            case NOTE_ID:
                return CONTENT_ITEM_TYPE_NOTE;
            case TODO:
                return CONTENT_TYPE_TODO;
            case TODO_ID:
                return CONTENT_ITEM_TYPE_TODO;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        if(method.equals("convertNoteToTodo")) {
            int _id = Integer.parseInt(arg);
            this.convertNoteToTodo(_id);
            return null;
        }
        else if(method.equals("convertTodoToNote")) {
            int _id = Integer.parseInt(arg);
            this.convertTodoToNote(_id);
            return null;
        }
        return null;
    }

    /**
     * convert Note to Todo using transaction
     *
     * @param _id _id of the note to convert
     */
    public void convertNoteToTodo(int _id) {
        // get note title by _id
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select " + MySQLiteOpenHelper.COLUMN_TITLE
                + " from " + MySQLiteOpenHelper.TABLE_NOTE
                + " where " + MySQLiteOpenHelper.COLUMN_ID + " = " + _id;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String title = cursor.getString(0);
        cursor.close();

        // current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_ENTRY, title);
            values.put(MySQLiteOpenHelper.COLUMN_DONE, 0);
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            // insert new todo
            db.insertOrThrow(MySQLiteOpenHelper.TABLE_TODO, null, values);
            // delete note
            db.delete(
                    MySQLiteOpenHelper.TABLE_NOTE,
                    MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                    null
            );
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI_NOTE, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI_TODO, null);
    }

    /**
     * Convert Todo to Note using transaction.
     *
     * @param _id _id of the todo to convert
     */
    public void convertTodoToNote(int _id) {
        // get todo entry by _id
        SQLiteDatabase db = mHelper.getWritableDatabase();
        String sql = "select " + MySQLiteOpenHelper.COLUMN_ENTRY
                + " from " + MySQLiteOpenHelper.TABLE_TODO
                + " where " + MySQLiteOpenHelper.COLUMN_ID + " = " + _id;
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        String entry = cursor.getString(0);
        cursor.close();

        // current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_TITLE, entry);
            values.put(MySQLiteOpenHelper.COLUMN_TEXT, "");
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            // insert new note
            db.insertOrThrow(MySQLiteOpenHelper.TABLE_NOTE, null, values);
            // delete todo
            db.delete(
                    MySQLiteOpenHelper.TABLE_TODO,
                    MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                    null
            );
            db.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI_NOTE, null);
        getContext().getContentResolver().notifyChange(CONTENT_URI_TODO, null);
    }

}
