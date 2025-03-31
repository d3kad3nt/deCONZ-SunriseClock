package org.d3kad3nt.sunriseClock.backend.data.remote.deconz.typeadapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class RemoteGroupListTypeAdapter implements JsonDeserializer<List<RemoteGroup>> {

    private final Gson gson;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *     response, therefore it has to be set manually for a specific RemoteLight when deserializing it.
     */
    public RemoteGroupListTypeAdapter(long endpointId) {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(RemoteGroup.class, new RemoteGroupTypeAdapter(endpointId))
                .create();
    }

    @Override
    public List<RemoteGroup> deserialize(
            final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject rawJson = json.getAsJsonObject();
        List<RemoteGroup> groups = new ArrayList<>();

        LogUtil.d("Parsing JSON for list of groups: %s" + rawJson.toString());

        for (String groupId : rawJson.keySet()) {
            JsonObject jsonGroup = rawJson.get(groupId).getAsJsonObject();

            // Returns a single group by calling the already existing Gson typeadapter.
            RemoteGroup group = gson.fromJson(jsonGroup, RemoteGroup.class);
            groups.add(group);
        }
        return groups;
    }
}
