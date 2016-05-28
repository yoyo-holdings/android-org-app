package com.dt.myapplication.main.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import com.dt.myapplication.R;
import com.dt.myapplication.main.viewholder.TodoListItemViewHolder;
import com.dt.myapplication.model.Todo;

/**
 * Created by DT on 27/05/2016.
 */
public class TodosCursorAdapter extends CursorAdapter {

    public static final int DEFAULT_MODE = 0;
    public static final int EDIT_MODE = 1;
    public static final int CONVERT_MODE = 2;

    Context mContext;
    int currentMode = 0;

    public TodosCursorAdapter(Context context) {
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
        View newView = inflater.inflate(R.layout.listviewitem_todo, viewGroup, false);
        TodoListItemViewHolder todoListItemViewHolder = new TodoListItemViewHolder(newView);
        newView.setTag(todoListItemViewHolder);
        return newView;
    }

    @Override public void bindView(View view, Context context, Cursor cursor) {
        TodoListItemViewHolder todoListItemViewHolder = (TodoListItemViewHolder) view.getTag();
        Todo todo = new Todo(cursor);
        todoListItemViewHolder.todoTextTextView.setText(todo.getText());
        if (todo.isDone()) {
            todoListItemViewHolder.todoTextTextView.setPaintFlags(
                todoListItemViewHolder.todoTextTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            todoListItemViewHolder.todoTextTextView.setPaintFlags(0);
        }
        switch (getCurrentMode()) {
            case DEFAULT_MODE:
                todoListItemViewHolder.todoEditModeIndicatorTextView.setVisibility(View.GONE);
                todoListItemViewHolder.todoConvertModeIndicatorTextView.setVisibility(View.GONE);
                break;
            case EDIT_MODE:
                todoListItemViewHolder.todoEditModeIndicatorTextView.setVisibility(View.VISIBLE);
                todoListItemViewHolder.todoConvertModeIndicatorTextView.setVisibility(View.GONE);
                break;
            case CONVERT_MODE:
                todoListItemViewHolder.todoEditModeIndicatorTextView.setVisibility(View.GONE);
                todoListItemViewHolder.todoConvertModeIndicatorTextView.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
