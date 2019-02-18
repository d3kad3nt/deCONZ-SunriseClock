package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.TestConnectionTask;
import org.asdfgamer.sunriseClock.network.DeconzConnection;
import org.asdfgamer.sunriseClock.network.DeconzRequest;
import org.asdfgamer.sunriseClock.network.DeconzRequestTestConn;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
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

    private void testConnection() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(this.getContext()));
        alertDialogBuilder.setTitle("Your Title")
                .setMessage("Message here!");
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        new TestConnectionTask(alertDialog).execute();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));

        DeconzRequestTestConn deconz = new DeconzRequestTestConn(builder.build(), preferences.getString("pref_api_key", ""));
        deconz.testConnection();
    }
}
