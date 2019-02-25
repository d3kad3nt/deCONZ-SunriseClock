package org.asdfgamer.sunriseClock.network.listener;

import android.util.Log;

import com.android.volley.Response;

import org.asdfgamer.sunriseClock.network.response.DeconzResponse;
import org.json.JSONObject;

public abstract class SuccessListener implements Response.Listener<JSONObject> {

    private final static String TAG = "SuccessListener";

    /**
     * The Listener that comes from the gui and returns the results to it.
     */
    private final GUIListener guiListener;
    private final DeconzResponse deconzResponse;


    SuccessListener(GUIListener guiListener, DeconzResponse respose) {
        this.guiListener = guiListener;
        this.deconzResponse = respose;
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.i(TAG, "The request was an Success");
        getGuiListener().callback(deconzResponse);
    }

    GUIListener getGuiListener() {
        return guiListener;
    }

    public DeconzResponse getDeconzResponse() {
        return deconzResponse;
    }
}
