package org.asdfgamer.sunriseClock.network.utils;

import android.net.Uri;

public class RequestPathBuilder {

    public RequestPathBuilder(Uri fullApiUrl) {
        this.fullApiUrl = fullApiUrl;
    }

    private final static String TAG = "RequestPathBuilder";

    private Uri fullApiUrl;

    public Uri getRequestUrl(String path) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .build();
    }

    public Uri getRequestUrl(String path, String id) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .build();
    }

    public Uri getRequestUrl(String path, String id, String additional) {
        return fullApiUrl
                .buildUpon()
                .appendPath(path)
                .appendPath(id)
                .appendPath(additional)
                .build();
    }
}
