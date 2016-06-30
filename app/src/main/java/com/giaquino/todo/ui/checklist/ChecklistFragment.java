package com.giaquino.todo.ui.checklist;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindDimen;
import butterknife.BindView;
import com.giaquino.todo.R;
import com.giaquino.todo.TodoApplication;
import com.giaquino.todo.common.app.BaseFragment;
import com.giaquino.todo.common.utils.DialogUtils;
import com.giaquino.todo.common.utils.MarginDecoration;
import com.giaquino.todo.common.widget.ChecklistView;
import com.giaquino.todo.common.widget.NoteView;
import com.giaquino.todo.flux.action.ChecklistActionCreator;
import com.giaquino.todo.flux.action.NoteActionCreator;
import com.giaquino.todo.flux.store.ChecklistStore;
import com.giaquino.todo.model.entity.Checklist;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class ChecklistFragment extends BaseFragment
    implements ChecklistAdapter.ChecklistAdapterListener {

    @Inject NoteActionCreator noteActionCreator;
    @Inject ChecklistStore checklistStore;
    @Inject ChecklistActionCreator checklistActionCreator;

    @BindView(R.id.todo_view_list_recycler_view) RecyclerView recyclerView;
    @BindDimen(R.dimen.todo_space_16) int space;

    private ChecklistAdapter checklistAdapter;
    private LayoutInflater inflater;
    private NoteView noteView;
    private ChecklistView checklistView;
    private AlertDialog updateChecklistDialog, createNoteDialog;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        this.inflater = inflater;
        return inflater.inflate(R.layout.todo_view_list, container, false);
    }

    @Override protected void initialize() {
        TodoApplication.get(getContext()).getApplicationComponent().inject(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
            LinearLayoutManager.VERTICAL, false);
        checklistAdapter = new ChecklistAdapter(getContext(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(checklistAdapter);
        recyclerView.addItemDecoration(new MarginDecoration(space, space));

        addSubscriptionToUnsubscribe(checklistStore.asObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(store -> {
                checklistAdapter.setChecklists(store.checklists());
                checklistAdapter.notifyDataSetChanged();
            }));
    }

    @SuppressWarnings("ConstantConditions") @Override
    public void onChecklistClicked(Checklist checklist) {
        showUpdateChecklistDialog(checklist);
    }

    @Override public void onChecklistCheckedChange(Checklist checklist, boolean checked) {
        checklistActionCreator.updateChecklist(checklist._id(), checklist.entry(), checked);
    }

    @SuppressWarnings("ConstantConditions")
    public void showUpdateChecklistDialog(Checklist checklist) {
        if (updateChecklistDialog == null) {
            checklistView = ChecklistView.create(inflater, null);
            updateChecklistDialog = DialogUtils.updateChecklistDialog(getContext(), checklistView);
        }
        checklistView.setEntry(checklist.entry());
        updateChecklistDialog.show();
        updateChecklistDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
            checklistActionCreator.deleteChecklist(checklist._id());
            updateChecklistDialog.dismiss();
        });
        updateChecklistDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!checklistView.isValid()) return;
            checklistActionCreator.updateChecklist(checklist._id(), checklistView.getEntry(),
                checklist.checked());
            updateChecklistDialog.dismiss();
        });
        updateChecklistDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
            showCreateNoteDialog(checklist);
            updateChecklistDialog.dismiss();
        });
    }

    public void showCreateNoteDialog(Checklist checklist) {
        if (createNoteDialog == null) {
            noteView = NoteView.create(inflater, null);
            createNoteDialog = DialogUtils.createNoteDialog(getContext(), noteView);
        }
        noteView.setTitle("");
        noteView.setText(checklist.entry());
        createNoteDialog.show();
        createNoteDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!noteView.isValid()) return;
            checklistActionCreator.deleteChecklist(checklist._id());
            noteActionCreator.createNote(noteView.getTitle(), noteView.getText());
            createNoteDialog.dismiss();
        });
    }
}
