package org.d3kad3nt.sunriseClock.ui.preference;

import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import org.d3kad3nt.sunriseClock.R;

public class InterfaceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_interface, rootKey);

    }
}
