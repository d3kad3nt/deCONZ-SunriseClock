package org.asdfgamer.sunriseClock.network;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

class VolleyErrorNetworkReponse {

    VolleyErrorNetworkReponse(VolleyError volleyError) {
        this.volleyError = volleyError;
    }

    private static final String TAG = "VolleyErrorNetReponse";

    private VolleyError volleyError;

    /**
     * Provides default error handling for VolleyErrors.
     * Currently, only prints warnings.
     *
     */
    void printError() {

        //Handle volley specific errors.
        if (volleyError instanceof NoConnectionError) {
            Log.w(TAG, "onError: No network connection or connection could not be established.");
        } else if (volleyError instanceof NetworkError) {
            Log.w(TAG, "onError: Connection could not be established.");
        } else if (volleyError instanceof TimeoutError) {
            Log.w(TAG, "onError: Request timed out.");
        } else if (volleyError instanceof ParseError) {
            Log.w(TAG, "onError: Server response could not be parsed.");
        }

        //Handle API specific errors.
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null) {
            switch (networkResponse.statusCode) {
                case 400:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Bad_Request);
                    break;
                case 401:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Unauthorized);
                    break;
                case 403:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Forbidden);
                    break;
                case 404:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Resource_Not_Found);
                    break;
                case 503:
                    Log.w(TAG, "onError: API returned: " + DeconzReturnCodes.Service_Unavailable);
                    break;
                default:
                    Log.w(TAG, "onError: API returned unknown error code: " + networkResponse.statusCode);
            }
        }
    }
}
