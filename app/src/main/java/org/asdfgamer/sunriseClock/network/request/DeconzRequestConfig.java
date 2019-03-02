package org.asdfgamer.sunriseClock.network.request;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.asdfgamer.sunriseClock.network.response.callback.BaseCallback;
import org.asdfgamer.sunriseClock.network.response.callback.BaseCallbackAdapter;
import org.asdfgamer.sunriseClock.network.response.callback.GetCallback;
import org.asdfgamer.sunriseClock.network.response.callback.GetCallbackAdapter;
import org.asdfgamer.sunriseClock.network.response.custDeserializer.GetallLightsDeserializer;
import org.asdfgamer.sunriseClock.network.response.model.Config;
import org.asdfgamer.sunriseClock.network.response.model.Light;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DeconzRequestConfig extends DeconzRequest {

    public DeconzRequestConfig(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        init();
    }

    private static final String TAG = "DeconzRequestConfig";

    private ConfigEndpoint configEndpoint;

    void init() {
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
