package org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.lang.reflect.Type;

public class BaseLightTypeAdapter implements JsonDeserializer<BaseLight> {

    private static final String TAG = "BaseLightTypeAdapter";

    private int endpointId;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is
     *                   not part of the JSON response, therefore it has to be set manually for a
     *                   specific BaseLight when deserializing it.
     */
    public BaseLightTypeAdapter(int endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public BaseLight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        BaseLight deserializedLight = new BaseLight(this.endpointId, 1);

        Log.d(TAG, "Parsing JSON for single light: " + json.toString());

        JsonObject rawJson = json.getAsJsonObject();
        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        if (rawJson.has("name")) {
            deserializedLight.friendlyName = rawJson.get("name").getAsString();
        }

        if (rawJsonState.has("on")) {
            deserializedLight.switchable = true;
            deserializedLight.setOn(rawJsonState.get("on").getAsBoolean());
        }

        if (rawJsonState.has("bri")) {
            deserializedLight.dimmable = true;
            deserializedLight.setBrightness(rawJsonState.get("bri").getAsInt());
        }

        if (rawJsonState.has("colormode")) {
            switch (rawJsonState.get("colormode").getAsString()) {
                case "ct":
                    deserializedLight.temperaturable = true;
                    deserializedLight.setColorTemperature(rawJsonState.get("ct").getAsInt());
                case "xy":
                    deserializedLight.colorable = true;
                    //TODO: deserializedLight.setColor();
                case "hs":
                    deserializedLight.colorable = true;
                    //TODO: deserializedLight.setColor();
            }
        }

        return deserializedLight;
    }
}
