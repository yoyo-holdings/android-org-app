package com.syvlabs.organizr.ui.mainscreen;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.Colors;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.model.db.helpers.EntriesDbHelper;
import com.syvlabs.organizr.model.db.objects.Entry;
import com.syvlabs.organizr.ui.UiUtil;
import com.thebluealliance.spectrum.SpectrumDialog;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.internal.ListenerClass;

public class EntryFragment extends DialogFragment {
    private static final String ARG_ENTRY_ID = "arg_entry_id";
    private static final String ARG_IS_NEW = "arg_is_new";
    private static final String ARG_NEW_TYPE = "arg_new_type";

    @Bind(R.id.toolbar_note)
    Toolbar toolbar;

    @Bind(R.id.text_type)
    TextView textType;

    @Bind(R.id.edit_title)
    EditText editTitle;

    @Bind(R.id.edit_body)
    EditText editBody;

    @Bind(R.id.edit_todo)
    EditText editTodo;

    @Bind(R.id.check_todo)
    AppCompatCheckBox checkTodo;

    @Bind(R.id.area_note)
    RelativeLayout areaNote;

    @Bind(R.id.area_todo)
    RelativeLayout areaTodo;

    private Spring transitionsSpring;
    private Entry entry;
    private boolean forDeletion;

    public EntryFragment() {
        // Required empty public constructor
    }

    public static EntryFragment newCreateEntryInstance(EntryType type) {
        return newInstance(-1, true, type.index);
    }

    public static EntryFragment newOpenEntryInstance(long entryId) {
        return newInstance(entryId, false, -1);
    }

    private static EntryFragment newInstance(long entryId, boolean isNew, int newType) {
        EntryFragment fragment = new EntryFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_ENTRY_ID, entryId);
        args.putBoolean(ARG_IS_NEW, isNew);
        args.putInt(ARG_NEW_TYPE, newType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forDeletion = false;
        long entryId = getArguments().getLong(ARG_ENTRY_ID);
        boolean isNew = getArguments().getBoolean(ARG_IS_NEW);
        EntryType newType = EntryType.getEntryTypeFromIndex(getArguments().getInt(ARG_NEW_TYPE));
        if (isNew)
            entry = EntriesDbHelper.createEntry(getContext(), newType);
        else
            entry = EntriesDbHelper.retrieveEntry(getContext(), entryId);
    }

    @Override
    public View onCreateView(LayoutInflater inf, ViewGroup vg, Bundle savedInstanceState) {
        View view = inf.inflate(R.layout.fragment_entry, vg, false);
        ButterKnife.bind(this, view);
        initToolbar();
        setKeyboardVisibility();
        setEntryVisibilities();
        setEntryValues();
        setEntryColors();
        return view;
    }

    private void initToolbar() {
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return EntryFragment.this.onMenuItemClick(item);
            }
        });
        toolbar.inflateMenu(R.menu.toolbar_note);
        toolbar.setNavigationIcon(R.drawable.ic_check_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EntryFragment.this.dismiss();
            }
        });
        toolbar.setBackgroundColor(entry.getColor());
    }

    private void setKeyboardVisibility() {
        if (entry.isNew())
            if (entry.getType() == EntryType.NOTE.index)
                forceOpenKeyboard(editTitle);
            else
                forceOpenKeyboard(editTodo);
    }

    private void setEntryVisibilities() {
        if (entry.getType() == EntryType.NOTE.index) {
            areaNote.setVisibility(View.VISIBLE);
            areaTodo.setVisibility(View.GONE);
            textType.setText("Note");
        } else {
            areaNote.setVisibility(View.GONE);
            areaTodo.setVisibility(View.VISIBLE);
            textType.setText("To-do");
        }
    }

    private void setEntryValues() {
        editTitle.setText(entry.getTitle());
        editBody.setText(entry.getContent());
        editTodo.setText(entry.getTitle());
        checkTodo.setChecked(entry.getDone());
    }

    private void setEntryColors() {
        areaNote.setBackgroundColor(entry.getColor());
        areaTodo.setBackgroundColor(entry.getColor());
    }

    private boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_color:
                showPalette();
                break;
            case R.id.action_convert:
                convertEntry();
                break;
            case R.id.action_delete:
                deletePrompt();
                break;
            default:
                break;
        }
        return true;
    }

    private void convertEntry() {
        storeEntryFields();
        if (entry.getType()==EntryType.TODO.index)
            entry.setType(EntryType.NOTE.index);
        else
            entry.setType(EntryType.TODO.index);
        setEntryVisibilities();
        setEntryValues();
    }

    private void deleteEntry() {
        forDeletion = true;
        if (!entry.isNew())
            EntriesDbHelper.deleteEntry(getContext(), entry);
        dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        boolean discard = false;
        if (!forDeletion)
            discard = saveEntry();
        super.onDismiss(dialog);
        if (getActivity() != null && (getActivity() instanceof onDismissListener))
            ((onDismissListener) getActivity()).onEntryFragmentDismiss(!discard, entry.getType());
    }

    private void storeEntryFields() {
        if (entry.getType() == EntryType.NOTE.index) {
            entry.setTitle(editTitle.getText().toString());
            entry.setContent(editBody.getText().toString());
        } else {
            entry.setTitle(editTodo.getText().toString());
            entry.setDone(checkTodo.isChecked());
        }
    }

    public boolean saveEntry() {
        boolean discard = false;
        storeEntryFields();
        if (entry.getType() == EntryType.NOTE.index) {
            if (entry.getTitle().isEmpty() && entry.getContent().isEmpty() && entry.isNew())
                discard = true;
        } else {
            if (entry.getTitle().isEmpty() && entry.isNew())
                discard = true;
        }
        if (!discard)
            EntriesDbHelper.insertEntry(getContext(), entry);
        return discard;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        createSpringAnimations();
        return dialog;
    }

    private void createSpringAnimations() {
        SpringSystem springSystem = SpringSystem.create();
        transitionsSpring = springSystem.createSpring().setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(50, 4));
        transitionsSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                setAnimationFrame((float) spring.getCurrentValue());
            }
        });
        transitionsSpring.setEndValue(1);
    }

    private void setAnimationFrame(float progress) {
        if (getDialog() != null) {
            final Window window = getDialog().getWindow();
            final View decorView = window.getDecorView();
            float scale = UiUtil.ratio(0.4f, 1f, progress);
            decorView.setScaleX(scale);
            decorView.setScaleY(scale);
        }
    }

    private void forceOpenKeyboard(EditText toFocus) {
        toFocus.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    public interface onDismissListener {
        void onEntryFragmentDismiss(boolean goToPage, int type);
    }

    public void deletePrompt() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Delete this entry?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteEntry();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void showPalette() {
        new SpectrumDialog.Builder(getContext())
                .setColors(R.array.noteColors)
                .setDismissOnColorSelected(true)
                .setSelectedColor(entry.getColor())
                .setOutlineWidth(0)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult)
                            changeColor(color);
                    }
                }).build().show(getFragmentManager(), "dialog_palette");
    }

    private void changeColor(int nextColor) {
        int curColor = entry.getColor();
        entry.setColor(nextColor);
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(curColor, nextColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                areaNote.setBackgroundColor(val);
                areaTodo.setBackgroundColor(val);
                toolbar.setBackgroundColor(val);
            }
        });
        anim.setDuration(200);
        anim.start();
    }
}
