package com.dt.myapplication.main.activity;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.astuetz.PagerSlidingTabStrip;
import com.dt.myapplication.R;
import com.dt.myapplication.main.adapter.TabFragmentPagerAdapter;
import com.dt.myapplication.main.dialog.LegendDialog;
import com.dt.myapplication.main.dialog.NoteDialog;
import com.dt.myapplication.main.dialog.TodoDialog;
import com.dt.myapplication.main.presenter.MainPresenter;
import com.dt.myapplication.main.presenter.impl.MainPresenterImpl;
import com.dt.myapplication.model.Note;
import com.dt.myapplication.model.Todo;

public class MainActivity extends AppCompatActivity implements MainContainer {

    @Bind(R.id.main_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.main_fab) FloatingActionButton mainFab;
    @Bind(R.id.add_fab) FloatingActionButton addFab;
    @Bind(R.id.edit_fab) FloatingActionButton editFab;
    @Bind(R.id.convert_fab) FloatingActionButton convertFab;
    @Bind(R.id.tabs) PagerSlidingTabStrip tabsStrip;

    private Animation fabOpenAnimation, fabCloseAnimation, rotateForwardAnimation,
        rotateBackwardAnimation;
    private TabFragmentPagerAdapter tabFragmentPagerAdapter;
    private MainPresenter presenter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initializeAnimations();
        tabFragmentPagerAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabFragmentPagerAdapter);
        tabsStrip.setViewPager(viewPager);
        setTabsStripColors();
        presenter = new MainPresenterImpl(this);
    }

    private void initializeAnimations() {
        fabOpenAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fabCloseAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        rotateForwardAnimation =
            AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotateBackwardAnimation =
            AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_backward);
    }

    private void setTabsStripColors() {
        tabsStrip.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        tabsStrip.setDividerColor(ContextCompat.getColor(this, R.color.colorWhite));
        tabsStrip.setIndicatorColor(ContextCompat.getColor(this, R.color.colorWhite));
        tabsStrip.setUnderlineColor(ContextCompat.getColor(this, R.color.colorWhite));
    }

    @Override public void showNoteDialog(int mode, Note note) {
        new NoteDialog(this, coordinatorLayout, mode, note);
    }

    @Override public void showTodoDialog(int mode, Todo todo) {
        new TodoDialog(this, coordinatorLayout, mode, todo);
    }

    @Override public void openAnimateFabs() {
        mainFab.startAnimation(rotateForwardAnimation);
        addFab.startAnimation(fabOpenAnimation);
        editFab.startAnimation(fabOpenAnimation);
        convertFab.startAnimation(fabOpenAnimation);
        addFab.setClickable(true);
        editFab.setClickable(true);
        convertFab.setClickable(true);
    }

    @Override public void closeAnimateFabs() {
        mainFab.startAnimation(rotateBackwardAnimation);
        addFab.startAnimation(fabCloseAnimation);
        editFab.startAnimation(fabCloseAnimation);
        convertFab.startAnimation(fabCloseAnimation);
        addFab.setClickable(false);
        editFab.setClickable(false);
        convertFab.setClickable(false);
    }

    @OnClick(R.id.main_fab) public void clickMainFab(View view) {
        presenter.animateFAB();
    }

    @OnClick(R.id.add_fab) public void clickAddFab(View view) {
        presenter.showAddDialog(viewPager.getCurrentItem());
    }

    @OnClick(R.id.edit_fab) public void clickEditFab(View view) {
        presenter.toggleEditMode(viewPager.getCurrentItem());
    }

    @OnClick(R.id.convert_fab) public void clickConvertFab(View view) {
        presenter.toggleConvertMode(viewPager.getCurrentItem());
    }

    @Override public void toggleNoteListEditMode() {
        if (tabFragmentPagerAdapter.getNoteListFragment() != null) {
            tabFragmentPagerAdapter.getNoteListFragment().toggleEditMode();
        }
    }

    @Override public void toggleTodoListEditMode() {
        if (tabFragmentPagerAdapter.getTodoListFragment() != null) {
            tabFragmentPagerAdapter.getTodoListFragment().toggleEditMode();
        }
    }

    @Override public void toggleNoteListConvertMode() {
        if (tabFragmentPagerAdapter.getNoteListFragment() != null) {
            tabFragmentPagerAdapter.getNoteListFragment().toggleConvertMode();
        }
    }

    @Override public void toggleTodoListConvertMode() {
        if (tabFragmentPagerAdapter.getTodoListFragment() != null) {
            tabFragmentPagerAdapter.getTodoListFragment().toggleConvertMode();
        }
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_legend:
                new LegendDialog(this);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
