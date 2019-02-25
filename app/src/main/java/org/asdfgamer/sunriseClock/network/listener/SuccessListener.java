package org.asdfgamer.sunriseClock.network.listener;

import android.util.Log;

import com.android.volley.Response;

import org.json.JSONObject;

public abstract class SuccessListener implements Response.Listener<JSONObject> {

    private final static String TAG = "SuccessListener";

    /**
     * The Listener that comes from the gui and returns the results to it.
     */
    private final GUIListener guiListener;


    SuccessListener(GUIListener guiListener) {
        this.guiListener = guiListener;
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.i(TAG, "The request was an Success");
    }

    GUIListener getGuiListener() {
        return guiListener;
    }
}
