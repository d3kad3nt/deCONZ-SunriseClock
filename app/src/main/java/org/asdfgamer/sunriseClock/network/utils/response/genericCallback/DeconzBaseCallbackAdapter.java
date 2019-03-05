package org.asdfgamer.sunriseClock.network.utils.response.genericCallback;

import android.util.Log;

import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see DeconzBaseCallback}.
 *
 * @param <T>
 */
public class DeconzBaseCallbackAdapter<T> extends RetrofitCallbackAdapter<T> {

    private DeconzBaseCallback<T> callback;

    private static final String TAG = "DecBaseCallbackAdapter";

    public DeconzBaseCallbackAdapter(DeconzBaseCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    void handleOK(Response<T> response) {

        callback.onEverytime();
        callback.onSuccess(response);
    }

    @Override
    void handleCreated(Response<T> response) {
        callback.onEverytime();
        callback.onSuccess(response);
    }

    @Override
    void handleAccepted(Response<T> response) {
        callback.onEverytime();
        callback.onSuccess(response);
    }

    @Override
    void handleNotModified(Response<T> response) {
        callback.onEverytime();
        callback.onSuccess(response);
    }

    @Override
    void handleBadRequest(Error error) {
        Log.w(TAG, "handleBadRequest() should not have been called. This callback adapter is NOT intended for this request.");
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleUnauthorized(Error error) {
        Log.w(TAG, "handleUnauthorized() should not have been called. This callback adapter is NOT intended for this request.");
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleForbidden(Error error) {
        callback.onEverytime();
        callback.onForbidden(error);
    }

    @Override
    void handleResourceNotFound(Error error) {
        Log.w(TAG, "handleResourceNotFound() should not have been called. This callback adapter is NOT intended for this request.");
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleServiceUnavailable() {
        Log.w(TAG, "handleServiceUnavailable() should not have been called. This callback adapter is NOT intended for this request.");
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleInvalidErrorObject(Response<T> response) {
        callback.onEverytime();
        callback.onInvalidErrorObject();
    }

    @Override
    void handleInvalidResponseObject(Call<T> call, Throwable throwable) {
        callback.onEverytime();
        callback.onInvalidResponseObject(call, throwable);
    }

    @Override
    void handleNetworkFailure(Call<T> call, Throwable throwable) {
        callback.onEverytime();
        callback.onNetworkFailure(call, throwable);
    }


}
