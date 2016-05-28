package com.dt.myapplication.main.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dt.myapplication.R;

/**
 * Created by DT on 28/05/2016.
 */
public class NoteDialogViewHolder {
    @Bind(R.id.dialog_note_title_edit_text)
    public TextView titleEditText;
    @Bind(R.id.dialog_note_text_edit_text)
    public TextView textEditText;

    public NoteDialogViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
