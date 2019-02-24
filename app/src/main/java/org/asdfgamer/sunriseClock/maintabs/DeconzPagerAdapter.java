package org.asdfgamer.sunriseClock.maintabs;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DeconzPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 4;
    private static String[] TITLES = new String[]{"Overview", "Lights", "Groups", "Time"};

    public DeconzPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(0, TITLES[position]);
            case 1:
                return LightsFragment.newInstance(1, TITLES[position]);
            case 2:
                return GroupsFragment.newInstance(2, TITLES[position]);
            case 3:
                return TimeFragment.newInstance(3, TITLES[position]);
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

}
