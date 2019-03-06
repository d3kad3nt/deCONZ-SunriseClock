package org.asdfgamer.sunriseClock.network.lights;

import android.net.Uri;

/**
 * Implements some useful endpoint-specific helper methods for requests to deconz.
 */
public class DeconzRequestLightsHelper extends DeconzRequestLights {
    public DeconzRequestLightsHelper(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }
}
