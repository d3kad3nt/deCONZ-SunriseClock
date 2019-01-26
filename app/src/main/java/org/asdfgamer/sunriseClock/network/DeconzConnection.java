package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.net.HttpURLConnection;

public class DeconzConnection {

    private static final String TAG = "DeconzConnection";

    private Uri baseUrl;
    private String apiKey;

    private Uri fullApiUrl;

    private RequestQueue networkRequestQueue = DeconzClient.getInstance().requestQueue;

    public DeconzConnection(Uri baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        this.fullApiUrl = baseUrl
                .buildUpon()
                .path("api")
                .appendPath(apiKey)
                .build();
        Log.i(TAG, "DeconzConnection created. fullApiUrl: " + fullApiUrl);
    }

    public void testConnection() {

        Uri requestURL = buildRequestUrl("lights");

        Log.i(TAG, "Requesting URL: " + requestURL.toString());

        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, requestURL.toString(), null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Connection succeeded. Response: " + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    switch (networkResponse.statusCode) {
                        case HttpURLConnection.HTTP_FORBIDDEN:
                            Log.w(TAG, "Connection not possible. HTTP_FORBIDDEN: " + error.networkResponse.statusCode);
                    }
                }
            }
        });

        // Add the request to the RequestQueue.
        networkRequestQueue.add(jsObjRequest);

    }

    private Uri buildRequestUrl(String path) {
        Uri requestURL = fullApiUrl
                .buildUpon()
                .appendPath(path)
                .build();
        return requestURL;
    }

}
