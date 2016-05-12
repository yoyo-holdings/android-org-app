package yoyo_holdings.com.androidorgapp.features.createupdate;

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
import yoyo_holdings.com.androidorgapp.data.model.Entry;

/**
 * Created by andrewconcepcion on 12/05/2016.
 */
public class BottomSheetDialogFragment extends android.support.design.widget.BottomSheetDialogFragment {
    @Bind(R.id.option_note)
    LinearLayout optionNote;
    @Bind(R.id.option_todo)
    LinearLayout optionTodo;
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
    private UpsertNotesContract.UserActionsListener listener;
    private Entry entry;

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_frag, null);
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

    @OnClick({R.id.option_note, R.id.option_todo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.option_note:
                entry.setTodo(false);
                break;
            case R.id.option_todo:
                entry.setTodo(true);
                break;
        }
        if (this.listener != null) {
            this.listener.saveEntry(entry);
        }
        dismiss();
    }

    public void setListener(Entry entry, UpsertNotesContract.UserActionsListener mActionsListener) {
        this.entry = entry;
        this.listener = mActionsListener;
    }
}