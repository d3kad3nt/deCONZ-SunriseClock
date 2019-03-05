package org.asdfgamer.sunriseClock.network.utils.response.genericCallback;

import org.asdfgamer.sunriseClock.network.utils.response.custDeserializer.model.Error;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see DeconzCreateCallback}.
 *
 * @param <T>
 */
public class DeconzCreateCallbackAdapter<T> extends DeconzBaseCallbackAdapter<T> {
    private DeconzCreateCallback<T> callback;

    private static final String TAG = "DeconzCreateCallbackAdapter";

    public DeconzCreateCallbackAdapter(DeconzCreateCallback<T> callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    void handleBadRequest(Error error) {
        callback.onEverytime();
        callback.onBadRequest(error);
    }

}
