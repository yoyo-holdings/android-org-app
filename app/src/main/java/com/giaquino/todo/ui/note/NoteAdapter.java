package com.giaquino.todo.ui.note;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.giaquino.todo.common.widget.NoteItemView;
import com.giaquino.todo.model.entity.Note;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteItemViewHolder> {

    public interface NoteAdapterListener {
        void onNoteClicked(@NonNull Note note);
    }

    private List<Note> notes = new ArrayList<>();
    private LayoutInflater inflater;
    private NoteAdapterListener listener;

    public NoteAdapter(@NonNull Context context, @NonNull NoteAdapterListener listener) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override public NoteItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NoteItemViewHolder(NoteItemView.create(inflater, parent));
    }

    @Override public void onBindViewHolder(NoteItemViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.view.setTitle(note.title());
        holder.view.setText(note.text());
        holder.view.setOnClickListener(v -> listener.onNoteClicked(note));
    }

    @Override public int getItemCount() {
        return notes.size();
    }

    public void setNotes(List<Note> notes) {
        this.notes.clear();
        this.notes.addAll(notes);
    }

    public static class NoteItemViewHolder extends RecyclerView.ViewHolder {

        private NoteItemView view;

        public NoteItemViewHolder(NoteItemView itemView) {
            super(itemView);
            view = itemView;
        }
    }
}
