package org.d3kad3nt.sunriseClock.old_network.lights;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.d3kad3nt.sunriseClock.old_network.DeconzRequest;
import org.d3kad3nt.sunriseClock.old_network.lights.model.Light;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzBaseCallbackAdapter;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.DeconzGetCallbackAdapter;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.SimplifiedCallback;
import org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback.SimplifiedCallbackAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Monitor and control single lights.
 * This implements some actions for the "lights/" endpoint.
 *
 * To use the functions of this class use {@code org.d3kad3nt.sunriseClock.network.lights.DeconzRequestLightsHelper}
 *
 * @see <a href="https://dresden-elektronik.github.io/deconz-rest-doc/lights/">https://dresden-elektronik.github.io/deconz-rest-doc/lights/</a>
 */
public abstract class DeconzRequestLights extends DeconzRequest {

    DeconzRequestLights(Uri baseUrl, String apiKey) {
        super(baseUrl, apiKey);
        init();
    }

    private static final String TAG = "DeconzRequestLights";

    private LightsEndpoint lightsEndpoint;

    @Override
    public void init() {
        Log.d(TAG, "Init() called.");

        //Set custom Gson deserializer
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(List.class, new GetLightsDeserializer())
                .create();
        super.setGsonDeserializer(gson);

        this.lightsEndpoint = super.createService(LightsEndpoint.class);
    }

    /**
     * This request returns a list of all lights.
     *
     * @param callback The callback to handle the request-related events.
     */
    public void getLights(GetLightsCallback callback) {
        Call<List<Light>> call = lightsEndpoint.getLights();
        call.enqueue(new DeconzBaseCallbackAdapter<>(callback));
    }

    /**
     * This request returns a list of all lights.
     *
     * @param callback The callback to handle the request-related events.
     */
    public void getLights(SimplifiedCallback<List<Light>> callback) {
        Call<List<Light>> call = lightsEndpoint.getLights();
        call.enqueue(new SimplifiedCallbackAdapter<>(callback));
    }

    /**
     * This request returns the full state of a light.
     *
     * @param callback The callback to handle the request-related events.
     * @param lightId Id of the light to aks deconz for.
     */
    public void getLight(GetLightCallback callback, String lightId) {
        Call<Light> call = lightsEndpoint.getLight(lightId);
        call.enqueue(new DeconzGetCallbackAdapter<>(callback));
    }

    /**
     * This request returns the full state of a light.
     *
     * @param callback The callback to handle the request-related events.
     * @param lightId Id of the light to aks deconz for.
     */
    public void getLight(SimplifiedCallback<Light> callback, String lightId) {
        Call<Light> call = lightsEndpoint.getLight(lightId);
        call.enqueue(new SimplifiedCallbackAdapter<>(callback));
    }

    private interface LightsEndpoint {

        @GET("lights/")
        Call<List<Light>> getLights();

        @GET("lights/{lightId}/")
        Call<Light> getLight(@Path("lightId") String lightId);
    }
}
