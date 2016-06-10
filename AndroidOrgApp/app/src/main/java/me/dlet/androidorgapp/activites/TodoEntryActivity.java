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
import me.dlet.androidorgapp.bus.ConvertNoteEvent;
import me.dlet.androidorgapp.bus.NewEvent;
import me.dlet.androidorgapp.bus.UpdateEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Todo;

/**
 * Created by darwinlouistoledo on 6/7/16.
 * Email: darwin.louis@ymail.com
 */
public class TodoEntryActivity extends AppCompatActivity{

    public static final String KEY_TYPE_OF_ACTION = "key_type_action";
    public static final String KEY_TODO_OBJECT = "key_todo_object";
    public static final String KEY_TODO_POS = "key_todo_position";

    public static final int TYPE_NEW = 0;
    public static final int TYPE_UPDATE = 1;
    public static final int TYPE_CONVERT = 2;

    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.etEntry)EditText etEntry;
    @BindView(R.id.inputLayoutEntry)TextInputLayout inputLayoutEntry;

    private int type = TYPE_NEW, position;
    private Unbinder unbinder;
    private Todo todo;

    public static void startActivity(Context context){
        Intent intent = new Intent(context, TodoEntryActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, int type_value,
                                     Todo todo, int object_pos){
        Intent intent = new Intent(context, TodoEntryActivity.class);
        intent.putExtra(KEY_TYPE_OF_ACTION, type_value);
        intent.putExtra(KEY_TODO_OBJECT, todo);
        intent.putExtra(KEY_TODO_POS, object_pos);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_entry);
        unbinder = ButterKnife.bind(this);
        Bus.getInstance().register(this);

        type=getIntent().getIntExtra(KEY_TYPE_OF_ACTION, 0);

        if (type==TYPE_UPDATE) {
            toolbar.setTitle(R.string.title_todo_update);
        } else if (type==TYPE_CONVERT) {
            toolbar.setTitle(R.string.title_todo_convert);
        }else {
            toolbar.setTitle(R.string.title_todo_new);
        }

        if (getIntent().hasExtra(KEY_TODO_OBJECT) && getIntent().hasExtra(KEY_TODO_POS)){
            todo = (Todo) getIntent().getSerializableExtra(KEY_TODO_OBJECT);
            etEntry.setText(todo.getTodo());
            position = getIntent().getIntExtra(KEY_TODO_POS, 0);
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
            Todo todo = new Todo(etEntry.getText().toString(), false, System.currentTimeMillis());
            long id = DaoHelper.getInstance().getTodosDao().insert(todo);
            todo.setId(id);
            Bus.getInstance().post(new NewEvent<>(todo));
        } else if (type==TYPE_CONVERT){
            this.todo.setTodo(etEntry.getText().toString());
            this.todo.setDone(false);
            this.todo.setLastUpdated(System.currentTimeMillis());
            long id = DaoHelper.getInstance().getTodosDao().insert(todo);
            todo.setId(id);
            Bus.getInstance().post(new NewEvent<>(todo));
            Bus.getInstance().post(new ConvertNoteEvent(position));
        }else {
            this.todo.setTodo(etEntry.getText().toString());
            this.todo.setDone(todo.isDone());
            this.todo.setLastUpdated(System.currentTimeMillis());
            DaoHelper.getInstance().getTodosDao().update(todo);
            Bus.getInstance().post(new UpdateEvent<>(todo, position));
        }

        finish();
    }

    private boolean isFieldsNotEmpty(){
        if (etEntry.getText().toString().length()==0) {
            inputLayoutEntry.setError(getString(R.string.error_msg_cant_empty, "Entry"));
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
