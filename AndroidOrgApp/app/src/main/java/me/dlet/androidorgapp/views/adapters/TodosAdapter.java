package me.dlet.androidorgapp.views.adapters;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.activites.NoteTakingActivity;
import me.dlet.androidorgapp.activites.TodoEntryActivity;
import me.dlet.androidorgapp.bus.Bus;
import me.dlet.androidorgapp.bus.DeleteEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Note;
import me.dlet.androidorgapp.models.Todo;
import me.dlet.androidorgapp.utils.DateTimeUtils;

/**
 * Created by darwinlouistoledo on 6/6/16.
 * Email: darwin.louis@ymail.com
 */
public class TodosAdapter extends RecyclerView.Adapter<TodosAdapter.TodosViewhHolder> {

    private Context context;
    private List<Todo> todos;

    public TodosAdapter(Context context, List<Todo> todos) {
        this.context = context;
        this.todos = todos;
    }

    @Override
    public TodosViewhHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        TodosViewhHolder todosViewhHolder = new TodosViewhHolder(context, v);
        return todosViewhHolder;
    }

    @Override
    public void onBindViewHolder(TodosViewhHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.setPosition(position);
        holder.setTodo(todo);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public static class TodosViewhHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.tvTodoText) TextView tvTodoText;
        @BindView(R.id.cbTodoCheck) CheckBox checkBox;

        private Context context;
        private int pos;
        private Todo todo;

        public TodosViewhHolder(Context context, View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.context = context;
        }

        @OnCheckedChanged(R.id.cbTodoCheck)
        public void onCheckChanged(boolean checked){
            if (todo.isDone()!=checked) {
                todo.setDone(checked);
                DaoHelper.getInstance().getTodosDao().update(todo);
                Toast.makeText(context, context.getString(R.string.toast_todo_success_updated), Toast.LENGTH_LONG).show();
            }
        }

        @OnClick(R.id.btnMoreAction)
        public void onPopUpMenu(View v){
            showPopUpMenu(v);
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.update:
                    editTODO();
                    break;
                case R.id.delete:
                    deleteTODO();
                    break;
                case R.id.convert:
                    convertNoteToTODO();
            }

            return true;
        }

        private void showPopUpMenu(View v){
            PopupMenu popup = new PopupMenu(this.context, v);
            popup.setOnMenuItemClickListener(this);
            popup.inflate(R.menu.action_todo_menu);
            popup.show();
        }

        private void deleteTODO(){
            DaoHelper.getInstance().getTodosDao().delete(todo.getId());
            Bus.getInstance().post(new DeleteEvent<>(todo));
        }

        private void editTODO(){
            TodoEntryActivity.startActivity(context,
                    TodoEntryActivity.TYPE_UPDATE,todo,pos);
        }

        private void convertNoteToTODO(){
            Note note = new Note("", todo.getTodo(), todo.getLastUpdated());
            NoteTakingActivity.startActivity(context,
                    NoteTakingActivity.TYPE_CONVERT,note,pos);
        }

        public void setPosition(int position) {
            this.pos = position;
        }

        public void setTodo(Todo todo) {
            this.todo = todo;
            tvTodoText.setText(todo.getTodo());
            tvTime.setText(DateTimeUtils.format(todo.getLastUpdated()));
            checkBox.setChecked(todo.isDone());
        }
    }
}
