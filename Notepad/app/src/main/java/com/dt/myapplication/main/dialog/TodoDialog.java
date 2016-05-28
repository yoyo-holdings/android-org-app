package com.dt.myapplication.main.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import com.dt.myapplication.R;
import com.dt.myapplication.main.viewholder.TodoDialogViewHolder;
import com.dt.myapplication.model.Todo;
import com.dt.myapplication.service.DatabaseIntentService;

/**
 * Created by DT on 27/05/2016.
 */
public class TodoDialog {

    public static final int MODE_ADD = 1;
    public static final int MODE_EDIT = 2;
    public static final int MODE_CONVERT = 3;

    public static final String TITLE_ADD_TODO = "Add Todo";
    public static final String TITLE_EDIT_TODO = "Edit Todo";
    public static final String TITLE_CONVERT_TO_TODO = "Convert To Todo";

    public static final String BUTTON_TEXT_ADD = "Add";
    public static final String BUTTON_TEXT_EDIT = "Save";
    public static final String BUTTON_TEXT_DELETE = "Delete";
    public static final String BUTTON_TEXT_CONVERT = "Convert";
    public static final String BUTTON_TEXT_CANCEL = "Cancel";

    public static final String SNACKBAR_MESSAGE_ADD_EMPTY_DATA = "Cannot Add With Empty Data";
    public static final String SNACKBAR_MESSAGE_EDIT_EMPTY_DATA = "Cannot Edit With Empty Data";
    public static final String SNACKBAR_MESSAGE_CONVERT_EMPTY_DATA =
        "Cannot Convert With Empty Data";
    public static final String SNACKBAR_MESSAGE_CONVERT_SUCCESSFUL =
        "Successfully Converted Note to Todo";

    public TodoDialog(final Activity activity, final CoordinatorLayout coordinatorLayout, int mode,
        final Todo todo) {
        View todoDialogView = activity.getLayoutInflater().inflate(R.layout.dialog_todo, null);
        final TodoDialogViewHolder todoDialogViewHolder = new TodoDialogViewHolder(todoDialogView);
        todoDialogViewHolder.textEditText.setText(todo.getText());
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.LightDialog);
        setTitle(builder, mode);
        setPositiveButton(activity, builder, mode, todoDialogViewHolder, coordinatorLayout,
            todo.getId(), todo.isDone());
        setNeutralButton(activity, builder, mode, todo.getId());
        builder.setNegativeButton(BUTTON_TEXT_CANCEL, null);
        builder.setView(todoDialogView);
        builder.show();
    }

    private void setTitle(AlertDialog.Builder builder, int mode) {
        switch (mode) {
            case MODE_ADD:
                builder.setTitle(TITLE_ADD_TODO);
                break;
            case MODE_EDIT:
                builder.setTitle(TITLE_EDIT_TODO);
                break;
            case MODE_CONVERT:
                builder.setTitle(TITLE_CONVERT_TO_TODO);
                break;
        }
    }

    private void setPositiveButton(final Activity activity, AlertDialog.Builder builder, int mode,
        final TodoDialogViewHolder todoDialogViewHolder, final CoordinatorLayout coordinatorLayout,
        final String id, final boolean isDone) {
        switch (mode) {
            case MODE_ADD:
                builder.setPositiveButton(BUTTON_TEXT_ADD, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(
                            todoDialogViewHolder.textEditText.getText().toString())) {
                            Snackbar.make(coordinatorLayout, SNACKBAR_MESSAGE_ADD_EMPTY_DATA,
                                Snackbar.LENGTH_SHORT).show();
                        } else {
                            DatabaseIntentService.addTodo(activity,
                                todoDialogViewHolder.textEditText.getText().toString(), false);
                        }
                    }
                });
                break;
            case MODE_EDIT:
                builder.setPositiveButton(BUTTON_TEXT_EDIT, new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(
                            todoDialogViewHolder.textEditText.getText().toString())) {
                            Snackbar.make(coordinatorLayout, SNACKBAR_MESSAGE_EDIT_EMPTY_DATA,
                                Snackbar.LENGTH_SHORT).show();
                        } else {
                            DatabaseIntentService.editTodo(activity, id,
                                todoDialogViewHolder.textEditText.getText().toString(), isDone);
                        }
                    }
                });
                break;
            case MODE_CONVERT:
                builder.setPositiveButton(BUTTON_TEXT_CONVERT,
                    new DialogInterface.OnClickListener() {
                        @Override public void onClick(DialogInterface dialogInterface, int i) {
                            if (TextUtils.isEmpty(
                                todoDialogViewHolder.textEditText.getText().toString())) {
                                Snackbar.make(coordinatorLayout,
                                    SNACKBAR_MESSAGE_CONVERT_EMPTY_DATA, Snackbar.LENGTH_SHORT)
                                    .show();
                            } else {
                                DatabaseIntentService.addTodo(activity,
                                    todoDialogViewHolder.textEditText.getText().toString(), false);
                                DatabaseIntentService.deleteNote(activity, id);
                                Snackbar.make(coordinatorLayout,
                                    SNACKBAR_MESSAGE_CONVERT_SUCCESSFUL, Snackbar.LENGTH_SHORT)
                                    .show();
                            }
                        }
                    });
                break;
        }
    }

    private void setNeutralButton(final Activity activity, AlertDialog.Builder builder, int mode,
        final String id) {
        if (mode == MODE_EDIT) {
            builder.setNeutralButton(BUTTON_TEXT_DELETE, new DialogInterface.OnClickListener() {
                @Override public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseIntentService.deleteTodo(activity, id);
                }
            });
        }
    }
}
