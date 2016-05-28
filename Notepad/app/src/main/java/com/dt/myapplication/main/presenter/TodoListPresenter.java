package com.dt.myapplication.main.presenter;

import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 28/05/2016.
 */
public interface TodoListPresenter {

    void onItemClick(Todo todo, int mode);

}
