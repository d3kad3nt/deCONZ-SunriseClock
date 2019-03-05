package org.asdfgamer.sunriseClock.network.config;

import org.asdfgamer.sunriseClock.network.config.model.Config;
import org.asdfgamer.sunriseClock.network.DeconzApiReturncodes;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.DeconzGetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.genericCallback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

public abstract class GetConfigCallback extends StandardCallback implements DeconzGetCallback<Config> {
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
