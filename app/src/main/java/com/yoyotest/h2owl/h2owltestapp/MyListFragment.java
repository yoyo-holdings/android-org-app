package com.yoyotest.h2owl.h2owltestapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yoyotest.h2owl.h2owltestapp.model.MyNote;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by h2owl on 16/04/25.
 */
public class MyListFragment extends ListFragment {
    public static final String EXTRA_POSITION = "position";

    private Realm realm;

    private ListView listView;

    private int position;

    public MyListFragment() {
    }

    public static MyListFragment newInstance() {
        return new MyListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_main, container, false);

        realm = MainActivity.getRealm(this.getActivity());

        listView = (ListView) view.findViewById(android.R.id.list);
        RealmResults<MyNote> myNotes = realm.where(MyNote.class).findAll();
        MyAdapter adapter = new MyAdapter(this.getContext(),myNotes.sort("date", Sort.DESCENDING),this);
        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        return view;
    }

    public void onListCheckBoxCheckedChangeHandler(MyNote note) {
        realm.beginTransaction();
        note.state = (note.state + 1) % 2;
        realm.commitTransaction();
    }

    @Override
    public void onListItemClick(ListView listView, View view, int i, long l) {
        super.onListItemClick(listView, view, i, l);
        MyNote listItem = (MyNote)listView.getItemAtPosition(i);
        Intent intent = new Intent(this.getActivity(), NoteEditActivity.class);
        intent.putExtra(NoteEditActivity.EXTRA_NOTE_ID,listItem.id);
        this.startActivity(intent);
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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        View listItem = info.targetView;
        TextView idText = (TextView)listItem.findViewById(R.id.note_id);
        int noteId = Integer.parseInt(idText.getText().toString());
        MyNote note = realm.where(MyNote.class).equalTo("id",noteId).findFirst();

        switch (item.getItemId()) {
            case R.id.note_delete:
                // delete selected note
                realm.beginTransaction();
                note.deleteFromRealm();
                realm.commitTransaction();
                return true;

            case R.id.note_to_todo:
                // convert note to todo
                realm.beginTransaction();
                note.type = (note.type+1) % 2;
                realm.commitTransaction();

                return true;

        }
        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }
}
