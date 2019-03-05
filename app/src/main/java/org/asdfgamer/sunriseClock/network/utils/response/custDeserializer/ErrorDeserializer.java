package org.asdfgamer.sunriseClock.network.utils.response.custDeserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import java.lang.reflect.Type;

public class ErrorDeserializer implements JsonDeserializer<Error> {

    private static final String TAG = "ErrorDeserializer";

    @Override
    public Error deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Log.i(TAG, "Starting custom deserialization.");

        JsonArray fullJsonArray = jsonElement.getAsJsonArray();

        Error deconzError = new Gson().fromJson(fullJsonArray.get(0).getAsJsonObject().get("error"), Error.class);

        Log.i(TAG, "Finished custom deserialization");

        return deconzError;
    }
}
