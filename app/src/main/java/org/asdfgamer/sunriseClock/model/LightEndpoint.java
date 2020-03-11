package org.asdfgamer.sunriseClock.model;

import androidx.lifecycle.LiveData;

import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.util.List;

public interface LightEndpoint {

    void requestSetOn(BaseLight light, boolean value);
    void requestSetColor(BaseLight light, int color);
    void requestSetBrightness(BaseLight light, int brightness);
    void requestSetColorTemperature(BaseLight light, int colortemp);

    LiveData<ApiResponse<List<BaseLight>>> getLights();
    LiveData<ApiResponse<BaseLight>>  getLight(String id);
}
