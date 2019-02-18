package org.asdfgamer.sunriseClock.network;

import android.net.Uri;
import android.util.Log;

import org.asdfgamer.sunriseClock.utils.ISO8601;
import org.json.JSONException;
import org.json.JSONObject;

public class DeconzRequestSchLight extends DeconzRequest {

    public DeconzRequestSchLight(Uri baseUrl, String apiKey, String lightId, ISO8601 date) {
        super(baseUrl, apiKey);
        this.lightId = lightId;
        this.date = date;
    }

    private static final String TAG = "DeconzRequestSchLight";

    private String lightId;
    private ISO8601 date;

    /**
     * Schedule basic power on for the given lightId. Time and date have to be given in the constructor.
     * See deconz REST-API docs for the required format.
     */
    public void scheduleLight() {
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

    public String getLightId() {
        return lightId;
    }

    public void setLightId(String lightId) {
        this.lightId = lightId;
    }

    public ISO8601 getDate() {
        return date;
    }

    public void setDate(ISO8601 date) {
        this.date = date;
    }
}
