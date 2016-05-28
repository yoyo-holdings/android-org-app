package com.dt.myapplication.main.presenter.impl;

import com.dt.myapplication.main.adapter.TodosCursorAdapter;
import com.dt.myapplication.main.fragment.TodoListContainer;
import com.dt.myapplication.main.presenter.TodoListPresenter;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 28/05/2016.
 */
public class TodoListPresenterImpl implements TodoListPresenter {

    private TodoListContainer todoListContainer;

    public TodoListPresenterImpl(TodoListContainer todoListContainer) {
        this.todoListContainer = todoListContainer;
    }

    @Override public void onItemClick(Todo todo, int mode) {
        switch (mode) {
            case TodosCursorAdapter.DEFAULT_MODE:
                todoListContainer.toggleTodoIsDoneState(todo);
                break;
            case TodosCursorAdapter.EDIT_MODE:
                todoListContainer.showEditTodoDialog(todo);
                break;
            case TodosCursorAdapter.CONVERT_MODE:
                todoListContainer.showConvertNoteDialog(todo);
                break;
            default:
                break;
        }
    }
}
