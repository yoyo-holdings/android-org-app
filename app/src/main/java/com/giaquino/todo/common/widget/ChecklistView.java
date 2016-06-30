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
public class ChecklistView extends LinearLayout {

    @BindView(R.id.todo_view_checklist_edit_text_entry) EditText entry;

    @NonNull public static ChecklistView create(@NonNull LayoutInflater inflater,
        @Nullable ViewGroup container) {
        return (ChecklistView) inflater.inflate(R.layout.todo_view_checklist, container, false);
    }

    public ChecklistView(Context context) {
        super(context);
    }

    public ChecklistView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChecklistView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @NonNull public String getEntry() {
        return entry.getText().toString();
    }

    public void setEntry(@NonNull String entry) {
        this.entry.setText(entry);
    }

    public void reset() {
        entry.setText("");
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(entry.getText().toString());
    }

}
