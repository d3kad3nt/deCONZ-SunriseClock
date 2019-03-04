package org.asdfgamer.sunriseClock.network.lights;

import org.asdfgamer.sunriseClock.network.utils.response.callback.GetCallback;
import org.asdfgamer.sunriseClock.network.utils.response.callback.StandardCallback;
import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

import java.util.List;

/**
 * Has to implement success, 403 forbidden, onFailure
 */
public abstract class GetLightsCallback extends StandardCallback implements GetCallback<List<Light>> {
    @Override
    public void onRessourceNotFound(Error error) {
        standardCallback(error);
    }

    @Override
    public void onServiceUnavailable() {
        standardCallback(ConnectionError.ServiceUnavailable);
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
