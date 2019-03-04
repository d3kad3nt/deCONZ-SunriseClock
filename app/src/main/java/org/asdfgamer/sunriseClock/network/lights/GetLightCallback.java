package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.utils.response.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.callback.GetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.callback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

import retrofit2.Call;
import retrofit2.Response;

public abstract class GetLightCallback extends StandardCallback implements GetCallback<Light> {
    @Override
    public abstract void onRessourceNotFound(Error error);

    @Override
    public abstract void onSuccess(Response<Light> response);

    @Override
    public abstract void onForbidden(Error error);

    @Override
    public void onServiceUnavailable() {
        standardCallback(DeconzApiReturncodes.Service_Unavailable);
    }

    @Override
    public void onEverytime() {
        everytime();
    }

    @Override
    public void onInvalidErrorObject() {
        standardCallback(ConnectionError.InvalidErrorObject);
    }

    @Override
    public abstract void onFailure(Call<Light> call, Throwable throwable);
}
