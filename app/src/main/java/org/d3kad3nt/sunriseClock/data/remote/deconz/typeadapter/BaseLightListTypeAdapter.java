package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.data.model.light.DbLight;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseLightListTypeAdapter implements JsonDeserializer<List<DbLight>> {

    private static final String TAG = "BaseLightListTypeAdapt.";

    private long endpointId;

    private Gson gson;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is
     *                   not part of the JSON response, therefore it has to be set manually for a
     *                   specific DbLight when deserializing it.
     */
    public BaseLightListTypeAdapter(long endpointId) {
        this.endpointId = endpointId;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(DbLight.class, new BaseLightTypeAdapter(endpointId))
                .create();
    }

    @Override
    public List<DbLight> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject rawJson = json.getAsJsonObject();
        List<DbLight> lights = new ArrayList<>();

        Log.d(TAG, "Parsing JSON for list of lights: " + rawJson.toString());

        for (String lightId : rawJson.keySet()) {
            JsonElement jsonLight = rawJson.get(lightId);

            // Returns a single light by calling the already existing Gson typeadapter.
            DbLight light = gson.fromJson(jsonLight.getAsJsonObject(), DbLight.class);

            // Postprocessing: Manipulate returned DbLight to include all required
            // (non-automatically deserialized) fields.
            light.setEndpointLightId(lightId);
            light.setEndpointId(this.endpointId);

            lights.add(light);
        }

        return lights;
    }
}
