package com.yoyo.exam.pdatu.myapplication.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yoyo.exam.pdatu.myapplication.R;
import com.yoyo.exam.pdatu.myapplication.adapters.NoteListAdapter;
import com.yoyo.exam.pdatu.myapplication.models.Note;

import java.util.Collections;
import java.util.List;

public class NotesFragment extends Fragment {

    private View mRootView;
    private List<Note> noteList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private TextView mAddNote;

    private Context mainContext;

    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_notes, container, false);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        noteList = Note.listAll(Note.class);
        Collections.sort(noteList);

        mAddNote = (TextView) mRootView.findViewById(R.id.notes_new);
        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.note_list);

        mLayoutManager = new LinearLayoutManager(mainContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new NoteListAdapter(noteList, mainContext);
        mRecyclerView.setAdapter(mAdapter);

        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i<noteList.size(); i++) {
                    if (noteList.get(i).isEditMode()) {
                        return;
                        //noteList.get(i).setEditMode(false);
                    }
                }
                Note note = new Note("","");
                noteList.add(note);
                Collections.sort(noteList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainContext = null;
    }
}
