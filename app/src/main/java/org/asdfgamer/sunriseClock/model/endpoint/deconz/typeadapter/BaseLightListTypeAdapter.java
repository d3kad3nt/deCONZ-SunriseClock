package org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseLightListTypeAdapter implements JsonDeserializer<List<BaseLight>> {

    private static final String TAG = "BaseLightListTypeAdapt.";

    private int endpointId;

    private Gson gson;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is
     *                   not part of the JSON response, therefore it has to be set manually for a
     *                   specific BaseLight when deserializing it.
     */
    public BaseLightListTypeAdapter(int endpointId) {
        this.endpointId = endpointId;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(BaseLight.class, new BaseLightTypeAdapter(endpointId))
                .create();
    }

    @Override
    public List<BaseLight> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rawJson = json.getAsJsonObject();
        List<BaseLight> lights = new ArrayList<>();

        Log.d(TAG, "Parsing JSON for list of lights: " + rawJson.toString());

        for (String lightId : rawJson.keySet()) {
            JsonElement jsonLight = rawJson.get(lightId);

            // Returns a single light by calling the already existing Gson typeadapter.
            BaseLight light = gson.fromJson(jsonLight.getAsJsonObject(), BaseLight.class);

            // Postprocessing: Manipulate returned BaseLight to include all required
            // (non-automatically deserialized) fields.
            light.setEndpointLightId(Integer.parseInt(lightId));
            light.setEndpointId(this.endpointId);

            light.id = Integer.parseInt(lightId);

            lights.add(light);
        }

        return lights;
    }
}
