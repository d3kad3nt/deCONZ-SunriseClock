package org.asdfgamer.sunriseClock.network.request;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.asdfgamer.sunriseClock.network.response.customDeserializers.LightsDeserializer;
import org.asdfgamer.sunriseClock.network.response.model.Light;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DeconzRequestLights extends DeconzRequest {

    public DeconzRequestLights(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
    }

    private static final String TAG = "DeconzRequestLights";

    private LightsEndpoint lightsEndpoint;

    public void init() {
        Log.d(TAG, "Init() called.");

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class, new LightsDeserializer())
                .create();
        super.setGsonDeserializer(gson);

        this.lightsEndpoint = super.createService(LightsEndpoint.class);
    }

    public void getLights(Callback<List<Light>> callback) {
        Call<List<Light>> call = lightsEndpoint.getLights();
        call.enqueue(callback);
    }

    public void getLight(Callback<Light> callback, String lightId) {
        Call<Light> call = lightsEndpoint.getLight(lightId);
        call.enqueue(callback);
    }


    private interface LightsEndpoint {

        @GET("lights/")
        Call<List<Light>> getLights();

        @GET("lights/{lightId}/")
        Call<Light> getLight(@Path("lightId") String lightId);
    }
}
