package org.asdfgamer.sunriseClock.network.utils;

import android.content.Context;

import com.android.volley.toolbox.Volley;

/**
 * Singleton class for unified access to volley request queue.
 */
public class DeconzRequestQueue {

    private static final String TAG = "DeconzRequestQueue";

    private static DeconzRequestQueue instance = null;

    private com.android.volley.RequestQueue requestQueue;

    private DeconzRequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized DeconzRequestQueue getInstance(Context context) {
        if (instance == null) {
            instance = new DeconzRequestQueue(context);
        }
        return instance;
    }

    public static synchronized DeconzRequestQueue getInstance() {
        if (instance == null) {
            throw new IllegalStateException(DeconzRequestQueue.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public com.android.volley.RequestQueue getRequestQueue() {
        return requestQueue;
    }
}
