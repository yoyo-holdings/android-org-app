package com.kiiro.yoyo.androidorgapp;

import android.database.Cursor;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kiiro.yoyo.androidorgapp.db.NotesContract;
import com.kiiro.yoyo.androidorgapp.db.TodosContract;
import com.kiiro.yoyo.androidorgapp.fragments.NotesListFragment;
import com.kiiro.yoyo.androidorgapp.fragments.TodosListFragment;
import com.kiiro.yoyo.androidorgapp.utils.NoteUtils;
import com.kiiro.yoyo.androidorgapp.utils.TodoUtils;

public class MainActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private NotesListFragment notesListFragment;
    private TodosListFragment todoListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (mViewPager.getCurrentItem()) {
                    case 0:
                        NoteUtils.startAddNoteOperation(MainActivity.this, notesListFragment);
                        break;
                    case 1:
                        TodoUtils.startAddTodoOperation(MainActivity.this, todoListFragment);
                        break;
                }
            }
        });

    }

    public void restartLoaders() {
        notesListFragment.restartLoader();
        todoListFragment.restartLoader();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (null == notesListFragment) {
                        notesListFragment = new NotesListFragment();
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.note_item, null, new String[]{NotesContract.Notes.COLUMN_NAME_CONTENT, NotesContract.Notes.COLUMN_NAME_DATE}, new int[]{android.R.id.text1, android.R.id.text2});
                        notesListFragment.setListAdapter(adapter);
                    }
                    return notesListFragment;
                case 1:
                    if (null == todoListFragment) {
                        todoListFragment = new TodosListFragment();
                        SimpleCursorAdapter adapter = new SimpleCursorAdapter(MainActivity.this, R.layout.task_item, null, new String[]{TodosContract.Todos.COLUMN_NAME_TASK, TodosContract.Todos.COLUMN_NAME_DATE, TodosContract.Todos.COLUMN_NAME_DONE, TodosContract.Todos._ID}, new int[]{android.R.id.text1, android.R.id.text2, R.id.checkbox});
                        adapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
                            @Override
                            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                                if (view.getId() == R.id.checkbox) {
                                    CheckBox cb = (CheckBox) view;
                                    view.setTag(cursor.getLong(0)); //_id
                                    cb.setChecked(cursor.getInt(3) > 0); //done
                                    cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @Override
                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                            long taskId = (long) buttonView.getTag();
                                            TodoUtils.toggleDone(MainActivity.this, isChecked, taskId);
                                        }
                                    });
                                    return true;
                                }
                                return false;
                            }
                        });
                        todoListFragment.setListAdapter(adapter);
                    }
                    return todoListFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "NOTES";
                case 1:
                    return "TO-DO";
            }
            return null;
        }
    }

}
