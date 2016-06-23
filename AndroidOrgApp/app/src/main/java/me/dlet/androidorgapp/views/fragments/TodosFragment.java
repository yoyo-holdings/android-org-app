package me.dlet.androidorgapp.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.bus.Bus;
import me.dlet.androidorgapp.bus.ConvertTodoEvent;
import me.dlet.androidorgapp.bus.DeleteEvent;
import me.dlet.androidorgapp.bus.NewEvent;
import me.dlet.androidorgapp.bus.UpdateEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Todo;
import me.dlet.androidorgapp.tasks.LoadTodosTask;
import me.dlet.androidorgapp.tasks.OnLoadListener;
import me.dlet.androidorgapp.views.adapters.TodosAdapter;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class TodosFragment extends Fragment{

    @BindView(R.id.rvTodos)RecyclerView rvTodos;
    @BindView(R.id.tvLabelEmpty) TextView tvLabelEmpty;
    private Unbinder unbinder;
    private TodosAdapter todosAdapter;

    private List<Todo> todoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todos, container, false);
        unbinder = ButterKnife.bind(this, v);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bus.getInstance().register(this);

        new LoadTodosTask(new OnLoadListener<Todo>() {
            @Override
            public void onFinish(List<Todo> objlist) {
                todoList = objlist;
                hideOrShowEmptyLabel();

                rvTodos.setLayoutManager(new LinearLayoutManager(getContext()));
                todosAdapter = new TodosAdapter(getContext(), todoList);
                rvTodos.setAdapter(todosAdapter);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus.getInstance().unregister(this);
        unbinder.unbind();
    }

    @Subscribe
    public void onNewTodoEvent(NewEvent<Todo> newTodoEvent){
        if (newTodoEvent.getObject() instanceof Todo) {
            todoList.add(newTodoEvent.getObject());
            todosAdapter.notifyDataSetChanged();

            hideOrShowEmptyLabel();
        }
    }

    @Subscribe
    public void onDeleteTodoEvent(DeleteEvent<Todo> deleteTodoEvent){
        if (deleteTodoEvent.getObject() instanceof Todo) {
            todoList.remove(deleteTodoEvent.getObject());
            todosAdapter.notifyDataSetChanged();

            hideOrShowEmptyLabel();
        }
    }

    @Subscribe
    public void onUpdateTodoEvent(UpdateEvent<Todo> updateEvent){
        if (updateEvent.getObject() instanceof Todo) {
            todoList.remove(updateEvent.getPosition());
            todoList.add(updateEvent.getObject());
            todosAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onConvertTodoEvent(ConvertTodoEvent convertEvent){
        Todo todo = todoList.get(convertEvent.getPosition());
        DaoHelper.getInstance().getTodosDao().delete(todo.getId());
        todoList.remove(convertEvent.getPosition());
        todosAdapter.notifyDataSetChanged();

        hideOrShowEmptyLabel();
    }

    private void hideOrShowEmptyLabel(){
        if (todoList.size()>0)
            tvLabelEmpty.setVisibility(View.GONE);
        else
            tvLabelEmpty.setVisibility(View.VISIBLE);
    }
}
