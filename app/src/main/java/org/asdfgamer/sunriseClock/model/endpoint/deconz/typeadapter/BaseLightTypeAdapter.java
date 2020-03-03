package org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter;

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

        BaseLight baseLight = new BaseLight()

        rawJson.get("name").getAsString();
    }
}
