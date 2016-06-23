package me.dlet.androidorgapp.views.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.bus.Bus;
import me.dlet.androidorgapp.bus.ConvertNoteEvent;
import me.dlet.androidorgapp.bus.DeleteEvent;
import me.dlet.androidorgapp.bus.NewEvent;
import me.dlet.androidorgapp.bus.UpdateEvent;
import me.dlet.androidorgapp.database.DaoHelper;
import me.dlet.androidorgapp.models.Note;
import me.dlet.androidorgapp.tasks.LoadNotesTask;
import me.dlet.androidorgapp.tasks.OnLoadListener;
import me.dlet.androidorgapp.views.adapters.NotesAdapter;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class NotesFragment extends Fragment{
    @BindView(R.id.rvNotes) RecyclerView rvNotes;
    @BindView(R.id.tvLabelEmpty) TextView tvLabelEmpty;
    private Unbinder unbinder;
    private NotesAdapter notesAdapter;

    private List<Note> noteList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notes, container, false);
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bus.getInstance().register(this);

        new LoadNotesTask(new OnLoadListener<Note>() {
            @Override
            public void onFinish(List<Note> notes) {
                noteList=notes;

                hideOrShowEmptyLabel();

                rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));
                notesAdapter = new NotesAdapter(getContext(), noteList);
                rvNotes.setAdapter(notesAdapter);
            }
        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Bus.getInstance().unregister(this);
        unbinder.unbind();
    }

    @Subscribe
    public void onNewNoteEvent(NewEvent newNoteEvent){
        if (newNoteEvent.getObject() instanceof Note) {
            noteList.add(0, (Note) newNoteEvent.getObject());
            notesAdapter.notifyDataSetChanged();

            hideOrShowEmptyLabel();
        }
    }

    @Subscribe
    public void onDeleteNoteEvent(DeleteEvent deleteNoteEvent){
        if (deleteNoteEvent.getObject() instanceof Note) {
            noteList.remove(deleteNoteEvent.getObject());
            notesAdapter.notifyDataSetChanged();

            hideOrShowEmptyLabel();
        }
    }

    @Subscribe
    public void onUpdateNoteEvent(UpdateEvent updateEvent){
        if (updateEvent.getObject() instanceof Note) {
            noteList.remove(updateEvent.getPosition());
            noteList.add(0, (Note) updateEvent.getObject());
            notesAdapter.notifyDataSetChanged();
        }
    }

    @Subscribe
    public void onConvertNoteEvent(ConvertNoteEvent convertEvent){
        Note note = noteList.get(convertEvent.getPosition());
        DaoHelper.getInstance().getNotesDao().delete(note.getId());
        noteList.remove(convertEvent.getPosition());
        notesAdapter.notifyDataSetChanged();

        hideOrShowEmptyLabel();
    }

    private void hideOrShowEmptyLabel(){
        if (noteList.size()>0)
            tvLabelEmpty.setVisibility(View.GONE);
        else
            tvLabelEmpty.setVisibility(View.VISIBLE);
    }
}
