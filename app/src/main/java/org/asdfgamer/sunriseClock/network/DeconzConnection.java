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
     * Default listeners are useful for debugging purposes because they print several status messages.
     *
     * @param fullApiPath Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     */
    private void getFromDeconz(Uri fullApiPath) {
        Log.d("getFromDeconz", "GET from: " + fullApiPath);
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, fullApiPath.toString(), null
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i(TAG, "Connection succeeded. Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        }) {
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                handleVolleyNetworkReponse(response);
                return super.parseNetworkResponse(response);
            }
        };
        networkRequestQueue.add(jsObjRequest);
    }

    /**
     * Uses GET to get data (duh!) from the deconz endpoint. Uses own listeners for response and Error Listeners.
     * Own listeners can be used to update GUI elements (eg in fragments). Don't use default listeners for these situations.
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
     * Default listeners are useful for debugging purposes because they print several status messages.
     * Expects to receive a JSON array (not object!): This is, at least, the case for the /schedules endpoint.
     *
     * @param fullApiPath  Full path within to the deconz API endpoint, e.g. 'deconz.example.org:8080/lights/1'
     * @param postJsonData Json object to send to the deconz API.
     */
    private void sendToDeconz(Uri fullApiPath, JSONObject postJsonData) {
        Log.d("sendToDeconz", "POSTing " + postJsonData.toString() + " to: " + fullApiPath);
        final CustomVolleyJsonArrayRequest jsObjRequest = new CustomVolleyJsonArrayRequest(Request.Method.POST, fullApiPath.toString(), postJsonData
                , new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i(TAG, "Connection succeeded. Response: " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handleVolleyError(error);
            }
        }) {
            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                handleVolleyNetworkReponse(response);
                return super.parseNetworkResponse(response);
            }
        };
        networkRequestQueue.add(jsObjRequest);
    }

    /**
     * Uses POST to send data to the deconz endpoint. Uses own listeners for Response and Error listeners.
     * Own listeners can be used to update GUI elements (eg in fragments). Don't use default listeners for these situations.
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

    /**
     * Provides default error handling for VolleyErrors.
     * Currently, only prints warnings.
     *
     * @param error Error from Volley.
     */
    private void handleVolleyError(VolleyError error) {

        //Handle volley specific errors.
        if (error instanceof NoConnectionError) {
            Log.w(TAG, "onError: No network connection or connection could not be established.");
        } else if (error instanceof NetworkError) {
            Log.w(TAG, "onError: Connection could not be established.");
        } else if (error instanceof TimeoutError) {
            Log.w(TAG, "onError: Request timed out.");
        } else if (error instanceof ParseError) {
            Log.w(TAG, "onError: Server response could not be parsed.");
        }

        //Handle API specific errors.
        NetworkResponse networkResponse = error.networkResponse;
        if (networkResponse != null) {
            switch (networkResponse.statusCode) {
                case 400:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Bad_Request);
                    break;
                case 401:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Unauthorized);
                    break;
                case 403:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Forbidden);
                    break;
                case 404:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Resource_Not_Found);
                    break;
                case 503:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Service_Unavailable);
                    break;
                default:
                    Log.w(TAG, "onError: API returned unknown error code: " + networkResponse.statusCode);
            }
        }
    }

    /**
     * Provides default handling for when Volley receives data from the API.
     * This happens before the onSuccess-listener is called.
     * Currently, only prints info.
     *
     * @param response Response from the API server.
     */
    private void handleVolleyNetworkReponse(NetworkResponse response) {
        //Handle API specific errors.
        if (response != null) {
            switch (response.statusCode) {
                case 200:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzReturnCodes.OK);
                    break;
                case 201:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzReturnCodes.Created);
                    break;
                case 202:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzReturnCodes.Accepted);
                    break;
                case 304:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzReturnCodes.Not_Modified);
                    break;
                default:
                    Log.i(TAG, "onParseNetworkResponse: API returned unknown success code: " + response.statusCode);
            }
        }
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
