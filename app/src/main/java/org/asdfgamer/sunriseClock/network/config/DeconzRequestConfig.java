package org.asdfgamer.sunriseClock.network.config;

import android.net.Uri;
import android.util.Log;

import org.asdfgamer.sunriseClock.network.config.model.Config;
import org.asdfgamer.sunriseClock.network.DeconzRequest;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzBaseCallbackAdapter;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallbackAdapter;

import retrofit2.Call;
import retrofit2.http.GET;

public class DeconzRequestConfig extends DeconzRequest {

    public DeconzRequestConfig(Uri baseUrl, String apiKey) {
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

    public void getConfig(GetConfigCallback callback) {
        Call<Config> call = configEndpoint.getConfig();
        call.enqueue(new DeconzBaseCallbackAdapter<>(callback));
    }

    private interface ConfigEndpoint {

        @GET("config/")
        Call<Config> getConfig();
    }
}
