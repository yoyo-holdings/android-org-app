package com.dt.myapplication.main.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dt.myapplication.R;

/**
 * Created by DT on 27/05/2016.
 */
public class NoteListItemViewHolder {
    @Bind(R.id.listviewitem_note_title_text_view)
    public TextView noteTitleTextView;
    @Bind(R.id.listviewitem_note_text_text_view)
    public TextView noteTextTextView;
    @Bind(R.id.listviewitem_note_edit_mode_indicator_text_view)
    public TextView noteEditModeIndicatorTextView;
    @Bind(R.id.listviewitem_note_convert_mode_indicator_text_view)
    public TextView noteConvertModeIndicatorTextView;

    public NoteListItemViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}