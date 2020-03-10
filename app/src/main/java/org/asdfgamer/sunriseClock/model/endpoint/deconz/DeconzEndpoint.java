package org.asdfgamer.sunriseClock.model.endpoint.deconz;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter.BaseLightListTypeAdapter;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter.BaseLightTypeAdapter;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.util.LiveDataCallAdapterFactory;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeconzEndpoint extends BaseEndpoint {

    private static final String TAG = "DeconzEndpoint";

    /* Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org' */
    //TODO: Change back to Uri class and write custom TypeAdapter
    @Expose
    private String baseUrl;

    /* Port of the deconz server (Phoscon Webapp), eg. '80' */
    @Expose
    private int port;

    /* API key for communicating with deconz API. */
    @Expose
    private String apiKey;

    private transient IServices retrofit;

    private transient OkHttpClient httpClient;

    public DeconzEndpoint(String baseUrl, int port, String apiKey) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.apiKey = apiKey;
    }

    public DeconzEndpoint init() {

        //TODO: De-Uglify
        Uri fullApiUrl = Uri.parse(baseUrl.concat(":" + port))
                .buildUpon()
                .scheme("http")
                .appendPath("api")
                .appendEncodedPath(apiKey + "/")
                .build();

        //Gson has to be instructed to use our custom type adapter for a list of light.
        Type baseLightListType = new TypeToken<List<BaseLight>>() {}.getType();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BaseLight.class, new BaseLightTypeAdapter())
                .registerTypeAdapter(baseLightListType, new BaseLightListTypeAdapter())
                .create();

        this.httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NonNull
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request request = chain.request();
                        okhttp3.Response response = chain.proceed(request);

                        Log.d(TAG, "Intercepted request to: " + response.request().url().toString() + " led to HTTP code: " + response.code());

                        return response;
                    }
                })
                .build();

        this.retrofit = new Retrofit.Builder()
                // Set base URL for all requests to this deconz endpoint.
                .baseUrl(fullApiUrl.toString())
                // Set custom OkHttpClient for additional logging possibilities (interception).
                .client(httpClient)
                // Set custom GSON deserializer, eg. for parsing JSON into BaseLight objects.
                .addConverterFactory(GsonConverterFactory.create(gson))
                // Allow retrofit to return observable LiveData<ApiResponse> objects.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                // Implement methods to access REST endpoints / URLs.
                .create(IServices.class);

        return this;
    }

    @Override
    public void requestSetOn(BaseLight light, boolean value) {
        //TODO
    }

    @Override
    public void requestSetColor(BaseLight light, int color) {
        //TODO
    }

    @Override
    public void requestSetBrightness(BaseLight light, int brightness) {
        //TODO
    }

    @Override
    public void requestSetColorTemperature(BaseLight light, int colortemp) {
        //TODO
    }

    @Override
    public LiveData<ApiResponse<List<BaseLight>>> getLights() {
        Log.d(TAG, "Requesting all lights from endpoint: " + this.baseUrl);
        return this.retrofit.getLights();
    }

    @Override
    public LiveData<ApiResponse<BaseLight>> getLight(long id) {
        Log.d(TAG, "Requesting single light with id " + id + " from endpoint: " + this.baseUrl);
        return this.retrofit.getLight(String.valueOf(id));
    }

}