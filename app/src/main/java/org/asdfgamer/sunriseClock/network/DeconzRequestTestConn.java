package org.asdfgamer.sunriseClock.network;

import android.net.Uri;

public class DeconzRequestTestConn extends DeconzRequest{

    public DeconzRequestTestConn(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    public void testConnection() {
        Uri requestUrl = buildRequestUrl("lights");

        getFromDeconz(requestUrl);
    }
}
