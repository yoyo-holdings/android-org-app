package com.yoyotest.h2owl.h2wltestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by h2owl on 16/04/25.
 */
public class NoteEditActivity extends Activity {

    private Button buttonSave;
    private Button buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        ButterKnife.bind(this);

        this.buttonSave = (Button)findViewById(R.id.button_note_save);
        this.buttonCancel = (Button)findViewById(R.id.button_note_cancel);
    }

    @OnClick(R.id.button_note_save) public void onClickButtonSave(View view) {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_note_cancel) public void onClickButtonCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
