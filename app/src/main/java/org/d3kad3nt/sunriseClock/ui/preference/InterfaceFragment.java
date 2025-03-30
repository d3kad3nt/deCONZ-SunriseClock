package org.d3kad3nt.sunriseClock.ui.preference;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import org.d3kad3nt.sunriseClock.R;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public class InterfaceFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        LogUtil.i("Settings");
        setPreferencesFromResource(R.xml.preferences_interface, rootKey);
    }
}
