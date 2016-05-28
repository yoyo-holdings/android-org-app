package com.dt.myapplication.main.viewholder;

import android.view.View;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dt.myapplication.R;

/**
 * Created by DT on 27/05/2016.
 */
public class TodoListItemViewHolder {
    @Bind(R.id.listviewitem_todo_text_text_view)
    public TextView todoTextTextView;
    @Bind(R.id.listviewitem_todo_edit_mode_indicator_text_view)
    public TextView todoEditModeIndicatorTextView;
    @Bind(R.id.listviewitem_todo_convert_mode_indicator_text_view)
    public TextView todoConvertModeIndicatorTextView;

    public TodoListItemViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}