package com.yoyo.jopet.androidorgapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DialogHelper {
    private Activity activity;
    private Context context;
    private DatabaseHelper dbHelper;
    private ViewPager viewPager;

    public DialogHelper(Activity activity, Context context, DatabaseHelper dbHelper, ViewPager viewPager) {
        this.activity = activity;
        this.context = context;
        this.dbHelper = dbHelper;
        this.viewPager = viewPager;
    }

    public DialogHelper(Activity activity, Context context, DatabaseHelper dbHelper) {
        this.activity = activity;
        this.context = context;
        this.dbHelper = dbHelper;
    }

    public void addItem() {

        CharSequence items[] = new CharSequence[]{"Add a note", "Add a task to do"};
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("NEW ENTRY");
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Entry.TYPE_NOTE:
                        addNote();
                        break;
                    case Entry.TYPE_TODO:
                        addToDo();
                        break;
                    default:
                        dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void addToDo() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.dialog_todo);
        dialog.setTitle("Add New To Do");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_notebook);
        dialog.show();
        final EditText editActivity = (EditText) dialog.findViewById(R.id.txtTitle);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioAnswer);
        TextView txtAdd = (TextView) dialog.findViewById(R.id.btnPositive);
        txtAdd.setText(R.string.add_text);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int status;
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioYes) {
                    status = Entry.STATUS_DONE;
                } else {
                    status = Entry.STATUS_NOT;
                }
                if (!editActivity.getText().toString().equals("")) {
                    Entry entry = new Entry(editActivity.getText().toString(), status);
                    dbHelper.createEntry(entry);
                    dialog.dismiss();
                    updateChanges();
                    viewPager.setCurrentItem(Entry.TYPE_TODO);
                } else {
                    showErrorToast();
                }
            }
        });
        TextView txtCancel = (TextView) dialog.findViewById(R.id.btnNegative);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void addNote() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.dialog_note);
        dialog.setTitle("Add New Note");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_notebook);
        dialog.show();
        final EditText editTitle = (EditText) dialog.findViewById(R.id.txtTitle);
        final EditText editContent = (EditText) dialog.findViewById(R.id.txtContent);
        TextView txtAdd = (TextView) dialog.findViewById(R.id.btnPositive);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editTitle.getText().toString().equals("") && !editContent.getText().toString().equals("")) {
                    Entry entry = new Entry(editTitle.getText().toString(), editContent.getText().toString());
                    dbHelper.createEntry(entry);
                    dialog.dismiss();
                    updateChanges();
                    viewPager.setCurrentItem(Entry.TYPE_NOTE);
                } else {
                    showErrorToast();
                }
            }
        });
        TextView txtCancel = (TextView) dialog.findViewById(R.id.btnNegative);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showNote(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle(entry.getTitle());
        builder.setMessage(entry.getContent());
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editNote(entry);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNote(entry);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void editNote(final Entry entry) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.dialog_note);
        dialog.setTitle("Edit Note");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_notebook);
        dialog.show();
        final EditText editTitle = (EditText) dialog.findViewById(R.id.txtTitle);
        final EditText editContent = (EditText) dialog.findViewById(R.id.txtContent);
        editTitle.setText(entry.getTitle());
        editContent.setText(entry.getContent());
        TextView txtAdd = (TextView) dialog.findViewById(R.id.btnPositive);
        txtAdd.setText(R.string.update_text);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!editTitle.getText().toString().equals("") && !editContent.getText().toString().equals("")) {
                    Entry newEntry = new Entry(entry.getId(), editTitle.getText().toString(), editContent.getText().toString(), entry.getStatus());
                    dbHelper.updateEntry(newEntry);
                    dialog.dismiss();
                    updateChanges();
                } else {
                    showErrorToast();
                }
            }
        });
        TextView txtCancel = (TextView) dialog.findViewById(R.id.btnNegative);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void deleteNote(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("Delete Note");
        builder.setMessage("Are you sure you want to delete \"" + entry.getTitle().toUpperCase() + "\"?");
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteEntry(entry.getId());
                updateChanges();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void changeToTodo(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("Change to To Do");
        builder.setMessage("Are you sure you want to change this note to a To Do task?");
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Entry newEntry = new Entry(entry.getTitle(), entry.getContent(), entry.getStatus(), Entry.TYPE_TODO);
                long entryId = dbHelper.createEntry(newEntry);
                dbHelper.deleteEntry(entry.getId());
                editTodo(dbHelper.getEntry(entryId));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void showTodo(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("To Do");
        builder.setMessage(entry.getActivity());
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editTodo(entry);
            }
        });
        builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteTodo(entry);
            }
        });
        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        if(entry.getStatus() == Entry.STATUS_DONE){
            builder.setIcon(R.drawable.ic_check);
        }
        else{
            builder.setIcon(R.drawable.ic_cross);
        }
        builder.show();
    }

    private void editTodo(final Entry entry) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
        dialog.setContentView(R.layout.dialog_todo);
        dialog.setTitle("Edit To Do");
        dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.mipmap.ic_notebook);
        dialog.show();
        final EditText editActivity = (EditText) dialog.findViewById(R.id.txtTitle);
        final RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioAnswer);
        editActivity.setText(entry.getActivity());
        if (entry.getStatus() == Entry.STATUS_DONE) {
            radioGroup.check(R.id.radioYes);
        } else {
            radioGroup.check(R.id.radioNo);
        }
        TextView txtAdd = (TextView) dialog.findViewById(R.id.btnPositive);
        txtAdd.setText(R.string.update_text);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int status;
                if (radioGroup.getCheckedRadioButtonId() == R.id.radioYes) {
                    status = Entry.STATUS_DONE;
                } else {
                    status = Entry.STATUS_NOT;
                }
                if (!editActivity.getText().toString().equals("")) {
                    Entry newEntry = new Entry(entry.getId(), editActivity.getText().toString(), status, entry.getContent());
                    dbHelper.updateEntry(newEntry);
                    dialog.dismiss();
                    updateChanges();
                } else {
                    showErrorToast();
                }
            }
        });
        TextView txtCancel = (TextView) dialog.findViewById(R.id.btnNegative);
        txtCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void deleteTodo(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("Delete To Do");
        builder.setMessage("Are you sure you want to delete this entry?");
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteEntry(entry.getId());
                updateChanges();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public void changeToNote(final Entry entry) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("Change to Note");
        builder.setMessage("Are you sure you want to change this task to a Note?");
        builder.setIcon(R.mipmap.ic_notebook);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Entry newEntry = new Entry(entry.getActivity(), entry.getStatus(), entry.getContent(), Entry.TYPE_NOTE);
                long entryId = dbHelper.createEntry(newEntry);
                dbHelper.deleteEntry(entry.getId());
                editNote(dbHelper.getEntry(entryId));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void updateChanges() {
        dbHelper.closeDB();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                NoteFragment.updateChanges();
                TodoFragment.updateChanges();
            }
        });
    }

    private void showErrorToast() {
        Toast.makeText(activity.getApplicationContext(), "Please fill in all the details", Toast.LENGTH_SHORT).show();
    }
}
