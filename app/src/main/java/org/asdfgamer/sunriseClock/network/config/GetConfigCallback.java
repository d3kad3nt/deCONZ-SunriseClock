package org.asdfgamer.sunriseClock.network.config;

import org.asdfgamer.sunriseClock.network.utils.response.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.callback.GetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.callback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

public abstract class GetConfigCallback extends StandardCallback implements GetCallback<Config> {
    @Override
    public void onRessourceNotFound(Error error) {
        standardCallback(error);
    }

    @Override
    public void onForbidden(Error error) {
        standardCallback(error);
    }

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


}
