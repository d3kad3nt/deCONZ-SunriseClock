package org.asdfgamer.sunriseClock.preferences;

import android.os.Bundle;
import android.util.Log;

import org.asdfgamer.sunriseClock.R;

import androidx.preference.PreferenceFragmentCompat;

public class InterfaceFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_interface, rootKey);

    }
}
