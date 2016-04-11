package com.example.noteandtodo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
        Log.d("APP", "NoteCreateActivity _id:"+_id);

        if(this._id != 0) {
            // restore existing subject and text
            NoteSQLiteOpenHelper helper = new NoteSQLiteOpenHelper(this);
            SQLiteDatabase db = helper.getReadableDatabase();
            /*
            String[] cols = {NoteSQLiteOpenHelper.COLUMN_SUBJECT, NoteSQLiteOpenHelper.COLUMN_TEXT};
            String selection = "_id = " + this._id;
            Cursor cursor = db.query(
                    NoteSQLiteOpenHelper.TABLE_NOTE,
                    cols, selection, null, null, null, null, null);
                    */
            String sql = "select " + NoteSQLiteOpenHelper.COLUMN_SUBJECT + "," +NoteSQLiteOpenHelper.COLUMN_TEXT
                    + " from " + NoteSQLiteOpenHelper.TABLE_NOTE
                    + " where _id = " + this._id;
            Cursor cursor = db.rawQuery(sql, null);
            cursor.moveToFirst();

            EditText subjectEdit = (EditText)findViewById(R.id.note_subject);
            subjectEdit.setText(cursor.getString(0));
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
        EditText subjectEdit = (EditText) findViewById(R.id.note_subject);
        String subject = subjectEdit.getText().toString();
        EditText textEdit = (EditText) findViewById(R.id.note_text);
        String text = textEdit.getText().toString();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = dateFormat.format(date);

        if (subject.equals("")) {
            Snackbar.make(v, "Subject is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        } else if (text.equals("")) {
            Snackbar.make(v, "Text is empty", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        NoteSQLiteOpenHelper helper = new NoteSQLiteOpenHelper(getApplicationContext());
        SQLiteDatabase db = helper.getWritableDatabase();

        if (this._id == 0) {
            // insert new record
            ContentValues values = new ContentValues();
            values.put(NoteSQLiteOpenHelper.COLUMN_SUBJECT, subject);
            values.put(NoteSQLiteOpenHelper.COLUMN_TEXT, text);
            values.put(NoteSQLiteOpenHelper.COLUMN_DATE, strDate);
            db.insertOrThrow(NoteSQLiteOpenHelper.TABLE_NOTE, null, values);
            finish();
        } else {
            //update existing record
            ContentValues values = new ContentValues();
            values.put(NoteSQLiteOpenHelper.COLUMN_SUBJECT, subject);
            values.put(NoteSQLiteOpenHelper.COLUMN_TEXT, text);
            values.put(NoteSQLiteOpenHelper.COLUMN_DATE, strDate);
            db.update(
                    NoteSQLiteOpenHelper.TABLE_NOTE,
                    values,
                    "_id = " + this._id,
                    null);
            finish();
        }
    }

}
