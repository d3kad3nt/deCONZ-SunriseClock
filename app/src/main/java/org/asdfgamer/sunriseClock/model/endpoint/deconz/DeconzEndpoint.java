package org.asdfgamer.sunriseClock.model.endpoint.deconz;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import org.asdfgamer.sunriseClock.model.endpoint.BaseEndpoint;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.typeadapter.BaseLightTypeAdapter;
import org.asdfgamer.sunriseClock.model.endpoint.deconz.util.LiveDataCallAdapterFactory;
import org.asdfgamer.sunriseClock.model.endpoint.remoteApi.ApiResponse;
import org.asdfgamer.sunriseClock.model.light.BaseLight;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeconzEndpoint extends BaseEndpoint {

    private static final String TAG = "DeconzEndpoint";

    /* Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org' */
    @Expose
    private Uri baseUrl;

    /* Port of the deconz server (Phoscon Webapp), eg. '80' */
    @Expose
    private int port;

    /* API key for communicating with deconz API. */
    @Expose
    private String apiKey;

    private transient IServices retrofit;

    private transient OkHttpClient httpClient;

    public DeconzEndpoint(Uri baseUrl, int port, String apiKey) {
        this.baseUrl = baseUrl;
        this.port = port;
        this.apiKey = apiKey;

        //TODO: De-Uglify
        Uri fullApiUrl = baseUrl
                .buildUpon()
                .appendEncodedPath(":" + port)
                .path("api")
                .appendEncodedPath(apiKey + "/")
                .build();

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(BaseLight.class, new BaseLightTypeAdapter())
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
                // Allow retrofit to return observable LiveData<Apiresponse> objects.
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .build()
                // Implement methods to access REST endpoints / URLs.
                .create(IServices.class);
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
        return this.retrofit.getLights();
    }

    @Override
    public LiveData<ApiResponse<BaseLight>> getLight(long id) {
        return this.retrofit.getLight(String.valueOf(id));
    }

}