package org.asdfgamer.sunriseClock.network;

import android.net.Uri;

public class DeconzRequestTestConn extends DeconzRequest{

    public DeconzRequestTestConn(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey, BASE_COMMAND_PATH);
    }

    private static final String TAG = "DeconzRequestTestConn";

    private static final Uri BASE_COMMAND_PATH = Uri.parse("lights");

    public void testConnection() {
        getFromDeconz();
    }
}
