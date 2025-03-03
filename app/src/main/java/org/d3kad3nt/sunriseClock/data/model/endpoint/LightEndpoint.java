package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.annotation.IntRange;
import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;

import java.util.List;

import okhttp3.ResponseBody;

public interface LightEndpoint {

    /**
     * @return All lights on this endpoint.
     */
    LiveData<ApiResponse<List<RemoteLight>>> getLights();

    /**
     * @param endpointLightId String identifying the light on this endpoint.
     * @return The requested light on this endpoint.
     */
    LiveData<ApiResponse<RemoteLight>> getLight(String endpointLightId);

    /**
     * Turn the light on or off.
     * @param endpointLightId String identifying the light on this endpoint.
     * @param newState Whether the light should be turned on (true) or off (false).
     */
    LiveData<ApiResponse<ResponseBody>> setOnState(String endpointLightId, boolean newState);

    /**
     * Set the brightness of the light.
     * @param endpointLightId String identifying the light on this endpoint.
     * @param brightness Desired light brightness, ranging from 0 (lowest) to 100 (highest).
     *                   TODO: Define whether 0 means off or lowest brightness (but still on).
     */
    LiveData<ApiResponse<ResponseBody>> setBrightness(String endpointLightId, @IntRange(from = 0, to = 100) int brightness);

    /**
     * Toggle all lights from on to off or vice versa.
     * <p>
     * If one or more lights are currently turned on, those should be turned off.
     * If all lights are currently turned off, all lights should be turned on.
     */
    LiveData<ApiResponse<ResponseBody>> toggleOnState();
}
