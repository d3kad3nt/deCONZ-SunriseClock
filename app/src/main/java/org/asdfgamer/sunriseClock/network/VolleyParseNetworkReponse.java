package org.asdfgamer.sunriseClock.network;

import android.util.Log;

import com.android.volley.NetworkResponse;

class VolleyParseNetworkReponse {

    VolleyParseNetworkReponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    private static final String TAG = "VolleyParseNetResponse";

    private NetworkResponse networkResponse;

    /**
     * Provides default handling for when Volley receives data from the API.
     * This happens before the onSuccess-listener is called.
     * Currently, only prints info.
     *
     */
    void printReturnCode() {
        //Handle API specific errors.
        if (networkResponse != null) {
            switch (networkResponse.statusCode) {
                case 200:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzApiReturncodes.OK);
                    break;
                case 201:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzApiReturncodes.Created);
                    break;
                case 202:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzApiReturncodes.Accepted);
                    break;
                case 304:
                    Log.i(TAG, "onParseNetworkResponse: API returned: " + DeconzApiReturncodes.Not_Modified);
                    break;
                default:
                    Log.i(TAG, "onParseNetworkResponse: API returned unknown success code: " + networkResponse.statusCode);
            }
        }
    }

}
