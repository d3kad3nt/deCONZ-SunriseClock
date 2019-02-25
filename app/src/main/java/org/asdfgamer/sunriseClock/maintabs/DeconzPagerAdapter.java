package org.asdfgamer.sunriseClock.maintabs;

import android.content.Context;

import org.asdfgamer.sunriseClock.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class DeconzPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 3;

    private Context context;

    public DeconzPagerAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);
        this.context = context;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    // Returns the fragment to display for that page
    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OverviewFragment.newInstance(0, context.getString(R.string.tab_overview));
            case 1:
                return LightsFragment.newInstance(1, context.getString(R.string.tab_lights));
            case 2:
                return TimeFragment.newInstance(3, context.getString(R.string.tab_time));
            default:
                return OverviewFragment.newInstance(0, context.getString(R.string.tab_overview));
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.tab_overview);
            case 1:
                return context.getString(R.string.tab_lights);
            case 2:
                return context.getString(R.string.tab_time);
            default:
                return null;
        }
    }

}
