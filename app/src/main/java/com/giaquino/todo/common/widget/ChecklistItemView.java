package com.giaquino.todo.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.todo.R;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/29/16
 */
public class ChecklistItemView extends CardView {

    @BindView(R.id.todo_item_checklist_checkbox) CheckBox checkBox;
    @BindView(R.id.todo_item_checklist_text_view_entry) TextView entry;

    @NonNull public static ChecklistItemView create(@NonNull LayoutInflater inflater,
        @Nullable ViewGroup container) {
        return (ChecklistItemView) inflater.inflate(R.layout.todo_item_checklist, container, false);
    }

    public ChecklistItemView(Context context) {
        super(context);
    }

    public ChecklistItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChecklistItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setEntry(@NonNull String entry) {
        this.entry.setText(entry);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    public void setOnCheckedChangeListener(
        @Nullable CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }
}
