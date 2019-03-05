package org.asdfgamer.sunriseClock.network.config;

import org.asdfgamer.sunriseClock.network.config.model.Config;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzBaseCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import retrofit2.Call;
import retrofit2.Response;

public abstract class GetConfigCallback extends StandardCallback implements DeconzBaseCallback<Config> {

    /* Getting the config from deconz (GET /config) does not return HTTP 404 and therefore does
     * not require a specific callback to inherit from.*/

    @Override
    public abstract void onSuccess(Response<Config> response);

    @Override
    public void onForbidden(Error error) {
        standardCallback(error);
    }

    @Override
    public void onEverytime() {
        everytime();
    }

    @Override
    public abstract void onNetworkFailure(Call<Config> call, Throwable throwable);

    @Override
    public abstract void onInvalidResponseObject(Call<Config> call, Throwable throwable);

    @Override
    public void onInvalidErrorObject() {
        standardCallback(ConnectionError.InvalidErrorObject);
    }
}
