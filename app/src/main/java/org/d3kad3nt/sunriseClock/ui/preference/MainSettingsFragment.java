package org.d3kad3nt.sunriseClock.ui.preference;

import android.os.Bundle;
import android.util.Log;

import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import org.d3kad3nt.sunriseClock.R;

import java.util.Map;
import java.util.Objects;

public class MainSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_main, rootKey);
        Map<String, Integer> links =
                Map.of("pref_connectivity_category", R.id.action_mainSettingsFragment_to_connectivityFragment,
                        "pref_alarm_category", R.id.action_mainSettingsFragment_to_alarmFragment,
                        "pref_interface_category", R.id.action_mainSettingsFragment_to_interfaceFragment,
                        "pref_debug_category", R.id.action_mainSettingsFragment_to_debugFragment);
        for (Map.Entry<String, Integer> entry : links.entrySet()) {
            ((Preference) Objects.requireNonNull(findPreference(entry.getKey()))).setOnPreferenceClickListener(
                    preference -> {
                        NavHostFragment.findNavController(this).navigate(entry.getValue());
                        return true;
                    });
        }
    }
}
