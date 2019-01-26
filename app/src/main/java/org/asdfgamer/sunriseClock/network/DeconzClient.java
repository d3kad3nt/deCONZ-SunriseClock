package org.asdfgamer.sunriseClock.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class for unified access to volley request queue.
 */
public class DeconzClient {

    private static final String TAG = "DeconzClient";

    private static DeconzClient instance = null;

    public RequestQueue requestQueue;

    private DeconzClient(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized DeconzClient getInstance(Context context){
        if (instance == null) {
            instance = new DeconzClient(context);
        }
        return instance;
    }

    public static synchronized DeconzClient getInstance(){
        if (instance == null) {
            throw new IllegalStateException(DeconzClient.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

}
