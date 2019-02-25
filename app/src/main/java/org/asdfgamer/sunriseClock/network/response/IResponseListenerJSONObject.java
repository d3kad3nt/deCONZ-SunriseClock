package org.asdfgamer.sunriseClock.network.response;

import com.android.volley.Response;

import org.json.JSONObject;

public interface IResponseListenerJSONObject {

    /* Should fire  the request towards deconz. Uses own success and error listeners. */
    void fire(Response.Listener<JSONObject> customListenerSuccess, Response.ErrorListener customListenerError);

}
