package org.asdfgamer.sunriseClock.network.utils.response.genericCallback;

import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

public class SimplifiedCallbackAdapter<T> extends RetrofitCallbackAdapter<T> {

    private final static String TAG = "SimplifiedCallbackAdapter";

    private SimplifiedCallback<T> callback;

    public SimplifiedCallbackAdapter(SimplifiedCallback<T> callback) {
        this.callback = callback;
    }

    @Override
    void handleOK(Response<T> response) {
        this.callback.onSuccess(response);
    }

    @Override
    void handleCreated(Response<T> response) {
        this.callback.onSuccess(response);
    }

    @Override
    void handleAccepted(Response<T> response) {
        this.callback.onSuccess(response);
    }

    @Override
    void handleNotModified(Response<T> response) {
        this.callback.onSuccess(response);
    }

    @Override
    void handleBadRequest(Error error) {
        this.callback.onError();
    }

    @Override
    void handleUnauthorized(Error error) {
        this.callback.onError();
    }

    @Override
    void handleForbidden(Error error) {
        this.callback.onError();
    }

    @Override
    void handleResourceNotFound(Error error) {
        this.callback.onError();
    }

    @Override
    void handleServiceUnavailable() {
        this.callback.onError();
    }

    @Override
    void handleInvalidErrorObject(Response<T> response) {
        this.callback.onError();
    }

    @Override
    void handleInvalidResponseObject(Call<T> call, Throwable throwable) {
        this.callback.onError();
    }

    @Override
    void handleNetworkFailure(Call<T> call, Throwable throwable) {
        this.callback.onError();
    }
}
