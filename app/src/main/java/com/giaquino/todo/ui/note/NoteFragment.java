package com.giaquino.todo.ui.note;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.giaquino.todo.flux.store.NoteStore;
import com.giaquino.todo.model.entity.Note;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class NoteFragment extends BaseFragment implements NoteAdapter.NoteAdapterListener {

    @Inject ChecklistActionCreator checklistActionCreator;
    @Inject NoteStore noteStore;
    @Inject NoteActionCreator noteActionCreator;

    @BindView(R.id.todo_view_list_recycler_view) RecyclerView recyclerView;
    @BindDimen(R.dimen.todo_space_16) int space;

    private NoteAdapter noteAdapter;
    private AlertDialog updateNoteDialog, createChecklistDialog;
    private NoteView noteView;
    private ChecklistView checklistView;
    private LayoutInflater inflater;

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
        noteAdapter = new NoteAdapter(getContext(), this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(noteAdapter);
        recyclerView.addItemDecoration(new MarginDecoration(space, space));

        addSubscriptionToUnsubscribe(
            noteStore.asObservable().observeOn(AndroidSchedulers.mainThread()).subscribe(store -> {
                noteAdapter.setNotes(store.notes());
                noteAdapter.notifyDataSetChanged();
            }));
    }

    @Override public void onNoteClicked(@NonNull Note note) {
        showUpdateNote(note);
    }

    public void showUpdateNote(Note note) {
        if (updateNoteDialog == null) {
            noteView = NoteView.create(inflater, null);
            updateNoteDialog = DialogUtils.updateNoteDialog(getContext(), noteView);
        }
        noteView.setTitle(note.title());
        noteView.setText(note.text());
        updateNoteDialog.show();
        updateNoteDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
            noteActionCreator.deleteNote(note._id());
            updateNoteDialog.dismiss();
        });
        updateNoteDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!noteView.isValid()) return;
            noteActionCreator.updateNote(note._id(), noteView.getTitle(), noteView.getText());
            updateNoteDialog.dismiss();
        });
        updateNoteDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(v -> {
            showCreateChecklist(note);
            updateNoteDialog.dismiss();
        });
    }

    public void showCreateChecklist(Note note) {
        if (createChecklistDialog == null) {
            checklistView = ChecklistView.create(inflater, null);
            createChecklistDialog = DialogUtils.createChecklistDialog(getContext(), checklistView);
        }
        checklistView.setEntry(note.text());
        createChecklistDialog.show();
        createChecklistDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(v -> {
            createChecklistDialog.dismiss();
        });
        createChecklistDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!checklistView.isValid()) return;
            noteActionCreator.deleteNote(note._id());
            checklistActionCreator.createChecklist(checklistView.getEntry());
            createChecklistDialog.dismiss();
        });
    }
}
