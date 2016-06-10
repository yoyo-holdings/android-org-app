package me.dlet.androidorgapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createNotesTable(db);
        createTodosTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void createNotesTable(SQLiteDatabase db){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ")
                .append(Constants.Notes.TABLE_NAME)
                .append(" (")
                .append(Constants.Notes.Columns.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,  ")
                .append(Constants.Notes.Columns.TITLE).append(" TEXT,  ")
                .append(Constants.Notes.Columns.TEXT).append(" TEXT,  ")
                .append(Constants.Notes.Columns.LASTUPDATED).append(" INT")
                .append(");");
        db.execSQL(query.toString());
    }

    private void createTodosTable(SQLiteDatabase db){
        StringBuilder query = new StringBuilder();
        query.append("CREATE TABLE IF NOT EXISTS ")
                .append(Constants.Todos.TABLE_NAME)
                .append(" (")
                .append(Constants.Todos.Columns.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,  ")
                .append(Constants.Todos.Columns.TODO).append(" TEXT,  ")
                .append(Constants.Todos.Columns.IS_DONE).append(" INT,  ")
                .append(Constants.Todos.Columns.LASTUPDATED).append(" INT")
                .append(");");
        db.execSQL(query.toString());
    }

    public static final class Constants{
        public static final String DATABASE_NAME ="android_org_app.db";
        public static final int DATABASE_VERSION=1;

        public static class Notes{
            public static final String TABLE_NAME ="notes";

            public static class Columns{
                public static final String ID = "_id";
                public static final String LASTUPDATED="last_updated";
                public static final String TITLE="title";
                public static final String TEXT="text";
            }
        }

        public static class Todos{
            public static final String TABLE_NAME ="todos";

            public static class Columns{
                public static final String ID = "_id";
                public static final String LASTUPDATED="last_updated";
                public static final String IS_DONE="is_done";
                public static final String TODO="todo";
            }
        }

    }
}
