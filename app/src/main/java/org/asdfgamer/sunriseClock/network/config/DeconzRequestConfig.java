package org.asdfgamer.sunriseClock.network.config;

import android.net.Uri;
import android.util.Log;

import org.asdfgamer.sunriseClock.network.utils.DeconzRequest;
import org.asdfgamer.sunriseClock.network.utils.response.callback.BaseCallback;
import org.asdfgamer.sunriseClock.network.utils.response.callback.BaseCallbackAdapter;

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

    public void getConfig(BaseCallback<Config> callback) {
        Call<Config> call = configEndpoint.getConfig();
        call.enqueue(new BaseCallbackAdapter<>(callback));
    }

    private interface ConfigEndpoint {

        @GET("config/")
        Call<Config> getConfig();
    }
}
