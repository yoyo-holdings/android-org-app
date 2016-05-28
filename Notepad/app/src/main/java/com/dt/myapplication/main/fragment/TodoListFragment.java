package com.dt.myapplication.main.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.dt.myapplication.R;
import com.dt.myapplication.data.dao.TodosDao;
import com.dt.myapplication.main.activity.MainActivity;
import com.dt.myapplication.main.adapter.TodosCursorAdapter;
import com.dt.myapplication.main.dialog.NoteDialog;
import com.dt.myapplication.main.dialog.TodoDialog;
import com.dt.myapplication.main.presenter.TodoListPresenter;
import com.dt.myapplication.main.presenter.impl.TodoListPresenterImpl;
import com.dt.myapplication.model.Note;
import com.dt.myapplication.model.Todo;
import com.dt.myapplication.service.DatabaseIntentService;

/**
 * Created by DT on 27/05/2016.
 */
public class TodoListFragment extends Fragment
    implements TodoListContainer, LoaderManager.LoaderCallbacks<Cursor>,
    AdapterView.OnItemClickListener {

    @Bind(R.id.list_view) public ListView todoListView;

    ListAdapter todosListAdapter;
    TodoListPresenter todoListPresenter;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_view, container, false);
        ButterKnife.bind(this, view);
        buildAdapterAndListView();
        todoListPresenter = new TodoListPresenterImpl(this);
        return view;
    }

    private void buildAdapterAndListView() {
        setListAdapter(new TodosCursorAdapter(getContext()));
        todoListView.setAdapter(getListAdapter());
        todoListView.setOnItemClickListener(this);
        getLoaderManager().initLoader(0, null, this);
    }

    public ListAdapter getListAdapter() {
        return todosListAdapter;
    }

    public void setListAdapter(ListAdapter notesListAdapter) {
        this.todosListAdapter = notesListAdapter;
    }

    private TodosCursorAdapter getTodosCursorAdapter() {
        return (TodosCursorAdapter) getListAdapter();
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return TodosDao.getTodosCursorLoader(getActivity());
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (getListAdapter() != null && cursor != null && cursor.getCount() > 0) {
            getTodosCursorAdapter().swapCursor(cursor);
            getTodosCursorAdapter().notifyDataSetChanged();
        }
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        getTodosCursorAdapter().changeCursor(null);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Cursor cursor = ((CursorAdapter) getListAdapter()).getCursor();
        cursor.moveToPosition(position);
        Todo todo = new Todo(cursor);
        todoListPresenter.onItemClick(todo, getTodosCursorAdapter().getCurrentMode());
    }

    @Override public void toggleTodoIsDoneState(Todo todo) {
        DatabaseIntentService.editTodo(getContext(), todo.getId(), todo.getText(), !todo.isDone());
    }

    @Override public void showEditTodoDialog(Todo todo) {
        ((MainActivity) getActivity()).showTodoDialog(TodoDialog.MODE_EDIT, todo);
    }

    @Override public void showConvertNoteDialog(Todo todo) {
        ((MainActivity) getActivity()).showNoteDialog(NoteDialog.MODE_CONVERT,
            new Note(todo.getId(), todo.getText(), ""));
    }

    @Override public void toggleEditMode() {
        if (getTodosCursorAdapter() != null) {
            getTodosCursorAdapter().toggleEditMode();
        }
    }

    @Override public void toggleConvertMode() {
        if (getTodosCursorAdapter() != null) {
            getTodosCursorAdapter().toggleConvertMode();
        }
    }
}
