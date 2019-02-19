package org.asdfgamer.sunriseClock.network;

import com.android.volley.Response;

import org.json.JSONObject;

interface IResponseListenerJSONObject {

    /* Should fire  the request towards deconz. Uses own success and error listeners. */
    void fire(Response.Listener<JSONObject> customListenerSuccess, Response.ErrorListener customListenerError);

}
