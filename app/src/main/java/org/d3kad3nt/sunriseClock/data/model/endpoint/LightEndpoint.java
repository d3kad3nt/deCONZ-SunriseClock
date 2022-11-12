package org.d3kad3nt.sunriseClock.data.model.endpoint;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;

import java.util.List;

import okhttp3.ResponseBody;

public interface LightEndpoint {
    LiveData<ApiResponse<List<BaseLight>>> getLights();
    LiveData<ApiResponse<BaseLight>>  getLight(String id);
    LiveData<ApiResponse<ResponseBody>> setOnState(String id, boolean newState);
}
