package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.remote.deconz.IServices;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RemoteLightListTypeAdapter implements JsonDeserializer<List<RemoteLight>> {

    private static final String TAG = "RemoteLightListTypeAda.";

    private final Gson gson;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *                   response, therefore it has to be set manually for a specific RemoteLight when deserializing
     *                   it.
     */
    public RemoteLightListTypeAdapter(long endpointId) {
        this.gson =
            new GsonBuilder().registerTypeAdapter(RemoteLight.class, new RemoteLightTypeAdapter(endpointId)).create();
    }

    @Override
    public List<RemoteLight> deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
        JsonObject rawJson = json.getAsJsonObject();
        List<RemoteLight> lights = new ArrayList<>();

        Log.d(TAG, "Parsing JSON for list of lights: " + rawJson.toString());

        for (String lightId : rawJson.keySet()) {
            JsonObject jsonLight = rawJson.get(lightId).getAsJsonObject();

            // Preprocessing: Manipulate returned json to include the light id.
            // This enables the existing Gson typeadapter (RemoteLightTypeAdapter) to work for requests for both
            // single and multiple light(s).
            jsonLight.addProperty(IServices.endpointLightIdHeader, lightId);

            // Returns a single light by calling the already existing Gson typeadapter.
            RemoteLight light = gson.fromJson(jsonLight, RemoteLight.class);
            lights.add(light);
        }
        return lights;
    }
}