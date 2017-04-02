package com.example.noteandtodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;


/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends ListFragment
        implements LoaderCallbacks<Cursor>
{

    public static final String EXTRA_ID = "_ID";

    private SimpleCursorAdapter mAdapter;

    public NoteListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NoteListFragment.
     */
    // TODO: Rename and change types and number of parameters
//    public static NoteFragment newInstance(String param1, String param2) {
    public static NoteListFragment newInstance() {
        NoteListFragment fragment = new NoteListFragment();
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
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        Cursor cursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_NOTE,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TITLE},
                null, null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");

        ListView list = (ListView) view.findViewById(android.R.id.list);
        this.mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.note_item,
                cursor,
                new String[]{MySQLiteOpenHelper.COLUMN_TITLE, MySQLiteOpenHelper.COLUMN_ID},
                new int[]{R.id.note_title, R.id.note_id},
                0);
        list.setAdapter(mAdapter);
        getActivity().getSupportLoaderManager().initLoader(0, null, this);

        registerForContextMenu(list);

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Go to NoteCreateActivity to read and edit
        TextView textView = (TextView)v.findViewById(R.id.note_id);
        int _id = Integer.parseInt(textView.getText().toString());
        Intent intent = new Intent(getActivity(), NoteCreateActivity.class);
        intent.putExtra(EXTRA_ID, _id);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_note, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // avoid reacting other fragments' context menu
        if(getUserVisibleHint() == false){
            return false;
        }
        // get longClicked note's _id
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        View listItem = info.targetView;
        TextView idText = (TextView)listItem.findViewById(R.id.note_id);
        int _id = Integer.parseInt(idText.getText().toString());

        switch (item.getItemId()) {
            case R.id.note_delete:
                // delete selected note
                getActivity().getContentResolver().delete(
                        MyContentProvider.CONTENT_URI_NOTE,
                        MySQLiteOpenHelper.COLUMN_ID + " = " +  _id,
                        null
                );

                return true;

            case R.id.note_to_todo:
                // convert note to todo
                getActivity().getContentResolver().call(
                        MyContentProvider.CONTENT_URI_NOTE,
                        "convertNoteToTodo",
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
                MyContentProvider.CONTENT_URI_NOTE,
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
