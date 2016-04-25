package com.yoyotest.h2owl.h2wltestapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.yoyotest.h2owl.h2wltestapp.model.MyNote;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by h2owl on 16/04/25.
 */
public class NoteEditActivity extends Activity {
    public static final String EXTRA_NOTE_ID = "note_id";
    public static final int STATE_DEFAULT  = 0;
    public static final int STATE_FINISHED = 1;
    public static final int STATE_PENDING  = 2;

    private Realm realm;
    private RealmConfiguration realmConfiguration;
    private MyNote note;

    private EditText noteTitle;
    private EditText noteContent;
    private Spinner groupSpinner;
    private Switch taskSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        ButterKnife.bind(this);

        this.noteTitle = (EditText) findViewById(R.id.note_edit_title);
        this.noteContent = (EditText) findViewById(R.id.note_edit_content);
        this.groupSpinner = (Spinner) findViewById(R.id.note_edit_group_spinner);
        this.taskSwitch = (Switch) findViewById(R.id.note_edit_switch_task);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_spinner_note_groups_default, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        this.groupSpinner.setAdapter(adapter);

        realm = MainActivity.getRealm(this);

        Intent intent = getIntent();
        long id = intent.getLongExtra(EXTRA_NOTE_ID, -1);

        if (id != -1) {
            note = realm.where(MyNote.class).equalTo("id",id).findFirst();
            this.noteTitle.setText(note.title);
            this.noteContent.setText(note.content);
            this.taskSwitch.setChecked(note.type == 1);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    @OnClick(R.id.button_note_save) public void onClickButtonSave(View view) {
        String noteTitleStr = noteTitle.getText().toString();
        if (noteTitleStr.isEmpty() || noteTitleStr.equals("")) {
            closeKeyBoard(noteTitle);
            Snackbar.make(view, "Please enter the title.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return;
        }

        int num = realm.allObjects(MyNote.class).size();
        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();
        if (note == null) {
            note = realm.createObject(MyNote.class);
            note.id = num + 1;
            note.state = STATE_DEFAULT;
        }
        note.group = 0;
        note.type = this.taskSwitch.isChecked() ? 1 : 0;
        note.date = System.currentTimeMillis();
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

    private void closeKeyBoard(EditText editText) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.getApplicationContext().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
}
