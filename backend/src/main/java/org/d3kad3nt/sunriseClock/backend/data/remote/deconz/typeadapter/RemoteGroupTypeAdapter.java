package org.d3kad3nt.sunriseClock.backend.data.remote.deconz.typeadapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import org.d3kad3nt.sunriseClock.backend.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroupBuilder;
import org.d3kad3nt.sunriseClock.util.LogUtil;

public class RemoteGroupTypeAdapter implements JsonDeserializer<RemoteGroup> {

    private final long endpointId;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *     response, therefore it has to be set manually for a specific DbGroup when deserializing it.
     */
    public RemoteGroupTypeAdapter(long endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public RemoteGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        RemoteGroupBuilder remoteGroupBuilder =
                new RemoteGroupBuilder().setEndpointType(EndpointType.DECONZ).setEndpointId(this.endpointId);

        LogUtil.d("Parsing JSON for single group: %s" + json.toString());

        JsonObject rawJson = json.getAsJsonObject();
        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        // Unlike the /lights path, the /groups path does return group IDs.
        // No workaround to carry over the ID is needed!
        if (rawJson.has("id")) {
            remoteGroupBuilder =
                    remoteGroupBuilder.setEndpointGroupId(rawJson.get("id").getAsString());
        }

        if (rawJson.has("name")) {
            remoteGroupBuilder = remoteGroupBuilder.setName(rawJson.get("name").getAsString());
        }

        if (rawJson.has("lights")) {
            ArrayList<String> lightIds = new ArrayList<>();
            JsonArray lightsArray = rawJson.getAsJsonArray("lights");
            for (JsonElement light : lightsArray) {
                lightIds.add(light.getAsString());
            }
            remoteGroupBuilder = remoteGroupBuilder.setEndpointLightIds(lightIds);
        }

        if (rawJsonState.has("all_on")) {
            remoteGroupBuilder =
                    remoteGroupBuilder.setIsOnAll(rawJsonState.get("all_on").getAsBoolean());
        }

        return remoteGroupBuilder.build();
    }
}
