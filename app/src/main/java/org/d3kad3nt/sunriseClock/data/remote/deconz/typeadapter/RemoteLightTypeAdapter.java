package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLightBuilder;
import org.d3kad3nt.sunriseClock.data.remote.deconz.IServices;

import java.lang.reflect.Type;

public class RemoteLightTypeAdapter implements JsonDeserializer<RemoteLight> {

    private static final String TAG = "RemoteLightTypeAdapter";

    private final long endpointId;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *                   response, therefore it has to be set manually for a specific DbLight when deserializing it.
     */
    public RemoteLightTypeAdapter(long endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public RemoteLight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        RemoteLightBuilder remoteLightBuilder = new RemoteLightBuilder().setEndpointType(EndpointType.DECONZ)
                                                                        .setEndpointId(this.endpointId);

        Log.d(TAG, "Parsing JSON for single light: " + json.toString());

        JsonObject rawJson = json.getAsJsonObject();
        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        // Workaround: Deconz endpoint does not return the id of a light when requesting a single
        // light. The Gson deserializer is automatically called and cannot access the id inside of
        // the original request. A okHttp interceptor is used to modify the JSON response from the
        // Deconz endpoint and adds this light id.
        // When requesting a list of RemoteLights, the endpointLightId is manually added to the json, too.
        // This enables the existing Gson typeadapter (RemoteLightTypeAdapter) to work for requests for both single
        // and multiple light(s).
        if (rawJson.has(IServices.endpointLightIdHeader)) {
            remoteLightBuilder = remoteLightBuilder.setEndpointLightId(rawJson.get(IServices.endpointLightIdHeader)
                                                                              .getAsString());
        }

        if (rawJson.has("name")) {
            remoteLightBuilder = remoteLightBuilder.setName(rawJson.get("name").getAsString());
        }

        if (rawJsonState.has("on")) {
            remoteLightBuilder = remoteLightBuilder.setIsSwitchable(true)
                                                   .setIsOn(rawJsonState.get("on").getAsBoolean());
        }

        if (rawJsonState.has("bri")) {
            remoteLightBuilder = remoteLightBuilder.setIsDimmable(true)
                                                   .setBrightness(rawJsonState.get("bri").getAsInt());
        }

        if (rawJsonState.has("colormode")) {
            switch (rawJsonState.get("colormode").getAsString()) {
                case "ct":
                    remoteLightBuilder = remoteLightBuilder.setIsTemperaturable(true)
                                                           .setColorTemperature(rawJsonState.get("ct").getAsInt());
                case "xy":
                    remoteLightBuilder = remoteLightBuilder.setIsColorable(true);
                    //TODO: .setColor();
                case "hs":
                    remoteLightBuilder = remoteLightBuilder.setIsColorable(true);
                    //TODO: .setColor();
            }
        }
        return remoteLightBuilder.build();
    }
}