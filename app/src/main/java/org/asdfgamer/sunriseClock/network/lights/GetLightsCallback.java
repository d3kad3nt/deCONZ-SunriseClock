package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.lights.model.Light;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzBaseCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public abstract class GetLightsCallback extends StandardCallback implements DeconzBaseCallback<List<Light>> {

    /* Getting all lights from deconz (GET /lights) does not return HTTP 404 and therefore does
     * not require a specific callback to inherit from.*/

    @Override
    public abstract void onSuccess(Response<List<Light>> response);

    @Override
    public abstract void onForbidden(Error error);

    @Override
    public void onEverytime() {
        everytime();
    }

    @Override
    public abstract void onNetworkFailure(Call<List<Light>> call, Throwable throwable);

    @Override
    public abstract void onInvalidResponseObject(Call<List<Light>> call, Throwable throwable);

    @Override
    public void onInvalidErrorObject() {
        standardCallback(ConnectionError.InvalidErrorObject);
    }

}
