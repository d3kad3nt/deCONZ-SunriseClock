package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.utils.response.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.callback.GetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.callback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public abstract class GetLightsCallback extends StandardCallback implements GetCallback<List<Light>> {
    @Override
    public void onRessourceNotFound(Error error) {
        standardCallback(error);
    }

    @Override
    public abstract void onSuccess(Response<List<Light>> response);

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
    public abstract void onFailure(Call<List<Light>> call, Throwable throwable);
}
