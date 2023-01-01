package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;

import java.util.List;

import okhttp3.ResponseBody;

public interface LightEndpoint {
    LiveData<ApiResponse<List<RemoteLight>>> getLights();

    LiveData<ApiResponse<RemoteLight>> getLight(String id);

    LiveData<ApiResponse<ResponseBody>> setOnState(String id, boolean newState);

    LiveData<ApiResponse<ResponseBody>> setBrightness(String endpointLightId, double brightness);
}
