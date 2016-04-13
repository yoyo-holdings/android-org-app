package com.example.noteandtodo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link TodoListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TodoListFragment extends ListFragment
        implements LoaderCallbacks<Cursor>
{

    public static final String EXTRA_ID = "_ID";

    private TodoCursorAdapter mAdapter;
    private Cursor mCursor;

    private class TodoCursorAdapter extends SimpleCursorAdapter {
        public TodoCursorAdapter(Context context, int layout, Cursor c,
                String[] from, int[] to, int flags) {
            super(context, layout, c, from, to, flags);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = super.newView(context, cursor, parent);
            CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkbox_done);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox checkBox = (CheckBox) v;
                    int done = checkBox.isChecked() ? 1 : 0;
                    View parent = (View) v.getParent();
                    TextView idText = (TextView) parent.findViewById(R.id.todo_id);
                    int _id = Integer.parseInt(idText.getText().toString());

                    // update done or not
                    MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
                    SQLiteDatabase db = helper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(MySQLiteOpenHelper.COLUMN_DONE, done);
                    db.update(
                            MySQLiteOpenHelper.TABLE_TODO,
                            values,
                            MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                            null);

                }
            });
            return view;
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
        /*
        Cursor cursor = db.query(
                MySQLiteOpenHelper.TABLE_TODO,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_ENTRY, MySQLiteOpenHelper.COLUMN_DONE},
                null,
                null,
                null,
                null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        */
        Cursor cursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_TODO,
                //new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_ENTRY, MySQLiteOpenHelper.COLUMN_DONE},
                null,
                null, null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        this.mCursor = cursor;
        ListView listView = (ListView)view.findViewById(android.R.id.list);
        this.mAdapter = new TodoCursorAdapter(
                getActivity(),
                R.layout.todo_item,
                cursor,
                new String[]{MySQLiteOpenHelper.COLUMN_ENTRY,
                        MySQLiteOpenHelper.COLUMN_ID,
                        MySQLiteOpenHelper.COLUMN_DONE},
                new int[]{R.id.todo_entry, R.id.todo_id, R.id.checkbox_done},
                0);
        this.mAdapter.setViewBinder(new TodoCursorAdapter.ViewBinder() {
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if(columnIndex == 2) {
                    // convert done(0/1) to CheckBox state
                    boolean done = cursor.getInt(2) == 1 ? true : false;
                    ((CheckBox)view).setChecked(done);
                    return true;
                }
                return false;
            }
        });
        listView.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(1,null, this);

        registerForContextMenu(listView);

        return view;
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
        // avoid reactinc other fragments' context menu
        if (getUserVisibleHint() == false) {
            return false;
        }

        // get _id
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        View listItem = info.targetView;
        TextView idText = (TextView) listItem.findViewById(R.id.todo_id);
        int _id = Integer.parseInt(idText.getText().toString());

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (item.getItemId()) {
            case R.id.todo_delete:
                // delete selected todo
                /*
                db.delete(
                        MySQLiteOpenHelper.TABLE_TODO,
                        MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                        null
                );
                */
                getActivity().getContentResolver().delete(
                        MyContentProvider.CONTENT_URI_TODO,
                        MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                        null
                );

                return true;

            case R.id.todo_to_note:
                /*
                TextView entryText = (TextView) listItem.findViewById(R.id.todo_entry);
                String entry = entryText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date);

                // add TODO and delete note
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(MySQLiteOpenHelper.COLUMN_TITLE, entry);
                    values.put(MySQLiteOpenHelper.COLUMN_TEXT, "");
                    values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
                    db.insertOrThrow(MySQLiteOpenHelper.TABLE_NOTE, null, values);
                    db.delete(
                            MySQLiteOpenHelper.TABLE_TODO,
                            MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                            null
                    );
                    db.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    db.endTransaction();
                }

                mCursor.requery();
                mAdapter.notifyDataSetChanged();
                */
                getActivity().getContentResolver().call(
                        MyContentProvider.CONTENT_URI_TODO,
                        "convertTodoToNote",
                        ""+_id,
                        null
                );

                return true;
        }

        return false;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MyContentProvider.CONTENT_URI_TODO,
                null,
                null,
                null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        mAdapter.swapCursor(c);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursor) {
        mAdapter.swapCursor(null);
    }
}
