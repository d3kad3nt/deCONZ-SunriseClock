package org.asdfgamer.sunriseClock.network.listener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.asdfgamer.sunriseClock.network.response.DeconzResponse;
import org.asdfgamer.sunriseClock.network.utils.VolleyErrorNetworkReponse;

/**
 * This is an default error listener for Network requests. It prints the description of the error.
 */
public class ErrorListener implements Response.ErrorListener {

    private final GUIListener guiListener;
    private final DeconzResponse response;

    public ErrorListener(GUIListener guiListener, DeconzResponse response) {
        this.guiListener = guiListener;
        this.response = response;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        VolleyErrorNetworkReponse interpreter = new VolleyErrorNetworkReponse(error);
        interpreter.printError();
        if (error.networkResponse != null) {
            response.setStatuscode(error.networkResponse.statusCode);
        }
        response.setSuccess(false);
        response.setError(error);
        getGuiListener().callback(response);
    }

    @SuppressWarnings("WeakerAccess")
    public GUIListener getGuiListener() {
        return guiListener;
    }
}
