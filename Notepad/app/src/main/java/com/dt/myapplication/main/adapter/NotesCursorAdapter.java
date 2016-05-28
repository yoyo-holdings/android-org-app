package com.dt.myapplication.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.dt.myapplication.R;
import com.dt.myapplication.main.viewholder.NoteListItemViewHolder;
import com.dt.myapplication.model.Note;

/**
 * Created by DT on 27/05/2016.
 */
public class NotesCursorAdapter extends CursorAdapter {

    public static final int DEFAULT_MODE = 0;
    public static final int EDIT_MODE = 1;
    public static final int CONVERT_MODE = 2;

    Context mContext;
    int currentMode = 0;

    public NotesCursorAdapter(Context context) {
        super(context, null, false);
        this.mContext = context;
    }

    public void toggleEditMode() {
        if (currentMode != EDIT_MODE) {
            setMode(EDIT_MODE);
        } else {
            setMode(DEFAULT_MODE);
        }
        notifyDataSetChanged();
    }

    public void toggleConvertMode() {
        if (currentMode != CONVERT_MODE) {
            setMode(CONVERT_MODE);
        } else {
            setMode(DEFAULT_MODE);
        }
        notifyDataSetChanged();
    }

    private void setMode(int mode) {
        currentMode = mode;
    }

    public int getCurrentMode() {
        return currentMode;
    }

    @Override public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View newView = inflater.inflate(R.layout.listviewitem_note, viewGroup, false);
        NoteListItemViewHolder noteListItemViewHolder = new NoteListItemViewHolder(newView);
        newView.setTag(noteListItemViewHolder);
        return newView;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        NoteListItemViewHolder noteListItemViewHolder = (NoteListItemViewHolder) view.getTag();
        Note note = new Note(cursor);
        if(TextUtils.isEmpty(note.getTitle())) {
            noteListItemViewHolder.noteTitleTextView.setVisibility(View.GONE);
        } else {
            noteListItemViewHolder.noteTitleTextView.setText(note.getTitle());
            noteListItemViewHolder.noteTitleTextView.setVisibility(View.VISIBLE);
        }
        if(TextUtils.isEmpty(note.getText())) {
            noteListItemViewHolder.noteTextTextView.setVisibility(View.GONE);
        } else {
            noteListItemViewHolder.noteTextTextView.setText(note.getText());
            noteListItemViewHolder.noteTextTextView.setVisibility(View.VISIBLE);
        }
        switch (getCurrentMode()) {
            case DEFAULT_MODE:
                noteListItemViewHolder.noteEditModeIndicatorTextView.setVisibility(View.GONE);
                noteListItemViewHolder.noteConvertModeIndicatorTextView.setVisibility(View.GONE);
                break;
            case EDIT_MODE:
                noteListItemViewHolder.noteEditModeIndicatorTextView.setVisibility(View.VISIBLE);
                noteListItemViewHolder.noteConvertModeIndicatorTextView.setVisibility(View.GONE);
                break;
            case CONVERT_MODE:
                noteListItemViewHolder.noteEditModeIndicatorTextView.setVisibility(View.GONE);
                noteListItemViewHolder.noteConvertModeIndicatorTextView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
