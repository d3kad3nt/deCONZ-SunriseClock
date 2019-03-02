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
import org.asdfgamer.sunriseClock.network.response.model.Light;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class DeconzRequestLights extends DeconzRequest {

    public DeconzRequestLights(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        init();
    }

    private static final String TAG = "DeconzRequestLights";

    private LightsEndpoint lightsEndpoint;

    void init() {
        Log.d(TAG, "Init() called.");

        //Set custom Gson deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class, new GetallLightsDeserializer())
                .create();
        super.setGsonDeserializer(gson);

        this.lightsEndpoint = super.createService(LightsEndpoint.class);
    }

    public void getLights(BaseCallback<List<Light>> callback) {
        Call<List<Light>> call = lightsEndpoint.getLights();
        call.enqueue(new BaseCallbackAdapter<>(callback));
    }

        public void getLight(GetCallback<Light> callback, String lightId) {
        Call<Light> call = lightsEndpoint.getLight(lightId);
        call.enqueue(new GetCallbackAdapter<>(callback));
    }

    private interface LightsEndpoint {

        @GET("lights/")
        Call<List<Light>> getLights();

        @GET("lights/{lightId}/")
        Call<Light> getLight(@Path("lightId") String lightId);
    }
}
