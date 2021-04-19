package org.d3kad3nt.sunriseClock.old_network.lights;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.old_network.lights.model.Light;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetLightsDeserializer implements JsonDeserializer<List<?>> {

    private static final String TAG = "GetallLightsDeserial.";

    @Override
    public List<Light> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Log.i(TAG, "Starting custom deserialization.");

        JsonObject fullJsonObject = jsonElement.getAsJsonObject();
        List<Light> lights = new ArrayList<>();

        for (Map.Entry<String,JsonElement> entry : fullJsonObject.entrySet()) {

            // Using a new instance of Gson to avoid infinite recursion to this deserializer.
            Light light = new Gson().fromJson(entry.getValue(), Light.class);
            int lightID = Integer.parseInt(entry.getKey());
            light.setLightId(lightID);
            lights.add(light);
        }

        Log.i(TAG, "Finished custom deserialization");

        return lights;
    }
}
