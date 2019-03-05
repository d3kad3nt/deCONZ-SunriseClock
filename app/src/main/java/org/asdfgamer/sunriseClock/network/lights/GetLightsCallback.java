package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public abstract class GetLightsCallback extends StandardCallback implements DeconzGetCallback<List<Light>> {
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
