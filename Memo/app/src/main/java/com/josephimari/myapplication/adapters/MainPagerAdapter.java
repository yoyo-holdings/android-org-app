package com.josephimari.myapplication.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.josephimari.myapplication.fragments.NoteFragment;
import com.josephimari.myapplication.fragments.TodoFragment;

/**
 * Created by Joseph on 5/30/2016.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

  final int PAGE_COUNT = 2;
  private String tabTitles[] = new String[] { "NOTE", "TODO" };

  public MainPagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return NoteFragment.newInstance();
      case 1:
        return TodoFragment.newInstance();
      default:
        return null;
    }
  }

  @Override public int getCount() {
    return PAGE_COUNT;
  }

  @Override public CharSequence getPageTitle(int position) {
    // Generate title based on item position
    return tabTitles[position];
  }

  public interface Updateable {
    public void update();
  }
}
