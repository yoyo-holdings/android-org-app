package com.dt.myapplication.main.activity;

import com.dt.myapplication.model.Note;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 28/05/2016.
 */
public interface MainContainer {

    void openAnimateFabs();

    void closeAnimateFabs();

    void showNoteDialog(int mode, Note note);

    void showTodoDialog(int mode, Todo todo);

    void toggleNoteListEditMode();

    void toggleTodoListEditMode();

    void toggleNoteListConvertMode();

    void toggleTodoListConvertMode();

}
