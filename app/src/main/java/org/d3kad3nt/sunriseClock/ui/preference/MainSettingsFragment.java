package org.d3kad3nt.sunriseClock.ui.preference;

import android.os.Bundle;
import androidx.navigation.fragment.NavHostFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import java.util.Map;
import java.util.Objects;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class MainSettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        LogUtil.d("Show settings view");
        setPreferencesFromResource(R.xml.preferences_main, rootKey);
        Map<String, Integer> links = Map.of(
                "pref_connectivity_category",
                R.id.action_mainSettingsFragment_to_connectivityFragment,
                "pref_alarm_category",
                R.id.action_mainSettingsFragment_to_alarmFragment,
                "pref_interface_category",
                R.id.action_mainSettingsFragment_to_interfaceFragment,
                "pref_debug_category",
                R.id.action_mainSettingsFragment_to_debugFragment);
        for (Map.Entry<String, Integer> entry : links.entrySet()) {
            ((Preference) Objects.requireNonNull(findPreference(entry.getKey())))
                    .setOnPreferenceClickListener(preference -> {
                        // The Integer has to be converted to a int.
                        // Otherwise a wrong function signature for navigate is selected
                        NavHostFragment.findNavController(this)
                                .navigate(entry.getValue().intValue());
                        return true;
                    });
        }
    }
}
