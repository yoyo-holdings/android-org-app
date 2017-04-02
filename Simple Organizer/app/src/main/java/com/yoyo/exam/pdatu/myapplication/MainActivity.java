package com.yoyo.exam.pdatu.myapplication;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yoyo.exam.pdatu.myapplication.fragments.NotesFragment;
import com.yoyo.exam.pdatu.myapplication.fragments.TodoFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private View btnNote, btnTodo;
    private static final int STATE_NOTE = 1001;
    private static final int STATE_TODO = 1002;
    private int state = STATE_NOTE;
    private ImageView ivNote, ivTodo;
    private TextView tvNote, tvTodo;

    private static final float ALPHA_SELECTED = 1.0f;
    private static final float ALPHA_UNSELECTED = 0.38f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        startNotesFragment();
    }

    private void initViews() {
        btnNote = findViewById(R.id.notes_container);
        btnTodo = findViewById(R.id.todo_container);

        btnNote.setOnClickListener(this);
        btnTodo.setOnClickListener(this);

        ivNote = ((ImageView)findViewById(R.id.notes_icon));
        tvNote = ((TextView)findViewById(R.id.notes_text));
        ivTodo = ((ImageView)findViewById(R.id.todo_icon));
        tvTodo = ((TextView)findViewById(R.id.todo_text));
    }

    private void startNotesFragment() {
        FragmentManager fm = getSupportFragmentManager();
        NotesFragment notesFragment = new NotesFragment();
        fm.beginTransaction().add(R.id.fragment_container, notesFragment).commit();
    }

    private void startNewFragment(Fragment fragment) {
        String name = fragment.getClass().getName();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fade_out,R.anim.fade_in);
        transaction.replace(R.id.fragment_container, fragment, name);
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notes_container:
                if (state == STATE_NOTE) return;
                state = STATE_NOTE;
                ivNote.setColorFilter(getResources().getColor(R.color.colorAccent));
                tvNote.setTextColor(getResources().getColor(R.color.colorAccent));
                tvNote.setAlpha(ALPHA_SELECTED);
                ivNote.setAlpha(ALPHA_SELECTED);
                ivTodo.setColorFilter(getResources().getColor(R.color.black));
                tvTodo.setTextColor(getResources().getColor(R.color.black));
                tvTodo.setAlpha(ALPHA_UNSELECTED);
                ivTodo.setAlpha(ALPHA_UNSELECTED);
                startNewFragment(new NotesFragment());
                break;
            case R.id.todo_container:
                if (state == STATE_TODO) return;
                state = STATE_TODO;
                ivTodo.setColorFilter(getResources().getColor(R.color.colorAccent));
                tvTodo.setTextColor(getResources().getColor(R.color.colorAccent));
                tvTodo.setAlpha(ALPHA_SELECTED);
                ivTodo.setAlpha(ALPHA_SELECTED);
                ivNote.setColorFilter(getResources().getColor(R.color.black));
                tvNote.setTextColor(getResources().getColor(R.color.black));
                tvNote.setAlpha(ALPHA_UNSELECTED);
                ivNote.setAlpha(ALPHA_UNSELECTED);
                startNewFragment(new TodoFragment());
                break;
        }
    }
}
