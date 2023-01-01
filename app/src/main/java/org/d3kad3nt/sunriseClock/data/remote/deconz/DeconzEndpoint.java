package org.d3kad3nt.sunriseClock.data.remote.deconz;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.d3kad3nt.sunriseClock.data.model.endpoint.BaseEndpoint;
import org.d3kad3nt.sunriseClock.data.model.light.RemoteLight;
import org.d3kad3nt.sunriseClock.data.remote.common.ApiResponse;
import org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter.RemoteLightListTypeAdapter;
import org.d3kad3nt.sunriseClock.data.remote.deconz.typeadapter.RemoteLightTypeAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeconzEndpoint extends BaseEndpoint {

    private static final String TAG = "DeconzEndpoint";

    /* Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org' */
    //TODO: Change back to Uri class and write custom TypeAdapter
    @Expose
    private final String baseUrl;

    /* Port of the deconz server (Phoscon Webapp), eg. '80' */
    @Expose
    private final int port;

    /* API key for communicating with deconz API. */
    @Expose
    private final String apiKey;

    private transient IServices retrofit;

    private transient OkHttpClient httpClient;

    public DeconzEndpoint(String baseUrl, int port, String apiKey) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.apiKey = apiKey;
    }

    @Override
    public DeconzEndpoint init() {

        //TODO: De-Uglify
        Uri fullApiUrl = Uri.parse(baseUrl.concat(":" + port))
                .buildUpon()
                .scheme("http")
                .appendPath("api")
                .appendEncodedPath(apiKey + "/")
                .build();
        //Gson has to be instructed to use our custom type adapter for a list of light.
        Type remoteLightType = new TypeToken<RemoteLight>() {}.getType();
        Type remoteLightListType = new TypeToken<List<RemoteLight>>() {}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(remoteLightType, new RemoteLightTypeAdapter(super.getOriginalEndpointConfig().getId()))
                .registerTypeAdapter(remoteLightListType, new RemoteLightListTypeAdapter(super.getOriginalEndpointConfig().getId()))
                .create();

        // Debugging HTTP interceptor for underlying okHttp library.
        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);
                        Log.d(TAG, "HTTP interceptor: Intercepted request to: " + response.request().url().toString() + " led to HTTP code: " + response.code());

                        if (response.code() >= 200 && response.code() <= 399 && response.body() != null) {

                            // Workaround: Deconz endpoint does not return the id of a light when requesting a single
                            // light. The Gson deserializer is automatically called and cannot access the id inside of
                            // the original request. A okHttp interceptor is used to modify the JSON response from the
                            // Deconz endpoint and adds this light id.
                            if (request.header(IServices.endpointLightIdHeader) != null) {
                                Log.d(TAG, "HTTP interceptor: Try to set light id in JSON response as workaround.");

                                assert response.body() != null;
                                String stringJson = response.body().string();
                                JSONObject jsonObject = null;

                                try {
                                    jsonObject = new JSONObject(stringJson);
                                    jsonObject.put(IServices.endpointLightIdHeader, request.header(IServices.endpointLightIdHeader));

                                    MediaType contentType = response.body().contentType();
                                    ResponseBody body = ResponseBody.create(contentType, String.valueOf(jsonObject));

                                    return response.newBuilder().body(body).build();

                                } catch (JSONException ignored) {

                                }

                            }
                        }

                        return response;
                    }
                })
                .build();

        this.retrofit = new Retrofit.Builder()
                // Set base URL for all requests to this deconz endpoint.
                .baseUrl(fullApiUrl.toString())
                // Set custom OkHttpClient for additional logging possibilities (interception).
                .client(httpClient)
                // Set custom GSON deserializer, eg. for parsing JSON into DbLight objects.
                .addConverterFactory(GsonConverterFactory.create(gson))
                // Allow retrofit to return observable LiveData<ApiResponse> objects.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                // Implement methods to access REST endpoints / URLs.
                .create(IServices.class);

        return this;
    }

    @Override
    public LiveData<ApiResponse<List<RemoteLight>>> getLights() {
        Log.d(TAG, "Requesting all lights from endpoint: " + this.baseUrl);
        return this.retrofit.getLights();
    }

    @Override
    public LiveData<ApiResponse<RemoteLight>> getLight(String id) {
        Log.d(TAG, "Requesting single light with id " + id + " from endpoint: " + this.baseUrl);

        // Workaround: Deconz endpoint does not return the id of a light when requesting a single
        // light. The Gson deserializer is automatically called and cannot access the id inside of
        // the original request. A okHttp interceptor is used to modify the JSON response from the
        // Deconz endpoint and adds this light id.
        return this.retrofit.getLight(id, id);
    }

    @Override
    public LiveData<ApiResponse<ResponseBody>> setOnState(String endpointLightId, boolean newState) {
        JsonObject requestBody = new JsonObject();
        requestBody.add("on", new JsonPrimitive(newState));
        return this.retrofit.updateLightState(endpointLightId, requestBody );
    }

    @Override
    public LiveData<ApiResponse<ResponseBody>> setBrightness(String endpointLightId, double brightness) {
        JsonObject requestBody = new JsonObject();
        //Deconz takes values from 0 to 255 for the brightness
        long deconzBrigtness = Math.round(brightness * 255);
        requestBody.add("bri", new JsonPrimitive(deconzBrigtness));
        return this.retrofit.updateLightState(endpointLightId, requestBody );
    }

}
