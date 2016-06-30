package com.giaquino.todo.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.todo.R;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class NoteView extends LinearLayout {

    @BindView(R.id.todo_view_edit_text_title) EditText title;
    @BindView(R.id.todo_view_edit_text_text) EditText text;

    @NonNull
    public static NoteView create(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return (NoteView) inflater.inflate(R.layout.todo_view_note, container, false);
    }

    public NoteView(Context context) {
        super(context);
    }

    public NoteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteView(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void reset() {
        title.setText("");
        text.setText("");
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(title.getText()) && !TextUtils.isEmpty(text.getText());
    }
}
