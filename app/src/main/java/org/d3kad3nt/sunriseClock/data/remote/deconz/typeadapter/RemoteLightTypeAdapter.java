package org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.d3kad3nt.sunriseClock.data.model.endpoint.EndpointType;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLightBuilder;
import org.d3kad3nt.sunriseClock.data.remote.deconz.IServices;
import org.d3kad3nt.sunriseclock.util.LogUtil;

public class RemoteLightTypeAdapter implements JsonDeserializer<RemoteLight> {

    private final long endpointId;

    /**
     * Custom type adapter for usage with Gson.
     *
     * @param endpointId ID of the associated endpoint for this deserializer. The endpoint ID is not part of the JSON
     *     response, therefore it has to be set manually for a specific DbLight when deserializing it.
     */
    public RemoteLightTypeAdapter(long endpointId) {
        this.endpointId = endpointId;
    }

    @Override
    public RemoteLight deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        RemoteLightBuilder remoteLightBuilder =
                new RemoteLightBuilder().setEndpointType(EndpointType.DECONZ).setEndpointId(this.endpointId);

        LogUtil.d("Parsing JSON for single light: %s", json.toString());

        JsonObject rawJson = json.getAsJsonObject();
        JsonObject rawJsonState = rawJson.getAsJsonObject("state");

        // Workaround: Deconz endpoint does not return the id of a light when requesting a single
        // light. The Gson deserializer is automatically called and cannot access the id inside of
        // the original request. A okHttp interceptor is used to modify the JSON response from the
        // Deconz endpoint and adds this light id.
        // When requesting a list of RemoteLights, the endpointLightId is manually added to the
        // json, too.
        // This enables the existing Gson typeadapter (RemoteLightTypeAdapter) to work for requests
        // for both single and multiple light(s).
        if (rawJson.has(IServices.endpointLightIdHeader)) {
            remoteLightBuilder.setEndpointLightId(
                    rawJson.get(IServices.endpointLightIdHeader).getAsString());
        }

        if (rawJson.has("name")) {
            remoteLightBuilder.setName(rawJson.get("name").getAsString());
        }

        if (rawJsonState.has("on")) {
            remoteLightBuilder
                    .setIsSwitchable(true)
                    .setIsOn(rawJsonState.get("on").getAsBoolean());
        }

        if (rawJsonState.has("bri")) {
            remoteLightBuilder = remoteLightBuilder
                    .setIsDimmable(true)
                    .setBrightness(rawJsonState.get("bri").getAsInt());
        }

        if (rawJsonState.has("colormode")) {
            switch (rawJsonState.get("colormode").getAsString()) {
                case "ct":
                    remoteLightBuilder
                            .setIsTemperaturable(true)
                            .setColorTemperature(rawJsonState.get("ct").getAsInt());
                    break;
                case "xy":
                    remoteLightBuilder.setIsColorable(true);
                    // TODO: .setColor();
                    break;
                case "hs":
                    remoteLightBuilder.setIsColorable(true);
                    // TODO: .setColor();
                    break;
                default:
                    LogUtil.i(
                            "Unknown colormode: %s.",
                            rawJsonState.get("colormode").getAsString());
            }
        }

        if (rawJsonState.has("reachable")) {
            remoteLightBuilder.setIsReachable(rawJsonState.get("reachable").getAsBoolean());
        }
        return remoteLightBuilder.build();
    }
}
