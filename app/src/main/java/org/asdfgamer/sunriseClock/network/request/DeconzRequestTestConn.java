package org.asdfgamer.sunriseClock.network.request;

import android.content.SharedPreferences;
import android.net.Uri;

import com.android.volley.Response;

import org.asdfgamer.sunriseClock.network.DeconzApiEndpoints;
import org.asdfgamer.sunriseClock.network.listener.ErrorListener;
import org.asdfgamer.sunriseClock.network.listener.GUIListener;
import org.asdfgamer.sunriseClock.network.listener.TestConnListener;
import org.asdfgamer.sunriseClock.network.response.DeconzResponseTestConn;
import org.asdfgamer.sunriseClock.network.response.IResponseListenerJSONObject;
import org.json.JSONObject;

public class DeconzRequestTestConn extends DeconzRequest implements IResponseListenerJSONObject {

    public DeconzRequestTestConn(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    private static final String TAG = "DeconzRequestTestConn";

    public void testConnection(SharedPreferences preferences, GUIListener listener) {

        DeconzResponseTestConn deconzResponseTestConn = new DeconzResponseTestConn();
        TestConnListener testConnListener = new TestConnListener(listener, deconzResponseTestConn, getBaseUrl(), preferences);
        fire(testConnListener, new ErrorListener(listener, deconzResponseTestConn));
    }

    @Override
    public void init() {
        super.setBaseCommandPath(super.getBaseCommandPath().buildUpon().appendPath(DeconzApiEndpoints.LIGHTS.getApiEndpoint()).build());
    }

    @Override
    public void fire(Response.Listener<JSONObject> customListenerSuccess, Response.ErrorListener customListenerError) {
        getFromDeconz(customListenerSuccess, customListenerError);
    }

    @Override
    public void fire() {
        getFromDeconz();
    }

}