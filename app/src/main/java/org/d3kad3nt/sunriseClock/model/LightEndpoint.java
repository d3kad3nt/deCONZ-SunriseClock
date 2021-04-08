package org.d3kad3nt.sunriseClock.model;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.List;

public interface LightEndpoint {
    LiveData<ApiResponse<List<BaseLight>>> getLights();
    LiveData<ApiResponse<BaseLight>>  getLight(String id);
}
