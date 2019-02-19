package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import com.android.volley.Response;

import org.asdfgamer.sunriseClock.utils.ISO8601;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeconzRequestSchLight extends DeconzRequest implements IResponseListenerJSONArray {

    /**
     * Schedule basic power on for the given lightId. Time and date have to be given in the constructor.
     * See deconz REST-API docs for the required format.
     */
    public DeconzRequestSchLight(Uri baseUrl, String apiKey, String lightId, ISO8601 date) {
        super(baseUrl, apiKey);

        this.lightId = lightId;
        this.date = date;
    }

    private static final String TAG = "DeconzRequestSchLight";

    private String lightId;
    private ISO8601 date;
    private JSONObject postJsonData = new JSONObject();

    @Override
    public void init() {
        super.setBaseCommandPath(super.getBaseCommandPath().buildUpon().appendPath(DeconzApiEndpoints.SCHEDULES.getApiEndpoint()).build());

        JSONObject commandRequest = new JSONObject();
        JSONObject commandBodyRequest = new JSONObject();
        String commandAddress = (super.getFullApiUrl().buildUpon().appendPath(DeconzApiEndpoints.LIGHTS.getApiEndpoint()).appendPath(this.lightId).appendPath("state").build()).getPath();

        try {
            postJsonData.put("name", "000SunriseClock");
            postJsonData.put("description", "created by SunriseClock for Deconz/Hue");
            postJsonData.put("time", date.toString());

            commandRequest.put("method", DeconzApiMethods.PUT.getMethod());
            commandRequest.put("address", commandAddress);

            commandBodyRequest.put("on", true);

            commandRequest.put("body", commandBodyRequest);

            postJsonData.put("command", commandRequest);

        } catch (JSONException e) {
            Log.e(TAG, "init: JSON could not be created.");
        }
    }

    @Override
    public void fire(Response.Listener<JSONArray> customListenerSuccess, Response.ErrorListener customListenerError) {
        sendToDeconz(postJsonData, customListenerSuccess, customListenerError);
    }

    @Override
    public void fire() {
        sendToDeconz(postJsonData);
    }

    public String getLightId() {
        return lightId;
    }

    public ISO8601 getDate() {
        return date;
    }
}
