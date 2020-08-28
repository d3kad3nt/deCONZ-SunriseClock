package org.d3kad3nt.sunriseClock.model;

import androidx.lifecycle.LiveData;

import org.d3kad3nt.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.d3kad3nt.sunriseClock.model.light.BaseLight;

import java.util.List;

public interface LightEndpoint {

    void requestSetOn(BaseLight light, boolean value);
    void requestSetColor(BaseLight light, int color);
    void requestSetBrightness(BaseLight light, int brightness);
    void requestSetColorTemperature(BaseLight light, int colortemp);

    LiveData<ApiResponse<List<BaseLight>>> getLights();
    LiveData<ApiResponse<BaseLight>>  getLight(String id);
}
