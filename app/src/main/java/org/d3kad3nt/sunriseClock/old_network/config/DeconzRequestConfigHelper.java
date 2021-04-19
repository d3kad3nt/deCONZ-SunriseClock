package org.d3kad3nt.sunriseClock.old_network.config;

import android.net.Uri;

/**
 * Implements some useful endpoint-specific helper methods for requests to deconz.
 */
public class DeconzRequestConfigHelper extends DeconzRequestConfig {
    public DeconzRequestConfigHelper(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }
}
