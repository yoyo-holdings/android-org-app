package com.syvlabs.organizr.ui.mainscreen.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.model.db.objects.Entry;

import butterknife.Bind;
import butterknife.ButterKnife;

public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @Bind(R.id.layout_note)
    RelativeLayout layoutNote;

    @Bind(R.id.text_title)
    TextView textTitle;

    @Bind(R.id.text_content)
    TextView textContent;

    Entry boundData;
    ClickListener listener;

    public NoteViewHolder(View itemView, ClickListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
        itemView.setOnClickListener(this);
    }

    public void bindData(Entry data) {
        boundData = data;
        textTitle.setText(data.getTitle());
        textContent.setText(data.getContent());
        layoutNote.setBackgroundColor(data.getColor());
        if (textContent.getText().toString().isEmpty() || (data.getType() == EntryType.TODO.index))
            textContent.setVisibility(View.GONE);
        else
            textContent.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(boundData, getAdapterPosition());
    }

    public interface ClickListener {
        void onClick(Entry data, int position);
    }
}
