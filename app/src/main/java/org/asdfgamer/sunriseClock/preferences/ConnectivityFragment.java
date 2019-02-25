package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.listener.GUIListener;
import org.asdfgamer.sunriseClock.network.request.DeconzRequestTestConn;
import org.asdfgamer.sunriseClock.network.response.DeconzResponse;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseTestConn;

import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public class ConnectivityFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "ConnectivityFragment";

    @Override
    public void onResume() {
        super.onResume();
        // Register the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        Log.i("sunriseClock", "Settings");
        setPreferencesFromResource(R.xml.preferences_connectivity, rootKey);

        findPreference("pref_test_connection").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Log.d(TAG, "Click on testConnection registered.");
                testConnection();
                return true;
            }
        });

        //Manually update the preferences summary on creation of this fragment.
        //This is used instead of a custom SimpleSummaryProvider. This method is easier for
        //updating the summary from different dialogs (eg. AlertDialog).
        updatePrefSummary(findPreference("pref_test_connection").getSharedPreferences(), "pref_test_connection");
    }

    private void testConnection() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Objects.requireNonNull(this.getContext()));
        //TODO: Show loading animation for running ConnectionTests instead of static message. volley returns after max 5 seconds (or so) automatically.
        alertDialogBuilder.setTitle(getResources().getString(R.string.connection_test))
                .setMessage(getResources().getString(R.string.connection_test_inprogress));
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        final SharedPreferences preferences = findPreference("pref_test_connection").getSharedPreferences();

        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));

        /* 1st Step: Tests connection (and authentication!) to deconz by requesting an API endpoint which is key-protected.
         * 2nd step: Requests some information from deconz to show it to the user.
         * */
        DeconzRequestTestConn deconz = new DeconzRequestTestConn(builder.build(), preferences.getString("pref_api_key", ""));
        deconz.init();
        deconz.testConnection(preferences, new GUIListener() {

            @Override
            public void successOutput(DeconzResponse response) {
                if (!(response instanceof DeconzResponseTestConn)) {
                    return;
                }
                DeconzResponseTestConn responseTestConn = (DeconzResponseTestConn) response;
                alertDialog.setMessage(getResources().getString(R.string.connection_test_success_apiversion) + ": " + responseTestConn.getApiVersion()
                        + System.getProperty("line.separator")
                        + getResources().getString(R.string.connection_test_success_ipaddress) + ": " + responseTestConn.getIp());
                alertDialog.setTitle(getResources().getString(R.string.connection_test_success_title));
            }

            @Override
            public void errorOutput(DeconzResponse response) {
                switch (response.getStatuscode()) {
                    case -1:
                        alertDialog.setMessage("unknown Networking error: " + response.getError().getMessage());
                        break;
                    case 400:
                        alertDialog.setMessage("onError: API returned: " + DeconzApiReturncodes.Bad_Request);
                        break;
                    case 401:
                        alertDialog.setMessage("onError: API returned: " + DeconzApiReturncodes.Unauthorized);
                        break;
                    case 403:
                        alertDialog.setMessage(getResources().getString(R.string.connection_test_error_wrongapikey));
                        break;
                    case 404:
                        alertDialog.setMessage("onError: API returned: " + DeconzApiReturncodes.Resource_Not_Found);
                        break;
                    case 503:
                        alertDialog.setMessage("onError: API returned: " + DeconzApiReturncodes.Service_Unavailable);
                        break;
                    default:
                        alertDialog.setMessage("onError: API returned unknown error code: " + response.getStatuscode());
                }
            }
        });
    }

    /**
     * Updates the summary of a preference.
     */
    private void updatePrefSummary(SharedPreferences sharedPreferences, String key) {
        String lastConnect = sharedPreferences.getString(key, "");
        Log.d(TAG, "updatePrefSummary with value: " + lastConnect);
        if (Objects.equals(lastConnect, "")) {
            findPreference(key).setSummary(getResources().getString(R.string.connection_test_summary_neverconnected));
        } else {
            findPreference(key).setSummary(getResources().getString(R.string.connection_test_summary_lastconnected) + ": " + lastConnect);
        }
    }

    /**
     * This is automatically called if a SharedPreference changes.
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, key + " changed.");
        //Only handle summary changes for preferences without SimpleSummaryProvider.
        if (key.equals("pref_test_connection")) {
            updatePrefSummary(sharedPreferences, key);
        }
    }
}
