package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.config.DeconzRequestConfigHelper;
import org.asdfgamer.sunriseClock.network.config.model.Config;
import org.asdfgamer.sunriseClock.network.config.DeconzRequestConfig;
import org.asdfgamer.sunriseClock.network.config.GetConfigCallback;
import org.asdfgamer.sunriseClock.network.lights.DeconzRequestLights;
import org.asdfgamer.sunriseClock.network.lights.DeconzRequestLightsHelper;
import org.asdfgamer.sunriseClock.network.lights.GetLightCallback;
import org.asdfgamer.sunriseClock.network.lights.GetLightsCallback;
import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.schedules.CreateScheduleCallback;
import org.asdfgamer.sunriseClock.network.schedules.DeconzRequestSchedules;
import org.asdfgamer.sunriseClock.network.schedules.DeconzRequestSchedulesHelper;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Success;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.SimplifiedCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.SimplifiedCallbackAdapter;
import org.asdfgamer.sunriseClock.utils.ISO8601;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import retrofit2.Call;
import retrofit2.Response;

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
         * 2nd step: Requests some information from deconz to show it to the user. */
        DeconzRequestLightsHelper deconzLights = new DeconzRequestLightsHelper(builder.build(), preferences.getString("pref_api_key", ""));

        deconzLights.getLights(new CustomGetLightsCallback() {
            @Override
            public void onSuccess(Response<List<Light>> response) {
                DeconzRequestConfigHelper deconzConfig = new DeconzRequestConfigHelper(builder.build(), preferences.getString("pref_api_key", ""));

                deconzConfig.getConfig(new SimplifiedCallback<Config>() {
                    @Override
                    public void onSuccess(Response<Config> response) {
                        Config config = response.body();

                        //TODO: Use xml layout for this alertDialog.
                        alertDialog.setMessage(getResources().getString(R.string.connection_test_success_apiversion) + ": " + Objects.requireNonNull(config).getApiversion()
                                + System.getProperty("line.separator")
                                + getResources().getString(R.string.connection_test_success_ipaddress) + ": " + config.getIpaddress());
                        alertDialog.setTitle(getResources().getString(R.string.connection_test_success_title));

                        SharedPreferences.Editor prefEditor = preferences.edit();
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.YY HH:mm", Locale.getDefault());
                        String strDate = mdformat.format(calendar.getTime());
                        prefEditor.putString("pref_test_connection", strDate);
                        prefEditor.apply();
                    }

                    @Override
                    public void onError() {
                        //This should not fail because getLights returned successfully just a few moments ago.
                    }
                });
            }

            @Override
            public void onForbidden(Error error) {
                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_wrongapikey));
            }

            @Override
            public void onNetworkFailure(Call<List<Light>> call, Throwable throwable) {
                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_noconnection));
            }

            @Override
            public void onInvalidResponseObject(Call<List<Light>> call, Throwable throwable) {
                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_nodeconz));
            }

            @Override
            public void onInvalidErrorObject() {
                alertDialog.setMessage(getResources().getString(R.string.connection_test_error_nodeconz));
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

    /**
     * Example for a callback in an inner class. This is used to 'remove'/hide some callbacks.
     * This particular example is not that useful, though.
     */
    abstract class CustomGetLightsCallback implements GetLightsCallback {

        @Override
        public abstract void onSuccess(Response<List<Light>> response);

        @Override
        public abstract void onForbidden(Error error);

        @Override
        public void onEverytime() {
            //Demo: Nothing should happen here.
        }

        @Override
        public abstract void onNetworkFailure(Call<List<Light>> call, Throwable throwable);

        @Override
        public abstract void onInvalidResponseObject(Call<List<Light>> call, Throwable throwable);

        @Override
        public abstract void onInvalidErrorObject();
    }

}
