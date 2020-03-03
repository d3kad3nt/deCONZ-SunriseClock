package org.asdfgamer.sunriseClock.model.endpoint.deconz;

import androidx.lifecycle.LiveData;

import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Defines all relevant deconz API endpoint addresses for usage with the retrofit library.
 */
interface IServices {

    @GET("lights/")
    LiveData<ApiResponse<List<BaseLight>>> getLights();

    @GET("lights/{lightId}/")
    LiveData<ApiResponse<BaseLight>> getLight(@Path("lightId") String lightId);
}
