package com.yoyotest.h2owl.h2wltestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.yoyotest.h2owl.h2wltestapp.model.MyNote;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by h2owl on 16/04/25.
 */
public class NoteEditActivity extends Activity {

    private EditText noteTitle;
    private EditText noteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        ButterKnife.bind(this);

        this.noteTitle = (EditText) findViewById(R.id.note_edit_title);
        this.noteContent = (EditText) findViewById(R.id.note_edit_content);
    }

    @OnClick(R.id.button_note_save) public void onClickButtonSave(View view) {

        MyNote note = new MyNote();
        note.title = noteTitle.getText().toString();
        note.content = noteContent.getText().toString();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_note_cancel) public void onClickButtonCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
