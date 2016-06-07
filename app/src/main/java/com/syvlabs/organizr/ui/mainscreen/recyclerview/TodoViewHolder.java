package com.syvlabs.organizr.ui.mainscreen.recyclerview;

import android.graphics.Paint;
import android.graphics.PointF;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.model.db.objects.Entry;

import butterknife.Bind;
import butterknife.ButterKnife;

public class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.bg)
    RelativeLayout layoutBg;

    @Bind(R.id.check_todo)
    AppCompatCheckBox checkTodo;

    @Bind(R.id.hitbox_check)
    View hitboxCheck;

    @Bind(R.id.text_todo)
    TextView textTodo;

    Entry boundData;
    ClickListener listener;

    public TodoViewHolder(View itemView, final ClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
        hitboxCheck.setOnClickListener(this);
    }

    public void bindData(Entry data) {
        boundData = data;
        layoutBg.setBackgroundColor(data.getColor());
        textTodo.setText(data.getTitle());
        checkTodo.setChecked(data.getDone());
        refreshStrikeThrough();
    }

    @Override
    public void onClick(View v) {
        if (v == itemView) {
            listener.onClick(boundData, getAdapterPosition());
        } else if (v == hitboxCheck) {
            checkTodo.toggle();
            listener.onCheckChanged(boundData, checkTodo.isChecked());
            refreshStrikeThrough();
        }
    }

    private void refreshStrikeThrough() {
        if (checkTodo.isChecked())
            textTodo.setPaintFlags(textTodo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        else
            textTodo.setPaintFlags(textTodo.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public interface ClickListener {
        void onClick(Entry data, int position);
        void onCheckChanged(Entry data, boolean isChecked);
    }
}
