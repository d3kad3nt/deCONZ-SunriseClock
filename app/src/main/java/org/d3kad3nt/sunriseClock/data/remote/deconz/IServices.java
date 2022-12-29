package org.d3kad3nt.sunriseClock.data.remote.deconz;

import androidx.lifecycle.LiveData;

import com.google.gson.JsonObject;

import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.model.light.BaseLight;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Defines all relevant deconz API endpoint addresses for usage with the retrofit library.
 */
public interface IServices {

    String endpointLightIdHeader = "X-Deconz-EndpointLightId";

    @GET("lights/")
    LiveData<ApiResponse<List<BaseLight>>> getLights();

    @GET("lights/{lightId}/")
    LiveData<ApiResponse<BaseLight>> getLight(@Path("lightId") String lightId, @Header(endpointLightIdHeader) String headerLightId);

    @Headers( "Content-Type: application/json" )
    @PUT("lights/{lightId}/state")
    LiveData<ApiResponse<ResponseBody>>updateLightState(@Path("lightId") String lightId, @Body JsonObject body);
}
