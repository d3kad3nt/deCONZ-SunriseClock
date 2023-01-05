package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.data.model.group.RemoteGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class RemoteGroupListTypeAdapter implements JsonDeserializer<List<RemoteGroup>> {

    private static final String TAG = "RemoteGroupListTypeAda.";

    private final Gson gson;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *                   response, therefore it has to be set manually for a specific RemoteLight when deserializing
     *                   it.
     */
    public RemoteGroupListTypeAdapter(long endpointId) {
        this.gson =
            new GsonBuilder().registerTypeAdapter(RemoteGroup.class, new RemoteGroupTypeAdapter(endpointId)).create();
    }

    @Override
    public List<RemoteGroup> deserialize(final JsonElement json,
        final Type typeOfT,
        final JsonDeserializationContext context) throws JsonParseException
    {
        JsonObject rawJson = json.getAsJsonObject();
        List<RemoteGroup> groups = new ArrayList<>();

        Log.d(TAG, "Parsing JSON for list of groups: " + rawJson.toString());

        for (String groupId : rawJson.keySet()) {
            JsonObject jsonGroup = rawJson.get(groupId).getAsJsonObject();

            // Returns a single group by calling the already existing Gson typeadapter.
            RemoteGroup group = gson.fromJson(jsonGroup, RemoteGroup.class);
            groups.add(group);
        }
        return groups;
    }
}
