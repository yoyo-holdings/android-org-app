package com.syvlabs.organizr.ui.mainscreen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringSystem;
import com.syvlabs.organizr.R;
import com.syvlabs.organizr.model.EntryType;
import com.syvlabs.organizr.ui.UiUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ContentListFragment.OnContentListInteractionListener,EntryFragment.onDismissListener,ViewPager.OnPageChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.pager)
    ViewPager viewPager;

    @Bind(R.id.tabs)
    TabLayout tabs;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.fab_note)
    RelativeLayout fabNote;

    @Bind(R.id.fab_todo)
    RelativeLayout fabTodo;

    @Bind(R.id.label_fab_note)
    TextView fabNoteLabel;

    @Bind(R.id.label_fab_todo)
    TextView fabTodoLabel;

    @Bind(R.id.cover_white)
    View whiteCover;

    Spring fabSpring;
    Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolbar();
        initViewPager();
        initFabs();
    }

    private void initToolbar() {
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initViewPager() {
        viewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        tabs.setupWithViewPager(viewPager);
        for (EntryType type : EntryType.values()) {
            View tab = createTab(type.tabTitle, type.icon);
            tabs.getTabAt(type.index).setCustomView(tab);
        }
        tabs.getTabAt(EntryType.NOTE.index).getCustomView().setSelected(true);
        viewPager.addOnPageChangeListener(this);
    }

    private View createTab(String title, int icon) {
        ViewGroup layout = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.tab_main_activity, null);
        ImageView iv = (ImageView) layout.findViewById(R.id.icon);
        iv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), icon, null));
        tintTab(iv);
        TextView tv = (TextView) layout.findViewById(R.id.text);
        tv.setText(title);
        return layout;
    }

    private void tintTab(@NonNull ImageView view) {
        ColorStateList colours = ContextCompat.getColorStateList(this, R.color.selector_tab_title);
        Drawable d = DrawableCompat.wrap(view.getDrawable());
        DrawableCompat.setTintList(d, colours);
        view.setImageDrawable(d);
    }

    private void initFabs() {
        fabNote.setAlpha(0f);
        fabTodo.setAlpha(0f);
        fabNoteLabel.setAlpha(0f);
        fabTodoLabel.setAlpha(0f);
        whiteCover.setClickable(false);
        SpringSystem springSystem = SpringSystem.create();
        fabSpring = springSystem.createSpring().setSpringConfig(SpringConfig.fromOrigamiTensionAndFriction(60, 5));
    }

    @OnClick(R.id.cover_white)
    public void onCoverClick(View v) {
        fabSpring.setEndValue(0);
    }

    @OnClick(R.id.fab)
    public void onMainFabClick(View v) {
        fabSpring.addListener(new SimpleSpringListener(){
            @Override
            public void onSpringUpdate(Spring spring) {
                float value = (float) spring.getCurrentValue();
                fabNote.setAlpha(value);
                fabTodo.setAlpha(value);
                fabNoteLabel.setAlpha(value);
                fabTodoLabel.setAlpha(value);
                whiteCover.setAlpha(Math.min(value*1.2f, 1f));
                whiteCover.setClickable(value > 0.2);
                float marginDpNote = UiUtil.ratio(-50, 10, value);
                float marginDpTodo = UiUtil.ratio(-50, 0, value);
                UiUtil.setMarginBottom(MainActivity.this, fabNote, marginDpNote);
                UiUtil.setMarginBottom(MainActivity.this, fabTodo, marginDpTodo);
                float angle = 135 * value;
                fab.setRotation(angle);
                if (spring.getEndValue() == 0)
                    refreshToolbarItems(viewPager.getCurrentItem());
            }
        });
        if (fabSpring.getCurrentValue() < 0.5) {
            fabSpring.setEndValue(1);
            if (toolbarMenu != null)
                toolbarMenu.findItem(R.id.action_clear).setVisible(false);
        } else {
            fabSpring.setEndValue(0);
        }
    }

    @OnClick({R.id.fab_todo_fab, R.id.label_fab_todo})
    public void onTodoFabClick(View v) {
        if (fabSpring.getCurrentValue() < 0.5)
            return;
        showEntryDialog(EntryFragment.newCreateEntryInstance(EntryType.TODO));
        fabSpring.setEndValue(0);
    }

    @OnClick({R.id.fab_note_fab, R.id.label_fab_note})
    public void onNoteFabClick(View v) {
        if (fabSpring.getCurrentValue() < 0.5)
            return;
        showEntryDialog(EntryFragment.newCreateEntryInstance(EntryType.NOTE));
        fabSpring.setEndValue(0);
    }

    private void showEntryDialog(EntryFragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "fragment_entry");
    }

    @Override
    public void onEntryFragmentDismiss(boolean goToPage, int entryType) {
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                forceCloseKeyboard();
            }
        };
        h.postDelayed(r, 100);
        if (goToPage)
            viewPager.setCurrentItem(entryType, true);
    }

    private void forceCloseKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (fabSpring.getCurrentValue() > 0.1)
            fabSpring.setEndValue(0);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_main, menu);
        toolbarMenu = menu;
        toolbarMenu.findItem(R.id.action_clear).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clearDonePrompt();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshToolbarItems(int viewPagerPos) {
        if (toolbarMenu == null)
            return;
        if (viewPagerPos == EntryType.NOTE.index) {
            toolbarMenu.findItem(R.id.action_clear).setVisible(false);
        } else if (viewPagerPos == EntryType.TODO.index) {
            toolbarMenu.findItem(R.id.action_clear).setVisible(true);
        }
    }

    private void clearDonePrompt() {
        new AlertDialog.Builder(this)
                .setTitle("Clear all done entries?")
                .setMessage("Doing so will delete all checked items.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearDone();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();
    }

    private void clearDone() {
        if (viewPager.getCurrentItem() == EntryType.TODO.index) {
            MainPagerAdapter adapter = (MainPagerAdapter) viewPager.getAdapter();
            ContentListFragment fragment = (ContentListFragment) adapter.getItem(viewPager.getCurrentItem());
            fragment.clearAllDone();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        refreshToolbarItems(position);
    }
}
