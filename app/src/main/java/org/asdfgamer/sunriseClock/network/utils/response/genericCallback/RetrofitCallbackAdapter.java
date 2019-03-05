package org.asdfgamer.sunriseClock.network.utils.response.genericCallback;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.ErrorDeserializer;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import java.io.IOError;
import java.util.Objects;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class RetrofitCallbackAdapter<T> implements Callback<T> {

    private final static String TAG = "AbstractCallBackAdapter";

    @Override
    public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {

        int responseCode = response.code();

        if (response.isSuccessful()) {
            Log.i(TAG, "Request to " + call.request().url() + " returned successfully with HTTP code: " + String.valueOf(responseCode));
            if (responseCode == DeconzApiReturncodes.OK.getCode()) {
                handleOK(response);
            } else if (responseCode == DeconzApiReturncodes.Created.getCode()) {
                handleCreated(response);
            } else if (responseCode == DeconzApiReturncodes.Accepted.getCode()) {
                handleAccepted(response);
            } else if (responseCode == DeconzApiReturncodes.Not_Modified.getCode()) {
                handleNotModified(response);
            }
        } else {
            Log.i(TAG, "Request to " + call.request().url() + " returned UNsucessfully with HTTP code: " + String.valueOf(responseCode));
            Gson gsonErrorDeserializer = new GsonBuilder()
                    .registerTypeAdapter(Error.class, new ErrorDeserializer())
                    .create();

            Error error;

            try {
                error = gsonErrorDeserializer.fromJson(Objects.requireNonNull(response.errorBody()).charStream(), Error.class);
            } catch (JsonParseException e) {
                Log.w(TAG, "Could not deserialize error object from response: " + e.getMessage());
                handleInvalidErrorObject(response);
                return;
            }

            if (responseCode == DeconzApiReturncodes.Bad_Request.getCode()) {
                handleBadRequest(error);
            } else if (responseCode == DeconzApiReturncodes.Unauthorized.getCode()) {
                handleUnauthorized(error);
            } else if (responseCode == DeconzApiReturncodes.Forbidden.getCode()) {
                handleForbidden(error);
            } else if (responseCode == DeconzApiReturncodes.Resource_Not_Found.getCode()) {
                handleResourceNotFound(error);
            } else if (responseCode == DeconzApiReturncodes.Service_Unavailable.getCode()) {
                handleServiceUnavailable();
            }
        }
    }

    @Override
    public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        Log.i(TAG, "Request to " + call.request().url() + " did not return anything at all!");

        /* Retrofits onFailures are in most cases IOErrors: This means that the network could not
         * be reached, the server could not be found etc. */
        if (t instanceof IOError) {
            handleNetworkFailure(call, t);
        } else {
            /* If this was not a network error then deconz probably returned an invalid
            * (unexpected-for-us) response that could not be deserialized by GSON. In most cases,
            * however, this just means that we reached a server that IS NOT a deconz instance. */
            handleInvalidResponseObject(call, t);
        }
    }

    abstract void handleOK(Response<T> response);

    abstract void handleCreated(Response<T> response);

    abstract void handleAccepted(Response<T> response);

    abstract void handleNotModified(Response<T> response);

    abstract void handleBadRequest(Error error);

    abstract void handleUnauthorized(Error error);

    abstract void handleForbidden(Error error);

    abstract void handleResourceNotFound(Error error);

    abstract void handleServiceUnavailable();

    //Methods NOT related to HTTP status codes.

    abstract void handleInvalidErrorObject(Response<T> response);

    abstract void handleInvalidResponseObject(Call<T> call, Throwable throwable);

    abstract void handleNetworkFailure(Call<T> call, Throwable throwable);
}
