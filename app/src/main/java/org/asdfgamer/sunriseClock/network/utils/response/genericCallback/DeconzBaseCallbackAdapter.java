package org.asdfgamer.sunriseClock.network.utils.response.genericCallback;

import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see DeconzBaseCallback}.
 *
 * @param <T>
 */
class DeconzBaseCallbackAdapter<T> extends RetrofitCallbackAdapter<T> {

    private DeconzBaseCallback<T> callback;

    private static final String TAG = "DeconzBaseCallbackAdapter";

    DeconzBaseCallbackAdapter(DeconzBaseCallback<T> callback) {
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
        callback.onEverytime();
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleUnauthorized(Error error) {
        callback.onEverytime();
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleForbidden(Error error) {
        callback.onEverytime();
        callback.onForbidden(error);
    }

    @Override
    void handleResourceNotFound(Error error) {
        callback.onEverytime();
        //Should not occur with this CallbackAdapter.
    }

    @Override
    void handleServiceUnavailable() {
        callback.onEverytime();
        callback.onServiceUnavailable();
    }

    @Override
    void handleInvalidErrorObject(Response<T> response) {
        callback.onEverytime();
        callback.onInvalidErrorObject();
    }

    @Override
    void handleFailure(Call<T> call, Throwable throwable) {
        callback.onFailure(call, throwable);
    }


}
