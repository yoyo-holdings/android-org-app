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

public class NoteCreateActivity extends AppCompatActivity
        implements View.OnClickListener
{

    private int _id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_create);

        Intent intent = getIntent();
        int _id = intent.getIntExtra(NoteListFragment.EXTRA_ID, 0);
        this._id = _id;

        if(this._id != 0) {
            // restore existing title and text
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();

            String sql = "select " + MySQLiteOpenHelper.COLUMN_TITLE + "," + MySQLiteOpenHelper.COLUMN_TEXT
                    + " from " + MySQLiteOpenHelper.TABLE_NOTE
                    + " where _id = " + this._id;
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            EditText titleEdit = (EditText)findViewById(R.id.note_title);
            titleEdit.setText(cursor.getString(0));
            EditText textEdit = (EditText)findViewById(R.id.note_text);
            textEdit.setText(cursor.getString(1));
            cursor.close();
        }

        Button saveButton = (Button)findViewById(R.id.note_save_button);
        saveButton.setOnClickListener(this);
    }

    // save button onClick
    @Override
    public void onClick(View v) {
        // get title
        EditText titleEdit = (EditText) findViewById(R.id.note_title);
        String title = titleEdit.getText().toString();
        // get text
        EditText textEdit = (EditText) findViewById(R.id.note_text);
        String text = textEdit.getText().toString();
        // current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        // check if title or text is empty
        if (title.equals("")) {
            Snackbar.make(v, "Title is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        if (this._id == 0) {
            // insert new record
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_TITLE, title);
            values.put(MySQLiteOpenHelper.COLUMN_TEXT, text);
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            getContentResolver().insert(
                    MyContentProvider.CONTENT_URI_NOTE,
                    values
            );
            finish();
        } else {
            // update existing record
            ContentValues values = new ContentValues();
            values.put(MySQLiteOpenHelper.COLUMN_TITLE, title);
            values.put(MySQLiteOpenHelper.COLUMN_TEXT, text);
            values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
            getContentResolver().update(
                    MyContentProvider.CONTENT_URI_NOTE,
                    values,
                    MySQLiteOpenHelper.COLUMN_ID + " = " + this._id,
                    null
            );
            finish();
        }
    }

}
