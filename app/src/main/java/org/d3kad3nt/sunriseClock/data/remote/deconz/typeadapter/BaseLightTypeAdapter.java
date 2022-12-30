package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.data.model.light.DbLight;
import org.d3kad3nt.sunriseClock.data.remote.deconz.IServices;

import java.lang.reflect.Type;

public class BaseLightTypeAdapter implements JsonDeserializer<DbLight> {

    private static final String TAG = "BaseLightTypeAdapter";

    private final long endpointId;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is
     *                   not part of the JSON response, therefore it has to be set manually for a
     *                   specific DbLight when deserializing it.
     */
    public BaseLightTypeAdapter(long endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public DbLight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        DbLight deserializedLight = new DbLight(this.endpointId);

        Log.d(TAG, "Parsing JSON for single light: " + json.toString());

        JsonObject rawJson = json.getAsJsonObject();
        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        // Workaround: Deconz endpoint does not return the id of a light when requesting a single
        // light. The Gson deserializer is automatically called and cannot access the id inside of
        // the original request. A okHttp interceptor is used to modify the JSON response from the
        // Deconz endpoint and adds this light id.
        if (rawJson.has(IServices.endpointLightIdHeader)) {
            deserializedLight.setEndpointLightId(rawJson.get(IServices.endpointLightIdHeader).getAsString());
        }

        if (rawJson.has("name")) {
            deserializedLight.setFriendlyName( rawJson.get("name").getAsString());
        }

        if (rawJsonState.has("on")) {
            deserializedLight.switchable = true;
            deserializedLight.setOn(rawJsonState.get("on").getAsBoolean());
        }

        if (rawJsonState.has("bri")) {
            deserializedLight.dimmable = true;
            //Deconz returns a value between 0 and 255 and baseLight uses a value between 0 and 1
            deserializedLight.setBrightness(rawJsonState.get("bri").getAsInt()/255);
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
