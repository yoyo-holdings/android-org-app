package com.dt.myapplication.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.dt.myapplication.main.fragment.NoteListFragment;
import com.dt.myapplication.main.fragment.TodoListFragment;

/**
 * Created by DT on 27/05/2016.
 */
public class TabFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final int PAGE_COUNT = 2;
    public static final int NOTE_LIST_FRAGMENT_POSITION = 0;
    public static final int TODO_LIST_FRAGMENT_POSITION = 1;
    private String tabTitles[] = new String[] { "Notes", "ToDos" };
    private NoteListFragment noteListFragment;
    private TodoListFragment todoListFragment;

    public TabFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        noteListFragment = new NoteListFragment();
        todoListFragment = new TodoListFragment();
    }

    @Override public Fragment getItem(int position) {
        switch (position){
            case NOTE_LIST_FRAGMENT_POSITION:
                return noteListFragment;
            case TODO_LIST_FRAGMENT_POSITION:
                return todoListFragment;
            default:
                return null;
        }
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    public NoteListFragment getNoteListFragment() {
        return noteListFragment;
    }

    public TodoListFragment getTodoListFragment() {
        return todoListFragment;
    }
}
