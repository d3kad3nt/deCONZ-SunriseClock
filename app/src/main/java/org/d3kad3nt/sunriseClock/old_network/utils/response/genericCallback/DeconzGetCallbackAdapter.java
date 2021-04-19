package org.d3kad3nt.sunriseClock.old_network.utils.response.genericCallback;

import org.d3kad3nt.sunriseClock.old_network.utils.response.custDeserializer.model.Error;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see DeconzGetCallback}.
 *
 * @param <T>
 */
public class DeconzGetCallbackAdapter<T> extends DeconzBaseCallbackAdapter<T> {
    private DeconzGetCallback<T> callback;

    private static final String TAG = "DeconzGetCallbackAdapter";

    public DeconzGetCallbackAdapter(DeconzGetCallback<T> callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    void handleResourceNotFound(Error error) {
        callback.onEverytime();
        callback.onRessourceNotFound(error);
    }
}
