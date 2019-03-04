package org.asdfgamer.sunriseClock.network.utils.response.callback;

import org.asdfgamer.sunriseClock.network.utils.response.model.Error;

/**
 * Callback interface adapter for deconz network requests returning the HTTP status codes from
 * {@see CreateCallback}.
 *
 * @param <T>
 */
public class CreateCallbackAdapter<T> extends BaseCallbackAdapter<T> {
    private CreateCallback<T> callback;

    private static final String TAG = "CreateCallbackAdapter";

    public CreateCallbackAdapter(CreateCallback<T> callback) {
        super(callback);
        this.callback = callback;
    }

    @Override
    void handleBadRequest(Error error) {
        callback.onEverytime();
        callback.onBadRequest(error);
    }

}
