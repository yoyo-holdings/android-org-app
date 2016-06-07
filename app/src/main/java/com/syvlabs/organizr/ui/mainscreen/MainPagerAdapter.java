package com.syvlabs.organizr.ui.mainscreen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new ContentListFragment();
        Bundle args = new Bundle();
        args.putInt(FRAGMENT_TYPE, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
