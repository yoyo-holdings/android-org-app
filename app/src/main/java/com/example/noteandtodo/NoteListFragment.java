package com.example.noteandtodo;

//import android.app.ListFragment;
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
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends ListFragment
        implements LoaderCallbacks<Cursor>
{

    public static final String EXTRA_ID = "_ID";

//    private NoteListAdapter mAdapter;
    private SimpleCursorAdapter mAdapter;
    private Cursor mCursor;

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

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        /*
        Cursor cursor = db.query(MySQLiteOpenHelper.TABLE_NOTE,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TITLE},
                null,
                null,
                null,
                null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        */
        Cursor cursor = getActivity().getContentResolver().query(
                MyContentProvider.CONTENT_URI_NOTE,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TITLE},
                null, null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        this.mCursor = cursor;
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
        // get _id
        AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
        View listItem = info.targetView;
        TextView idText = (TextView)listItem.findViewById(R.id.note_id);
        int _id = Integer.parseInt(idText.getText().toString());

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        switch (item.getItemId()) {
            case R.id.note_delete:
                // delete selected note
                /*
                db.delete(
                        MySQLiteOpenHelper.TABLE_NOTE,
                        MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                        null
                );
                */
                getActivity().getContentResolver().delete(
                        MyContentProvider.CONTENT_URI_NOTE,
                        MySQLiteOpenHelper.COLUMN_ID + " = " +  _id,
                        null
                );

                return true;

            case R.id.note_to_todo:
                /*
                TextView titleText = (TextView)listItem.findViewById(R.id.note_title);
                String title = titleText.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String strDate = dateFormat.format(date);

                // add TODO and delete note
                db.beginTransaction();
                try {
                    ContentValues values = new ContentValues();
                    values.put(MySQLiteOpenHelper.COLUMN_ENTRY, title);
                    values.put(MySQLiteOpenHelper.COLUMN_DONE, 0);
                    values.put(MySQLiteOpenHelper.COLUMN_DATE, strDate);
                    db.insertOrThrow(MySQLiteOpenHelper.TABLE_TODO, null, values);
                    db.delete(
                            MySQLiteOpenHelper.TABLE_NOTE,
                            MySQLiteOpenHelper.COLUMN_ID + " = " + _id,
                            null
                    );
                    db.setTransactionSuccessful();
                } catch (Exception e) {

                } finally {
                    db.endTransaction();
                }
                */
                //TextView idText = (TextView)listItem.findViewById(R.id.note_id);
                //int _id = Integer.parseInt(idText.getText().toString());

                getActivity().getContentResolver().call(
                        MyContentProvider.CONTENT_URI_NOTE,
                        "convertNoteToTodo",
                        ""+_id,
                        null
                );
                //MyContentProvider provider = new MyContentProvider();
                //provider.convertNoteToTodo(_id);


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
