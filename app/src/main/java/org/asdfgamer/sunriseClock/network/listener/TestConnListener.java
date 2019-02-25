package org.asdfgamer.sunriseClock.network.listener;

import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Response;

import org.asdfgamer.sunriseClock.network.request.DeconzRequestGetConf;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseTestConn;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TestConnListener extends SuccessListener {
    private final Uri uri;
    private final SharedPreferences preferences;
    private final DeconzResponseTestConn deconzResponse;


    public TestConnListener(GUIListener guiListener, DeconzResponseTestConn respose, Uri uri, SharedPreferences preferences) {
        super(guiListener, respose);
        this.deconzResponse = respose;
        this.uri = uri;
        this.preferences = preferences;
    }

    @Override
    public void onResponse(JSONObject response) {
        DeconzRequestGetConf deconzRequestGetConf = new DeconzRequestGetConf(uri, preferences.getString("pref_api_key", ""));
        deconzRequestGetConf.init();

        //No error handling on this step. Simply requests information from deconz without error handling.
        deconzRequestGetConf.fire(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    deconzResponse.setApiVersion(response.get("apiversion").toString());
                    deconzResponse.setIp(response.get("ipaddress").toString());
                    deconzResponse.setSuccess(true);

                    SharedPreferences.Editor prefEditor = preferences.edit();
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat mdformat = new SimpleDateFormat("dd.MM.YY HH:mm", Locale.getDefault());
                    String strDate = mdformat.format(calendar.getTime());
                    prefEditor.putString("pref_test_connection", strDate);
                    prefEditor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getGuiListener().callback(deconzResponse);
            }
        }, new ErrorListener(getGuiListener(), deconzResponse));
    }
}
