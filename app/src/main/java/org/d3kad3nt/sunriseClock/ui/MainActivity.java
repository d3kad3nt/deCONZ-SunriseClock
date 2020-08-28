package org.d3kad3nt.sunriseClock.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.tabs.TabLayout;

import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.ui.maintabs.DeconzPagerAdapter;
import org.d3kad3nt.sunriseClock.utils.SettingKeys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        toolbar.inflateMenu(R.menu.toolbar);
        setSupportActionBar(toolbar);

        /* Add a ViewPager to swipe between fragments/tabs. */
        ViewPager viewPager = findViewById(R.id.view_pager);
        FragmentPagerAdapter adapterViewPager = new DeconzPagerAdapter(getSupportFragmentManager(), this.getApplicationContext());
        viewPager.setAdapter(adapterViewPager);

        /* Bind TabLayout to our ViewPager. TabLayout in combination with ViewPager
         * allows us to set up a tabbed layout, while being able to use swipe gestures
         * to switch between fragments. */
        TabLayout tabLayout = findViewById(R.id.tab_bar);
        tabLayout.setupWithViewPager(viewPager);

        setDefaultPreferences(getApplicationContext());
    }

    private void setDefaultPreferences(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        initPreference(SettingKeys.ALARM_ACTIVE, true, preferences);
        initPreference(SettingKeys.IP, "localhost", preferences);
        initPreference(SettingKeys.PORT, "80", preferences);

    }

    private void initPreference(SettingKeys setting, boolean value, SharedPreferences preferences) {
        if (!preferences.contains(setting.toString())) {
            Log.i(TAG, "Set default value for " + setting + " to " + value);
            preferences.edit().putBoolean(setting.toString(), value).apply();
        }
    }

    private void initPreference(SettingKeys setting, String value, SharedPreferences preferences) {
        if (!preferences.contains(setting.toString())) {
            Log.i(TAG, "Set default value for " + setting + " to " + value);
            preferences.edit().putString(setting.toString(), value).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                showPreferences();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showPreferences() {
        Intent showPreferences = new Intent(this, PreferencesActivity.class);
        startActivity(showPreferences);
    }
}

