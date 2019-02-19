package org.asdfgamer.sunriseClock.network;

import android.net.Uri;

import com.android.volley.Response;

import org.json.JSONObject;

public class DeconzRequestTestConn extends DeconzRequest implements  IResponseListenerJSONObject {

    public DeconzRequestTestConn(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    private static final String TAG = "DeconzRequestTestConn";

    public void testConnection(Response.Listener<JSONObject> customListenerSuccess, Response.ErrorListener customListenerError) {
        getFromDeconz(customListenerSuccess, customListenerError);
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
