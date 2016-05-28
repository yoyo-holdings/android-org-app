package com.dt.myapplication.main.presenter.impl;

import com.dt.myapplication.main.activity.MainContainer;
import com.dt.myapplication.main.adapter.TabFragmentPagerAdapter;
import com.dt.myapplication.main.dialog.NoteDialog;
import com.dt.myapplication.main.dialog.TodoDialog;
import com.dt.myapplication.main.presenter.MainPresenter;
import com.dt.myapplication.model.Note;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 28/05/2016.
 */
public class MainPresenterImpl implements MainPresenter {

    private MainContainer mainContainer;
    private Boolean isFabOpen = false;

    public MainPresenterImpl(MainContainer mainContainer) {
        this.mainContainer = mainContainer;
    }

    @Override public void animateFAB() {
        if (isFabOpen) {
            mainContainer.closeAnimateFabs();
            isFabOpen = false;
        } else {
            mainContainer.openAnimateFabs();
            isFabOpen = true;
        }
    }

    @Override public void showAddDialog(int currentItem) {
        if (currentItem == TabFragmentPagerAdapter.NOTE_LIST_FRAGMENT_POSITION) {
            mainContainer.showNoteDialog(NoteDialog.MODE_ADD, createBlankNote());
        } else if (currentItem == TabFragmentPagerAdapter.TODO_LIST_FRAGMENT_POSITION) {
            mainContainer.showTodoDialog(TodoDialog.MODE_ADD, createBlankTodo());
        }
    }

    private Note createBlankNote() {
        return new Note("", "", "");
    }

    private Todo createBlankTodo() {
        return new Todo("", "", false);
    }

    @Override public void toggleEditMode(int currentItem) {
        if (currentItem == TabFragmentPagerAdapter.NOTE_LIST_FRAGMENT_POSITION) {
            mainContainer.toggleNoteListEditMode();
        } else if (currentItem == TabFragmentPagerAdapter.TODO_LIST_FRAGMENT_POSITION) {
            mainContainer.toggleTodoListEditMode();
        }
    }

    @Override public void toggleConvertMode(int currentItem) {
        if (currentItem == TabFragmentPagerAdapter.NOTE_LIST_FRAGMENT_POSITION) {
            mainContainer.toggleNoteListConvertMode();
        } else if (currentItem == TabFragmentPagerAdapter.TODO_LIST_FRAGMENT_POSITION) {
            mainContainer.toggleTodoListConvertMode();
        }
    }
}
