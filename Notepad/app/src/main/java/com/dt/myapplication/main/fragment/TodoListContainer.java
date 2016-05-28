package com.dt.myapplication.main.fragment;

import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 28/05/2016.
 */
public interface TodoListContainer {

    void toggleTodoIsDoneState(Todo todo);

    void showEditTodoDialog(Todo todo);

    void showConvertNoteDialog(Todo todo);

    void toggleEditMode();

    void toggleConvertMode();
}
