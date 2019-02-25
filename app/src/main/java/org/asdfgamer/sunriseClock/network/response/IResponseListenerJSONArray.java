package org.asdfgamer.sunriseClock.network.response;

import com.android.volley.Response;

import org.json.JSONArray;

public interface IResponseListenerJSONArray {

    /* Should fire  the request towards deconz. Uses own success and error listeners. */
    void fire(Response.Listener<JSONArray> customListenerSuccess, Response.ErrorListener customListenerError);

}
