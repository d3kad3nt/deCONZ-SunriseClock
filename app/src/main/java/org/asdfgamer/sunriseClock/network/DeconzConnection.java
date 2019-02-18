package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.asdfgamer.sunriseClock.utils.ISO8601;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Used to communicate with the deconz endpoint. Expects the right required arguments to pass them
 * DIRECTLY to the endpoint. Therefore you have to ensure that the parameters are correctly
 * formatted for the deconz endpoint to successfully process them.
 */
public class DeconzConnection {

    private static final String TAG = "DeconzConnection";

    /* Full path to the API endpoint including API key, eg. 'deconz.example.org:8080/XXXXX' */
    private Uri fullApiUrl;

    /* Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org:8080' */
    private Uri baseUrl;

    /* API key for cmmunicating with deconz API. */
    private String apiKey;

    /* Used for sending out network requests to the deconz endpoint. */
    com.android.volley.RequestQueue networkRequestQueue = RequestQueue.getInstance().requestQueue;

    DeconzConnection() {

    }

    DeconzConnection(Uri baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        this.fullApiUrl = baseUrl
                .buildUpon()
                .path("api")
                .appendPath(apiKey)
                .build();
        Log.i(TAG, "DeconzConnection created. fullApiUrl: " + fullApiUrl);
    }

    Uri buildRequestUrl(String path) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .build();
    }

    Uri buildRequestUrl(String path, String id) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .build();
    }

    Uri buildRequestUrl(String path, String id, String additional) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .appendPath(additional)
                .build();
    }

    public Uri getFullApiUrl() {
        return fullApiUrl;
    }

    public Uri getBaseUrl() {
        return baseUrl;
    }

    public String getApiKey() {
        return apiKey;
    }

    public com.android.volley.RequestQueue getNetworkRequestQueue() {
        return networkRequestQueue;
    }

}
