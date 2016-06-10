package me.dlet.androidorgapp.activites;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.dlet.androidorgapp.R;
import me.dlet.androidorgapp.views.adapters.MainPagerAdapter;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class MainActivity extends AppCompatActivity implements TabLayout.OnTabSelectedListener {
    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.pager)ViewPager pager;
    @BindView(R.id.addLabel) TextView tvAddLabel;
    @BindView(R.id.addLayout) LinearLayout addLayout;

    private final int REQUEST_NOTE_TAKING=100;
    private final int REQUEST_TODO_CREATION=200;

    private PagerAdapter pagerAdapter;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_activity_notes)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.main_activity_todos)));

        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());

        pager.setAdapter(pagerAdapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        pager.setCurrentItem(tab.getPosition());
        tvAddLabel.setText(tab.getPosition()==0?
                getString(R.string.main_activity_add_note):
                getString(R.string.main_activity_add_todo));
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick(R.id.addLayout)
    public void onClickAdd(){
        if (pager.getCurrentItem()==0){
            NoteTakingActivity.startActivity(this);
        } else {
            TodoEntryActivity.startActivity(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
