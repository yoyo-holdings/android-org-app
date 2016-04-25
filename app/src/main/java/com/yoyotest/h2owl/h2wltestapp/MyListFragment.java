package com.yoyotest.h2owl.h2wltestapp;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yoyotest.h2owl.h2wltestapp.model.MyNote;
import com.yoyotest.h2owl.h2wltestapp.model.MyRealmMigration;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by h2owl on 16/04/25.
 */
public class MyListFragment extends ListFragment {
    public static final String EXTRA_POSITION = "position";

    private Realm realm;
    private RealmConfiguration realmConfiguration;

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

        realmConfiguration = new RealmConfiguration.Builder(this.getActivity())
                .schemaVersion(0)
                .migration(new MyRealmMigration())
                .build();
        // Open the Realm for the UI thread.
        realm = Realm.getInstance(realmConfiguration);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        RealmResults<MyNote> myNotes = realm.where(MyNote.class).findAll();
        MyAdapter adapter = new MyAdapter(this.getContext(),myNotes.sort("date", Sort.DESCENDING));
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                return true;
            }
        });

        registerForContextMenu(listView);

        return view;
    }
}
