package com.example.noteandtodo;

//import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
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


/**
 * A simple {@link ListFragment} subclass.
 * Use the {@link NoteListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteListFragment extends ListFragment {

    public static final String EXTRA_ID = "_ID";

//    private NoteListAdapter mAdapter;
    private SimpleCursorAdapter mAdapter;
    private Cursor mCursor;


/*
    private class NoteItem {
        private int id = -1;
        public String title = "";
        public String text = "";

        public NoteItem() {
        }

        public NoteItem(String title, String text) {
            this.title = title;
            this.text = text;
        }
    }

    private class NoteListAdapter extends ArrayAdapter<NoteItem> {
        public NoteListAdapter(Context context) {
            super(context, 0);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view;

            if(convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getContext());
                view = inflater.inflate(R.layout.note_item, parent, false);
            }
            else {
                view = convertView;
            }

            NoteItem noteItem = getItem(position);
            TextView textView = (TextView)view.findViewById(R.id.note_title);
            textView.setText(noteItem.title);

            return view;
        }

    }
    //
    // end NoteListAdapter
    //
*/

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
        View view =  inflater.inflate(R.layout.fragment_note_list, container, false);

        MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(MySQLiteOpenHelper.TABLE_NOTE,
                new String[]{MySQLiteOpenHelper.COLUMN_ID, MySQLiteOpenHelper.COLUMN_TITLE},
                null,
                null,
                null,
                null,
                MySQLiteOpenHelper.COLUMN_DATE + " DESC");
        this.mCursor = cursor;
        ListView list = (ListView)view.findViewById(android.R.id.list);
        this.mAdapter = new SimpleCursorAdapter(getActivity(),
                R.layout.note_item,
                cursor,
                new String[]{MySQLiteOpenHelper.COLUMN_TITLE, MySQLiteOpenHelper.COLUMN_ID},
                new int[]{R.id.note_title, R.id.note_id},
                0);

        list.setAdapter(mAdapter);
        registerForContextMenu(list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // TODO: better way to reflect db changes
        mCursor.requery();
        mAdapter.notifyDataSetChanged();
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
        switch (item.getItemId()) {
            case R.id.note_delete:
                // delete selected note
                AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
                View listItem = info.targetView;
                TextView idText = (TextView)listItem.findViewById(R.id.note_id);
                int _id = Integer.parseInt(idText.getText().toString());

                MySQLiteOpenHelper helper = new MySQLiteOpenHelper(getActivity());
                SQLiteDatabase db = helper.getWritableDatabase();
                db.delete(
                        MySQLiteOpenHelper.TABLE_NOTE,
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
