package com.dt.myapplication.main.presenter;

/**
 * Created by DT on 28/05/2016.
 */
public interface MainPresenter {

    void animateFAB();

    void showAddDialog(int currentItem);

    void toggleEditMode(int currentItem);

    void toggleConvertMode(int currentItem);

}
