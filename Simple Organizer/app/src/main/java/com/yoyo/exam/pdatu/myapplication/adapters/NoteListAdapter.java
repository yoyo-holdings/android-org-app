package com.yoyo.exam.pdatu.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoyo.exam.pdatu.myapplication.R;
import com.yoyo.exam.pdatu.myapplication.models.Note;

import java.util.Collections;
import java.util.List;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.ViewHolder> {
    private List<Note> mNoteList;
    private Context mContext;
    private Note mTempNote;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText mTitle;
        public EditText mMessage;
        public TextView mDate;
        public ImageView mMore;
        public ImageView mDone;

        public ViewHolder(View v) {
            super(v);
            mTitle = (EditText) v.findViewById(R.id.note_title);
            mMessage = (EditText) v.findViewById(R.id.note_message);
            mDate = (TextView) v.findViewById(R.id.note_date);
            mMore = (ImageView) v.findViewById(R.id.note_more);
            mDone = (ImageView) v.findViewById(R.id.note_done);
        }
    }

    public NoteListAdapter(List<Note> mNoteList, Context mainContext) {
        this.mNoteList = mNoteList;
        mContext = mainContext;
    }

    @Override
    public NoteListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_note, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTitle.setText(mNoteList.get(position).getTitle());
        holder.mMessage.setText(mNoteList.get(position).getMessage());
        holder.mDate.setText(mNoteList.get(position).getDateModified());

        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(mContext, holder.mMore);
                popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_edit) {
                            for (int i = 0; i < mNoteList.size(); i++) {
                                if (mNoteList.get(i).isEditMode()) {
                                    return true;
                                }
                            }
                            mNoteList.get(position).setEditMode(true);
                            notifyDataSetChanged();
                        } else if (item.getItemId() == R.id.action_delete) {
                            mTempNote = new Note(mNoteList.get(position));
                            mNoteList.remove(position).delete();
                            notifyDataSetChanged();
                            Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(R.id.fragment_container),
                                    mContext.getString(R.string.note_deleted),
                                    Snackbar.LENGTH_LONG);
                            snackbar.setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mTempNote.save();
                                    mNoteList.add(mTempNote);
                                    Collections.sort(mNoteList);
                                    notifyDataSetChanged();
                                }
                            });
                            snackbar.show();
                        }
                        return true;
                    }
                });

                popup.show();
            }
        });

        if (mNoteList.get(position).isEditMode()) {
            showDone(holder);
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

            holder.mDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mNoteList.get(position).setTitle(holder.mTitle.getText().toString());
                    mNoteList.get(position).setMessage(holder.mMessage.getText().toString());
                    mNoteList.get(position).setEditMode(false);
                    mNoteList.get(position).save();
                    showMore(holder);

                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.mTitle.getWindowToken(), 0);
                    imm.hideSoftInputFromWindow(holder.mMessage.getWindowToken(), 0);
                    notifyDataSetChanged();
                }
            });
        } else {
            showMore(holder);
        }

    }

    private void showDone(final ViewHolder holder) {
        holder.mMore.setVisibility(View.GONE);
        holder.mDone.setVisibility(View.VISIBLE);
        holder.mMessage.setMaxLines(Integer.MAX_VALUE);
        holder.mMessage.setEnabled(true);
        holder.mTitle.setEnabled(true);
        holder.mTitle.setSelection(holder.mTitle.getText().length());
        holder.mTitle.requestFocus();
    }

    private void showMore(ViewHolder holder) {
        holder.mMore.setVisibility(View.VISIBLE);
        holder.mDone.setVisibility(View.GONE);
        holder.mTitle.clearFocus();
        holder.mTitle.setEnabled(false);
        holder.mMessage.clearFocus();
        holder.mMessage.setMaxLines(5);
        holder.mMessage.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        if (null != mNoteList)
            return mNoteList.size();
        else return 0;
    }
}