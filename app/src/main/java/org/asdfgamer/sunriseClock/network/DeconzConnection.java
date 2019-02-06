package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.asdfgamer.sunriseClock.utils.ISO8601;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Date;

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
    private com.android.volley.RequestQueue networkRequestQueue = RequestQueue.getInstance().requestQueue;

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
        Uri requestUrl = buildRequestUrl("lights");

        getFromDeconz(requestUrl);
    }

    /**
     * Schedule basic power on for the given lightId. Time and date have to be given in the specified
     * format (see deconz REST-API docs for this).
     *
     * @param lightId ID representing the light to be controlled.
     * @param date    Date and time for power on.
     */
    public void scheduleLight(String lightId, ISO8601 date) {
        Uri requestUrl = buildRequestUrl("schedules");

        JSONObject postJsonData = new JSONObject();
        JSONObject commandRequest = new JSONObject();
        JSONObject commandBodyRequest = new JSONObject();
        String commandAddress = (buildRequestUrl("lights", lightId, "state")).getPath();

        try {
            postJsonData.put("name", "000SunriseClock");
            postJsonData.put("description", "created by SunriseClock for Deconz/Hue");
            postJsonData.put("time", date.toString());

            commandRequest.put("method", "PUT");
            commandRequest.put("address", commandAddress);

            commandBodyRequest.put("on", true);

            commandRequest.put("body", commandBodyRequest);

            postJsonData.put("command", commandRequest);

        } catch (JSONException e) {
            Log.e(TAG, "scheduleLight: JSON could not be created.");
        }
        sendToDeconz(requestUrl, postJsonData);
    }

    /**
     * Uses GET to get data (duh!) from the deconz endpoint. Uses default listeners for response and Error Listeners.
     *
     * @param fullApiPath Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     */
    private void getFromDeconz(Uri fullApiPath) {
        Log.d("getFromDeconz", "GET from: " + fullApiPath);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, fullApiPath.toString(), null, new Response.Listener<JSONObject>() {
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
                    Log.w(TAG, "Connection not possible: " + error.networkResponse.statusCode);
                }
            }
        });
        networkRequestQueue.add(jsObjRequest);
    }

    /**
     * Uses GET to get data (duh!) from the deconz endpoint. Uses own listeners for response and Error Listeners.
     *
     * @param fullApiPath           Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     * @param customListenerSuccess Custom listener for when the request returns successfully.
     * @param customListenerError   Custom listener for when the request returns unsuccessfully.
     */
    private void getFromDeconz(Uri fullApiPath, Response.Listener<JSONObject> customListenerSuccess, Response.ErrorListener customListenerError) {
        Log.d("getFromDeconz", "GET from: " + fullApiPath);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, fullApiPath.toString(), null, customListenerSuccess, customListenerError);
        networkRequestQueue.add(jsObjRequest);
    }

    /**
     * Uses POST to send data to the deconz endpoint. Uses default listeners for Response and Error listeners.
     * Expects to receive a JSON array (not object!): This is, at least, the case for the /schedules endpoint.
     *
     * @param fullApiPath  Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     * @param postJsonData Json object to send to the deconz API.
     */
    private void sendToDeconz(Uri fullApiPath, JSONObject postJsonData) {
        Log.d("sendToDeconz", "POSTing " + postJsonData.toString() + " to: " + fullApiPath);
        final CustomVolleyJsonArrayRequest jsObjRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, fullApiPath.toString(), postJsonData, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Connection succeeded. Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null) {
                    Log.w(TAG, "Connection not possible: " + error.networkResponse.statusCode);
                }
            }
        });
        networkRequestQueue.add(jsObjRequest);
    }

    /**
     * Uses POST to send data to the deconz endpoint. Uses own listeners for Response and Error listeners.
     * Expects to receive a JSON array (not object!): This is, at least, the case for the /schedules endpoint.
     *
     * @param fullApiPath           Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     * @param postJsonData          Json object to send to the deconz API.
     * @param customListenerSuccess Custom listener for when the request returns successfully.
     * @param customListenerError   Custom listener for when the request returns unsuccessfully.
     */
    private void sendToDeconz(Uri fullApiPath, JSONObject postJsonData, Response.Listener<JSONArray> customListenerSuccess, Response.ErrorListener customListenerError) {
        Log.d("sendToDeconz", "POSTing " + postJsonData.toString() + " to: " + fullApiPath);
        final CustomVolleyJsonArrayRequest jsObjRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, fullApiPath.toString(), postJsonData, customListenerSuccess, customListenerError);
        networkRequestQueue.add(jsObjRequest);
    }

    private Uri buildRequestUrl(String path) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .build();
    }

    private Uri buildRequestUrl(String path, String id) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .build();
    }

    private Uri buildRequestUrl(String path, String id, String additional) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .appendPath(additional)
                .build();
    }

}
