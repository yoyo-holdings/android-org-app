package com.example.noteandtodo;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TodoListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends ListFragment {

    public static final String EXTRA_ID = "_ID";

    private SimpleCursorAdapter mAdapter;
    private Cursor mCursor;

    private class TodoCursorAdapter extends SimpleCursorAdapter {
        public TodoCursorAdapter(Context context, int layout, Cursor c,
                String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

     }


    public TodoListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TodoListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TodoListFragment newInstance() {
        TodoListFragment fragment = new TodoListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_todo_list, container, false);

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(
                MySQLiteOpenHelper.TABLE_TODO,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_ENTRY, MySQLiteOpenHelper.COLUMN_DONE},
                null,
                null,
                null,
                null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        this.mCursor = cursor;
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        this.mAdapter = new SimpleCursorAdapter(
                getActivity(),
                R.layout.todo_item,
                cursor,
                new String[]{MySQLiteOpenHelper.COLUMN_ENTRY, MySQLiteOpenHelper.COLUMN_ID},
                new int[]{R.id.todo_entry, R.id.todo_id},
                0);
        listView.setAdapter(mAdapter);
        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: better way than deprecated method
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        TextView textView = (TextView)v.findViewById(R.id.todo_id);
        int _id = Integer.parseInt(textView.getText().toString());
        Intent intent = new Intent(getActivity(), TodoCreateActivity.class);
        intent.putExtra(EXTRA_ID, _id);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_todo, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.todo_delete:
                // delete selected todo
                AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
                View listItem = info.targetView;
                TextView idText = (TextView)listItem.findViewById(R.id.todo_id);
                int _id = Integer.parseInt(idText.getText().toString());

                MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(
                        MySQLiteOpenHelper.TABLE_TODO,
                        MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                        null
                );

                mCursor.requery();
                mAdapter.notifyDataSetChanged();

                return true;
        }

        return false;
    }
}
