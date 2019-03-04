package org.asdfgamer.sunriseClock.network.utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Used to communicate with the deconz endpoint. Expects the right required arguments to pass them
 * DIRECTLY to the endpoint. Therefore you have to ensure that the parameters are correctly
 * formatted for the deconz endpoint to successfully process them.
 */
public abstract class DeconzRequest {

    private final static String TAG = "DeconzRequest";

    /* Full path to the API endpoint including API key, eg. 'deconz.example.org:8080/api/XXXXX' */
    private Uri fullApiUrl;

    /* Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org:8080' */
    private Uri baseUrl;

    /* API key for communicating with deconz API. */
    private String apiKey;

    private Retrofit retrofit;

    private OkHttpClient httpClient;

    private Gson gsonDeserializer;

    protected DeconzRequest(Uri baseUrl, String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        fullApiUrl = baseUrl
                .buildUpon()
                .path("api")
                .appendEncodedPath(apiKey + "/")
                .build();

        this.gsonDeserializer = new Gson();
        buildRetrofit();

        Log.i(TAG, "DeconzRequest created. fullApiUrl: " + fullApiUrl);
    }

    private void buildRetrofit() {
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
                .baseUrl(fullApiUrl.toString())
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gsonDeserializer))
                .build();
    }

    protected <S> S createService(Class<S> serviceClass) {
        return this.retrofit.create(serviceClass);
    }

    protected void setGsonDeserializer(Gson gson) {
        this.gsonDeserializer = gson;
        Log.i(TAG, "Setting custom deserializer and rebuilding retrofit http client.");
        buildRetrofit();
    }

    /**
     * @return Full path to the API endpoint including API key,
     * eg. 'deconz.example.org:8080/api/XXXXX'.
     */
    public Uri getFullApiUrl() {
        return fullApiUrl;
    }

    /**
     * @return Path to the deconz server (Phoscon Webapp), eg. 'deconz.example.org:8080'.
     */
    public Uri getBaseUrl() {
        return baseUrl;
    }

    /**
     * @return The API key used to communicate with deconz.
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * This initializes the request to deconz. It MUST be called before starting requests.
     */
    public abstract void init();

}
