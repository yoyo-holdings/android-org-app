package yoyo_holdings.com.androidorgapp.util;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.data.model.EntryEntity;
import yoyo_holdings.com.androidorgapp.features.notes.NotesContract;
import yoyo_holdings.com.androidorgapp.features.todo.TodoContract;

/**
 * Created by andrewconcepcion on 12/05/2016.
 */
public class BottomSheetContextMenu extends android.support.design.widget.BottomSheetDialogFragment {
    @Bind(R.id.option_delete)
    LinearLayout optionDelete;
    @Bind(R.id.option_edit)
    LinearLayout optionEdit;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };
    private EntryEntity entry;
    private TodoContract.UserActionsListener todoListener;
    private NotesContract.UserActionsListener notesListener;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_context_frag, null);
        ButterKnife.bind(this, contentView);
        dialog.setContentView(contentView);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.option_delete, R.id.option_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_delete:
                if (this.todoListener != null) {
                    this.todoListener.removeEntry(entry);
                }
                if (this.notesListener != null) {
                    this.notesListener.removeEntry(entry);
                }
                break;
            case R.id.option_edit:
                if (this.todoListener != null) {
                    this.todoListener.editEntry(entry);
                }
                if (this.notesListener != null) {
                    this.notesListener.editEntry(entry);
                }
                break;
        }

        dismiss();
    }

    public void setNotesContractListener(EntryEntity entry, NotesContract.UserActionsListener mActionsListener) {
        this.entry = entry;
        this.notesListener = mActionsListener;
    }

    public void setTodoContractListener(EntryEntity entry, TodoContract.UserActionsListener mActionsListener) {
        this.entry = entry;
        this.todoListener = mActionsListener;
    }
}