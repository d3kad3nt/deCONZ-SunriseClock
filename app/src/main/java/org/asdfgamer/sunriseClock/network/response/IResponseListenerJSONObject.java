package org.asdfgamer.sunriseClock.network.response;

import android.content.SharedPreferences;

import com.android.volley.Response;

import org.asdfgamer.sunriseClock.network.listener.GUIListener;
import org.json.JSONObject;

public interface IResponseListenerJSONObject {

    /* Should fire  the request towards deconz. Uses own success and error listeners. */
    void fire(GUIListener guiListener);

}
