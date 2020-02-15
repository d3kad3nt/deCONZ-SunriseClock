package org.asdfgamer.sunriseClock;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import java.util.Objects;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class PreferencesActivity extends AppCompatActivity implements PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    /**
     * This is the name that is added on the backstack if a subcategory of items is opened.
     */
    private static final String SUB_CATEGORY = "subcat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        Toolbar toolbar = findViewById(R.id.preferences_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragmentCompat caller, Preference pref) {
        Log.i("Preferences", "Switch to " + pref.toString());
        // Instantiate the new Fragment
        final Bundle args = pref.getExtras();
        final Fragment fragment = getSupportFragmentManager().getFragmentFactory().instantiate(
                getClassLoader(),
                pref.getFragment());
        fragment.setArguments(args);
        fragment.setTargetFragment(caller, 0);
        // Replace the existing Fragment with the new Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.preferences, fragment)
                .addToBackStack(SUB_CATEGORY)
                .commit();
        return true;
    }

    /**
     * This overrides the standard action when an element on the Action Bar is pressed.
     * Currently it is only used to correct the action ot the "up/back" Arrow.
     *
     * @param item the Item that is pressed
     * @return true, if everything was successful, otherwise false
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return navigateUp(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This navigates to the upper layer. If a subcategory is shown it will pop the BackStack,
     * otherwise is will navigate to the parent activity.
     */
    private boolean navigateUp(MenuItem item) {
        FragmentManager manager = getSupportFragmentManager();
        if (manager.getBackStackEntryCount() > 0 && Objects.equals(manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName(), SUB_CATEGORY)) {
            getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
