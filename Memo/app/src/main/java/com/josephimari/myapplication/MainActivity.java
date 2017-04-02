package com.josephimari.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.astuetz.PagerSlidingTabStrip;
import com.josephimari.myapplication.adapters.MainPagerAdapter;
import com.josephimari.myapplication.database.MemoItem;
import com.josephimari.myapplication.fragments.NoteFragment;
import com.josephimari.myapplication.fragments.TodoFragment;

/**
 * Created by Joseph on 5/30/2016.
 */
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getSimpleName();

  @Bind(R.id.toolbar) Toolbar toolbar;
  @Bind(R.id.fab) FloatingActionButton fab;
  @Bind(R.id.viewpager) ViewPager viewPager;
  @Bind(R.id.tabs) PagerSlidingTabStrip tabsStrip;

  private MainPagerAdapter pagerAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    setSupportActionBar(toolbar);
    setupOnPageChangeListener();
    initPager();

    fab.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        Intent i = new Intent(MainActivity.this, NewMemoActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra(NewMemoActivity.EXTRA_TYPE,
            viewPager.getCurrentItem() == 0 ? MemoItem.TYPE_NOTE : MemoItem.TYPE_TODO);
        startActivity(i);
      }
    });
  }

  private void setupOnPageChangeListener() {
    viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        Object object = pagerAdapter.instantiateItem(viewPager, position);
        if (object instanceof NoteFragment) {
          ((NoteFragment) object).update();
        } else if (object instanceof TodoFragment) {
          ((TodoFragment) object).update();
        }
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  private void initPager() {
    pagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
    viewPager.setAdapter(pagerAdapter);
    tabsStrip.setViewPager(viewPager);
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
      case R.id.action_about_app:
        AlertDialog.Builder builder =
            new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(null);
        builder.setMessage("Created by: Joseph Imari Nolasco\n\n"
            + "This app is created as part of the technical qualification for YOYO Holdings PTE. LTD.");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
          @Override public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        });
        builder.show();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
