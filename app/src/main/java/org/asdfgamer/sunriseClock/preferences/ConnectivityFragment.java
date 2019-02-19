package org.asdfgamer.sunriseClock.preferences;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkError;
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
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(this.getContext()));

        final Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .encodedAuthority(preferences.getString("pref_ip", "") + ":" + preferences.getString("pref_port", ""));

        DeconzRequestTestConn deconz = new DeconzRequestTestConn(builder.build(), preferences.getString("pref_api_key", ""));
        deconz.init();
        deconz.testConnection(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                DeconzRequestGetConf deconzRequestGetConf = new DeconzRequestGetConf(builder.build(), preferences.getString("pref_api_key", ""));
                deconzRequestGetConf.init();

                deconzRequestGetConf.fire(new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            alertDialog.setMessage("API version: " + response.get("apiversion").toString() + " on IP address: " + response.get("ipaddress"));
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

                if (volleyError instanceof NoConnectionError) {
                    alertDialog.setMessage("No network connection or connection could not be established.");
                } else if (volleyError instanceof NetworkError) {
                    alertDialog.setMessage("Connection could not be established.");
                } else if (volleyError instanceof TimeoutError) {
                    alertDialog.setMessage("Request timed out.");
                } else if (volleyError instanceof ParseError) {
                    alertDialog.setMessage("onError: Server response could not be parsed.");
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
                            alertDialog.setMessage("onError: API returned: " + DeconzApiReturncodes.Forbidden);
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
}
