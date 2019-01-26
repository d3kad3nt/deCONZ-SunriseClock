package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

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

        //TODO: Generalize sending of data to deconz endpoint
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

    public void scheduleLight(int lightId, Date date) {
        Uri requestURL = buildRequestUrl("schedules");

        Log.i(TAG, "Requesting URL: " + requestURL.toString());

        JSONObject fullRequest = new JSONObject();
        JSONObject commandRequest = new JSONObject();
        JSONObject commandBodyRequest = new JSONObject();
        String commandAddress = (buildRequestUrl("lights", String.valueOf(lightId), "state")).getPath();

        Log.d(TAG, "CommandAddress set to: " + commandAddress);

        try {
            fullRequest.put("name", "000SunriseClock");
            fullRequest.put("description", "created by SunriseClock for Deconz/Hue");
            fullRequest.put("time", "2013-07-29T09:30:00"); //TODO: remove test value, date object not used

            commandRequest.put("method", "PUT");
            commandRequest.put("address", commandAddress);

            commandBodyRequest.put("on", true);

            commandRequest.put("body", commandBodyRequest);

            fullRequest.put("command", commandRequest);

        } catch (JSONException e) {
            Log.e(TAG, "scheduleLight: JSON could not be created.");
        }

        Log.i(TAG, "Created JSON: " + fullRequest.toString());

        //TODO: Generalize sending of data to deconz endpoint
        final CustomVolleyJsonArrayRequest jsObjRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, requestURL.toString(), fullRequest, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Connection succeeded. Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        });
        networkRequestQueue.add(jsObjRequest);
    }


    private Uri buildRequestUrl(String path) {
        Uri requestURL = fullApiUrl
                .buildUpon()
                .appendPath(path)
                .build();
        return requestURL;
    }

    private Uri buildRequestUrl(String path, String id) {
        Uri requestURL = fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .build();
        return requestURL;
    }

    private Uri buildRequestUrl(String path, String id, String additional) {
        Uri requestURL = fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .appendPath(additional)
                .build();
        return requestURL;
    }

}
