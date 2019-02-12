package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.DeconzConnection;

import java.util.Objects;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class ConnectivityFragment extends PreferenceFragmentCompat {

    private static final String TAG = "ConnectivityFragment";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_connectivity, rootKey);


        Preference testConnectionPreference = findPreference("pref_test_connection");

        testConnectionPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Click on testConnection registered.");
                testConnection();
                return true;
            }
        });

        testConnectionPreference.setSummaryProvider(new Preference.SummaryProvider<Preference>() {
            @Override
            public CharSequence provideSummary(Preference preference) {
                return "TODO";
            }
        });
    }

    //TODO: Actually make the GUI give some feedback to the user.
    private void testConnection() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));

        DeconzConnection deconz = new DeconzConnection(builder.build(), preferences.getString("pref_api_key", ""));
        deconz.testConnection();
    }
}
