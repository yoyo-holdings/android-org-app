package com.yoyotest.h2owl.h2wltestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yoyotest.h2owl.h2wltestapp.model.MyNote;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by h2owl on 16/04/25.
 */
public class NoteEditActivity extends Activity {

    private Realm realm;
    private RealmConfiguration realmConfiguration;

    private EditText noteTitle;
    private EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        ButterKnife.bind(this);

        this.noteTitle = (EditText) findViewById(R.id.note_edit_title);
        this.noteContent = (EditText) findViewById(R.id.note_edit_content);

        realmConfiguration = new RealmConfiguration.Builder(this).build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfiguration);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    @OnClick(R.id.button_note_save) public void onClickButtonSave(View view) {

//        MyNote note = new MyNote();
        int s = realm.allObjects(MyNote.class).size();
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        MyNote note = realm.createObject(MyNote.class);
        note.id = s + 1;
        note.title = noteTitle.getText().toString();
        note.content = noteContent.getText().toString();
        // When the transaction is committed, all changes a synced to disk.
        realm.commitTransaction();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_note_cancel) public void onClickButtonCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
