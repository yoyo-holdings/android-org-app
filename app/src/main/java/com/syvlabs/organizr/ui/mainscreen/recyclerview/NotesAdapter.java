package com.syvlabs.organizr.ui.mainscreen.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.db.objects.Entry;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NoteViewHolder> {

    List<Entry> dataSrc;
    NoteViewHolder.ClickListener clickListener;

    public NotesAdapter(List<Entry> dataSrc, NoteViewHolder.ClickListener clickListener) {
        this.dataSrc = dataSrc;
        this.clickListener = clickListener;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notes, parent, false);
        return new NoteViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        holder.bindData(dataSrc.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSrc.size();
    }
}
