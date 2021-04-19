package org.d3kad3nt.sunriseClock.old_network.config;

import android.net.Uri;
import android.util.Log;

import org.d3kad3nt.sunriseClock.old_network.config.model.Config;
import org.d3kad3nt.sunriseClock.old_network.DeconzRequest;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzBaseCallbackAdapter;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.SimplifiedCallback;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.SimplifiedCallbackAdapter;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * The configuration endpoint allows to retrieve and modify the current configuration of the gateway.
 * This implements some actions for the "config/" endpoint.
 *
 * @see <a href="https://dresden-elektronik.github.io/deconz-rest-doc/configuration/">https://dresden-elektronik.github.io/deconz-rest-doc/configuration/</a>
 */
public abstract class DeconzRequestConfig extends DeconzRequest {

    DeconzRequestConfig(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        init();
    }

    private static final String TAG = "DeconzRequestConfig";

    private ConfigEndpoint configEndpoint;

    @Override
    public void init() {
        Log.d(TAG, "Init() called.");

        this.configEndpoint = super.createService(ConfigEndpoint.class);
    }

    /**
     * This request returns the current gateway configuration.
     *
     * @param callback The callback to handle the request-related events.
     */
    public void getConfig(GetConfigCallback callback) {
        Call<Config> call = configEndpoint.getConfig();
        call.enqueue(new DeconzBaseCallbackAdapter<>(callback));
    }

    /**
     * This request returns the current gateway configuration.
     *
     * @param callback The callback to handle the request-related events.
     */
    public void getConfig(SimplifiedCallback<Config> callback) {
        Call<Config> call = configEndpoint.getConfig();
        call.enqueue(new SimplifiedCallbackAdapter<>(callback));
    }

    private interface ConfigEndpoint {

        @GET("config/")
        Call<Config> getConfig();
    }
}
