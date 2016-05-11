package yoyo_holdings.com.androidorgapp.features;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gigamole.library.NavigationTabBar;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import yoyo_holdings.com.androidorgapp.R;
import yoyo_holdings.com.androidorgapp.features.notes.NotesFragment;

public class NavigationActivity extends AppCompatActivity {

    @Bind(R.id.vp_horizontal_ntb)
    ViewPager vpHorizontalNtb;
    @Bind(R.id.bg_ntb_horizontal)
    View bgNtbHorizontal;
    @Bind(R.id.ntb_horizontal)
    NavigationTabBar ntbHorizontal;
    @Bind(R.id.wrapper_ntb_horizontal)
    FrameLayout wrapperNtbHorizontal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_navigation_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        initUi();
    }

    private void initUi() {
        vpHorizontalNtb.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return NotesFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model(
                ContextCompat.getDrawable(NavigationActivity.this, R.drawable.ic_note_text), Color.parseColor(colors[0]), "Note"));
        models.add(new NavigationTabBar.Model(
                ContextCompat.getDrawable(NavigationActivity.this, R.drawable.ic_note_plus), Color.parseColor(colors[1]), "New"));
        models.add(new NavigationTabBar.Model(
                ContextCompat.getDrawable(NavigationActivity.this, R.drawable.ic_clipboard_check), Color.parseColor(colors[2]), "ToDo"));
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(vpHorizontalNtb, 1);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.post(new Runnable() {
            @Override
            public void run() {
                final View bgNavigationTabBar = findViewById(R.id.bg_ntb_horizontal);
                bgNavigationTabBar.getLayoutParams().height = (int) navigationTabBar.getBarHeight();
                bgNavigationTabBar.requestLayout();
            }
        });
    }
}
