package org.d3kad3nt.sunriseClock.old_network.utils.response.custDeserializer;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.old_network.utils.response.custDeserializer.model.Success;

import java.lang.reflect.Type;

public class SuccessDeserializer implements JsonDeserializer<Success> {

    private static final String TAG = "SuccessDeserializer";

    @Override
    public Success deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jdc) throws JsonParseException
    {
        Log.i(TAG, "Starting custom deserialization.");

        JsonArray fullJsonArray = jsonElement.getAsJsonArray();

        Success deconzSuccess = new Gson().fromJson(fullJsonArray.get(0).getAsJsonObject().get("success"), Success.class);

        Log.i(TAG, "Finished custom deserialization");

        return deconzSuccess;
    }
}
