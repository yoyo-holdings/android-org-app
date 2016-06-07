package com.syvlabs.organizr.ui.mainscreen.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.db.objects.Entry;

import java.util.List;

public class TodosAdapter extends RecyclerView.Adapter<TodoViewHolder> {

    List<Entry> dataSrc;
    TodoViewHolder.ClickListener clickListener;

    public TodosAdapter(List<Entry> dataSrc, TodoViewHolder.ClickListener clickListener) {
        this.dataSrc = dataSrc;
        this.clickListener = clickListener;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_todos, parent, false);
        return new TodoViewHolder(v, clickListener);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        holder.bindData(dataSrc.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSrc.size();
    }
}
