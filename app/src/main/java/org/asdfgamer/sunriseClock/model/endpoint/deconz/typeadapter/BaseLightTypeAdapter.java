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

    @Override
    public BaseLight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Gson gson = new Gson();
        JsonObject rawJson = json.getAsJsonObject();

        Log.d(TAG, "Parsing JSON for single light: " + rawJson.toString());

        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        String friendlyName = rawJson.get("name").getAsString();
        boolean switchable = rawJsonState.has("on");
        boolean dimmable = rawJsonState.has("bri");
        //TODO: Other capabilites of BaseLight

        //TODO: Set correct values for light id and endpointid
        return new BaseLight(1, 1, friendlyName, switchable, dimmable, false, false);
    }
}
