package com.giaquino.todo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.giaquino.todo.common.utils.DialogUtils;
import com.giaquino.todo.common.widget.ChecklistView;
import com.giaquino.todo.common.widget.NoteView;
import com.giaquino.todo.flux.action.ChecklistActionCreator;
import com.giaquino.todo.flux.action.NoteActionCreator;
import com.giaquino.todo.ui.checklist.ChecklistFragment;
import com.giaquino.todo.ui.note.NoteFragment;
import javax.inject.Inject;

/**
 * @author Gian Darren Azriel Aquino
 * @since 6/30/16
 */
public class MainActivity extends AppCompatActivity {

    @Inject NoteActionCreator noteActionCreator;
    @Inject ChecklistActionCreator checklistActionCreator;

    @BindView(R.id.todo_activity_main_toolbar) Toolbar toolbar;
    @BindView(R.id.todo_activity_main_view_pager) ViewPager viewPager;
    @BindView(R.id.todo_activity_main_tabs) TabLayout tabLayout;
    @BindView(R.id.todo_activity_main_fab) FloatingActionButton fab;

    private ChecklistView checklistView;
    private NoteView noteView;
    private AlertDialog createNote, createChecklist;
    private LayoutInflater inflater;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_activity_main);
        TodoApplication.get(this).getApplicationComponent().inject(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new NoteFragment();
                    case 1:
                        return new ChecklistFragment();
                    default:
                        throw new IllegalArgumentException("Invalid position: " + position);
                }
            }

            @Override public int getCount() {
                return 2;
            }

            @Override public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Note";
                    case 1:
                        return "Checklist";
                    default:
                        throw new IllegalArgumentException("Invalid position: " + position);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
        fab.setOnClickListener(v -> {
            switch (viewPager.getCurrentItem()) {
                case 0:
                    showCreateNote();
                    break;
                case 1:
                    showCreateChecklist();
                    break;
            }
        });
    }

    public void showCreateNote() {
        if (createNote == null) {
            noteView = NoteView.create(inflater, null);
            createNote = DialogUtils.createNoteDialog(this, noteView);
        }
        noteView.reset();
        createNote.show();
        createNote.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!noteView.isValid()) return;
            noteActionCreator.createNote(noteView.getTitle(), noteView.getText());
            createNote.dismiss();
        });
    }

    public void showCreateChecklist() {
        if (createChecklist == null) {
            checklistView = ChecklistView.create(inflater, null);
            createChecklist = DialogUtils.createChecklistDialog(this, checklistView);
        }
        checklistView.reset();
        createChecklist.show();
        createChecklist.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            if (!checklistView.isValid()) return;
            checklistActionCreator.createChecklist(checklistView.getEntry());
            createChecklist.dismiss();
        });
    }
}
