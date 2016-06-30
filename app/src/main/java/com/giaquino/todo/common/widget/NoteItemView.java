package com.giaquino.todo.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.todo.R;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class NoteItemView extends CardView {

    @BindView(R.id.todo_item_note_text_view_title) TextView title;
    @BindView(R.id.todo_item_note_text_view_text) TextView text;

    @NonNull public static NoteItemView create(@NonNull LayoutInflater inflater,
        @Nullable ViewGroup container) {
        return (NoteItemView) inflater.inflate(R.layout.todo_item_note, container, false);
    }

    public NoteItemView(Context context) {
        super(context);
    }

    public NoteItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @NonNull public String getTitle() {
        return title.getText().toString();
    }

    public void setTitle(@NonNull String title) {
        this.title.setText(title);
    }

    @NonNull public String getText() {
        return text.getText().toString();
    }

    public void setText(@NonNull String text) {
        this.text.setText(text);
    }
}
