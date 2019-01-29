package org.asdfgamer.sunriseClock.network;

import android.content.Context;

import com.android.volley.toolbox.Volley;

/**
 * Singleton class for unified access to volley request queue.
 */
public class RequestQueue {

    private static final String TAG = "RequestQueue";

    private static RequestQueue instance = null;

    com.android.volley.RequestQueue requestQueue;

    private RequestQueue(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized RequestQueue getInstance(Context context){
        if (instance == null) {
            instance = new RequestQueue(context);
        }
        return instance;
    }

    static synchronized RequestQueue getInstance(){
        if (instance == null) {
            throw new IllegalStateException(RequestQueue.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

}
