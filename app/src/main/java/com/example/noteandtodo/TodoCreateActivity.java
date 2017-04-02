package com.example.noteandtodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoCreateActivity extends AppCompatActivity
        implements View.OnClickListener
{
    private int _id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_create);

        Intent intent = getIntent();
        int _id = intent.getIntExtra(TodoListFragment.EXTRA_ID, 0);
        this._id = _id;

        if(this._id != 0) {
            // restore existing todo
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            String sql = "select " + MySQLiteOpenHelper.COLUMN_ENTRY
                    + " from " + MySQLiteOpenHelper.TABLE_TODO
                    + " where _id = " + this._id;
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            EditText entryEdit = (EditText)findViewById(R.id.todo_entry);
            entryEdit.setText(cursor.getString(0));
            cursor.close();
        }

        Button saveButton = (Button)findViewById(R.id.todo_save_button);
        saveButton.setOnClickListener(this);
    }

    // save button onClick
    @Override
    public void onClick(View v) {
        // get entry
        EditText entryEdit = (EditText)findViewById(R.id.todo_entry);
        String entry = entryEdit.getText().toString();
        // current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        // check if entry is empty
        if(entry.equals("")) {
            Snackbar.make(v, "Subject is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if(this._id == 0) {
            // insert new record
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_ENTRY, entry);
            values.put(MySQLiteOpenHelper.COLUMN_DONE, 0);
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            getContentResolver().insert(
                    MyContentProvider.CONTENT_URI_TODO,
                    values
            );
            finish();
        }
        else {
            // update existing record
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_ENTRY, entry);
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            getContentResolver().update(
                    MyContentProvider.CONTENT_URI_TODO,
                    values,
                    MySQLiteOpenHelper.COLUMN_ID + " = " + this._id,
                    null
            );
            finish();
        }
    }
}
