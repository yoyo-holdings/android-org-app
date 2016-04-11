package com.yoyo.exam.pdatu.myapplication.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.yoyo.exam.pdatu.myapplication.R;
import com.yoyo.exam.pdatu.myapplication.adapters.TodoListAdapter;
import com.yoyo.exam.pdatu.myapplication.models.Todo;

import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodoFragment extends Fragment {

    private View mRootView;
    private List<Todo> todoList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText mNewTodoText;
    private TextView mAddButton;

    private Context mainContext;

    public TodoFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_todo, container, false);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        todoList = Todo.listAll(Todo.class);
        Collections.sort(todoList);

        mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.todo_list);

        mLayoutManager = new LinearLayoutManager(mainContext);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new TodoListAdapter(todoList, mainContext);
        mRecyclerView.setAdapter(mAdapter);


        mAddButton = (TextView) mRootView.findViewById(R.id.todo_add);
        mNewTodoText = (EditText) mRootView.findViewById(R.id.todo_new);

        mNewTodoText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mNewTodoText.getText().toString().equals("")) {
                    mAddButton.setClickable(false);
                    mAddButton.setOnClickListener(null);
                } else {
                    mAddButton.setClickable(true);
                    mAddButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Todo newTodo = new Todo(mNewTodoText.getText().toString());
                            newTodo.save();
                            todoList.add(newTodo);
                            Collections.sort(todoList);
                            mAdapter.notifyDataSetChanged();
                            mNewTodoText.setText("");
                            mNewTodoText.clearFocus();
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainContext = null;
    }
}
