package org.asdfgamer.sunriseClock.network.response;

import org.json.JSONArray;

/**
 * This is used in all DeconzResponses that use JSON-Arrays to send the Data.
 */
public abstract class DeconzResponseArray extends DeconzResponse {

    /**
     * The data of the response as JSONArray
     */
    private JSONArray data;

    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
}
