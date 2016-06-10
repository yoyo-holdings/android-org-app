package me.dlet.androidorgapp.views.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.dlet.androidorgapp.views.fragments.NotesFragment;
import me.dlet.androidorgapp.views.fragments.TodosFragment;

/**
 * Created by darwinlouistoledo on 6/5/16.
 * Email: darwin.louis@ymail.com
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter{

    private int numOfTabs;

    public MainPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new NotesFragment();
            case 1:
                return new TodosFragment();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
