package org.asdfgamer.sunriseClock.network;

import android.net.Uri;

import com.android.volley.Response;

import org.json.JSONObject;

public class DeconzRequestGetConf extends DeconzRequest implements IResponseListenerJSONObject {

    public DeconzRequestGetConf(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    private static final String TAG = "DeconzRequestGetConf";

    @Override
    public void init() {
        super.setBaseCommandPath(super.getBaseCommandPath().buildUpon().appendPath(DeconzApiEndpoints.CONFIGURATION.getApiEndpoint()).build());
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
