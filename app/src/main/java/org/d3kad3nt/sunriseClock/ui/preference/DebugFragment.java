package org.d3kad3nt.sunriseClock.ui.preference;

import android.os.Bundle;
import android.util.Log;

import org.d3kad3nt.sunriseClock.R;

import androidx.preference.PreferenceFragmentCompat;

public class DebugFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_debug, rootKey);

    }


}
