package org.asdfgamer.sunriseClock.preferences;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.asdfgamer.sunriseClock.R;
import org.asdfgamer.sunriseClock.network.DeconzRequestGetConf;
import org.asdfgamer.sunriseClock.network.DeconzRequestTestConn;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.VolleyErrorNetworkReponse;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

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
        deconz.testConnection(new Response.Listener<JSONObject>() {
            //1st Step: If something fails error handling is used to give status information to the user.
            @Override
            public void onResponse(JSONObject response) {
                DeconzRequestGetConf deconzRequestGetConf = new DeconzRequestGetConf(builder.build(), preferences.getString("pref_api_key", ""));
                deconzRequestGetConf.init();

                //2nd Step: No error handling on this step. Simply requests information from deconz without error handling.
                //Only reached if 1st step returned successfully (-> we got an answer from deconz).
                deconzRequestGetConf.fire(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            alertDialog.setMessage(getResources().getString(R.string.connection_test_success_apiversion) + ": " + response.get("apiversion").toString()
                                    + System.getProperty("line.separator")
                                    + getResources().getString(R.string.connection_test_success_ipaddress) + ": " + response.get("ipaddress"));
                            alertDialog.setTitle(getResources().getString(R.string.connection_test_success_title));

                            SharedPreferences.Editor prefEditor = preferences.edit();
                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.YY HH:mm", Locale.getDefault());
                            String strDate = mdformat.format(calendar.getTime());
                            prefEditor.putString("pref_test_connection", strDate);
                            prefEditor.apply();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //TODO
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyErrorNetworkReponse volleyErrorNetworkReponse = new VolleyErrorNetworkReponse(volleyError);
                volleyErrorNetworkReponse.printError();

                alertDialog.setTitle(getResources().getString(R.string.connection_test_error_title));

                if (volleyError instanceof NoConnectionError || volleyError instanceof TimeoutError) {
                    alertDialog.setMessage(getResources().getString(R.string.connection_test_error_noconnection));
                } else if (volleyError instanceof ParseError) {
                    alertDialog.setMessage(getResources().getString(R.string.connection_test_error_parse));
                }

                NetworkResponse networkResponse = volleyError.networkResponse;
                if (networkResponse != null) {
                    switch (networkResponse.statusCode) {
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
                            alertDialog.setMessage("onError: API returned unknown error code: " + networkResponse.statusCode);
                    }
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
