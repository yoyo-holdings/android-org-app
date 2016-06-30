package com.giaquino.todo.ui.checklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.giaquino.todo.common.widget.ChecklistItemView;
import com.giaquino.todo.model.entity.Checklist;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class ChecklistAdapter
    extends RecyclerView.Adapter<ChecklistAdapter.ChecklistItemViewHolder> {

    public interface ChecklistAdapterListener {
        void onChecklistClicked(Checklist checklist);

        void onChecklistCheckedChange(Checklist checklist, boolean checked);
    }

    private List<Checklist> checklists = new ArrayList<>();
    private LayoutInflater inflater;
    private ChecklistAdapterListener listener;

    public ChecklistAdapter(Context context, ChecklistAdapterListener listener) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;
    }

    @Override public ChecklistItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChecklistItemViewHolder(ChecklistItemView.create(inflater, parent));
    }

    @SuppressWarnings("ConstantConditions") @Override
    public void onBindViewHolder(ChecklistItemViewHolder holder, int position) {
        Checklist checklist = checklists.get(position);
        holder.view.setOnCheckedChangeListener(null);
        holder.view.setEntry(checklist.entry());
        holder.view.setChecked(checklist.checked());
        holder.view.setOnClickListener(v -> listener.onChecklistClicked(checklist));
        holder.view.setOnCheckedChangeListener(
            (buttonView, isChecked) -> listener.onChecklistCheckedChange(checklist, isChecked));
    }

    @Override public int getItemCount() {
        return checklists.size();
    }

    public void setChecklists(List<Checklist> checklists) {
        this.checklists.clear();
        this.checklists.addAll(checklists);
    }

    public static class ChecklistItemViewHolder extends RecyclerView.ViewHolder {

        private ChecklistItemView view;

        public ChecklistItemViewHolder(ChecklistItemView itemView) {
            super(itemView);
            view = itemView;
        }
    }
}

