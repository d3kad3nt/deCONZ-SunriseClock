package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

public abstract class GetLightCallback extends StandardCallback implements DeconzGetCallback<Light> {
    @Override
    public abstract void onRessourceNotFound(Error error);

    @Override
    public abstract void onSuccess(Response<Light> response);

    @Override
    public abstract void onForbidden(Error error);

    @Override
    public void onEverytime() {
        everytime();
    }

    @Override
    public void onNetworkFailure(Call<Light> call, Throwable throwable) {

    }

    @Override
    public void onInvalidResponseObject(Call<Light> call, Throwable throwable) {

    }

    @Override
    public void onInvalidErrorObject() {
        standardCallback(ConnectionError.InvalidErrorObject);
    }


}
