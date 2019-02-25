package org.asdfgamer.sunriseClock.network.response;

import org.json.JSONObject;

/**
 * This is used in all DeconzResponses that use JSON-Arrays to send the Data.
 */
public abstract class DeconzResponseObject extends DeconzResponse {

    /**
     * The data of the response als JSONObject.
     */
    private JSONObject data;

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
