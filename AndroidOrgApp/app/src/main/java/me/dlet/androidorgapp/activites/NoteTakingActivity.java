package me.dlet.androidorgapp.activites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.bus.Bus;
import me.dlet.androidorgapp.bus.ConvertTodoEvent;
import me.dlet.androidorgapp.bus.NewEvent;
import me.dlet.androidorgapp.bus.UpdateEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Note;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class NoteTakingActivity extends AppCompatActivity{

    public static final String KEY_TYPE_OF_ACTION = "key_type_action";
    public static final String KEY_NOTE_OBJECT = "key_note_object";
    public static final String KEY_NOTE_POS = "key_note_position";

    public static final int TYPE_NEW = 0;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_CONVERT = 2;

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.etTitle)EditText etTitle;
    @BindView(R.id.etText)EditText etText;
    @BindView(R.id.inputLayoutText)TextInputLayout inputLayoutText;
    @BindView(R.id.inputLayoutTitle)TextInputLayout inputLayoutTitle;

    private int type = 0, position;
    private Unbinder unbinder;
    private Note note;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, NoteTakingActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type_value,
                                     Note note, int object_pos){
        Intent intent = new Intent(context, NoteTakingActivity.class);
        intent.putExtra(KEY_TYPE_OF_ACTION, type_value);
        intent.putExtra(KEY_NOTE_OBJECT, note);
        intent.putExtra(KEY_NOTE_POS, object_pos);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_taking);
        unbinder = ButterKnife.bind(this);
        Bus.getInstance().register(this);

        type=getIntent().getIntExtra(KEY_TYPE_OF_ACTION, 0);

        if (type==TYPE_UPDATE) {
            toolbar.setTitle(R.string.title_note_taking_update);
        } else if (type==TYPE_CONVERT) {
            toolbar.setTitle(R.string.title_note_convert);
        }else {
            toolbar.setTitle(R.string.title_note_taking_new);
        }

        if (getIntent().hasExtra(KEY_NOTE_OBJECT) && getIntent().hasExtra(KEY_NOTE_POS)){
            note = (Note)getIntent().getSerializableExtra(KEY_NOTE_OBJECT);
            etText.setText(note.getText());
            etTitle.setText(note.getTitle());
            position = getIntent().getIntExtra(KEY_NOTE_POS, 0);
        }

        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_takin_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.save:
                if (isFieldsNotEmpty())
                    save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void save(){
        if (type==TYPE_NEW) {
            Note note = new Note(etTitle.getText().toString(),
                    etText.getText().toString(), System.currentTimeMillis());
            long id = DaoHelper.getInstance().getNotesDao().insert(note);
            note.setId(id);
            Bus.getInstance().post(new NewEvent<>(note));
        } else if (type==TYPE_CONVERT){
            this.note.setTitle(etTitle.getText().toString());
            this.note.setText(etText.getText().toString());
            this.note.setLastUpdated(System.currentTimeMillis());
            long id = DaoHelper.getInstance().getNotesDao().insert(note);
            note.setId(id);
            Bus.getInstance().post(new NewEvent<>(note));
            Bus.getInstance().post(new ConvertTodoEvent(position));
        }else {
            this.note.setTitle(etTitle.getText().toString());
            this.note.setText(etText.getText().toString());
            this.note.setLastUpdated(System.currentTimeMillis());
            DaoHelper.getInstance().getNotesDao().update(note);
            Bus.getInstance().post(new UpdateEvent<>(note, position));
        }

        finish();
    }

    private boolean isFieldsNotEmpty(){
        if (etTitle.getText().toString().length()==0) {
            inputLayoutTitle.setError(getString(R.string.error_msg_cant_empty, "Title"));
            return false;
        }

        if (etText.getText().toString().length()==0) {
            inputLayoutText.setError(getString(R.string.error_msg_cant_empty, "Text"));
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Bus.getInstance().unregister(this);
        unbinder.unbind();
    }

}
