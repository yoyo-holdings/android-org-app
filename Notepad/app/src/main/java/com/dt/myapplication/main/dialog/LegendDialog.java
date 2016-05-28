package com.dt.myapplication.main.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import com.dt.myapplication.R;
import com.dt.myapplication.main.viewholder.NoteDialogViewHolder;
import com.dt.myapplication.model.Note;
import com.dt.myapplication.service.DatabaseIntentService;

/**
 * Created by DT on 27/05/2016.
 */
public class LegendDialog {

    public static final String TITLE_LEGEND = "Legend";
    public static final String MESSAGE_LEGEND =
        "Tap note entry to expand/hide long texts.\n\nTap todo entry to mark as done/undone.";
    public static final String POSITIVE_BUTTON_TEXT = "OK";

    public LegendDialog(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.LightDialog);
        builder.setTitle(TITLE_LEGEND);
        builder.setMessage(MESSAGE_LEGEND);
        builder.setPositiveButton(POSITIVE_BUTTON_TEXT, null);
        builder.show();
    }
}
