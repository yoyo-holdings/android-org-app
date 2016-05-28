package com.dt.myapplication.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.dt.myapplication.data.helper.DatabaseHelper;
import com.dt.myapplication.data.table.TodosTable;

/**
 * Created by DT on 26/05/2016.
 */
public class TodosProvider extends ContentProvider {
    public static final int IDS = 0;
    public static final int ID = 1;
    private static final String AUTHORITY = "com.dt.myapplication.data.provider.TodosProvider";
    private static final String BASE_PATH = "todos";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, IDS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", ID);
    }

    DatabaseHelper mDatabaseHelper;
    SQLiteDatabase mDatabase;

    @Override public boolean onCreate() {
        mDatabaseHelper = DatabaseHelper.getInstance(getContext());
        mDatabase = mDatabaseHelper.getWritableDatabase();
        return (mDatabase != null);
    }

    @Override public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            // ---get all conversation---
            case IDS:
                return "vnd.android.cursor.dir/com.dt.myapplication.notes";
            // ---get a particular conversation---
            case ID:
                return "vnd.android.cursor.item/com.dt.myapplication.notes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
        String sortOrder) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(TodosTable.TABLE_NAME);
        int uri_type = sURIMatcher.match(uri);
        switch (uri_type) {
            case IDS:
                break;
            case ID:
                builder.appendWhere(TodosTable.ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor =
            builder.query(mDatabase, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override public Uri insert(Uri uri, ContentValues values) {
        long rowID = mDatabase.insertWithOnConflict(TodosTable.TABLE_NAME, null, values,
            SQLiteDatabase.CONFLICT_REPLACE);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (sURIMatcher.match(uri)) {
            case IDS:
                count = mDatabase.update(TodosTable.TABLE_NAME, values, selection, selectionArgs);
                break;
            case ID:
                count = mDatabase.update(TodosTable.TABLE_NAME, values,
                    TodosTable.ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" +
                            selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        int rowsAffected;
        switch (uriType) {
            case IDS:
                rowsAffected = mDatabase.delete(TodosTable.TABLE_NAME, selection, selectionArgs);
                break;
            case ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsAffected =
                        mDatabase.delete(TodosTable.TABLE_NAME, TodosTable.ID + "=" + id, null);
                } else {
                    rowsAffected = mDatabase.delete(TodosTable.TABLE_NAME,
                        selection + " and " + TodosTable.ID + "=" + id, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }
}
