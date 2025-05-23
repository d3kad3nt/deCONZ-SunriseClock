package org.d3kad3nt.sunriseClock.backend.data.model.endpoint;

import androidx.lifecycle.LiveData;
import java.util.List;
import okhttp3.ResponseBody;
import org.d3kad3nt.sunriseClock.backend.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;

public interface LightEndpoint {

    /** @return All lights on this endpoint. */
    LiveData<ApiResponse<List<RemoteLight>>> getLights();

    /**
     * @param endpointLightId String identifying the light on this endpoint.
     * @return The requested light on this endpoint.
     */
    LiveData<ApiResponse<RemoteLight>> getLight(String endpointLightId);

    /**
     * Turn the light on or off.
     *
     * @param endpointLightId String identifying the light on this endpoint.
     * @param newState Whether the light should be turned on (true) or off (false).
     */
    LiveData<ApiResponse<ResponseBody>> setOnState(String endpointLightId, boolean newState);

    /**
     * Set the brightness of the light.
     *
     * <p>TODO: Define whether a brightness of 0 means off or lowest BRIGHTNESS (but still on).
     *
     * @param endpointLightId String identifying the light on this endpoint.
     * @param brightness Desired light brightness, ranging from 0 (lowest) to 100 (highest).
     */
    LiveData<ApiResponse<ResponseBody>> setBrightness(String endpointLightId, int brightness);

    /**
     * Toggle all lights from on to off or vice versa.
     *
     * <p>If one or more lights are currently turned on, those should be turned off. If all lights are currently turned
     * off, all lights should be turned on.
     */
    LiveData<ApiResponse<ResponseBody>> toggleOnState();

    /**
     * Change the name of the light.
     *
     * <p>
     *
     * @param endpointLightId String identifying the light on this endpoint.
     * @param newName Desired name for this light.
     * @return A Livedata of the response from the endpoint. This doesn't have to have a meaningful content
     */
    LiveData<ApiResponse<ResponseBody>> setName(String endpointLightId, String newName);
}
