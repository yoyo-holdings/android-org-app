package com.yoyo.exam.pdatu.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.design.widget.Snackbar;

import com.yoyo.exam.pdatu.myapplication.R;
import com.yoyo.exam.pdatu.myapplication.models.Todo;

import java.util.Collections;
import java.util.List;

/**
 * Created by Patrich on 4/10/2016.
 */
public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private List<Todo> mTodoList;
    private Todo mTempTodo;
    private Context mContext;
    private boolean isDeleteMode = false;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText mContent;
        public CheckBox mCheckBox;
        public ImageView mDelete;

        public ViewHolder(View v) {
            super(v);
            mContent = (EditText) v.findViewById(R.id.todo_text_item);
            mCheckBox = (CheckBox) v.findViewById(R.id.todo_checkbox);
            mDelete = (ImageView) v.findViewById(R.id.todo_delete);
        }
    }

    public TodoListAdapter(List<Todo> mTodoList, Context mainContext) {
        this.mTodoList = mTodoList;
        mContext = mainContext;
    }

    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mContent.setText(mTodoList.get(position).getContent());
        holder.mContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    isDeleteMode = false;
                    holder.mDelete.setVisibility(View.VISIBLE);
                    holder.mDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            isDeleteMode = true;
                            mTempTodo = new Todo(mTodoList.get(position));
                            mTodoList.get(position).delete();
                            refreshData();
                            Snackbar snackbar = Snackbar.make(((Activity) mContext).findViewById(R.id.fragment_container),
                                    mContext.getString(R.string.todo_deleted),
                                    Snackbar.LENGTH_LONG);
                            snackbar.setAction("UNDO", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    mTempTodo.save();
                                    refreshData();
                                }
                            });
                            snackbar.show();
                        }
                    });
                } else {
                    if (!isDeleteMode) {
                        mTodoList.get(position).setContent(holder.mContent.getText().toString());
                        mTodoList.get(position).save();
                    }
                    holder.mDelete.setVisibility(View.GONE);
                }
            }
        });

        holder.mCheckBox.setOnCheckedChangeListener(null);
        holder.mCheckBox.setChecked(mTodoList.get(position).isDone());
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mTodoList.get(position).setDone(b);
                mTodoList.get(position).save();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (null != mTodoList)
            return mTodoList.size();
        else return 0;
    }

    private void refreshData() {
        mTodoList.clear();
        mTodoList.addAll(Todo.find(Todo.class, ""));
        Collections.sort(mTodoList);
        notifyDataSetChanged();
    }
}