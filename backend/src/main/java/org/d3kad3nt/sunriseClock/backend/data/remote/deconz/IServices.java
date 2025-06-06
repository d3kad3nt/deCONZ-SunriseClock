package org.d3kad3nt.sunriseClock.backend.data.remote.deconz;

import androidx.lifecycle.LiveData;
import com.google.gson.JsonObject;
import java.util.List;
import okhttp3.ResponseBody;
import org.d3kad3nt.sunriseClock.backend.data.model.group.RemoteGroup;
import org.d3kad3nt.sunriseClock.backend.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.backend.data.remote.common.ApiResponse;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/** Defines all relevant deconz API endpoint addresses for usage with the retrofit library. */
public interface IServices {

    String endpointLightIdHeader = "X-Deconz-EndpointLightId";

    @GET("lights/")
    LiveData<ApiResponse<List<RemoteLight>>> getLights();

    @GET("lights/{lightId}/")
    LiveData<ApiResponse<RemoteLight>> getLight(
            @Path("lightId") String lightId, @Header(endpointLightIdHeader) String headerLightId);

    @Headers("Content-Type: application/json")
    @PUT("lights/{lightId}")
    LiveData<ApiResponse<ResponseBody>> updateLightAttributes(@Path("lightId") String lightId, @Body JsonObject body);

    @Headers("Content-Type: application/json")
    @PUT("lights/{lightId}/state")
    LiveData<ApiResponse<ResponseBody>> updateLightState(@Path("lightId") String lightId, @Body JsonObject body);

    @GET("groups/")
    LiveData<ApiResponse<List<RemoteGroup>>> getGroups();

    @Headers("Content-Type: application/json")
    @PUT("groups/{groupId}/action")
    LiveData<ApiResponse<ResponseBody>> updateGroupState(@Path("groupId") String groupId, @Body JsonObject body);
}
